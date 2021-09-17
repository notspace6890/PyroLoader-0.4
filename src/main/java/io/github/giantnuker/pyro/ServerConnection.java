package io.github.giantnuker.pyro;

import java.lang.Process;
import java.util.function.*;
import java.net.*;
import oshi.*;
import java.util.*;
import java.nio.charset.*;
import oshi.software.os.*;
import oshi.hardware.*;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ServerConnection extends Thread
{
    public static ServerConnection INSTANCE;
    public Status status;
    private Consumer<Status> statusConsumer;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean waitingForDiscord;
    
    public ServerConnection(final Consumer<Status> statusConsumer) {
        this.status = Status.CONNECTING;
        this.in = null;
        this.out = null;
        this.waitingForDiscord = false;
        this.statusConsumer = statusConsumer;
        ServerConnection.INSTANCE = this;
    }
    
    public void updateDiscord() {
        if (this.waitingForDiscord) {
            if (DiscordThread.INSTANCE.initialized) {
                try {
                    this.out.writeUTF(DiscordThread.INSTANCE.userID);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    this.out.writeUTF("nodiscord");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.waitingForDiscord = false;
        }
    }
    
    @Override
    public void run() {
        this.setStatus(Status.CONNECTING);
        try {
            this.socket = new Socket("51.161.9.106", 42069);
            this.in = new DataInputStream(this.socket.getInputStream());
            (this.out = new DataOutputStream(this.socket.getOutputStream())).writeUTF("connect");
            this.out.writeUTF(this.getHWID());
            this.out.flush();
            this.setStatus(Status.LOGIN);
            while (!this.socket.isClosed()) {
                final String line = this.in.readUTF();
                if (line.equals("loginfail")) {
                    this.setStatus(Status.LOGINFAIL);
                }
                else if (line.equals("channels")) {
                    final int count = this.in.readInt();
                    ClassStorage.channels = new HashMap<String, List<String>>();
                    for (int i = 0; i < count; ++i) {
                        final String name = this.in.readUTF();
                        final int ccount = this.in.readInt();
                        final List<String> versions = new ArrayList<String>(ccount);
                        for (int j = 0; j < ccount; ++j) {
                            versions.add(this.in.readUTF());
                        }
                        ClassStorage.channels.put(name, versions);
                    }
                    this.setStatus(Status.PICKCHANNEL);
                }
                else {
                    if (!line.equals("download")) {
                        continue;
                    }
                    if (this.in.readUTF().equals("refmap")) {
                        ClassStorage.refmap = new byte[this.in.readInt()];
                        this.in.readFully(ClassStorage.refmap);
                    }
                    ClassStorage.fileCount = this.in.readInt();
                    ClassStorage.files = new HashMap<String, byte[]>();
                    this.setStatus(Status.SENDING);
                    for (int k = 0; k < ClassStorage.fileCount; ++k) {
                        final String name2 = this.in.readUTF();
                        final int size = this.in.readInt();
                        final byte[] bytes = new byte[size];
                        this.in.readFully(bytes);
                        ClassStorage.files.put(name2, bytes);
                        this.setStatus(Status.SENDING);
                    }
                }
            }
            this.terminateConnection();
        }
        catch (IOException e) {
            this.setStatus(Status.CONNECTFAIL);
        }
    }
    public void write(final String value) throws IOException {
        this.out.writeUTF(value);
    }
    
    public void login(final String username, final String password) throws IOException {
        this.out.writeUTF("login");
        this.out.writeUTF(username);
        this.out.writeUTF(password);
        this.out.writeUTF(this.getHWID());
        if (DiscordThread.INSTANCE.initialized) {
            this.out.writeUTF(DiscordThread.INSTANCE.userID);
        }
        else if (DiscordThread.INSTANCE.noconn) {
            this.out.writeUTF("nodiscord");
        }
        else {
            this.waitingForDiscord = true;
        }
    }
    
    public void terminateConnection() throws IOException {
        this.socket.close();
    }
    
    private void setStatus(final Status status) {
        this.status = status;
        this.statusConsumer.accept(status);
    }
    
    private String getHWID() {
        final SystemInfo systemInfo = new SystemInfo();
        final OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        final HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        final Processor[] centralProcessor = hardwareAbstractionLayer.getProcessors();
        String str = "";
        for (final Processor processor : centralProcessor) {
            str = str + processor.getIdentifier() + processor.getName() + processor.getVendor();
        }
        final String osName = System.getProperty("os.name").toLowerCase();
        if (!osName.startsWith("mac os x")) {
            final String command = osName.startsWith("windows") ? "wmic baseboard get serialnumber" : "dmidecode -s baseboard-serial-number";
            try {
                final Process child = Runtime.getRuntime().exec(command);
                final InputStream in = child.getInputStream();
                int ch;
                while ((ch = in.read()) != -1) {
                    str += ch;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        str = str + operatingSystem.getManufacturer() + operatingSystem.getFamily() + System.getProperty("os.version");
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }
    
    static {
        ServerConnection.INSTANCE = null;
    }
    
    public enum Status
    {
        CONNECTING, 
        CONNECTFAIL, 
        LOGIN, 
        LOGINFAIL, 
        PICKCHANNEL, 
        SENDING;
    }
}

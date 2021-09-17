package io.github.giantnuker.pyro.ui;

import javax.swing.plaf.nimbus.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import io.github.giantnuker.pyro.*;
import java.awt.event.*;

public class LauncherUI extends JFrame
{
    private ServerConnection connection;
    private Thread mainThread;
    private JPanel panel;
    
    public LauncherUI(final Thread thread) {
        super("Pyro Client");
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        }
        catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("/iconsquare.png")));
        this.mainThread = thread;
        this.setSize(600, 250);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        final HeaderPanel header = new HeaderPanel();
        this.add(header, "North");
        this.setVisible(true);
        this.panel = new ConnectingPanel();
        (this.connection = new ServerConnection(this::processStatus)).start();
    }
    
    private void continueMC() {
        try {
            this.connection.terminateConnection();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        this.dispose();
        this.mainThread.resume();
    }
    
    private void reconnect() {
        (this.connection = new ServerConnection(this::processStatus)).start();
    }
    
    private void processStatus(final ServerConnection.Status status) {
        switch (status) {
            case CONNECTING: {
                this.remove(this.panel);
                this.add(this.panel = new ConnectingPanel());
                break;
            }
            case CONNECTFAIL: {
                this.remove(this.panel);
                this.add(this.panel = new ReconnectPanel(this::reconnect));
                break;
            }
            case LOGIN: {
                this.remove(this.panel);
                this.add(this.panel = new LoginPanel(this.connection));
                break;
            }
            case LOGINFAIL: {
                ((LoginPanel)this.panel).fail();
                break;
            }
            case PICKCHANNEL: {
                if (this.panel instanceof LoginPanel) {
                    ((LoginPanel)this.panel).saveLogin();
                }
                this.remove(this.panel);
                this.add(this.panel = new PickChannelPanel(this.connection));
                break;
            }
            case SENDING: {
                if (!(this.panel instanceof DownloadActivityPanel)) {
                    this.remove(this.panel);
                    this.add(this.panel = new DownloadActivityPanel());
                }
                final DownloadActivityPanel p = (DownloadActivityPanel)this.panel;
                if (ClassStorage.fileCount == -1) {
                    break;
                }
                p.setDownloadProgress(ClassStorage.files.size(), ClassStorage.fileCount);
                if (ClassStorage.files.size() == ClassStorage.fileCount) {
                    PyroLoaderConstants.EXIT = false;
                    try {
                        this.connection.terminateConnection();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    this.continueMC();
                    break;
                }
                break;
            }
        }
        this.revalidate();
    }
    
    @Override
    protected void processWindowEvent(final WindowEvent e) {
        if (e.getID() == 201) {
            this.continueMC();
        }
        else {
            super.processWindowEvent(e);
        }
    }
}

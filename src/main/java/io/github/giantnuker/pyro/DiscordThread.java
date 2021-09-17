package io.github.giantnuker.pyro;

import java.util.*;
import net.arikia.dev.drpc.*;

public class DiscordThread extends Thread
{
    public static DiscordThread INSTANCE;
    private Date startTime;
    private Date endTime;
    private String image;
    private String imageStatus;
    private String status;
    private int partySize;
    private int partyMax;
    private boolean updateRP;
    public boolean initialized;
    public boolean noconn;
    public String userID;
    private int loops;
    
    public DiscordThread() {
        this.startTime = new Date();
        this.endTime = null;
        this.image = null;
        this.imageStatus = null;
        this.status = null;
        this.partySize = -1;
        this.partyMax = -1;
        this.updateRP = false;
        this.initialized = false;
        this.noconn = false;
        this.loops = 0;
    }
    
    @Override
    public void run() {
        final DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(user -> {
            System.out.println(user.userId);
            PyroLoaderConstants.LOG.info("Discord initialized as " + user.username + "#" + user.discriminator);
            this.userID = user.userId;
            this.initialized = true;
            this.noconn = false;
            this.setStatus("Loading");
            if (ServerConnection.INSTANCE != null) {
                ServerConnection.INSTANCE.updateDiscord();
            }
            return;
        }).setErroredEventHandler((code, msg) -> System.out.println(code + " : " + msg)).setDisconnectedEventHandler((code, msg) -> System.out.println(code + " : " + msg)).build();
        DiscordRPC.discordInitialize("695781056904691812", handlers, true);
        while (true) {
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!this.initialized && this.loops < 10) {
                ++this.loops;
            }
            else if (!this.initialized) {
                this.noconn = true;
                if (ServerConnection.INSTANCE != null) {
                    ServerConnection.INSTANCE.updateDiscord();
                }
            }
            if (this.updateRP && this.initialized) {
                final DiscordRichPresence.Builder presence = new DiscordRichPresence.Builder((this.status == null) ? "" : this.status).setDetails("discord.gg/xUuf7Ch");
                if (this.image != null) {
                    presence.setBigImage(this.image, this.imageStatus);
                    presence.setSmallImage("logo", "Pyro Client");
                }
                else {
                    presence.setBigImage("logo", "Pyro Client");
                }
                if (this.endTime != null) {
                    presence.setEndTimestamp(this.endTime.getTime());
                }
                else if (this.startTime != null) {
                    presence.setStartTimestamps(this.startTime.getTime());
                }
                if (this.partySize != -1 && this.partyMax != -1) {
                    presence.setParty("party", this.partySize, this.partyMax);
                }
                DiscordRPC.discordUpdatePresence(presence.build());
                this.updateRP = false;
            }
            DiscordRPC.discordRunCallbacks();
        }
    }
    
    public void setStartTime(final Date time) {
        this.startTime = time;
        this.updateRP = true;
    }
    
    public void setEndTime(final Date time) {
        this.endTime = time;
        this.updateRP = true;
    }
    
    public void setStatus(final String status) {
        this.status = status;
        this.updateRP = true;
    }
    
    public void setPartyMax(final int size, final int max) {
        this.partySize = size;
        this.partyMax = max;
        this.updateRP = true;
    }
    
    public void setImage(final String image, final String imageStatus) {
        this.image = image;
        this.imageStatus = imageStatus;
        this.updateRP = true;
    }
    
    static {
        DiscordThread.INSTANCE = new DiscordThread();
    }
}

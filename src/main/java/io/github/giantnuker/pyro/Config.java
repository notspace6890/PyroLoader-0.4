package io.github.giantnuker.pyro;

import com.google.gson.*;
import java.io.*;

public class Config
{
    public static transient Config INSTANCE;
    public String username;
    public String password;
    public String channel;
    
    public Config() {
        this.username = null;
        this.password = null;
        this.channel = null;
    }
    
    public static void load() {
        try {
            if (new File("Pyro/launcher.json").exists()) {
                final Gson gson = new Gson();
                final FileReader reader = new FileReader("Pyro/launcher.json");
                Config.INSTANCE = (Config)gson.fromJson(reader, (Class)Config.class);
                reader.close();
            }
            else {
                Config.INSTANCE = new Config();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void save() {
        try {
            final Gson gson = new Gson();
            final FileWriter writer = new FileWriter("Pyro/launcher.json");
            gson.toJson(Config.INSTANCE, writer);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

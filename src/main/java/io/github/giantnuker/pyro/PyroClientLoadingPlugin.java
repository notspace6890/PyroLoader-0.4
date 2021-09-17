package io.github.giantnuker.pyro;

import net.minecraftforge.fml.relauncher.*;
import io.github.giantnuker.pyro.ui.*;
import java.nio.file.*;
import org.spongepowered.asm.launch.*;
import org.spongepowered.asm.mixin.*;
import java.io.*;
import javax.annotation.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class PyroClientLoadingPlugin implements IFMLLoadingPlugin
{
    public PyroClientLoadingPlugin() throws IOException {
       PyroLoaderConstants.LOG.info("Initializing Pyro Launcher");
       DiscordThread.INSTANCE.start();
       Config.load();
       PyroLoaderConstants.DIRECTORY.mkdirs();
       new LauncherUI(Thread.currentThread());
       Thread.currentThread().suspend();
        if (PyroLoaderConstants.EXIT) {
            DiscordThread.INSTANCE.setStatus("Stopping");
            new ExitWindow();
        }
        if (ClassStorage.refmap != null) {
            Files.write(new File(PyroLoaderConstants.DIRECTORY, "mxr").toPath(), ClassStorage.refmap, new OpenOption[0]);
        }
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.pyroclient.json");
        Config.save();
        DiscordThread.INSTANCE.setStatus(null);
        PyroLoaderConstants.LOG.info("Pyro initialization complete");
    }
    
    public String[] getASMTransformerClass() {
        return new String[] { "io.github.giantnuker.pyro.PyroClassTransformer" };
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    @Nullable
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> data) {
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
}

package io.github.giantnuker.pyro;

import net.minecraftforge.fml.relauncher.*;
import net.minecraft.launchwrapper.*;
import java.io.*;
import java.net.*;
import java.util.*;
import org.spongepowered.asm.lib.tree.*;
import org.spongepowered.asm.mixin.extensibility.*;

public class PyroMixinPlugin implements IMixinConfigPlugin
{
    private List<String> mixins;
    
    public PyroMixinPlugin() {
        this.mixins = new ArrayList<String>();
    }
    
    @Override
    public void onLoad(final String mixinPackage) {
        if (!PyroLoaderConstants.EXIT) {
            PyroLoaderConstants.LOG.info("Loading mixin classes");
            for (final String clazz : ClassStorage.files.keySet()) {
                if (clazz.startsWith(mixinPackage)) {
                    this.mixins.add(clazz.substring(clazz.lastIndexOf(".") + 1));
                    if (!FMLLaunchHandler.isDeobfuscatedEnvironment()) {
                        continue;
                    }
                    try {
                        PyroMixinPlugin.class.getClassLoader().loadClass(clazz);
                    }
                    catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                ((LaunchClassLoader)PyroMixinPlugin.class.getClassLoader()).addURL(new File(PyroLoaderConstants.DIRECTORY, "mxr").toURI().toURL());
            }
            catch (MalformedURLException e2) {
                e2.printStackTrace();
            }
        }
    }
    
    @Override
    public String getRefMapperConfig() {
        if (PyroLoaderConstants.EXIT) {
            return "mixins.pyroclient.refmap.json";
        }
        if (ClassStorage.refmap != null) {
            try {
                return new File(PyroLoaderConstants.DIRECTORY, "mxr").toURI().toURL().toString();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    @Override
    public boolean shouldApplyMixin(final String targetClassName, final String mixinClassName) {
        return false;
    }
    
    @Override
    public void acceptTargets(final Set<String> myTargets, final Set<String> otherTargets) {
    }
    
    @Override
    public List<String> getMixins() {
        return PyroLoaderConstants.EXIT ? Collections.singletonList("StopAsapMixin") : this.mixins;
    }
    
    @Override
    public void preApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) {
    }
    
    @Override
    public void postApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) {
    }
}

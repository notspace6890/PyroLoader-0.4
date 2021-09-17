package io.github.giantnuker.pyro.mixin;

import org.spongepowered.asm.mixin.*;
import net.minecraft.init.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import io.github.giantnuker.pyro.*;
import net.minecraftforge.fml.common.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ Bootstrap.class })
public class StopAsapMixin
{
    @Inject(method = { "register" }, at = { @At("HEAD") })
    private static void stop(final CallbackInfo ci) {
        if (PyroLoaderConstants.EXIT) {
            PyroLoaderConstants.LOG.info("Performing exit. Everything below this point is perfectly normal.");
            FMLCommonHandler.instance().exitJava(0, true);
        }
    }
}

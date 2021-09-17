package io.github.giantnuker.pyro;

import net.minecraft.launchwrapper.*;

public class PyroClassTransformer implements IClassTransformer
{
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
        if (ClassStorage.files != null && ClassStorage.files.keySet().contains(name)) {
            return ClassStorage.files.get(name);
        }
        return basicClass;
    }
}

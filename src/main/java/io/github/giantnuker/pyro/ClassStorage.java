package io.github.giantnuker.pyro;

import java.util.*;

public class ClassStorage
{
    public static int fileCount;
    public static Map<String, byte[]> files;
    public static byte[] refmap;
    public static Map<String, List<String>> channels;
    
    static {
        ClassStorage.fileCount = -1;
        ClassStorage.refmap = null;
        ClassStorage.channels = null;
    }
}

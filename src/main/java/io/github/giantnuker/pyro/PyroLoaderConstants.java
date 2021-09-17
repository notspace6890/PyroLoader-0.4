package io.github.giantnuker.pyro;

import java.io.*;
import org.apache.logging.log4j.*;

public class PyroLoaderConstants
{
    public static final Logger LOG;
    public static final File DIRECTORY;
    public static boolean EXIT;
    
    static {
        LOG = LogManager.getLogger("Pyro Client");
        DIRECTORY = new File("Pyro");
        PyroLoaderConstants.EXIT = false;
    }
}

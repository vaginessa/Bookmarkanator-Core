package com.bookmarkanator.settings;

import java.util.*;

public class SettingsUtil
{
    public static String getOSName()
    {
        return System.getProperty("os.name");
    }

    public static String getOSVersion()
    {
        return System.getProperty("os.version");
    }

    public static void printSystemProperties()
    {
        Properties props = System.getProperties();
        props.list(System.out);
    }

    public static String getSystemSpecificTerminalCommand(String systemType)
    {
        systemType = systemType.toUpperCase();
        if (systemType.contains("MAC"))
        {
            return "open -a Terminal";
        }
        else if (systemType.contains("WINDOW"))
        {
            return "cmd.exe /c start";
        }
        else if (systemType.contains("LINUX"))
        {
           if (systemType.contains("KDE"))
            {
                return "Konsole";
            }
            else if (systemType.contains("MINT"))
            {
                return "gnome-terminal";
            }
            return "XTERM";
        }
        else return null;
    }
}

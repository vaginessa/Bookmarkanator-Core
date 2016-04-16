package com.bookmarkanator.settings;

import java.io.IOException;
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

    public static Process openTerminal()
    {
        String os = getOSName();
        os = os.toUpperCase();
        String[] st;

        if (os.contains("MAC"))
        {
            st = new String[]{"open -a Terminal"};
        }
        else if (os.contains("WINDOW"))
        {
            st = new String[]{"cmd.exe /c start"};
        }
        else
        {
           st = new String[]{"gnome-terminal","Xterm","Konsole","Guake","rxvt"};
        }

        Process process;

        for (String s:st)
        {
            try {
                process = Runtime.getRuntime().exec(s);
                return process;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

//    public static String getSystemSpecificTerminalCommand(String systemType)
//    {
//        systemType = systemType.toUpperCase();
//        System.out.println(systemType);
//        System.out.println(getOSVersion());
//        if (systemType.contains("MAC"))
//        {
//            return "open -a Terminal";
//        }
//        else if (systemType.contains("WINDOW"))
//        {
//            return "cmd.exe /c start";
//        }
//        else if (systemType.contains("LINUX"))
//        {
//            String[] st = new String[]{"Konsole","gnome-terminal","Xterm"};
//
//            for (String s:st)
//            {
//                if (checkProgramCanRun(s))
//                {
//                    return s;
//                }
//            }
//        }
//        return null;
//    }
//
//    private static boolean checkProgramCanRun(String progName)
//    {
//        try {
//            Process process = Runtime.getRuntime().exec(progName);
////            ProcessBuilder pb = new ProcessBuilder();
////            pb.command(command);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//return true;
//    }
}

package com.bookmarkanator.util;

/**
 * Detects which OS we are running on.
 * <p>
 * Note: This code copied from https://stackoverflow.com/questions/7024031/java-open-a-file-windows-mac
 */
public class OSDetector
{
    private static boolean isWindows = false;
    private static boolean isLinux = false;
    private static boolean isMac = false;

    static
    {
        String os = System.getProperty("os.name").toLowerCase();
        isWindows = os.contains("win");
        isLinux = os.contains("nux") || os.contains("nix");
        isMac = os.contains("mac");
    }

    public static boolean isWindows()
    {
        return isWindows;
    }

    public static boolean isLinux()
    {
        return isLinux;
    }

    public static boolean isMac()
    {
        return isMac;
    }
}

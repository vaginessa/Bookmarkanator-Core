package com.bookmarking.util;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import com.bookmarking.error.*;

public class VersionParser
{

    /**
     * If the system version is not higher than the lowest version string supplied it will pop up a JDialog with the supplied message.
     * @param lowestVersion  the lowest java system version acceptable
     * @param additionalMessage  the message to display if the java version does not meet the minimum version.
     * @param exitIfBadVersion  Calls system.exit if the version is too low.
     * @return  Returns true if running a system with java version higher than the supplied string, false otherwise.
     */
    public boolean checkMyJavaVersion(String lowestVersion, String additionalMessage, boolean exitIfBadVersion)
    {
        // Detecting java version, and bailing if it is not the right version.
        Version systemVersion = VersionParser.getJavaVersion();
        System.out.println("System Java Version: " + systemVersion);
        Version requiredVersion = VersionParser.parse(lowestVersion);
        System.out.println("Required Java Version: " + requiredVersion);

        if (requiredVersion.compareTo(systemVersion) > 0)
        {
            JDialog dialog = new JDialog();
            dialog.setLayout(new BorderLayout());

            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            dialog.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e)
                {
                    super.windowClosed(e);
                    System.out.println("Required java version is missing. Exiting.");
                    if (exitIfBadVersion)
                    {
                        System.exit(0);
                    }
                }
            });

            Panel panel = new Panel();
            panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
            dialog.setLocationRelativeTo(null);
            dialog.setPreferredSize(new Dimension(300, 200));
            JLabel l1 = new JLabel("Required Java Version: " + requiredVersion );
            JLabel l2 = new JLabel("Current Version: " + systemVersion);
            final JLabel downloadJavaLink = new JLabel();
            downloadJavaLink.setText("<html> Website : <a href=\"\">https://java.com</a></html>");
            downloadJavaLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
            downloadJavaLink.addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    try
                    {
                        Desktop.getDesktop().browse(new URI("https://java.com"));
                        // They are handling the new java action. Go ahead and exit.
                        if (exitIfBadVersion)
                        {
                            System.exit(0);
                        }
                        else
                        {
                            dialog.dispose();
                        }
                    }
                    catch (Exception ex)
                    {
                        ErrorHandler.handle(ex);
                    }
                }
            });

            panel.add(l1);
            panel.add(l2);

            if (additionalMessage != null)
            {
                JLabel additionalMessageLabel = new JLabel(additionalMessage);
                panel.add(additionalMessageLabel);
            }

            panel.add(downloadJavaLink);
            dialog.add(panel, BorderLayout.CENTER);

            dialog.pack();
            dialog.setVisible(true);
            return false;
        }
        return true;
    }

    public Version getVersion()
    {
        return parse(System.getProperty("java.version"));
    }

    public Version parseVersion(String versionString)
    {
        Version version = new Version();
        version.setJavaVersionString(versionString);

        String[] numStrings = versionString.split("\\.");

        if (numStrings.length>3)
        {
            throw new NumberFormatException("Version \""+versionString+"\" must have one the following formats xx.xx.xx, xx.xx.xx-xx, or xx.xx.xx_xx ");
        }

        version.setMajorVersion(Integer.parseInt(numStrings[0]));
        version.setMinorVersion(Integer.parseInt(numStrings[1]));

        boolean containsHyphen = numStrings[2].contains("-");
        boolean containsUnderscore = numStrings[2].contains("_");

        if ( containsHyphen || containsUnderscore )
        {
            if (containsUnderscore)
            {
                numStrings = numStrings[2].split("_");
            }
            else
            {
                numStrings = numStrings[2].split("-");
            }

            version.setMajorBuildNo(Integer.parseInt(numStrings[0]));
            version.setMinorBuildNo(Integer.parseInt(numStrings[1]));

        }
        return version;
    }

    public static Version getJavaVersion()
    {
        VersionParser versionParser = new VersionParser();
        return versionParser.getVersion();
    }

    public static Version parse(String javaVersionString)
    {
        VersionParser versionParser = new VersionParser();
        return versionParser.parseVersion(javaVersionString);
    }

    public static boolean javaVersionCheck(String lowestVersion, String additionalMessage, boolean exitIfBad)
    {
        VersionParser versionParser = new VersionParser();
        return versionParser.checkMyJavaVersion(lowestVersion, additionalMessage, exitIfBad);
    }

}

package com.bookmarking.util;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import com.bookmarking.error.*;

public class VersionParser
{

    public boolean checkMyVersion(String lowestVersion, String additionalMessage, boolean exitIfBadVersion)
    {
        // Detecting java version, and bailing if it is not the right version.
        Version systemVersion = VersionParser.getJavaVersion();
        System.out.println("System Java Version: " + systemVersion);
        Version requiredVersion = VersionParser.parseJavaVersion(lowestVersion);
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
        return parseJavaVersion(System.getProperty("java.version"));
    }

    public Version parseVersion(String javaVersionString)
    {
        Version version = new Version();
        version.setJavaVersionString(javaVersionString);

        String[] numStrings = javaVersionString.split("\\.");

        version.setMajorVersion(Integer.parseInt(numStrings[0]));
        version.setMinorVersion(Integer.parseInt(numStrings[1]));

        if (numStrings[2].contains("_") || numStrings[2].contains("-"))
        {
            if (numStrings[2].contains("_"))
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

    public static Version parseJavaVersion(String javaVersionString)
    {
        VersionParser versionParser = new VersionParser();
        return versionParser.parseVersion(javaVersionString);
    }

    public static boolean javaVersionCheck(String lowestVersion, String additionalMessage, boolean exitIfBad)
    {
        VersionParser versionParser = new VersionParser();
        return versionParser.checkMyVersion(lowestVersion, additionalMessage, exitIfBad);
    }

    public static Version parse(String versionString)
    {
        VersionParser versionParser = new VersionParser();
        return versionParser.parseVersion(versionString);
    }
}

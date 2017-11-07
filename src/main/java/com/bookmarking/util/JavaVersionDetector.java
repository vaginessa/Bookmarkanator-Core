package com.bookmarking.util;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import com.bookmarking.error.*;

public class JavaVersionDetector
{

    public boolean checkMyVersion(String lowestVersion, String additionalMessage, boolean exitIfBadVersion)
    {
        // Detecting java version, and bailing if it is not the right version.
        JavaVersionDetector.JavaVersion systemVersion = JavaVersionDetector.getJavaVersion();
        System.out.println("System Java Version: " + systemVersion);
        JavaVersionDetector.JavaVersion requiredVersion = JavaVersionDetector.parseJavaVersion(lowestVersion);
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

    public JavaVersion getVersion()
    {
        return parseJavaVersion(System.getProperty("java.version"));
    }

    public JavaVersion parseVersion(String javaVersionString)
    {
        JavaVersion javaVersion = new JavaVersion();
        javaVersion.setJavaVersionString(javaVersionString);

        String[] numStrings = javaVersionString.split("\\.");

        javaVersion.setMajorVersion(Integer.parseInt(numStrings[0]));
        javaVersion.setMinorVersion(Integer.parseInt(numStrings[1]));

        numStrings = numStrings[2].split("_");

        javaVersion.setMajorBuildNo(Integer.parseInt(numStrings[0]));
        javaVersion.setMinorBuildNo(Integer.parseInt(numStrings[1]));

        return javaVersion;
    }

    public static JavaVersion getJavaVersion()
    {
        JavaVersionDetector javaVersionDetector = new JavaVersionDetector();
        return javaVersionDetector.getVersion();
    }

    public static JavaVersion parseJavaVersion(String javaVersionString)
    {
        JavaVersionDetector javaVersionDetector = new JavaVersionDetector();
        return javaVersionDetector.parseVersion(javaVersionString);
    }

    public static boolean javaVersionCheck(String lowestVersion, String additionalMessage, boolean exitIfBad)
    {
        JavaVersionDetector javaVersionDetector = new JavaVersionDetector();
        return javaVersionDetector.checkMyVersion(lowestVersion, additionalMessage, exitIfBad);
    }

    public class JavaVersion implements Comparable
    {
        private int majorVersion;
        private int minorVersion;
        private int majorBuildNo;
        private int minorBuildNo;
        private String javaVersionString;

        public String getJavaVersionString()
        {
            return javaVersionString;
        }

        public void setJavaVersionString(String javaVersionString)
        {
            this.javaVersionString = javaVersionString;
        }

        public int getMajorVersion()
        {
            return majorVersion;
        }

        public void setMajorVersion(int majorVersion)
        {
            this.majorVersion = majorVersion;
        }

        public int getMinorVersion()
        {
            return minorVersion;
        }

        public void setMinorVersion(int minorVersion)
        {
            this.minorVersion = minorVersion;
        }

        public int getMajorBuildNo()
        {
            return majorBuildNo;
        }

        public void setMajorBuildNo(int majorBuildNo)
        {
            this.majorBuildNo = majorBuildNo;
        }

        public int getMinorBuildNo()
        {
            return minorBuildNo;
        }

        public void setMinorBuildNo(int minorBuildNo)
        {
            this.minorBuildNo = minorBuildNo;
        }

        public String toJSON()
        {
            return "JavaVersion{" + "majorVersion=" + majorVersion + ", minorVersion=" + minorVersion + ", majorBuildNo=" + majorBuildNo +
                ", minorBuildNo=" + minorBuildNo + ", javaVersionString='" + javaVersionString + '\'' + '}';
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            JavaVersion that = (JavaVersion) o;

            if (getMajorVersion() != that.getMajorVersion())
                return false;
            if (getMinorVersion() != that.getMinorVersion())
                return false;
            if (getMajorBuildNo() != that.getMajorBuildNo())
                return false;
            return getMinorBuildNo() == that.getMinorBuildNo();
        }

        @Override
        public int hashCode()
        {
            int result = getMajorVersion();
            result = 31 * result + getMinorVersion();
            result = 31 * result + getMajorBuildNo();
            result = 31 * result + getMinorBuildNo();
            return result;
        }

        @Override
        public String toString()
        {
            return javaVersionString;
        }

        @Override
        public int compareTo(Object o)
        {
            if (o instanceof JavaVersion)
            {
                JavaVersion other = (JavaVersion) o;

                int tmp = this.getMajorVersion() - other.getMajorVersion();

                if (tmp != 0)
                {
                    return tmp;
                }
                else
                {// Compare next version number
                    tmp = this.getMinorVersion() - other.getMinorVersion();

                    if (tmp != 0)
                    {
                        return tmp;
                    }
                    else
                    {// Compare next version number
                        tmp = this.getMajorBuildNo() - other.getMajorBuildNo();

                        if (tmp != 0)
                        {
                            return tmp;
                        }
                        else
                        {// Compare next version number
                            tmp = this.getMinorBuildNo() - other.getMinorBuildNo();

                            return tmp;
                        }
                    }
                }
            }
            return 1;
        }
    }
}

package com.bookmarking.util;

public class Version implements Comparable
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

        Version that = (Version) o;

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
        if (o instanceof Version)
        {
            Version other = (Version) o;

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

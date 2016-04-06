package com.bookmarkanator.settings;

import java.util.*;
import com.bookmarkanator.resourcetypes.*;

public class SystemType
{
    private String systemName;
    private String systemVersionName;
    private String systemVersion;
    private List<BasicResource> resourceList;

    public String getSystemName()
    {
        return systemName;
    }

    public void setSystemName(String systemName)
    {
        this.systemName = systemName;
    }

    public String getSystemVersionName()
    {
        return systemVersionName;
    }

    public void setSystemVersionName(String systemVersionName)
    {
        this.systemVersionName = systemVersionName;
    }

    public String getSystemVersion()
    {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion)
    {
        this.systemVersion = systemVersion;
    }

    public List<BasicResource> getResourceList()
    {
        return resourceList;
    }

    public void setResourceList(List<BasicResource> resourceList)
    {
        this.resourceList = resourceList;
    }
}

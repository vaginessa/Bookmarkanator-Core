package com.bookmarkanator.settings;

import java.util.*;
import com.bookmarkanator.interfaces.*;

public class Settings implements XMLWritable
{
    private String version;
    private List<SystemType> systemTypes;

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public List<SystemType> getSystemTypes()
    {
        return systemTypes;
    }

    public void setSystemTypes(List<SystemType> systemTypes)
    {
        this.systemTypes = systemTypes;
    }

    public void toXML(StringBuilder sb, String prependTabs)
    {
        sb.append("\n<settings version=\"");
        sb.append(getVersion());
        sb.append("\">\n");

        for (SystemType systemType: getSystemTypes())
        {
            systemType.toXML(sb,"\t");
            sb.append("\n");
        }

        sb.append("</settings>");
    }


}

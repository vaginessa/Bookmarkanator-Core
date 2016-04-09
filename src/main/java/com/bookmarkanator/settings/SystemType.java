package com.bookmarkanator.settings;

import java.util.*;
import com.bookmarkanator.interfaces.*;
import com.bookmarkanator.resourcetypes.*;

public class SystemType implements XMLWritable
{
    private String systemName;
    private String systemVersionName;
    private String systemVersion;
    private List<BasicResource> resourceList;

    public SystemType()
    {
        resourceList = new ArrayList<>();
    }

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

    public void addResource(BasicResource resource)
    {
        resourceList.add(resource);
    }

//    public void toXML(StringBuilder sb, String prependTabs)
//    {
//        sb.append(prependTabs+"<system name=\"");
//        sb.append(getSystemName());
//        sb.append("\" version=\"");
//        sb.append(getSystemVersion());
//        sb.append("\" versionname=\"");
//        sb.append(getSystemVersionName());
//        sb.append("\">");
//        sb.append("\n");
//
//        for (BasicResource br: getResourceList())
//        {
//            br.toXML(sb,prependTabs+"\t");
//            sb.append("\n");
//        }
//        sb.append(prependTabs+"</system>");
//    }

    public void toXML(StringBuilder sb, String prependTabs)
    {
        sb.append(prependTabs+"<system name=\"");
        sb.append(getSystemName());
        sb.append("\" version=\"");
        sb.append(getSystemVersion());
        sb.append("\" versionname=\"");
        sb.append(getSystemVersionName());
        sb.append("\">");

        for (BasicResource br: getResourceList())
        {
            br.toXML(sb,prependTabs+"");
        }
        sb.append(prependTabs+"</system>");
    }

    @Override
    public int hashCode() {
        return systemName.hashCode()+systemVersion.hashCode()+systemVersionName.hashCode()+resourceList.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj!=null)
        {
            if (obj == this) {
                return true;
            }

            if (obj instanceof SystemType)
            {
                SystemType st = (SystemType)obj;

                if (st.getSystemName().equals(systemName))
                {
                    if (st.getSystemVersion().equals(getSystemVersion()))
                    {
                        if (st.getSystemVersionName().equals(getSystemVersionName()))
                        {
                            if (st.getResourceList().size()==getResourceList().size())
                            {//deep equals
                                for (BasicResource b: st.getResourceList())
                                {
                                    int a = getResourceList().indexOf(b);

                                    if (a>-1)
                                    {
                                        if (!b.equals(getResourceList().get(a)))
                                        {
                                            return false;
                                        }
                                    }
                                    else
                                    {
                                        return false;
                                    }
                                }
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}

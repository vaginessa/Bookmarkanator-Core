package com.bookmarkanator.settings;

import java.util.*;
import com.bookmarkanator.interfaces.*;

/**
 * This class represents system specific configuration.
 *
 * <p>
 *     This class will be written to/read from the settings file for the app. Entries in the settings file represent custom resource types, or standard
 *     resource types that are being overwritten for this system for whatever reason.
 * </p>
 * <p>
 *     For instance one might prefer to override the default file editor resource type. To do this they would add an entry in the settings
 *     file with the name <file-editor> and the app will use the settings for this entry in place of the standard (java based) file editor settings.
 *     When a file-editor bookmark is accessed, instead of Java calling the default system file editor, it will run the commands specified in the
 *     <file-editor> tag.
 * </p>
 * <p>
 *     This would be an override. If one wanted to have multiple file editors referenced in the bookmarks, they would not override the file-editor,
 *     but would add terminal resource entries in the settings file. The default file editor, and the newly defined file editors will be
 *     displayed in the app.
 * </p>
 * <p>
 *     The settings file is used to override standard bookmark types, or add to the list of available bookmark types the user will choose from when
 *     creating new bookmarks.
 * </p>
 */
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

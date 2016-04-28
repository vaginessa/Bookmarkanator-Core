package com.bookmarkanator.settings;

import java.util.*;
import com.bookmarkanator.interfaces.*;

/**
 * The quick access tag group object is used to store state about what are basically shortcuts to groups of tags.
 */
public class QuickAccessTagGroup implements XMLWritable
{
    private Set<String> tags;
    private int index;
    private String label;

    public QuickAccessTagGroup()
    {
        tags = new HashSet<>();
    }

    public Set<String> getTags()
    {
        return tags;
    }

    public void setTags(Set<String> tags)
    {
        this.tags = tags;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public void addTag(String tag)
    {
        tags.add(tag);
    }

    @Override
    public void toXML(StringBuilder sb, String prependTabs)
    {
        sb.append(prependTabs);
        sb.append("\t<quick-access-tag-group label=\"");
        sb.append(getLabel());
        sb.append("\" index=\"");
        sb.append(getIndex());
        sb.append("\" tags=\"");

        Iterator<String> i = getTags().iterator();
        while (i.hasNext())
        {
            sb.append(i.next());
            if (i.hasNext())
            {
                sb.append(",");
            }
        }
        sb.append("\"/>");

    }
}

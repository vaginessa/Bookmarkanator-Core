package com.bookmarkanator.core;

import java.net.*;
import java.util.*;

/**
 * This class represents a settings item in list form.
 */
public class SettingsItemList extends SettingItem
{
    private List<String> items;
    public SettingsItemList(String key)
    {
        super(key);
        items = new ArrayList<>();
    }

    public void addItem(String item)
        throws Exception
    {
        items.add(item);
        value = getValue();
    }

    public boolean remove(String item)
        throws Exception
    {
        boolean res = items.remove(item);
        value = getValue();
        return res;
    }

    public List<String> getItems()
    {
        return items;
    }

    @Override
    public void setValue(String settingString)
        throws Exception
    {
        Objects.requireNonNull(settingString, "Setting string must be not null.");
        String[] strings = settingString.split(",");

        for (String s: strings)
        {
            this.addItem(URLDecoder.decode(s, "UTF-8"));
        }
        super.setValue(settingString);
    }

    @Override
    public String getValue()
    {
        StringBuilder sb = new StringBuilder(items.size());

        for (int c=0;c<items.size();c++)
        {
            String i = items.get(c);
            sb.append(i);

            if (c<(items.size()-1))
            {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}

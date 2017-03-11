package com.bookmarkanator.core;

import java.net.*;
import java.util.*;

public class SettingsItemList extends SettingItem
{
    private List<String> items;
    public SettingsItemList(String key)
    {
        super(key);
        items = new ArrayList<>();
    }

    public void addItem(String item)
    {
        items.add(item);
    }

    public boolean remove(String item)
    {
        return items.remove(item);
    }

    public List<String> getItems()
    {
        return items;
    }

    @Override
    public void setSetting(String settingString)
        throws Exception
    {
        Objects.requireNonNull(settingString, "Setting string must be not null.");
        String[] strings = settingString.split(",");

        for (String s: strings)
        {
            this.addItem(URLDecoder.decode(s, "UTF-8"));
        }
        super.setSetting(settingString);
    }

    @Override
    public String getSetting()
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

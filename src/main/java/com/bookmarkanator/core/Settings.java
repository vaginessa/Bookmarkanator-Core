package com.bookmarkanator.core;

import java.io.*;
import java.util.*;
import com.bookmarkanator.xml.*;

public class Settings
{
    private Map<String, SettingItem> map;
    private Map<String, Set<SettingItem>> typesMap;

    public Settings()
    {
        map = new HashMap<>();
        typesMap = new HashMap<>();
    }

    public void putSettings(List<SettingItem> list)
    {
        if (list==null || list.isEmpty())
        {
            return;
        }

        for (SettingItem itemInterface: list)
        {
            putSetting(itemInterface);
        }
    }

    public void putSetting(SettingItem itemInterface)
    {
        map.put(itemInterface.getKey(), itemInterface);

        Set<SettingItem> tmp = typesMap.get(itemInterface.getType());
        if (tmp==null)
        {
            tmp=new HashSet<>();
            typesMap.put(itemInterface.getType(), tmp);
        }

        tmp.add(itemInterface);
    }

    public Set<SettingItem> getByType(String type)
    {
        return typesMap.get(type);
    }

    public SettingItem getSetting(String key)
    {
        return map.get(key);
    }

    public Map<String, SettingItem> getSettingsMap()
    {
        return Collections.unmodifiableMap(map);
    }

    public Map<String, Set<SettingItem>> getSettingsTypesMap()
    {
        return Collections.unmodifiableMap(typesMap);
    }

    /**
     * This method imports settings from another settings object. If a value exists in the imported settings object it
     * replaces the existing object.
     * For example:
     * This Settings Object
     * A = 1
     * B = 2
     * C = 3
     * E = 4
     * <p/>
     * Diff settings Object
     * A = 7
     * B = 2
     * D = 5
     * F = 21
     * <p/>
     * After calling this importSettings method the this settings object would look like:
     * <p/>
     * This Settings Object
     * A = 1
     * B = 2
     * C = 3
     * D = 5
     * E = 4
     * F = 21
     * <p/>
     * If a setting is present in this settings object it leaves it alone, if it is missing it adds it.
     *
     * @param settings The settings to diff into this settings object.
     */
    public boolean importSettings(Settings settings)
    {
        boolean hasChanged = false;
        Map<String, SettingItem> tmpMap = settings.getSettingsMap();

        for (String key : tmpMap.keySet())
        {//ensure default settings are in place if other settings are missing.
            SettingItem item = tmpMap.get(key);
            if (item != null)
            {
                this.putSetting(item);
                hasChanged = true;
            }
        }
        return hasChanged;
    }

    // ============================================================
    // Static Methods
    // ============================================================

    public static void validateXML(InputStream inputStream, String xsdFile)
        throws Exception
    {
        InputStream xsd = Settings.class.getResourceAsStream(xsdFile);
        XMLValidator.validate(inputStream, xsd);
    }

    public static Settings parseSettings(InputStream in)
        throws Exception
    {
        SettingsXMLParser parser = new SettingsXMLParser(in);
        return parser.parse();
    }

    public static String settingsToString(Settings globalSettings)
        throws Exception
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        Settings.writeSettings(globalSettings, bout);
        return bout.toString();
    }

    public static void writeSettings(Settings globalSettings, OutputStream out)
        throws Exception
    {
        SettingsXMLWriter writer = new SettingsXMLWriter(globalSettings, out);
        writer.write();
        out.flush();
        out.close();
    }
}

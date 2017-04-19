package com.bookmarkanator.core;

import java.io.*;
import java.util.*;
import com.bookmarkanator.xml.*;

/**
 * This class is used to as the main source of settings for the bookmarkanator program. All versions of this program
 * will at a minimum need some way to access settings.
 */
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

    public SettingItem getSetting(String type, String key)
    {
        Set<SettingItem> settingItems = typesMap.get(type);

        if (settingItems==null)
        {
            return null;
        }

        for (SettingItem settingItem: settingItems)
        {
            if (settingItem.getKey().equals(key))
            {
                return settingItem;
            }
        }
        return null;
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
        //TODO Fix importing of settings to use the settings types and not necessarily replace settings, but merge them.
        boolean hasChanged = false;
        Map<String, SettingItem> tmpMap = settings.getSettingsMap();

        for (String key : tmpMap.keySet())
        {//ensure default settings are in place if other settings are missing.
            SettingItem item = tmpMap.get(key);
            SettingItem original = this.getSettingsMap().get(key);

            if (original == null)
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

    public static Collection<String> extractKeys(Collection<SettingItem> settingItems)
    {
        Objects.requireNonNull(settingItems);

        Collection<String> res = new HashSet<>();

        for (SettingItem settingItem: settingItems)
        {
            res.add(settingItem.getKey());
        }

        return res;
    }

    public static Collection<String> extractValues(Collection<SettingItem> settingItems)
    {
        Objects.requireNonNull(settingItems);

        Collection<String> res = new HashSet<>();

        for (SettingItem settingItem: settingItems)
        {
            res.add(settingItem.getValue());
        }

        return res;
    }

    public static void validateXML(InputStream inputStream, String xsdFile)
        throws Exception
    {
        InputStream xsd = Settings.class.getResourceAsStream(xsdFile);
        XMLValidator.validate(inputStream, xsd);
    }

    public static Settings parseSettings(InputStream in, ClassLoader classLoader)
        throws Exception
    {
        SettingsXMLParser parser = new SettingsXMLParser(in, classLoader);
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

    /**
     * Saves specific settings to a specific directory (with the default setting file name)
     * @param settings  The settings to save
     * @param directory  The directory to place the settings file.
     * @throws Exception
     */
    public void saveSettingsFile(Settings settings, File directory)
        throws Exception
    {
        FileOutputStream fout = new FileOutputStream(directory);
        Settings.writeSettings(settings, fout);
    }



}

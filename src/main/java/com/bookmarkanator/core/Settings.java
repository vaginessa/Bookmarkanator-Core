package com.bookmarkanator.core;

import java.util.*;
import org.apache.logging.log4j.*;

/**
 * This class is used to as the main source of settings for the bookmarkanator program. All versions of this program
 * will at a minimum need some way to access settings.
 */
public class Settings
{
    private static final Logger logger = LogManager.getLogger(Settings.class.getCanonicalName());
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
     * This method imports settings from another settings object.
     *
     * For example:
     * This Settings Object
     * A = 1
     * B = 2
     * C = 3
     * E = 4
     * <p/>
     * Imported settings Object
     * A = 7
     * B = 2
     * D = 5
     * F = 21
     * <p/>
     * After calling this importSettings method the settings object would look like:
     * <p/>
     * This Settings Object
     * A = 1
     * B = 2
     * C = 3
     * D = 5
     * E = 4
     * F = 21
     * <p/>
     *
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
}

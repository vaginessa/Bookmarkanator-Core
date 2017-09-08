package com.bookmarking;

import java.util.*;
import com.bookmarking.exception.*;
import org.apache.logging.log4j.*;

/**
 * This class is used to as the main source of settings for the bookmarking program. All versions of this program
 * will at a minimum need some way to access settings.
 */
public class Settings
{
    private static final Logger logger = LogManager.getLogger(Settings.class.getCanonicalName());

    // <Type, Map<Key, SettingItem>
    private Map<String, Map<String, SettingItem>> typesMap;

    public Settings()
    {
        typesMap = new HashMap<>();
    }

    public void putSettings(List<SettingItem> list)
    {
        if (list == null || list.isEmpty())
        {
            return;
        }

        for (SettingItem itemInterface : list)
        {
            putSetting(itemInterface);
        }
    }

    public void putSetting(SettingItem setting)
    {
        Objects.requireNonNull(setting);
        Objects.requireNonNull(setting.type);
        Objects.requireNonNull(setting.key);

        Map<String, SettingItem> tmp = typesMap.get(setting.getType());
        if (tmp == null)
        {
            tmp = new HashMap<>();
            typesMap.put(setting.getType(), tmp);
        }

        tmp.put(setting.getKey(), setting);
    }

    public Set<SettingItem> getByType(String type)
    {
        Map<String, SettingItem> res = typesMap.get(type);

        if (res == null)
        {
            return null;
        }

        return new HashSet<>(res.values());
    }

    public SettingItem getSetting(String type, String key)
    {
        Map<String, SettingItem> settingItems = typesMap.get(type);

        if (settingItems == null)
        {
            return null;
        }

        return settingItems.get(key);
    }

    public Map<String, Map<String, SettingItem>> getSettingsTypesMap()
    {
        return Collections.unmodifiableMap(typesMap);
    }

    public void renameType(String original, String newName)
    {
        Map<String, SettingItem> settingItems = typesMap.get(original);

        if (settingItems != null)
        {
            typesMap.remove(original);

            for (SettingItem settingItem : settingItems.values())
            {
                settingItem.setType(newName);
                this.putSetting(settingItem);
            }
        }
    }

    public void deleteType(String type)
    {
        typesMap.remove(type);
    }

    public void renameKey(String type, String key, String newKey)
        throws DuplicateKeyException
    {
        if (key.trim().equals(newKey.trim()))
        {
            return;
        }

        Map<String, SettingItem> settingsMap = typesMap.get(type);

        if (settingsMap != null)
        {
            if (settingsMap.containsKey(newKey))
            {
                throw new DuplicateKeyException("Key \"" + newKey + "\" is already present in this settings object for type \"" + type + "\"");
            }

            SettingItem settingItem = settingsMap.remove(key);

            if (settingItem != null)
            {
                settingItem.setKey(newKey);
                settingsMap.put(newKey, settingItem);
            }
        }
    }

    public void deleteKeyValuePair(String type, String key)
    {
        Map<String, SettingItem> settingsMap = typesMap.get(type);

        if (settingsMap != null)
        {
            settingsMap.remove(key);
        }
    }

    /**
     * This method imports settings from another settings object.
     * <p>
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
     * <p>
     * If a setting is present in this settings object it leaves it alone, if it is missing it adds it.
     *
     * @param other The settings to diff into this settings object.
     */
    public boolean importSettings(Settings other)
    {
        boolean hasChanged = false;
        Map<String, Map<String, SettingItem>> otherTypes = other.getSettingsTypesMap();

        for (String key : otherTypes.keySet())
        {
            Map<String, SettingItem> otherTypeMap = other.getSettingsTypesMap().get(key);
            Map<String, SettingItem> thisTypeMap = this.getSettingsTypesMap().get(key);

            // Add the type if it is not present.
            if (thisTypeMap == null)
            {
                thisTypeMap = new HashMap<>();
                this.getSettingsTypesMap().put(key, thisTypeMap);
                hasChanged = true;
            }

            // Add values not present in the type
            for (String otherKey : otherTypeMap.keySet())
            {
                if (!thisTypeMap.containsKey(otherKey))
                {
                    thisTypeMap.put(otherKey, otherTypeMap.get(otherKey));
                }
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

        for (SettingItem settingItem : settingItems)
        {
            res.add(settingItem.getKey());
        }

        return res;
    }

    public static Collection<String> extractValues(Collection<SettingItem> settingItems)
    {
        Objects.requireNonNull(settingItems);

        Collection<String> res = new HashSet<>();

        for (SettingItem settingItem : settingItems)
        {
            res.add(settingItem.getValue());
        }

        return res;
    }
}

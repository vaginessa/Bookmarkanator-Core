package com.bookmarking.settings;

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
    private Map<String, Map<String, Setting>> typesMap;

    public Settings()
    {
        typesMap = new HashMap<>();
    }

    public void putSettings(List<Setting> list)
    {
        if (list == null || list.isEmpty())
        {
            return;
        }

        for (Setting itemInterface : list)
        {
            putSetting(itemInterface);
        }
    }

    public void putSetting(Setting setting)
    {
        Objects.requireNonNull(setting);
        Objects.requireNonNull(setting.group);
        Objects.requireNonNull(setting.key);

        Map<String, Setting> tmp = typesMap.get(setting.getGroup());
        if (tmp == null)
        {
            tmp = new HashMap<>();
            typesMap.put(setting.getGroup(), tmp);
        }

        tmp.put(setting.getKey(), setting);
    }

    public Set<Setting> getByType(String type)
    {
        Map<String, Setting> res = typesMap.get(type);

        if (res == null)
        {
            return null;
        }

        return new HashSet<>(res.values());
    }

    public Setting getSetting(String type, String key)
    {
        Map<String, Setting> settingItems = typesMap.get(type);

        if (settingItems == null)
        {
            return null;
        }

        return settingItems.get(key);
    }

    public Map<String, Map<String, Setting>> getSettingsTypesMap()
    {
        return Collections.unmodifiableMap(typesMap);
    }

    public void renameType(String original, String newName)
    {
        Map<String, Setting> settingItems = typesMap.get(original);

        if (settingItems != null)
        {
            typesMap.remove(original);

            for (Setting setting : settingItems.values())
            {
                setting.setGroup(newName);
                this.putSetting(setting);
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

        Map<String, Setting> settingsMap = typesMap.get(type);

        if (settingsMap != null)
        {
            if (settingsMap.containsKey(newKey))
            {
                throw new DuplicateKeyException("Key \"" + newKey + "\" is already present in this settings object for group \"" + type + "\"");
            }

            Setting setting = settingsMap.remove(key);

            if (setting != null)
            {
                setting.setKey(newKey);
                settingsMap.put(newKey, setting);
            }
        }
    }

    public void deleteKeyValuePair(String type, String key)
    {
        Map<String, Setting> settingsMap = typesMap.get(type);

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
        Map<String, Map<String, Setting>> otherTypes = other.getSettingsTypesMap();

        for (String key : otherTypes.keySet())
        {
            Map<String, Setting> otherTypeMap = other.getSettingsTypesMap().get(key);
            Map<String, Setting> thisTypeMap = this.getSettingsTypesMap().get(key);

            // Add the group if it is not present.
            if (thisTypeMap == null)
            {
                thisTypeMap = new HashMap<>();
                this.getSettingsTypesMap().put(key, thisTypeMap);
                hasChanged = true;
            }

            // Add values not present in the group
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

    public static Collection<String> extractKeys(Collection<Setting> settings)
    {
        Objects.requireNonNull(settings);

        Collection<String> res = new HashSet<>();

        for (Setting setting : settings)
        {
            res.add(setting.getKey());
        }

        return res;
    }

    public static Collection<String> extractValues(Collection<Setting> settings)
    {
        Objects.requireNonNull(settings);

        Collection<String> res = new HashSet<>();

        for (Setting setting : settings)
        {
            res.add(setting.getValue());
        }

        return res;
    }
}

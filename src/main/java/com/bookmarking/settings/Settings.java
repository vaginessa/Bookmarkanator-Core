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
    public static String DEFAULT_SETTINGS_GROUP = "default";

    // <Group String, SetingsGroup>
    private HashMap<String, SettingsGroup> groups;

    public Settings()
    {
        groups = new HashMap<>();
    }

    public HashMap<String, SettingsGroup> getGroups()
    {
        return groups;
    }

    public void setGroups(HashMap<String, SettingsGroup> groups)
    {
        this.groups = groups;
    }

    //    public void putSettings(List<AbstractSetting> list)
//    {
//        if (list == null || list.isEmpty())
//        {
//            return;
//        }
//
//        for (AbstractSetting itemInterface : list)
//        {
//            putSetting(itemInterface);
//        }
//    }

    public void putSetting(AbstractSetting setting)
        throws Exception
    {
        Objects.requireNonNull(setting);
        if (setting.getKey()==null && setting.value!=null)
        {
            throw new Exception("Value present without key");
        }

        if (setting.getGroup()==null || setting.getGroup().trim().isEmpty())
        {
            setting.setGroup(DEFAULT_SETTINGS_GROUP);
        }

        SettingsGroup group= this.groups.get(setting.getGroup());

        if (group==null)
        {
            group = new SettingsGroup();
            this.groups.put(setting.getGroup(), group);
        }

        if (setting.getKey()==null)
        {
            return;
        }

        group.getSettings().put(setting.getKey(), setting);
    }

    public Set<AbstractSetting> getByType(String type)
    {
//        Map<String, Setting> res = groups.get(type);
//
//        if (res == null)
//        {
//            return null;
//        }
//
//        return new HashSet<>(res.values());
        return null;
    }

    public AbstractSetting getSetting(String type, String key)
    {
//        Map<String, Setting> settingItems = groups.get(type);
//
//        if (settingItems == null)
//        {
//            return null;
//        }
//
//        return settingItems.get(key);
        return null;
    }

    public Map<String, Map<String, AbstractSetting>> getSettingsTypesMap()
    {
//        return Collections.unmodifiableMap(groups);
        return null;
    }

    public void renameType(String original, String newName)
    {
//        Map<String, Setting> settingItems = groups.get(original);
//
//        if (settingItems != null)
//        {
//            groups.remove(original);
//
//            for (Setting setting : settingItems.values())
//            {
//                setting.setGroup(newName);
//                this.putSetting(setting);
//            }
//        }
    }

    public void deleteType(String type)
    {
        groups.remove(type);
    }

    public void renameKey(String type, String key, String newKey)
        throws DuplicateKeyException
    {
        if (key.trim().equals(newKey.trim()))
        {
            return;
        }

//        Map<String, Setting> settingsMap = groups.get(type);
//
//        if (settingsMap != null)
//        {
//            if (settingsMap.containsKey(newKey))
//            {
//                throw new DuplicateKeyException("Key \"" + newKey + "\" is already present in this settings object for groups \"" + type + "\"");
//            }
//
//            Setting setting = settingsMap.remove(key);
//
//            if (setting != null)
//            {
//                setting.setKey(newKey);
//                settingsMap.put(newKey, setting);
//            }
//        }
    }

    public void deleteKeyValuePair(String type, String key)
    {
//        Map<String, Setting> settingsMap = groups.get(type);
//
//        if (settingsMap != null)
//        {
//            settingsMap.remove(key);
//        }
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
        Map<String, Map<String, AbstractSetting>> otherTypes = other.getSettingsTypesMap();

        for (String key : otherTypes.keySet())
        {
            Map<String, AbstractSetting> otherTypeMap = other.getSettingsTypesMap().get(key);
            Map<String, AbstractSetting> thisTypeMap = this.getSettingsTypesMap().get(key);

            // Add the groups if it is not present.
            if (thisTypeMap == null)
            {
                thisTypeMap = new HashMap<>();
                this.getSettingsTypesMap().put(key, thisTypeMap);
                hasChanged = true;
            }

            // Add values not present in the groups
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

    public static Collection<String> extractKeys(Collection<AbstractSetting> settings)
    {
        Objects.requireNonNull(settings);

        Collection<String> res = new HashSet<>();

        for (AbstractSetting setting : settings)
        {
            res.add(setting.getKey());
        }

        return res;
    }

    public static Collection<String> extractValues(Collection<AbstractSetting> settings)
    {
        Objects.requireNonNull(settings);

        Collection<String> res = new HashSet<>();

        for (AbstractSetting setting : settings)
        {
//            res.add(setting.getValue());
        }

        return res;
    }

    @Override
    public int hashCode()
    {
        return this.getGroups().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Settings)
        {
            Settings other = (Settings)obj;

            return this.getGroups().equals(other.getGroups());
        }
        return false;
    }
}

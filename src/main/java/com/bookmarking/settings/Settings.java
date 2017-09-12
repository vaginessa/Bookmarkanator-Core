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

        public void putSettings(List<AbstractSetting> list)
            throws Exception
        {
        if (list == null || list.isEmpty())
        {
            return;
        }

        for (AbstractSetting itemInterface : list)
        {
            putSetting(itemInterface);
        }
    }

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

    public Set<AbstractSetting> getByType(Class typeClass)
    {
        Set<AbstractSetting>  res = new HashSet<>();

        for (String s: getGroups().keySet())
        {
            SettingsGroup group = getGroups().get(s);

            for (AbstractSetting abstractSetting: group.getSettings().values())
            {
                if (abstractSetting.getValue().getClass().equals(typeClass))
                {
                    res.add(abstractSetting);
                }
            }
        }

        return res;
    }

    public Set<AbstractSetting> getByGroup(String group)
    {
        Set<AbstractSetting> res = new HashSet<>();
        SettingsGroup settingsGroup = getGroups().get(group);
        if (settingsGroup==null)
        {
            return res;
        }

        res.addAll(settingsGroup.getSettings().values());

        return res;

    }

    public Set<AbstractSetting> getByGroupAndtype(String group, Class typeClass)
    {
        Set<AbstractSetting> res = new HashSet<>();
        SettingsGroup settingsGroup = getGroups().get(group);
        if (settingsGroup==null)
        {
            return res;
        }

        for (AbstractSetting abstractSetting: settingsGroup.getSettings().values())
        {
            if (abstractSetting.getValue().getClass().equals(typeClass))
            {
                res.add(abstractSetting);
            }
        }

        return res;

    }

    public AbstractSetting getSetting(String group, String key)
    {
        SettingsGroup settingsGroup = getGroups().get(group);
        if (settingsGroup==null)
        {return null;}

        return settingsGroup.getSettings().get(key);
    }

    public void renameGroup(String original, String newName)
        throws DuplicateKeyException
    {
        if (getGroups().containsKey(newName))
        {
            throw new DuplicateKeyException("Duplicate key "+newName);
        }
        SettingsGroup settingsGroup = getGroups().get(original);
        getGroups().remove(original);
        getGroups().put(newName, settingsGroup);
    }

    public void deleteGroup(String groupName)
    {
        groups.remove(groupName);
    }

    public void renameKey(String group, String key, String newKey)
        throws Exception
    {
        SettingsGroup settingsGroup = getGroups().get(group);
        if (settingsGroup==null)
        {
            throw new Exception("Group "+group+" not found.");
        }

        AbstractSetting abstractSetting = settingsGroup.getSettings().get(key);
        if (abstractSetting==null)
        {
            throw new Exception("Key "+key+" not found.");
        }

        settingsGroup.getSettings().remove(key);

        abstractSetting.setKey(newKey);
        settingsGroup.getSettings().put(key, abstractSetting);
    }

    public boolean deleteKeyValuePair(String group, String key)
    {
        SettingsGroup settingsGroup = getGroups().get(group);

        if (settingsGroup!=null)
        {
            return settingsGroup.getSettings().remove(key)!=null;
        }

        return false;
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
        throws Exception
    {
        boolean hasChanged = false;

        for (String s: other.getGroups().keySet())
        {
            SettingsGroup otherGroup = other.getGroups().get(s);
            SettingsGroup thisGroup = this.getGroups().get(s);

            if (thisGroup==null)
            {
                // Group not present. Add all settings.
                for (AbstractSetting abstractSetting: otherGroup.getSettings().values())
                {
                    this.putSetting(abstractSetting);
                    hasChanged = true;
                }
            }
            else
            {
                // Group present. Selectively add settings.
                for (AbstractSetting abstractSetting: otherGroup.getSettings().values())
                {
                    if (!thisGroup.getSettings().containsKey(abstractSetting.getKey()))
                    {
                        this.putSetting(abstractSetting);
                        hasChanged = true;
                    }
                }
            }
        }

        return hasChanged;
    }

    // ============================================================
    // Static Methods
    // ============================================================

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
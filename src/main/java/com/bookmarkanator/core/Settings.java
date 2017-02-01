package com.bookmarkanator.core;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.bookmarkanator.xml.*;

public class Settings<K, V>
{
    private static final Logger logger = Logger.getLogger(Settings.class.getName());
    private Map<K, Object> map;

    public Settings()
    {
        map = new HashMap<>();
    }

    public void putSettings(K key, List<V> list)
    {
        logger.finest("Putting settings: key \"" + key + "");
        this.map.put(key, list);
    }

    public void putSetting(K key, V value)
    {
        logger.finest("Putting setting: key \"" + key + "");
        this.map.put(key, value);
    }

    public List<V> getSettings(K key)
    {
        Object object = this.map.get(key);

        if (object==null)
        {
            return null;
        }

        if (object instanceof List)
        {
            return (List<V>) object;
        }
        else
        {//Return single item list
            List<V> l = new ArrayList<>();
            l.add((V) object);
            return l;
        }
    }

    public V getSetting(K key)
    {
        Object object = this.map.get(key);

        if (object instanceof List)
        {//Convert single item list into a single item.
            List l = (List) object;
            if (l.size() == 1)
            {
                return (V) l.get(0);
            }
            return null;
        }
        else
        {
            return (V) object;
        }
    }

    public Set<K> keySet()
    {
        return map.keySet();
    }

    /**
     * This method accepts a global settings object, goes through it and sets any field that is not currently set in this settings object.
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
     * After calling this diffInto method the this settings object would look like:
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
     * @param diffSettings The settings to diff into this settings object.
     */
    public boolean diffInto(Settings<K, V> diffSettings)
    {
        boolean hasChanged = false;
        for (K s : diffSettings.keySet())
        {//ensure default settings are in place if other settings are missing.
            List<V> l = this.getSettings(s);
            if (l == null)
            {
                this.putSettings(s, diffSettings.getSettings(s));
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

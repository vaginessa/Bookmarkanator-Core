package com.bookmarking.update;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.bind.*;
import com.bookmarking.settings.*;
import com.bookmarking.settings.types.*;
import com.bookmarking.update.config.*;
import com.bookmarking.update.manifest.*;
import com.bookmarking.util.*;
import org.apache.logging.log4j.*;

/**
 * Updater that checks web repositories for updates.
 */
public class WebUpdater implements UpdaterInterface
{
    // ============================================================
    // Fields
    // ============================================================

    private static final Logger logger = LogManager.getLogger(WebUpdater.class.getCanonicalName());
    private UpdateUIInterface updateUIInterface;
    private boolean updateInterfacePresent = false;
    private Settings settings;

    // All values in this group are considered URL's to the repositories we need to check. The keys don't actually matter here as they are read in
    // as a list of items.
    public static final String WEB_UPDATER_REPOSITORIES_GROUP_KEY = "web-updater-repositories-group-key";
    // The key for local settings relating to the local config file
    public static final String WEB_UPDATER_LOCAL_SETTINGS_GROUP_KEY = "web-updater-local-settings-group-key";
    public static final String WEB_UPDATER_LOCAL_SETTINGS_CONFIG_FILE = "WEB_UPDATER_LOCAL_SETTINGS_CONFIG_FILE";

    public static final String WEB_UPDATER_CONFIG_URL_KEY = "web-updater-config-url-key";

    // Settings for storing update config in settings rather than a local file.
    public static final String UPDAT_CONFIG_SETTINGS_GROUP_KEY = "UPDAT_CONFIG_SETTINGS_GROUP_KEY";

    // ============================================================
    // Methods
    // ============================================================

    @Override
    public Map<URLSetting, Set<UpdateConfigEntry>> checkForUpdates(Settings settings)
        throws Exception
    {
        logger.info("Checking for updates");

        this.settings = settings;

        UpdateConfig updateConfig = loadLocalUpdateConfig();

        Map<URLSetting , Set<UpdateConfigEntry>> res = new HashMap<>();

        // Get all settings with the repositories group key - should all be url addresses of some kind.
        Set<AbstractSetting> settingsInGroup = settings.getByGroup(WEB_UPDATER_REPOSITORIES_GROUP_KEY);

        for (AbstractSetting abstractSetting: settingsInGroup)
        {
            if (abstractSetting instanceof URLSetting)
            {
                URLSetting urlSetting = (URLSetting)abstractSetting;

                // Download and parse config file
                UpdateManifest updateManifest = downloadUpdateManifest(urlSetting);

                if (updateManifest!=null)
                {
                    // Inspect items in the update list and compare against items in config.
                    for (UpdateManifestEntry entry : updateManifest.getResources())
                    {
                        UpdateConfigEntry configEntry = updateConfig.getUpdateConfigEntry(entry.getResourceKey());

                        if (shouldUpdate(configEntry, entry))
                        {
                            Set<UpdateConfigEntry> set = res.get(urlSetting);

                            if (set == null)
                            {
                                set = new HashSet<>();
                                res.put(urlSetting, set);
                            }

                            set.add(configEntry);
                        }
                    }
                }
            }
            else
            {
                logger.warn("Non URL setting found in \""+WEB_UPDATER_REPOSITORIES_GROUP_KEY+"\" group. Setting is of type \""+abstractSetting.getClass().getCanonicalName()+"\"");
            }
        }

        logger.info("Done checking for updates.");
        return res;
    }

    @Override
    public Map<String, String> performUpdates(Map<URLSetting, Set<UpdateConfigEntry>> updates)
    {
        logger.info("Performing updates...");
        Map<String, String> res = new HashMap<>();

//        for (UpdateConfigEntry update: updates)
//        {
//            logger.info("Doing fake update now... "+update.getResourceKey()+" "+update.getCurrentVersion());
//            // Download update
//                // Show download progress in UI Interface
//            // Compare file hash to config hash
//                // Update ui status
//            // Rename old file temporarily
//            // place new file in it's place
//            // update config with new version information
//            // If TransitionInterface present run transition interface
//                // Show transition interface output
//            // If all is well, delete old version.
//        }

        logger.info("Done performing updates.");
        return res;
    }

    @Override
    public void setUpdateUIInterface(UpdateUIInterface updateUIInterface)
    {
        this.updateUIInterface = updateUIInterface;
        this.updateInterfacePresent = this.updateUIInterface!=null;
    }

    @Override
    public UpdateUIInterface getUpdateUIInterface()
    {
        return this.updateUIInterface;
    }

    // ============================================================
    // Private Methods
    // ============================================================

    private boolean shouldUpdate(UpdateConfigEntry updateConfigEntry, UpdateManifestEntry updateManifestEntry)
    {
        Version currentVersion = VersionParser.parse(updateConfigEntry.getCurrentVersion());
        Version otherVersion = VersionParser.parse(updateManifestEntry.getVersion());

        return currentVersion.compareTo(otherVersion)<0;
    }

    /**
     * Loads the local update config file from disk, or extracts the config settings from settings.
     * @return UpdateConfig filled with config settings.
     */
    private UpdateConfig loadLocalUpdateConfig()
        throws Exception
    {
        UpdateConfig res = null;

        URLSetting urlSetting = (URLSetting) settings.getSetting(WEB_UPDATER_LOCAL_SETTINGS_GROUP_KEY, WEB_UPDATER_CONFIG_URL_KEY);
        if (urlSetting!=null)
        {
            // Try loading from disk
            return loadUpdateConfigFromDisk(urlSetting);
        }
        else
        {
            // Update config location is null, try loading settings from the settings passed into this class.
            return loadUpdateConfigFromSettings();
        }
    }

    private UpdateConfig loadUpdateConfigFromSettings()
        throws Exception
    {
        UpdateConfig res = new UpdateConfig();

        // Should be a bunch of string settings that can be parsed to get settings necessary for filling UpdateConfigEntry objects.
        Set<AbstractSetting> settings = this.settings.getByGroup(UPDAT_CONFIG_SETTINGS_GROUP_KEY);

        for (AbstractSetting setting: settings)
        {
            if (setting instanceof StringSetting)
            {
                StringSetting stringSetting = (StringSetting)setting;
                res.addUpdateConfigEntry(UpdateConfigEntry.parse(stringSetting.getValue()));
            }
            else
            {
                logger.trace("Non string setting encountered for \""+UPDAT_CONFIG_SETTINGS_GROUP_KEY+"\" group where only strings are expected. Setting key \""+setting.getKey()+"\"");
            }
        }

        return res;
    }

    private UpdateConfig loadUpdateConfigFromDisk(URLSetting urlSetting)
        throws JAXBException, IOException
    {
        UpdateConfig res;

        ByteArrayOutputStream bout = downloadFileToByteArray(urlSetting.getValue());
        try(ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray()))
        {
            res = Util.convertStreamToObject(UpdateConfig.class, bin);
        }
        catch(Exception e)
        {
            throw e;
        }

        return res;
    }

    private UpdateManifest downloadUpdateManifest(URLSetting urlSetting)
        throws Exception
    {
        UpdateManifest res = null;

        if (checkWebURL(urlSetting.getValue()))
        {

            ByteArrayOutputStream bout = downloadFileToByteArray(urlSetting.getValue());
            try (ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray()))
            {
                res = Util.convertStreamToObject(UpdateManifest.class, bin);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return res;
    }

    private ByteArrayOutputStream downloadFileToByteArray(URL url)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (InputStream is = url.openStream()) {
            byte[] byteChunk = new byte[4096];
            int n;

            while ( (n = is.read(byteChunk)) > 0 ) {
                baos.write(byteChunk, 0, n);
            }
        }
        catch (IOException e) {
            System.err.printf ("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
            e.printStackTrace ();
        }

        return baos;
    }

    /**
     * Does a check to make sure the resource exists before trying to download it.
     */
    private boolean checkWebURL(URL url)
        throws Exception
    {
        try
        {
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");  //OR  huc.setRequestMethod ("HEAD");
            huc.connect();
            int code = huc.getResponseCode();
            System.out.println(code);
            return code==200;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}

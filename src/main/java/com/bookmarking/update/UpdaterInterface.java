package com.bookmarking.update;

import java.util.*;
import com.bookmarking.settings.*;
import com.bookmarking.settings.types.*;
import com.bookmarking.ui.*;
import com.bookmarking.update.config.*;

/**
 * This class calls the update service, and updates software as needed.
 */
public interface UpdaterInterface
{
    public static final String UPDATE_CONFIG_GROUP_KEY = "update-config-group";
    public static final String UPDATE_CONFIG_ITEMS_GROUP_KEY = "update-config-items-group";
    public static final String UPDATER_CLASS_KEY = "updater-class";
    public static final String REPO_CONFIG_KEY = "repo-config-key";

    /**
     * Checks the update service for things that are available for update, compares their versions to what is installed locally,
     * and returns a list of things that are not up to date.
     * @return  Set up UpdateConfigEntry items that represents items to update.
     */
    Map<URLSetting, Set<UpdateConfigEntry>> checkForUpdates(Settings settings)
        throws Exception;

    /**
     * Performs updates and indicates their status in the map returned. Map<resource key, message>
     * @param updates  Accepts the items gotten from the checkForUpdates() method as items to download and update.
     * @return  A map representing the status of each updated item.
     */
    Map<String, String> performUpdates(Map<URLSetting, Set<UpdateConfigEntry>> updates);

    void setUpdateUIInterface(UpdateUIInterface updateUIInterface);

    UpdateUIInterface getUpdateUIInterface();
}

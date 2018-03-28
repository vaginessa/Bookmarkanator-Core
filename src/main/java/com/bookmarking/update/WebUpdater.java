package com.bookmarking.update;

import java.util.*;
import com.bookmarking.settings.*;

/**
 * Updater that checks web repositories for updates.
 */
public class WebUpdater implements UpdaterInterface
{
    private UpdateUIInterface updateUIInterface;
    private boolean updateInterfacePresent = false;

    @Override
    public Set<UpdateConfigEntry> checkForUpdates(Settings settings)
    {
        Set<UpdateConfigEntry> res = new HashSet<>();

        // Query web service for config file.
        // Get versions of ones present that we are tracking in the local config
        // compare version strings to determine if we should update, and if so add it to the 'res' list.
        // update ui interface the whole time.


        if (updateUIInterface!=null)
        {
            res = updateUIInterface.approveUpdates(res);
        }

        return res;
    }

    @Override
    public Map<String, String> performUpdates(Set<UpdateConfigEntry> updates)
    {
        Map<String, String> res = new HashMap<>();

        for (UpdateConfigEntry update: updates)
        {
            // Download update
                // Show download progress in UI Interface
            // Compare file hash to config hash
                // Update ui status
            // Rename old file temporarily
            // place new file in it's place
            // update config with new version information
            // If TransitionInterface present run transition interface
                // Show transition interface output
            // If all is well, delete old version.
        }

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
}

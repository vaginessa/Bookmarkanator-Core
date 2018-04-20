package com.bookmarking.update.config;

import java.util.*;

/**
 * The UpdateConfig describes the things needed to be checked for updates. This is often stored locally.
 */
public class UpdateConfig
{
    private Map<String, UpdateConfigEntry> updateConfigEntryMap;

    public UpdateConfig()
    {
        updateConfigEntryMap = new HashMap<>();
    }

    public Set<UpdateConfigEntry> getUpdateConfigEntryMap()
    {
        return new HashSet<>(updateConfigEntryMap.values());
    }

    public void addUpdateConfigEntry(UpdateConfigEntry updateConfigEntry)
    {
        updateConfigEntryMap.put(updateConfigEntry.getResourceKey(), updateConfigEntry);
    }

    public UpdateConfigEntry getUpdateConfigEntry(String resourceKey)
    {
        return updateConfigEntryMap.get(resourceKey);
    }
}

package com.bookmarking.update.manifest;

import java.util.*;

/**
 * This is a class representation of a textual manifest file that describes the locations and versions of updatable things.
 *
 * This will often be hosted on the remote server that also host's the updated software but not necessarily.
 */
public class UpdateManifest
{
    private List<UpdateManifestEntry> resources;
    private String manifestVersion;
    private String manifestType;

    public List<UpdateManifestEntry> getResources()
    {
        return resources;
    }

    public void setResources(List<UpdateManifestEntry> resources)
    {
        this.resources = resources;
    }

    public String getManifestVersion()
    {
        return manifestVersion;
    }

    public void setManifestVersion(String manifestVersion)
    {
        this.manifestVersion = manifestVersion;
    }

    public String getManifestType()
    {
        return manifestType;
    }

    public void setManifestType(String manifestType)
    {
        this.manifestType = manifestType;
    }
}

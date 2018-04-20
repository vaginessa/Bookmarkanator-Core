package com.bookmarking.update.manifest;

import java.net.*;

/**
 * Manifest entry that points to a single update source.
 */
public class UpdateManifestEntry
{
    // The location of this particular item
    private URL resource;
    // The key used to locate this item (the file could be a different name than the key)
    private String resourceKey;
    // Standard version string
    private String version;
    // The hash of the file in the repository (should be verified on download)
    private String md5Hash;

    // The class that implements the TransitionInterface, this class could be in the updating jar itself, or on the classpath.
    // This class is responsible for transitioning between versions.
    private Class transitionClass;

    public URL getResource()
    {
        return resource;
    }

    public void setResource(URL resource)
    {
        this.resource = resource;
    }

    public String getResourceKey()
    {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey)
    {
        this.resourceKey = resourceKey;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getMd5Hash()
    {
        return md5Hash;
    }

    public void setMd5Hash(String md5Hash)
    {
        this.md5Hash = md5Hash;
    }

    public Class getTransitionClass()
    {
        return transitionClass;
    }

    public void setTransitionClass(Class transitionClass)
    {
        this.transitionClass = transitionClass;
    }
}

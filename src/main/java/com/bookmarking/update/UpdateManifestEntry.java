package com.bookmarking.update;

import java.net.*;

public class UpdateManifestEntry
{
    private URL resource;
    private String resourceKey;
    private String version;
    private String md5Hash;

    // The class that implements the TransitionInterface, this class could be in the updating jar itself, or on the classpath.
    private Class transitionClass;
}

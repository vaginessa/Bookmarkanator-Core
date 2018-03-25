package com.bookmarking.update;

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
}

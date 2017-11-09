package com.bookmarking.update;

import java.util.*;
import com.bookmarking.util.*;

public interface UpdateInterface
{
    boolean hasNewerVersion(Version currentVersion);
    List<Version> getAllVersions();
    boolean switchToVersion(Version updateVersion);
    void restartJVM();
}

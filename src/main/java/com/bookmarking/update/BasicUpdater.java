package com.bookmarking.update;

import java.util.*;
import com.bookmarking.util.*;
import sun.reflect.generics.reflectiveObjects.*;

public class BasicUpdater implements UpdateInterface
{
    @Override
    public boolean hasNewerVersion(Version currentVersion)
    {
        return false;
    }

    @Override
    public List<Version> getAllVersions()
    {
        return new ArrayList<>();
    }

    @Override
    public boolean switchToVersion(Version updateVersion)
    {
        throw new NotImplementedException();
    }

    @Override
    public void restartJVM()
    {

    }
}

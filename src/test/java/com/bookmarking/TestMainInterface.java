package com.bookmarking;

import com.bookmarking.settings.*;
import com.bookmarking.settings.types.*;
import org.junit.*;

public class TestMainInterface
{

//    @Ignore
    @Test
    public void testLocalInterface()
        throws Exception
    {
        Settings settings = new Settings();
        BooleanSetting useFileSystem = new BooleanSetting(Defaults.FILE_IO_SETTINGS_GROUP, Defaults.USE_FILE_SYSTEM, false);
        settings.putSetting(useFileSystem);

        LocalInstance localInstance = LocalInstance.use(settings);

        System.out.println();
    }
}

package com.bookmarkanator;

import java.util.*;
import com.bookmarkanator.io.*;
import com.bookmarkanator.util.*;
import org.junit.*;

public class TestLoadingModules
{
    @Ignore
    @Test
    public void testLoadingModules()
        throws Exception
    {
        FileIO io = new FileIO();

//        io.init();

        Collection col = Util.listFiles("/Users/lloyd1/Desktop", "jar");
        System.out.println();
    }
}

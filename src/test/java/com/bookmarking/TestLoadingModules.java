package com.bookmarking;

import java.util.*;
import com.bookmarking.io.*;
import com.bookmarking.util.*;
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

package com.bookmarkanator;

import java.util.*;
import com.bookmarkanator.io.*;
import org.junit.*;

public class TestLoadingModules
{
    @Test
    public void testLoadingModules()
        throws Exception
    {
        FileIO io = new FileIO();

//        io.init();

        Collection col = io.listFiles("/Users/lloyd1/Desktop");
        System.out.println();
    }
}

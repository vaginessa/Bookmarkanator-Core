package com.bookmarking;

import com.bookmarking.bootstrap.*;
import com.bookmarking.io.*;
import com.bookmarking.update.*;
import org.junit.*;

public class TestBootstrap
{
    @Test
    public void testBootstrap()
        throws Exception
    {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.init();
    }

    //TODO Add a bunch of ui interface classes, and have them output the bootstrap actions.

    private class MainUI implements MainUIInterface
    {

        @Override
        public UpdateUIInterface getUpdateUIInterface()
        {
            return null;
        }

        @Override
        public IOUIInterface getIOUIInterface()
        {
            return null;
        }
    }
}

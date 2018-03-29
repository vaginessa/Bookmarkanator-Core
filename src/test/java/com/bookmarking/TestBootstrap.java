package com.bookmarking;

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

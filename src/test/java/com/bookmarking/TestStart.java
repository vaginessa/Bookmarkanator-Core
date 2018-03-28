package com.bookmarking;

import com.bookmarking.io.*;
import com.bookmarking.update.*;
import org.junit.*;

public class TestStart
{
    @Test
    public void testBootstrap()
        throws Exception
    {
        Start start = new Start();
        start.init();
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

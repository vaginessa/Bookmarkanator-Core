package com.bookmarking;

import java.util.*;
import com.bookmarking.bootstrap.*;
import com.bookmarking.settings.input.*;
import com.bookmarking.ui.*;
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

        @Override
        public InitUIInterface getInitUIInterface()
        {
            return null;
        }

        @Override
        public void setUIState(UIStateEnum state)
        {

        }

        @Override
        public void setStatus(String message)
        {

        }

        @Override
        public void setProgress(int level, int min, int max)
        {

        }

        @Override
        public void setProgressOn()
        {

        }

        @Override
        public void setProgressOff()
        {

        }

        @Override
        public void postMessage(String message)
        {

        }

        @Override
        public void notifyUI(String message)
        {

        }

        @Override
        public List<InputOption> requestUserInput(List<InputOption> inputOption)
        {
            return null;
        }
    }
}

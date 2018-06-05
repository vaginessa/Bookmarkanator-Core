package com.bookmarking.ui.defaults;

import java.util.*;
import com.bookmarking.settings.input.*;
import com.bookmarking.ui.*;
import org.apache.logging.log4j.*;

public class ConsoleBookmarkUI implements BookmarkUIInterface
{
    private static final Logger logger = LogManager.getLogger(ConsoleBookmarkUI.class.getCanonicalName());

    @Override
    public void setUIState(UIStateEnum state)
    {
        logger.info("State \""+state.name()+"\"");
    }

    @Override
    public void setStatus(String message)
    {
        logger.info("Status \""+message+"\"");
    }

    @Override
    public void setProgress(int level, int min, int max)
    {
        logger.info("Progress = "+level+" min = "+min+" max = "+max);
    }

    @Override
    public void setProgressOn()
    {
        logger.info("Progress On");
    }

    @Override
    public void setProgressOff()
    {
        logger.info("Progress Off");
    }

    @Override
    public void postMessage(String message)
    {
        logger.info("Posting Message \""+message+"\"");
    }

    @Override
    public void notifyUI(String message)
    {
        logger.info("Notification Message \""+message+"\"");
    }

    @Override
    public List<InputOption> requestUserInput(List<InputOption> inputOption)
    {
        for (InputOption option: inputOption)
        {
            logger.info("Requesting input \""+option.getText()+"\"");
        }

        return inputOption;
    }
}

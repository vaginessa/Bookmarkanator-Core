package com.bookmarking.ui.defaults;

import java.util.*;
import com.bookmarking.settings.input.*;
import com.bookmarking.ui.*;
import org.apache.logging.log4j.*;

public class ConsoleUI implements MainUIInterface
{
    private static final Logger logger = LogManager.getLogger(ConsoleUI.class.getCanonicalName());

    private IOUIInterface iouiInterface;
    private InitUIInterface initUIInterface;

    public ConsoleUI()
    {
        iouiInterface = new ConsoleIOUI();
    }

    @Override
    public IOUIInterface getIOUIInterface()
    {
        return iouiInterface;
    }

    @Override
    public InitUIInterface getInitUIInterface()
    {
        return initUIInterface;
    }

    @Override
    public void setUIState(UIStateEnum state)
    {
        logger.info("UI state = \""+state.name()+"\"");
    }

    @Override
    public void setStatus(String message)
    {
        logger.info("Status message = \""+message+"\"");
    }

    @Override
    public void setProgress(int level, int min, int max)
    {
        logger.info("Progress level = "+level+", min = "+min+", max = "+max);
    }

    @Override
    public void setProgressOn()
    {
        logger.info("Progress ON");
    }

    @Override
    public void setProgressOff()
    {
        logger.info("Progress OFF");
    }

    @Override
    public void postMessage(String message)
    {
        logger.info("Posting message = \""+message+"\"");
    }

    @Override
    public void notifyUI(String message)
    {
        logger.info("Notify UI message = \""+message+"\"");
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

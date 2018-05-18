package com.bookmarking.action;

import org.apache.logging.log4j.*;

public class TestAction extends AbstractAction
{
    private static final Logger logger = LogManager.getLogger(TestAction.class.getCanonicalName());

    @Override
    public String runAction(String actionString)
        throws Exception
    {
        logger.info("Executing single action "+actionString);
        return actionString;
    }

    @Override
    public void systemInit()
    {

    }

    @Override
    public void systemShuttingDown()
    {

    }
}

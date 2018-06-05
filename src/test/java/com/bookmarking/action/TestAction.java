package com.bookmarking.action;

import org.apache.logging.log4j.*;

public class TestAction extends AbstractAction
{
    private static final Logger logger = LogManager.getLogger(TestAction.class.getCanonicalName());

    @Override
    public String[] runAction(String[]... actionStrings)
        throws Exception
    {
        logger.info("Executing single action "+actionStrings);
        return new String[0];
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

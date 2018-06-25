package com.bookmarking.settings;

import org.apache.logging.log4j.*;

public class TestSettingsIO implements SettingsIOInterface
{
    private static final Logger logger = LogManager.getLogger(TestSettingsIO.class.getCanonicalName());
    private Settings settings;

    @Override
    public Settings init(Settings settings)
        throws Exception
    {
        this.settings = settings;
        return settings;
    }

    @Override
    public Settings getSettings()
    {
        return this.settings;
    }

    @Override
    public void setSettings(Settings settings)
    {
        this.settings = settings;
    }

    @Override
    public void save()
        throws Exception
    {
        logger.info("Saving settings.");
    }

    @Override
    public void prepExit()
        throws Exception
    {
        logger.info("Prepping exit");
    }

    @Override
    public void exit()
    {
        logger.info("Exiting");
    }
}

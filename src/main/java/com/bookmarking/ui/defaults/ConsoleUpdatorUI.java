package com.bookmarking.ui.defaults;

import java.util.*;
import com.bookmarking.ui.*;
import com.bookmarking.update.config.*;
import org.apache.logging.log4j.*;

public class ConsoleUpdatorUI implements UpdateUIInterface
{
    private static final Logger logger = LogManager.getLogger(ConsoleUpdatorUI.class.getCanonicalName());

    @Override
    public Set<UpdateConfigEntry> approveUpdates(Set<UpdateConfigEntry> updates)
    {
        return updates;
    }

    @Override
    public void setUpdateProgress(String updateItemKey, Double progress)
    {
        logger.info("UpdateItemKey = \""+updateItemKey+"\" Progress = "+progress);
    }

    @Override
    public void setUpdateStatus(String updateItemKey, String status)
    {
        logger.info("UpdateItemKey = \""+updateItemKey+"\" status = "+status);
    }
}

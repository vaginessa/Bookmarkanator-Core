package com.bookmarking.error;

import com.bookmarking.ui.*;
import org.apache.logging.log4j.*;

public class ErrorHandler
{
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(ErrorHandler.class.getCanonicalName());
    private static ErrorHandler errorHandler;

    private UIInterface uiInterface;

    public UIInterface getUiInterface()
    {
        return uiInterface;
    }

    public void setUiInterface(UIInterface uiInterface)
    {
        this.uiInterface = uiInterface;
    }

    public void handleException(Exception ex)
    {
        if (uiInterface!=null)
        {
            uiInterface.setUIState(UIStateEnum.ERROR);
            uiInterface.setStatus(ex.toString());
        }

        logger.error(ex);
        ex.printStackTrace();
    }

    public static ErrorHandler use()
    {
        if (errorHandler==null)
        {
            errorHandler = new ErrorHandler();
        }
        return errorHandler;
    }

    public static ErrorHandler handle(Exception ex)
    {
        ErrorHandler errorHandler = ErrorHandler.use();
        errorHandler.handleException(ex);
        return errorHandler;
    }
}

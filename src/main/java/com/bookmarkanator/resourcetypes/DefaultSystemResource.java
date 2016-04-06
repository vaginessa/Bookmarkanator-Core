package com.bookmarkanator.resourcetypes;

import java.awt.*;
import java.io.*;
import java.net.*;

public class DefaultSystemResource extends BasicResource
{
    public static final String RESOURCE_TYPE_DEFAULT_WEB_BROWSER = "Default web browser";
    public static final String RESOURCE_TYPE_DEFAULT_FILE_BROWSER = "Default file browser";
    public static final String RESOURCE_TYPE_DEFAULT_FILE_EDITOR = "Default file editor";

    private String type;

    public DefaultSystemResource(String type)
    {
        this.type = type;
    }

    @Override
    public String execute()
        throws Exception
    {
        if (getType().equals(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER))
        {
            Desktop.getDesktop().browse(new URI(getText()));
        }
        else if (getType().equals(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_EDITOR))
        {
            Desktop.getDesktop().edit(new File(getText()));
        }
        else if (getType().equals(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_BROWSER))
        {
            Desktop.getDesktop().open(new File(getText()));
        }
        else
        {
            throw new Exception("Cannot identify default system resource type: "+getType());
        }
        return getText();
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}

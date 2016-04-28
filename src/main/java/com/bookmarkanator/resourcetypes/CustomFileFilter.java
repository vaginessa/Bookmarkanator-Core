package com.bookmarkanator.resourcetypes;

import com.bookmarkanator.abstracted.*;

/**
 * This class is for use in extending the file filter resources to do any kind of custom file processing.
 */
public class CustomFileFilter extends CustomClass
{
    @Override
    public void execute(StringBuilder sb)
        throws Exception
    {
        System.out.println("Running custom file filter class now.");
    }

    @Override
    public String getTypeString()
    {
        return "Custom";
    }
}

package com.bookmarkanator.resourcetypes;

import java.util.*;

/**
 * This class is for use in extending the file filter resources to do any kind of custom file processing. It acts more like a place holder in the
 * bookmarks, and for writing to/from settings files.
 */
public class CustomFileFilter extends BasicResource
{
    private Map<String, String> parameterKeys;//parameters and their descriptions
    private List<String> parameters;//the actual parameters to send to the class in use
    private String pathToClass;//the class to be loaded.

    public Map<String, String> getParameterKeys()
    {
        return parameterKeys;
    }

    public void setParameterKeys(Map<String, String> parameterKeys)
    {
        this.parameterKeys = parameterKeys;
    }

    public List<String> getParameters()
    {
        return parameters;
    }

    public void setParameters(List<String> parameters)
    {
        this.parameters = parameters;
    }

    public String getPathToClass()
    {
        return pathToClass;
    }

    public void setPathToClass(String pathToClass)
    {
        this.pathToClass = pathToClass;
    }

    @Override
    public void toXML(StringBuilder sb, String prependTabs)
    {
        sb.append(prependTabs+"<custom-file-filter-resource>");
        sb.append("\n");
        sb.append(prependTabs+"\t<name>");
        sb.append("\n");
        sb.append(prependTabs+"\t\t"+getName());
        sb.append("\n");
        sb.append(prependTabs+"\t</name>");
        sb.append("\n");
        sb.append(prependTabs+"\t<text>");
        sb.append("\n");
        sb.append(prependTabs+"\t\t"+getText());
        sb.append("\n");
        sb.append(prependTabs+"\t</text>");
        sb.append("\n");
        sb.append(prependTabs+"</custom-file-filter-resource>");
        //TODO Finish implementing the xml output...
    }
}

package com.bookmarkanator.abstracted;

import com.bookmarkanator.resourcetypes.FileFilterParameter;

import java.util.List;
import java.util.Observable;

public abstract class CustomFileFilter {

    private List<FileFilterParameter> parameters;

    public List<FileFilterParameter> getParameters()
    {
        return parameters;
    }
    public void setParameters(List<FileFilterParameter> parameters)
    {
        this.parameters = parameters;
    }

    /**
     * Called to execute the custom code in the class that extends this class
     * @return  Returns a string message of the run status.
     * @throws Exception
     */
    public abstract String execute() throws Exception;


}

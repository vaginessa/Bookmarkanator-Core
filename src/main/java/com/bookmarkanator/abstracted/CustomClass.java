package com.bookmarkanator.abstracted;

import java.util.*;
import com.bookmarkanator.customClass.*;
import com.bookmarkanator.resourcetypes.*;

public abstract class CustomClass extends BasicResource
{
    private List<CustomClassParameter> parameters;//the actual parameters set.

    public CustomClass() {
        parameters = new ArrayList<>();
    }

    /**
     * The main program will call this method to get a list of parameters to display to the user. They will modify the parameters, and submit them
     * once again. After that execute can be called to run the custom code.
     *
     * @return
     */
    public List<CustomClassParameter> getParameters()
    {
        return parameters;
    }

    public void setParameters(List<CustomClassParameter> parameters)
    {
        this.parameters = parameters;
    }

    public void addParameter(CustomClassParameter parameter)
    {
        this.parameters.add(parameter);
    }

    /**
     * Called by the main program to initiate the action defined by the custom code (parameters must be set first)
     *
     * @param sb A string builder to write the output to.
     * @throws Exception
     */
    public abstract void execute(StringBuilder sb)
        throws Exception;

    @Override
    public void toXML(StringBuilder sb, String prependTabs)
    {
        sb.append(prependTabs + "<custom-class>");
        sb.append("\n");
        sb.append(prependTabs + "\t<name>");
        sb.append(getName());
        sb.append("</name>");
        sb.append("\n");
        sb.append(prependTabs + "\t<text>");
        sb.append(getText());
        sb.append("</text>");
        sb.append("\n");
        sb.append(prependTabs + "\t<class-pointer>");
        sb.append(getClass());
        sb.append("</class-pointer>");
        sb.append("\n");
        parametersToXML(sb, prependTabs+"\t");
        sb.append(prependTabs + "</custom-class>");
    }

    private void parametersToXML(StringBuilder sb, String prependTabs)
    {
        for (CustomClassParameter param : getParameters())
        {
            param.toXML(sb, prependTabs + "");
        }
    }


}

package com.bookmarkanator.abstracted;

import java.util.*;
import com.bookmarkanator.customClass.*;
import com.bookmarkanator.resourcetypes.*;

public abstract class CustomClass extends BasicResource
{
    private List<CustomClassParameter> parameters;//the actual parameters set.

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

    /**
     * Called by the main program to initiate the action defined by the custom code.
     *
     * @param sb A string builder to write the output to.
     * @throws Exception
     */
    public abstract void execute(StringBuilder sb)
        throws Exception;

    @Override
    public void toXML(StringBuilder sb, String prependTabs)
    {
        sb.append(prependTabs + "<custom-class index-within-bookmark=\"");
        sb.append(getIndexWithinBookmark());
        sb.append("\">");
        sb.append("\n");
        sb.append(prependTabs + "\t<name>");
        sb.append("\n");
        sb.append(prependTabs + "\t\t" + getName());
        sb.append("\n");
        sb.append(prependTabs + "\t</name>");
        sb.append("\n");
        sb.append(prependTabs + "\t<text>");
        sb.append("\n");
        sb.append(prependTabs + "\t\t" + getText());
        sb.append("\n");
        sb.append(prependTabs + "\t</text>");
        sb.append("\n");
        sb.append(prependTabs + "\t<class-pointer>");
        sb.append("\n");
        sb.append(prependTabs + "\t\t" + getClass());
        sb.append("\n");
        sb.append(prependTabs + "\t</class-pointer>");
        parametersToXML(sb, prependTabs);
        sb.append(prependTabs + "</custom-class>");

    }

    private void parametersToXML(StringBuilder sb, String prependTabs)
    {
        for (CustomClassParameter param : getParameters())
        {
            sb.append("\n");
            param.toXML(sb, prependTabs + "\t");
        }
    }
}

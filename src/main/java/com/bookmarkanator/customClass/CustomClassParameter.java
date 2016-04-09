package com.bookmarkanator.customClass;

import javax.swing.*;
import com.bookmarkanator.interfaces.*;

/**
 * This class represents a parameter that is to be used in the custom file filter class.
 */
public class CustomClassParameter implements XMLWritable{
    private String key;
    private String value;
    private String description;
    private InputVerifier inputVerifier;//don't need to write this to xml because it is specified within the class and cannot be overridden in the xml.
    private boolean required;//required parameters must be supplied before the class can be run.
    private boolean showIfOverridden;//display parameters that have been set in the settings xml file to the user
    private boolean isOverridden;//indicates if this parameter was gotten from the settings xml file. Only overridden parameters are written to xml.

    public CustomClassParameter()
    {
        required = true;
        showIfOverridden = false;
        isOverridden = false;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public InputVerifier getInputVerifier() {
        return inputVerifier;
    }

    public void setInputVerifier(InputVerifier inputVerifier) {
        this.inputVerifier = inputVerifier;
    }

    public boolean isRequired()
    {
        return required;
    }

    public void setRequired(boolean required)
    {
        //TODO add logic in here to ensure that proper states occure between showIfOverridden, is overridden and this.
        this.required = required;
    }

    public boolean isShowIfOverridden()
    {
        return showIfOverridden;
    }

    public void setShowIfOverridden(boolean showIfOverridden)
    {
        this.showIfOverridden = showIfOverridden;
    }

    public boolean isOverridden()
    {
        return isOverridden;
    }

    public void setOverridden(boolean overridden)
    {
        isOverridden = overridden;
    }

//    @Override
//    public void toXML(StringBuilder sb, String prependTabs)
//    {
//        if (isOverridden)
//        {//only write out overridden parameters.
//            sb.append(prependTabs + "<parameter required=\"");
//            sb.append(required);
//            sb.append("\">");
//            sb.append("\n");
//            sb.append(prependTabs + "\t<key>");
//            sb.append("\n");
//            sb.append(prependTabs + "\t\t" + getKey());
//            sb.append("\n");
//            sb.append(prependTabs + "\t</key>");
//            sb.append("\n");
//            sb.append(prependTabs + "\t<value>");
//            sb.append("\n");
//            sb.append(prependTabs + "\t\t" + getValue());
//            sb.append("\n");
//            sb.append(prependTabs + "\t</value>");
//            sb.append("\n");
//            sb.append(prependTabs + "\t<description>");
//            sb.append("\n");
//            sb.append(prependTabs + "\t\t" + getDescription());
//            sb.append("\n");
//            sb.append(prependTabs + "\t</description>");
//            sb.append("\n");
//            sb.append(prependTabs + "</parameter>");
//        }
//    }

    @Override
    public void toXML(StringBuilder sb, String prependTabs)
    {
        if (isOverridden)
        {//only write out overridden parameters.
            sb.append( "<parameter required=\"");
            sb.append(required);
            sb.append("\">");
            sb.append("<key>");
            sb.append("" + getKey());
            sb.append("</key>");
            sb.append("<value>");
            sb.append(getValue());
            sb.append("</value>");
            sb.append("<description>");
            sb.append(getDescription());
            sb.append("</description>");
            sb.append("</parameter>");
        }
    }
}

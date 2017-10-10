package com.bookmarking.settings;

/**
 * This class is used to wrap a setting with a message and required flag for the purpose of sending to the UI requesting information from the user.
 * @param <T>
 */
public class InputOption<T>
{
    private String text;
    private boolean required;
    private AbstractSetting<T> abstractSetting;

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public boolean isRequired()
    {
        return required;
    }

    public void setRequired(boolean required)
    {
        this.required = required;
    }

    public AbstractSetting<T> getAbstractSetting()
    {
        return abstractSetting;
    }

    public void setAbstractSetting(AbstractSetting<T> abstractSetting)
    {
        this.abstractSetting = abstractSetting;
    }
}

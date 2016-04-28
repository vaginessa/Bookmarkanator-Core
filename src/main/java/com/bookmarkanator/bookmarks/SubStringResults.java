package com.bookmarkanator.bookmarks;

import java.util.*;

public class SubStringResults
{
    private Set<String> tags;//the broken up text
    private String text;//the text to be broken up

    public SubStringResults()
    {
        text = "";
        tags = new HashSet<>();
    }

    public Set<String> getTags()
    {
        return tags;
    }

    public void setTags(Set<String> tags)
    {
        this.tags = tags;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void addSubstring(String sub)
    {
        tags.add(sub);
    }

    @Override
    public int hashCode()
    {
        return getText().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj==null)
        {
            return false;
        }

        if (obj instanceof SubStringResults)
        {
            SubStringResults ss = (SubStringResults)obj;

            if (ss.getText().equals(this.getText()))
            {//true if the text's are equal.
                return true;
            }
        }

        return false;
    }
}

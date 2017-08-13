package com.bookmarking.search;

import java.util.*;

public class TagsInfo implements Comparable
{
    public enum TagOptions
    {
        ANY_TAG("Any Tag"),
        ALL_TAGS("All Tags"),
        WITHOUT_TAGS("Without Tags");

        private String text;

        TagOptions(String text)
        {
            this.text = text;
        }

        String text()
        {
            return text;
        }
    }

    private Set<String> tags;
    private Date date;
    private TagOptions operation;
    private UUID id;

    public TagsInfo()
    {
        tags = new HashSet<>();
        date = new Date();
        id = UUID.randomUUID();
        operation = TagOptions.ALL_TAGS;
    }

    public Set<String> getTags()
    {
        return tags;
    }

    public void setTags(Set<String> tags)
    {
        this.tags.clear();
        this.tags.addAll(tags);
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public TagOptions getOperation()
    {
        return operation;
    }

    public void setOperation(TagOptions operation)
    {
        this.operation = operation;
    }

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    @Override
    public int compareTo(Object o)
    {
        if (o instanceof TagsInfo)
        {
            TagsInfo tmp = (TagsInfo) o;
            id.compareTo(tmp.getId());
        }
        if (o instanceof UUID)
        {
            return id.compareTo((UUID) o);
        }
        return 1;
    }
}

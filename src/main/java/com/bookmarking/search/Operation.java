package com.bookmarking.search;

import java.util.*;

/**
 * A Operation represents how it's tags are to be applied to a list of bookmarks. If it's an "All Tags" operation that represents
 * a sort of AND operation. It's the same as saying "if this bookmark has this tag AND this other tag AND tag n.... then include it"
 * "Any Tag" is an OR operation, and "Without Tags" is an exclusionary operation, meaning exclude any bookmark with one of these tags.
 *
 * Lists of Operation objects can be combined to create specific search results.
 */
public class Operation implements Comparable
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

    public Operation()
    {
        tags = new HashSet<>();
        date = new Date();
        id = UUID.randomUUID();
        operation = TagOptions.ALL_TAGS;
    }

    public Date getCreationDate()
    {
        return date;
    }

    public TagOptions getOperation()
    {
        return operation;
    }

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public void setOperation(TagOptions operation)
    {
        if (operation!=null)
        {
            this.operation = operation;
        }
    }

    public Set<String> getTags()
    {
        return tags;
    }

    public void setTags(Set<String> tags)
    {
        if (tags!=null)
        {
            this.tags = tags;
        }
    }

    @Override
    public int compareTo(Object o)
    {
        if (o instanceof Operation)
        {
            Operation tmp = (Operation) o;
            id.compareTo(tmp.getId());
        }
        if (o instanceof UUID)
        {
            return id.compareTo((UUID) o);
        }
        return 1;
    }
}

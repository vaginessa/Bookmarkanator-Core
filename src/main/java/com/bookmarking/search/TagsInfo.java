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
    private SearchOptions searchOptions;

    public TagsInfo()
    {
        tags = new HashSet<>();
        date = new Date();
        id = UUID.randomUUID();
        operation = TagOptions.ALL_TAGS;
    }

    public Set<String> getTags()
    {
        return Collections.unmodifiableSet(tags);
    }

    public Date getCreationDate()
    {
        return new Date(date.toInstant().toEpochMilli());
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

    public void addTag(String tag)
    {
        this.tags.add(tag);

        if (searchOptions!=null)
        {
            searchOptions.computeTagsPresent();
        }
    }

    public void addTags(Collection<String> tags)
    {
        this.tags.addAll(tags);

        if (searchOptions!=null)
        {
            searchOptions.computeTagsPresent();
        }
    }

    public void removeTag(String tag)
    {
        this.tags.remove(tag);

        if (searchOptions!=null)
        {
            searchOptions.computeTagsPresent();
        }
    }

    public void removeTags(Collection<String> tags)
    {
        this.tags.removeAll(tags);

        if (searchOptions!=null)
        {
            searchOptions.computeTagsPresent();
        }
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
        this.operation = operation;
    }

    public SearchOptions getSearchOptions()
    {
        return searchOptions;
    }

    public void setSearchOptions(SearchOptions searchOptions)
    {
        this.searchOptions = searchOptions;
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

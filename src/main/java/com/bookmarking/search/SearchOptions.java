package com.bookmarking.search;

import java.time.*;
import java.util.*;

/**
 * This is a class used to encapsulate all the search options that are possible. Encapsulating them here allows the saving of search
 * recipes, and it simplifies the search interface.
 */
public class SearchOptions
{
    // ============================================================
    // Enums
    // ============================================================

    public enum DateType
    {
        CREATION_DATE,
        ACCESSED_DATE
    }

    public enum DateOffset
    {
        TODAY (LocalDateTime.of(LocalDate.now(), earlyMorning)),
        THIS_WEEK(LocalDateTime.of(LocalDate.now(), earlyMorning).minusDays(7)),
        THIS_MONTH(LocalDateTime.of(LocalDate.now(), earlyMorning).minusMonths(1)),
        THIS_YEAR(LocalDateTime.of(LocalDate.now(), earlyMorning).minusYears(1));

        LocalDateTime localDateTime;

        DateOffset(LocalDateTime localDateTime)
        {
            this.localDateTime = localDateTime;
        }
    }

    // ============================================================
    // Fields
    // ============================================================

    private String searchTerm;

    // -------------------------------------------
    // Date settings variables:
    //
    // Note: if more than one is set it the filter by search options method considers dates in the following order:
    // -dateOffset
    // -absoluteDateOffset
    // -absoluteStartDate/absoluteEndDate
    // It will then apply them using the date type (created date vs modified date)
    // -------------------------------------------

    // Start capturing bookmarks at this date.
    private Date absoluteStartDate;
    // Stop capturing bookmarks at this date.
    private Date absoluteEndDate;
    // Capture bookmarks from now minus this date value. A simple literal subtraction of milliseconds from now.
    private long absoluteDateOffset;

    // Capture bookmarks from now minus a specific amount rounded to the morning of the day closest to the value.
    // For example if it was TODAY it would be all bookmarks created today at 12:00am to now
    // Or if it were THIS_WEEK it would be all bookmarks 7 days from now (12:00am again) to now.
    private DateOffset dateOffset;

    // Which date type to use for filtering.
    private DateType dateType;
    private static final LocalTime earlyMorning = LocalTime.of(0, 0);

    // Aspects of bookmarks to search
    private boolean searchBookmarkText = true;
    private boolean searchBookmarkNames = true;
    private boolean searchBookmarkTypes = true;
    private boolean searchTags = true;

    // List of tag search options along with type (all, any, none)
    private List<Operation> operationList;

    // Exclude all bookmark types not present, unless the list is null. If null include all.
    private Set<Class> selectedBookmarkTypes;

    // ============================================================
    // Methods
    // ============================================================

    public SearchOptions()
    {
        operationList = new ArrayList<>();
        dateType = DateType.CREATION_DATE;
        selectedBookmarkTypes = new HashSet<>();
    }

    public String getSearchTerm()
    {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm)
    {
        this.searchTerm = searchTerm;
    }

    public Date computeStartDate()
    {
        Date startDate = null;

        if (getDateOffset() != null)
        {// Compute the date based on offset value.
            startDate = getDate(getDateOffset().localDateTime);
        }
        else if (getAbsoluteDateOffset() > 0)
        {
            startDate = new Date(computeEndDate().toInstant().toEpochMilli() - getAbsoluteDateOffset());
        }
        else
        {
            startDate = getAbsoluteStartDate();

            if (startDate == null)
            {
                // Super old date as beginning range.
                startDate = new Date(Long.MIN_VALUE);
            }
        }

       return startDate;
    }

    public Date computeEndDate()
    {
        if (getDateOffset() != null || getAbsoluteDateOffset() < 0)
        {// Return now
            return new Date();
        }

        return getAbsoluteEndDate();
    }

    public void resetDateFields()
    {
        absoluteDateOffset = -1;
        absoluteEndDate = null;
        absoluteStartDate = null;
        dateOffset = null;
    }

    public Date getAbsoluteStartDate()
    {
        return absoluteStartDate;
    }

    public void setAbsoluteStartDate(Date absoluteStartDate)
    {
        this.absoluteStartDate = absoluteStartDate;
    }

    public Date getAbsoluteEndDate()
    {
        return absoluteEndDate;
    }

    public void setAbsoluteEndDate(Date absoluteEndDate)
    {
        this.absoluteEndDate = absoluteEndDate;
    }

    public long getAbsoluteDateOffset()
    {
        return absoluteDateOffset;
    }

    public void setAbsoluteDateOffset(long absoluteDateOffset)
    {
        this.absoluteDateOffset = absoluteDateOffset;
    }

    public boolean getSearchBookmarkText()
    {
        return searchBookmarkText;
    }

    public void setSearchBookmarkText(boolean searchBookmarkText)
    {
        this.searchBookmarkText = searchBookmarkText;
    }

    public boolean getSearchBookmarkNames()
    {
        return searchBookmarkNames;
    }

    public void setSearchBookmarkNames(boolean searchBookmarkNames)
    {
        this.searchBookmarkNames = searchBookmarkNames;
    }

    public boolean getSearchBookmarkTypes()
    {
        return searchBookmarkTypes;
    }

    public void setSearchBookmarkTypes(boolean searchBookmarkTypes)
    {
        this.searchBookmarkTypes = searchBookmarkTypes;
    }

    public boolean getSearchTags()
    {
        return searchTags;
    }

    public void setSearchTags(boolean searchTags)
    {
        this.searchTags = searchTags;
    }

    public Operation add(Operation.TagOptions operation, String tag)
    {
        Set<String> tags = new HashSet<>();
        tags.add(tag);
        return add(operation, tags);
    }

    public Operation add(Operation.TagOptions operation, Set<String> tags)
    {
        Operation tagsInfo = new Operation();
        tagsInfo.setOperation(operation);
        tagsInfo.setTags(tags);

        operationList.add(tagsInfo);

        return tagsInfo;
    }

    public void add(Operation operation)
    {
        if (operation != null)
        {
            operationList.add(operation);
        }
    }

    public void remove(Operation operation)
    {
        operationList.remove(operation);
    }

    public List<Operation> getTagOperations()
    {
        List<Operation> res = new ArrayList<>();
        Set<Operation> removals = new HashSet<>();

        for (Operation o : operationList)
        {
            if (o.getTags().isEmpty())
            {
                removals.add(o);
            }
            else
            {
                res.add(o);
            }
        }

        operationList.removeAll(removals);

        return res;
    }

    public Operation getLastOperation()
    {
        if (operationList == null || operationList.isEmpty())
        {
            return null;
        }

        return operationList.get(operationList.size() - 1);
    }

    public void clearOperationsList()
    {
        operationList.clear();
    }

    /**
     * @return Returns all tags present in the list of tag operations.
     */
    public Set<String> getTagsPresent()
    {
        Set<String> res = new HashSet<>();
        Set<Operation> removals = new HashSet<>();

        for (Operation o : operationList)
        {
            if (o.getTags().isEmpty())
            {
                removals.add(o);
            }
            else
            {
                res.addAll(o.getTags());
            }
        }

        operationList.removeAll(removals);

        return res;
    }

    public void removeAll(String tag)
    {
        for (Operation o : operationList)
        {
            o.getTags().remove(tag);
        }
    }

    public void setSelectedBKType(Class bkType)
    {
        selectedBookmarkTypes.add(bkType);
    }

    public void setUnselectedBKType(Class bkType)
    {
        selectedBookmarkTypes.remove(bkType);
    }

    public void setSelectAllBKTypes(Set<Class> bkTypes)
    {
        selectedBookmarkTypes.clear();
        selectedBookmarkTypes.addAll(bkTypes);
    }

    public void clearSelectedBKTypes()
    {
        selectedBookmarkTypes.clear();
    }

    public Set<Class> getSelectedBKTypes()
    {
        if (selectedBookmarkTypes == null)
        {
            return null;
        }

        return Collections.unmodifiableSet(selectedBookmarkTypes);
    }

    public DateType getDateType()
    {
        return dateType;
    }

    public void setDateType(DateType dateType)
    {
        this.dateType = dateType;
    }

    public DateOffset getDateOffset()
    {
        return dateOffset;
    }

    public void setDateOffset(DateOffset dateOffset)
    {
        this.dateOffset = dateOffset;
    }

    public void reset()
    {
        this.resetDateFields();
        this.clearOperationsList();
        this.setSearchBookmarkNames(true);
        this.setSearchTags(true);
        this.setSearchBookmarkText(true);
        this.setSearchBookmarkTypes(true);
        this.setSearchTerm("");
    }

    // ============================================================
    // Private Methods
    // ============================================================

    private Date getDate(LocalDateTime dateTime)
    {
        ZoneId myTimeZone = ZoneId.systemDefault();
        ZoneOffset offset = myTimeZone.getRules().getOffset(Instant.now());
        Instant instant = dateTime.toInstant(offset);
        Date start = Date.from(instant);
        return start;
    }
}

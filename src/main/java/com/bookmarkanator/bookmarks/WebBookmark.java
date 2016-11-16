package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.core.*;

/**
 * The text of this bookmark would represent a web address.
 */
public class WebBookmark extends AbstractBookmark{

    /**
     * This constructor is used to allow custom behaviour or pre-processing to occur relative to the bookmark context object, or for instance
     * to throw an error if more than one bookmark is created, or other such custom behaviours.
     *
     * @param contextInterface The Bookmark context object for the custom bookmark to use.
     */
    public WebBookmark(ContextInterface contextInterface)
    {
        super(contextInterface);
    }

    @Override
    public String getTypeName() {
        return "web";
    }

    @Override
    public void action(FileContext context) throws Exception {

    }

    @Override
    public List<String> getTypeLocation()
    {
        List<String> list = new ArrayList<>();
        list.add("Web");
        return list;
    }

    @Override
    public AbstractBookmark getNew()
    {
        return new WebBookmark(null);
    }

    @Override
    public String getSettings() {
        return null;
    }

    @Override
    public void setSettings(String xml) {

    }
}

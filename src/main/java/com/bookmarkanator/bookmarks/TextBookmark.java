package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.core.*;

public class TextBookmark extends AbstractBookmark{

    /**
     * This constructor is used to allow custom behaviour or pre-processing to occure relative to the bookmark context object, or for instance
     * to throw an error if more than one bookmark is created, or other such custom behaviours.
     *
     * @param contextInterface The Bookmark context object for the custom bookmark to use.
     */
    public TextBookmark(ContextInterface contextInterface)
    {
        super(contextInterface);
    }

    @Override
    public String getTypeName() {
        return "Text";
    }

    @Override
    public void action(FileContext context) throws Exception {

    }

    @Override
    public List<String> getTypeLocation()
    {
        List<String> list = new ArrayList<>();
        list.add("");
        return list;
    }

    @Override
    public AbstractBookmark getNew()
    {
        return new TextBookmark(null);
    }

    @Override
    public String toXML() {
        return null;
    }

    @Override
    public void fromXML(String xml) {

    }
}

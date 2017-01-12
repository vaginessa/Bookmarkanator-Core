package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.io.*;

public class EncryptedBookmark extends AbstractBookmark{
    private String passwordHash;
    private long valid;//stores how long after entering this password, until the bookmark text is encrypted again.

    /**
     * This constructor is used to allow custom behaviour or pre-processing to occure relative to the bookmark context object, or for instance
     * to throw an error if more than one bookmark is created, or other such custom behaviours.
     *
     * @param contextInterface The Bookmark context object for the custom bookmark to use.
     */
    public EncryptedBookmark(ContextInterface contextInterface)
    {
        super(contextInterface);
    }

    /**
     * Add field to the context if this bookmark is unencrypted so that the user doesn't have to unencrypt it each time.
     */

    @Override
    public String getTypeName() {
        return "encrypted";
    }

    @Override
    public void action(ContextInterface context) throws Exception {

    }

    @Override
    public List<String> getTypeLocation()
    {
        List<String> list = new ArrayList<>();
        list.add("Secure");
        return list;
    }

    @Override
    public AbstractBookmark getNew()
    {
        return new EncryptedBookmark(null);
    }

    @Override
    public String getSettings() {
        return null;
    }

    @Override
    public void setSettings(String xml) {

    }

    @Override
    public int compareTo(AbstractBookmark o)
    {
        //TODO IMPLEMENT
        return 0;
    }
}

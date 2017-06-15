package com.bookmarkanator.bookmarks;

import java.util.*;
import org.apache.logging.log4j.*;

public class EncryptedBookmark extends AbstractBookmark{
    private static final Logger logger = LogManager.getLogger(EncryptedBookmark.class.getCanonicalName());
    private static String secretKey;
    private String passwordHash;
    private String salt;
    private long valid;//stores how long after entering this password, until the bookmark text is encrypted again.

    @Override
    public Set<String> getSearchWords()
    {
        return null;
    }

    @Override
    public void systemInit()
    {

    }

    @Override
    public void systemShuttingDown()
    {

    }

    @Override
    public HandleData canHandle(String content)
    {
        return null;
    }

    /**
     * Add field to the context if this bookmark is unencrypted so that the user doesn't have to unencrypt it each time.
     */

    @Override
    public String getTypeName() {
        return "Encrypted";
    }

    @Override
    public boolean setSecretKey(String secretKey)
    {
        if (EncryptedBookmark.secretKey == null && secretKey!=null)
        {
            EncryptedBookmark.secretKey = secretKey;
            return true;
        }
        return false;
    }

    @Override
    public String runAction(String actionString) throws Exception {
return null;
    }

    @Override
    public void notifyBeforeAction(AbstractBookmark source, String actionString)
    {

    }

    @Override
    public void notifyAfterAction(AbstractBookmark source, String actionString)
    {

    }

    @Override
    protected String runTheAction(String action)
        throws Exception
    {
        return null;
    }

    @Override
    public void destroy()
        throws Exception
    {

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
        return new EncryptedBookmark();
    }

    @Override
    public String getContent() {
        return null;
    }

    @Override
    public void setContent(String content) {

    }

    @Override
    public int compareTo(AbstractBookmark o)
    {
        //TODO IMPLEMENT
        return 0;
    }
}

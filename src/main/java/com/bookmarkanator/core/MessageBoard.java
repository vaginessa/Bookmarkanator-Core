package com.bookmarkanator.core;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import org.apache.logging.log4j.*;

public class MessageBoard
{
    private static final Logger logger = LogManager.getLogger(MessageBoard.class.getCanonicalName());
    private static MessageBoard messageBoard;
    private Map<String, Map<String, Object>> messagesMap;//<bookmark class name, Map<message key, message object> >
    // secret key assigned after bookmarks are loaded, and is used to restrict write access to the message board.
    private Map<String, String> messageBoardKeyMap;//<bookmark class name, secret key>

    private MessageBoard()
    {
        messagesMap = new HashMap<>();
        messageBoardKeyMap = new HashMap<>();
    }

    public Object readBoard(String className, String objKey)
    {
        Map<String, Object> map = messagesMap.get(className);
        if (map == null)
        {
            return null;
        }

        final Object Obj = map.get(objKey);//Prevent reassignment of the returned object.
        return Obj;
    }

    public void writeBoard(String className, String secretKey, String objKey, Object value)
    {
        String obtainedKey = messageBoardKeyMap.get(className);

        if (secretKey.equals(obtainedKey))
        {//Allow writing on message board
            Map<String, Object> tmp = messagesMap.get(className);

            if (tmp == null)
            {
                tmp = new HashMap<>();
                messagesMap.put(className, tmp);
            }

            tmp.put(objKey, value);
        }
    }

    public void setSecretKey(AbstractBookmark bookmark)
    {
        String secretKey = UUID.randomUUID().toString();
        String className = bookmark.getClass().getCanonicalName();
        String str = messageBoardKeyMap.get(className);

        if (str == null)
        {
            if (bookmark.setSecretKey(secretKey))
            {
                messageBoardKeyMap.put(bookmark.getClass().getCanonicalName(), secretKey);
            }
            else
            {
                logger.warn("Could not set secret key for " + className + " bookmark with Id \"" + bookmark.getId() + "\"");
            }
        }
    }

    /**
     * Single message board object allowed.
     */
    public static MessageBoard use()
    {
        if (messageBoard == null)
        {
            messageBoard = new MessageBoard();
        }
        return messageBoard;
    }
}

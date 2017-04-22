package com.bookmarkanator.core;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import org.apache.logging.log4j.*;

public class MessageBoard
{
    private static final Logger logger = LogManager.getLogger(MessageBoard.class.getCanonicalName());
    private static MessageBoard messageBoard;
    private Map<String, Map<String, Object>> messagesMap;//<bookmark class name, message board map>
    // secret key assigned after bookmarks are loaded, and is used to restrict write access to the message board.
    private Map<String, String> messageBoardKeyMap;//<bookmark class name, secret key>

    public MessageBoard()
    {
        messagesMap = new HashMap<>();
        messageBoardKeyMap = new HashMap<>();
    }

    public Object readBoard(String className, String objKey)
    {
        Map<String, Object> map = messagesMap.get(className);
        if (map==null)
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
            if (tmp!=null)
            {
                tmp.put(objKey, value);
            }
        }
    }

    public void setSecretKey(AbstractBookmark bookmark)
    {
        String secretKey = UUID.randomUUID().toString();

        if (bookmark.setSecretKey(secretKey))
        {
            messageBoardKeyMap.put(bookmark.getClass().getCanonicalName(), secretKey);
        }
    }

    public static MessageBoard use()
    {
        if (messageBoard==null)
        {
            messageBoard = new MessageBoard();
        }
        return messageBoard;
    }
}

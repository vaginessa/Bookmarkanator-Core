package com.bookmarkanator.util;

import java.util.*;

public class TextAssociator {
    private Set<String> words;//All words present
    private Map<String, Set<String>> itemToText;//<Id of item, set of text words associated with it>
    private Map<String, Set<String>> textToItem;//<Word, set of id's that contain this word>

    public TextAssociator() {
        words = new HashSet<>();
        itemToText = new HashMap<>();
        textToItem = new HashMap<>();
    }

    public void add(String itemId, String word)
    {
        words.add(word);
        Set<String> items = itemToText.get(itemId);
        if (items==null)
        {
            items = new HashSet<>();
            itemToText.put(itemId, items);
        }

        items.add(word);

        Set<String> ids = textToItem.get(word);

        if (ids==null)
        {
            ids = new HashSet<>();
            textToItem.put(word, ids);
        }

        ids.add(itemId);
    }

    public void remove(UUID itemId, String word)
    {
        Set<String> items = itemToText.get(itemId);

        if (items !=null)
        {
            items.remove(word);
        }

        Set<String> ids = textToItem.get(word);

        if (ids!=null)
        {
            ids.remove(itemId);
        }

        if (ids.isEmpty())
        {//remove the word from the master list when all other items that have it are gone.
            words.remove(word);
        }
    }

    public Set<String> getWords(UUID itemId)
    {
        return itemToText.get(itemId);
    }

    public Set<String> getIDs(String word)
    {
        return textToItem.get(word);
    }

    public Set<String> getAllWords()
    {
        return Collections.unmodifiableSet(words);
    }

    public Set<String> getAllIds()
    {
        return Collections.unmodifiableSet(itemToText.keySet());
    }
}

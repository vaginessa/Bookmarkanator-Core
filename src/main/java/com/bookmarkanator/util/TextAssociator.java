package com.bookmarkanator.util;

import java.util.*;

/**
 * A series of lists and functions intended to link a series of objects (The Id's) with a series of words.
 *
 * @param <T> The object to associate with words.
 */
public class TextAssociator<T>
{
    private Set<String> words;//All words present
    private Map<T, Set<String>> itemToText;//<Id of item, set of text words associated with it>
    private Map<String, Set<T>> textToItem;//<Word, set of id's that contain this word>

    public TextAssociator()
    {
        words = new HashSet<>();
        itemToText = new HashMap<>();
        textToItem = new HashMap<>();
    }

    /**
     * Adds a given word to the supplied item object. If the itemId doesn't exist, it will add it.
     *
     * @param itemId The item to associate a word with.
     * @param word   The word to associate with the item.
     */
    public void add(T itemId, String word)
    {
        words.add(word);
        Set<String> items = itemToText.get(itemId);
        if (items == null)
        {
            items = new HashSet<>();
            itemToText.put(itemId, items);
        }

        items.add(word);

        Set<T> ids = textToItem.get(word);

        if (ids == null)
        {
            ids = new HashSet<>();
            textToItem.put(word, ids);
        }

        ids.add(itemId);
    }

    /**
     * Removes a word from the given itemId. If the itemId no longer has associations it will also be removed.
     *
     * @param itemId The thing to remove the word from.
     * @param word   The word to remove.
     */
    public void remove(T itemId, String word)
    {
        Set<String> items = itemToText.get(itemId);

        if (items != null)
        {
            items.remove(word);
        }

        Set<T> ids = textToItem.get(word);

        if (ids != null)
        {
            ids.remove(itemId);
        }

        if (ids.isEmpty())
        {//remove the word from the master list when all other items that have it are gone.
            words.remove(word);
        }
    }

    /**
     * Remove this word from all objects.
     *
     * @param word The word to remove from all items.
     */
    public void remove(String word)
    {
        words.remove(word);

        Set<T> items = textToItem.get(word);

        for (T i : items)
        {//remove the word from individual lists
            Set<String> theWords = itemToText.get(i);

            theWords.remove(word);

            if (theWords.isEmpty())
            {
                itemToText.remove(i);
            }
        }

        textToItem.remove(word);
    }

    /**
     * Remove this item and all associations it had.
     *
     * @param itemId The item to remove, and unassociate.
     */
    public void remove(T itemId)
    {
        Set<String> itemWords = itemToText.get(itemId);
        Set<String> wordsToRemove = new HashSet<>();

        for (String s : itemWords)
        {//remove this id from the list of words.
            Set<T> items = textToItem.get(s);
            items.remove(itemId);
            if (items.isEmpty())
            {//remove word if no more items.
                textToItem.remove(s);
                wordsToRemove.add(s);
            }
        }

        for (String s: wordsToRemove)
        {
            words.remove(s);
        }

        itemToText.remove(itemId);
    }

    /**
     * Returns a list of words that are associated with a particular ID.
     *
     * @param itemId The ID that has words associated with it.
     * @return A set of words that are associated with the ID supplied.
     */
    public Set<String> getWords(T itemId)
    {
        return itemToText.get(itemId);
    }

    /**
     * Returns a list of ID's that have the supplied word associated with them.
     *
     * @param word The word used to get a list of ID's it is in.
     * @return A set of ID's that contain the supplied word.
     */
    public Set<T> getIDs(String word)
    {
        return textToItem.get(word);
    }

    public Set<String> getAllWords()
    {
        return Collections.unmodifiableSet(words);
    }

    public Set<T> getAllIds()
    {
        return Collections.unmodifiableSet(itemToText.keySet());
    }
}

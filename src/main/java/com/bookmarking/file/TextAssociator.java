package com.bookmarking.file;

import java.util.*;
import org.apache.logging.log4j.*;

/**
 * This class associates words with id's, enabling the ability to look up an item by word, or Id.
 * <p>
 * For example you could have a set of UUID's that that are associated with strings that represent tags.
 * You could get a list of all strings associated with a particular Id, or a list of Id's associated with a particular
 * string.
 *
 * @param <T> The object to associate with words.
 */
public class TextAssociator<T>
{
    private static final Logger logger = LogManager.getLogger(TextAssociator.class.getCanonicalName());
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
        logger.trace("Associating item \"" + itemId.toString() + "\" with word \"" + word + "\"");
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
        logger.trace("Un-associating item \"" + itemId.toString() + "\" from \"" + word + "\"");
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

        // remove the word from the master list when all other items that have it are gone.
        if (ids.isEmpty())
        {
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
        logger.trace("Removing \"" + word + "\" from all associations.");
        words.remove(word);

        Set<T> items = textToItem.get(word);

        // remove the word from individual lists
        for (T i : items)
        {
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
        logger.trace("Removing this item \"" + itemId.toString() + "\" and all associated words it had.");

        if (itemId == null)
        {
            return;
        }

        Set<String> itemWords = itemToText.get(itemId);

        if (itemWords == null)
        {
            return;
        }

        Set<String> wordsToRemove = new HashSet<>();

        // remove this id from the list of words.
        for (String s : itemWords)
        {
            Set<T> items = textToItem.get(s);
            items.remove(itemId);

            // remove word if no more items.
            if (items.isEmpty())
            {
                textToItem.remove(s);
                wordsToRemove.add(s);
            }
        }

        for (String s : wordsToRemove)
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

package com.bookmarking.file;

import java.util.*;
import javax.xml.bind.annotation.*;
import com.bookmarking.util.*;

/**
 * This class enables searching of key value pairs (usually UUID to tag pairs) by splitting up the text in various ways and associating
 * those splits to the ID that is linked to it.
 *
 * @param <T> The Object to associate the strings with (for bookmark this is the bookmark Id's)
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Search<T>
{
    @XmlElement
    private TextAssociator<T> theFullText;
    @XmlElement
    private TextAssociator<T> theFullTextRotated;
    @XmlElement
    private TextAssociator<T> theFullTextRotatedSubstrings;
    @XmlElement
    private TextAssociator<T> wordsInTheText;

    public Search()
    {
        theFullText = new TextAssociator<>();
        theFullTextRotated = new TextAssociator<>();
        theFullTextRotatedSubstrings = new TextAssociator<>();
        wordsInTheText = new TextAssociator<>();
    }

    /**
     * Gets a set of strings that point to the objects.
     *
     * @return Set of strings that point to the objects.
     */
    public Set<String> getFullTextWords()
    {
        return theFullText.getAllWords();
    }

    public boolean contains(T item)
    {
        return (theFullText.contains(item) && theFullTextRotated.contains(item) && theFullTextRotatedSubstrings.contains(item) &&
            wordsInTheText.contains(item));
    }

    /**
     * Adds an association of this item to the supplied word. It will add the full text of the word string,
     * the full text rotations, and the rotated substrings.
     *
     * @param item The item to assicate with the word, and all it's derivative's
     * @param word The word to associate, and break up to find sub parts.
     */
    public void add(T item, String word)
    {
        String newWord = word.toLowerCase();
        theFullText.add(item, newWord);

        Set<String> rotatedText = Util.getAllStringRotations(newWord);

        for (String s2 : rotatedText)
        {
            theFullTextRotated.add(item, s2);

            Set<String> subs = Util.getSubstrings(s2);

            for (String s3 : subs)
            {
                s3 = s3.toLowerCase();
                theFullTextRotatedSubstrings.add(item, s3);
            }
        }
    }

    public void add(T item, Set<String> items)
    {
        for (String s : items)
        {
            s = s.toLowerCase();
            add(item, s);
        }
    }

    public void remove(T item, String word)
    {
        //        String newWord = word.toLowerCase();
        //        theFullText.remove(item, newWord);
        //        Set<String> words = Util.getWords(newWord);
        //        for (String s: words)
        //        {
        //            wordsInTheText.remove(item, s);
        //
        //            Set<String> rotations = Util.getAllStringRotations(s);
        //
        //            Set<String> subs;
        //
        //            for (String s2: rotations)
        //            {
        //                wordsInTheTextRotated.remove(item, s2);
        //
        //                subs = Util.getSubstrings(s2);
        //
        //                for (String s3: subs)
        //                {
        //                    substringsOfRotatedWords.remove(item, s3);
        //                }
        //            }
        //
        //            subs = Util.getSubstrings(s);
        //
        //            for (String s2: subs)
        //            {
        //                subStrings.remove(item, s2);
        //            }
        //        }
        //TODO implement full text rotations and substrings.
    }

    public void remove(String word)
    {
        //TODO implement full text rotations and substrings.
        //        String newWord = word.toLowerCase();
        //        theFullText.remove(newWord);
        //        Set<String> words = Util.getWords(newWord);
        //        for (String s: words)
        //        {
        //            wordsInTheText.remove(s);
        //
        //            Set<String> rotations = Util.getAllStringRotations(s);
        //            Set<String> subs;
        //
        //            for (String s2: rotations)
        //            {
        //                wordsInTheTextRotated.remove(s2);
        //                subs = Util.getSubstrings(s2);
        //
        //                for (String s3: subs)
        //                {
        //                    substringsOfRotatedWords.remove(s3);
        //                }
        //            }
        //
        //            subs = Util.getSubstrings(s);
        //
        //            for (String s2: subs)
        //            {
        //                subStrings.remove(s2);
        //            }
        //        }
    }

    public void remove(T item)
    {
        theFullText.remove(item);
        //TODO implement full text rotations and substrings.
        //        wordsInTheText.remove(item);
        //        wordsInTheTextRotated.remove(item);
        //        substringsOfRotatedWords.remove(item);
        //        subStrings.remove(item);
    }

    public LinkedHashSet<T> searchAll(String string, int numberOfResults)
    {
        String newString = string.toLowerCase();
        LinkedHashSet<T> results = new LinkedHashSet<>();

        Set<T> items = theFullText.getIDs(newString);

        if (items != null)
        {
            results.addAll(items);
        }

        if (results.size() < numberOfResults)
        {
            items = wordsInTheText.getIDs(newString);
            if (items != null)
            {
                results.addAll(items);
            }
        }

        if (results.size() < numberOfResults)
        {
            items = theFullTextRotated.getIDs(newString);
            if (items != null)
            {
                results.addAll(items);
            }
        }

        if (results.size() < numberOfResults)
        {
            items = theFullTextRotatedSubstrings.getIDs(newString);
            if (items != null)
            {
                results.addAll(items);
            }
        }

        return results;
    }

    public LinkedHashSet<T> searchFullTextOnly(String string)
    {
        LinkedHashSet<T> items = new LinkedHashSet<>();
        Set<T> res = theFullText.getIDs(string.toLowerCase());
        if (res != null)
        {
            items.addAll(res);
        }
        return items;
    }

    public LinkedHashSet<T> searchWordsInTextOnly(String string)
    {
        LinkedHashSet<T> items = new LinkedHashSet<>();
        Set<T> res = wordsInTheText.getIDs(string.toLowerCase());
        if (res != null)
        {
            items.addAll(res);
        }
        return items;
    }

    public LinkedHashSet<T> searchRotatedWordsOnly(String string)
    {
        LinkedHashSet<T> items = new LinkedHashSet<>();
        //TODO implement full text rotations and substrings.
        //        Set<T> res = wordsInTheTextRotated.getIDs(string.toLowerCase());
        //        if (res!=null)
        //        {
        //            items.addAll(res);
        //        }
        return items;
    }

    public LinkedHashSet<T> searchSubstringsOfRotatedWordsOnly(String string)
    {
        LinkedHashSet<T> items = new LinkedHashSet<>();
        //TODO implement full text rotations and substrings.
        //        Set<T> res = substringsOfRotatedWords.getIDs(string.toLowerCase());
        //        if (res!=null)
        //        {
        //            items.addAll(res);
        //        }
        return items;
    }

    public LinkedHashSet<T> searchSubStringsOnly(String string)
    {
        LinkedHashSet<T> items = new LinkedHashSet<>();
        //TODO implement full text rotations and substrings.
        //        Set<T> res = subStrings.getIDs(string.toLowerCase());
        //        if (res!=null)
        //        {
        //            items.addAll(res);
        //        }
        return items;
    }

}

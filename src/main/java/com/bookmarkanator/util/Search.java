package com.bookmarkanator.util;

import java.util.*;

public class Search<T> {
    private TextAssociator<T> theFullText;
    private TextAssociator<T> wordsInTheText;//The text split into words
    private TextAssociator<T> wordsInTheTextRotated;//All words found in text rotated fully
    private TextAssociator<T> substringsOfRotatedWords;
    private TextAssociator<T> subStrings;//all substrings (down to a single character) of words found in the text.

    public Search()
    {
        theFullText = new TextAssociator<>();
        wordsInTheText = new TextAssociator<>();
        wordsInTheTextRotated = new TextAssociator<>();
        substringsOfRotatedWords = new TextAssociator<>();
        subStrings = new TextAssociator<>();
    }

    public void add(T item, String word)
    {
        String newWord = word.toLowerCase();
        theFullText.add(item, newWord);
        Set<String> words = Util.getWords(newWord);
        for (String s: words)
        {
            s = s.toLowerCase();
            wordsInTheText.add(item, s);

            Set<String> rotations = Util.getAllStringRotations(s);
            Set<String> subs;

            for (String s2: rotations)
            {
                s2 = s2.toLowerCase();
                wordsInTheTextRotated.add(item, s2);

                subs = Util.getSubstrings(s2);

                for (String s3: subs)
                {
                    s3 = s3.toLowerCase();
                    substringsOfRotatedWords.add(item, s3);
                }
            }

            subs = Util.getSubstrings(s);

            for (String s2: subs)
            {
                s2 = s2.toLowerCase();
                subStrings.add(item, s2);
            }
        }
    }

    public void add(T item, Set<String> items)
    {
        for (String s: items)
        {
            add(item, s);
        }
    }

    public void remove(T item, String word)
    {
        String newWord = word.toLowerCase();
        theFullText.remove(item, newWord);
        Set<String> words = Util.getWords(newWord);
        for (String s: words)
        {
            wordsInTheText.remove(item, s);

            Set<String> rotations = Util.getAllStringRotations(s);

            Set<String> subs;

            for (String s2: rotations)
            {
                wordsInTheTextRotated.remove(item, s2);

                subs = Util.getSubstrings(s2);

                for (String s3: subs)
                {
                    substringsOfRotatedWords.remove(item, s3);
                }
            }

            subs = Util.getSubstrings(s);

            for (String s2: subs)
            {
                subStrings.remove(item, s2);
            }
        }
    }

    public void remove(String word)
    {
        String newWord = word.toLowerCase();
        theFullText.remove(newWord);
        Set<String> words = Util.getWords(newWord);
        for (String s: words)
        {
            wordsInTheText.remove(s);

            Set<String> rotations = Util.getAllStringRotations(s);
            Set<String> subs;

            for (String s2: rotations)
            {
                wordsInTheTextRotated.remove(s2);
                subs = Util.getSubstrings(s2);

                for (String s3: subs)
                {
                    substringsOfRotatedWords.remove(s3);
                }
            }

            subs = Util.getSubstrings(s);

            for (String s2: subs)
            {
                subStrings.remove(s2);
            }
        }
    }

    public void remove(T item)
    {
        theFullText.remove(item);
        wordsInTheText.remove(item);
        wordsInTheTextRotated.remove(item);
        substringsOfRotatedWords.remove(item);
        subStrings.remove(item);
    }

    public LinkedHashSet<T> searchAll(String string, int numberOfResults)
    {
        String newString = string.toLowerCase();
        LinkedHashSet<T> results = new LinkedHashSet<>();

        Set<T> items = theFullText.getIDs(newString);

        if (items!=null)
        {
            results.addAll(items);
        }

        if (results.size()<numberOfResults)
        {
            items = wordsInTheText.getIDs(newString);
            if (items!=null)
            {
                results.addAll(items);
            }
        }

        if (results.size()<numberOfResults)
        {
            items = subStrings.getIDs(newString);
            if (items!=null)
            {
                results.addAll(items);
            }
        }

        if (results.size()<numberOfResults)
        {
            items = substringsOfRotatedWords.getIDs(newString);
            if (items!=null)
            {
                results.addAll(items);
            }
        }

        if (results.size()<numberOfResults)
        {//last resort - the results still don't have enough, search here as well...
            items = wordsInTheTextRotated.getIDs(newString);
            if (items!=null)
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
        if (res!=null)
        {
            items.addAll(res);
        }
        return items;
    }

    public LinkedHashSet<T> searchWordsInTextOnly(String string)
    {
        LinkedHashSet<T> items = new LinkedHashSet<>();
        Set<T> res = wordsInTheText.getIDs(string.toLowerCase());
        if (res!=null)
        {
            items.addAll(res);
        }
        return items;
    }

    public LinkedHashSet<T> searchRotatedWordsOnly(String string)
    {
        LinkedHashSet<T> items = new LinkedHashSet<>();
        Set<T> res = wordsInTheTextRotated.getIDs(string.toLowerCase());
        if (res!=null)
        {
            items.addAll(res);
        }
        return items;
    }

    public LinkedHashSet<T> searchSubstringsOfRotatedWordsOnly(String string)
    {
        LinkedHashSet<T> items = new LinkedHashSet<>();
        Set<T> res = substringsOfRotatedWords.getIDs(string.toLowerCase());
        if (res!=null)
        {
            items.addAll(res);
        }
        return items;
    }

    public LinkedHashSet<T> searchSubStringsOnly(String string)
    {
        LinkedHashSet<T> items = new LinkedHashSet<>();
        Set<T> res = subStrings.getIDs(string.toLowerCase());
        if (res!=null)
        {
            items.addAll(res);
        }
        return items;
    }



}

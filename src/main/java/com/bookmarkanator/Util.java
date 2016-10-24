package com.bookmarkanator;

import java.util.*;

public class Util {

    /**
     * This method extracts all items that are considered individual words
     * @param theString  The string to use to extract the words.
     * @return  A set of words contained in the string.
     */
    public static Set<String> getWords(String theString)
    {
        Set<String> words = new HashSet<>();

        for (String s: theString.split(" "))
        {//simple white space split
            s = s.trim();
            if (!s.isEmpty()) {
                words.add(s);
            }
        }

        return words;
    }

    /**
     * This method splits a word up into all substrings that it contains.
     * @param theString  The string to extract substrings from.
     * @return  A set of the sub strings contained in the supplied string.
     */
    public static Set<String> getSubstrings(String theString)
    {
        Set<String> words = new HashSet<>();

        for (int c = 0;c< theString.length();c++)
        {
            words.add(theString.substring(0, c));
        }

        return words;
    }
}

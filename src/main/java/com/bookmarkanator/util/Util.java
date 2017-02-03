package com.bookmarkanator.util;

import java.io.*;
import java.util.*;
import org.apache.commons.io.*;

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
            String s = theString.substring(0, c);
//            s = s.trim();
            if (!s.isEmpty()) {
                words.add(s);
            }
        }

        return words;
    }

    /**
     * This method gets a list of all strings that would be present if the characters were rotated and any that would fall of the end were
     * placed at the beginning.
     * @param string  The string to get the substrings from.
     * @return  A set of rotated strings.
     */
    public static Set<String> getAllStringRotations(String string)
    {
        Set<String> strings = new HashSet<>();

        for (int c=0;c<string.length();c++)
        {
            strings.add(rotateString(string, c));
        }

        return strings;
    }

    public static String rotateString(String theString, int characters)
    {
        int a = characters;
        while (a>theString.length())
        {//reduce the number to less than the string length.
           a = a-theString.length();
        }

        return theString.substring(a, theString.length())+theString.substring(0, a);
    }

    //Copied from:
    //http://stackoverflow.com/questions/19776063/java-list-files-recursively-in-subdirectories-with-apache-commons-io-2-4
    public static Collection<File> listFiles(String directoryBase, String suffix)

    {
        final String[] SUFFIX = {suffix};  // use the suffix to filter
        File rootDir = new File(directoryBase);
        Collection<File> files = FileUtils.listFiles(rootDir, SUFFIX, true);
        return files;
    }

    public static <E> E getItem(List<E> items, int index)
    {
        if (items.size()<=index)
        {
            return null;
        }
        else return items.get(index);
    }
}

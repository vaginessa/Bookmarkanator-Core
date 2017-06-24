package com.bookmarkanator.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.apache.commons.io.*;

public class Util
{

    /**
     * This method extracts all items that are considered individual words, divided by spaces.
     *
     * @param theString The string to use to extract the words.
     * @return A set of words contained in the string.
     */
    public static Set<String> getWords(String theString)
    {
        Set<String> words = new HashSet<>();

        // simple white space split
        for (String s : theString.split(" "))
        {
            s = s.trim();
            if (!s.isEmpty())
            {
                words.add(s);
            }
        }

        return words;
    }

    /**
     * This method splits a word up into all substrings that it contains.
     *
     * For example the text "ABCD" split into all substrings would become the list:
     *
     * ABCD
     * ABC
     * AB
     * A
     *
     * @param theString The string to extract substrings from.
     * @return A set of the sub strings contained in the supplied string.
     */
    public static Set<String> getSubstrings(String theString)
    {
        Set<String> words = new HashSet<>();

        for (int c = 0; c < theString.length(); c++)
        {
            String s = theString.substring(0, c);
            if (!s.isEmpty())
            {
                words.add(s);
            }
        }

        return words;
    }

    /**
     * This method gets a list of all strings that would be present if the characters were rotated and any that would fall of the end were
     * placed at the beginning. For example:
     *
     * Text "ABCD"
     *
     * Fully rotated strings would look like:
     *
     * ABCD
     * DABC
     * CDAB
     * BCDA
     *
     * @param string The string to get the string rotations.
     * @return A set of rotated strings.
     */
    public static Set<String> getAllStringRotations(String string)
    {
        Set<String> strings = new HashSet<>();

        for (int c = 0; c < string.length(); c++)
        {
            strings.add(rotateString(string, c));
        }

        return strings;
    }

    /**
     * Rotates a string so many characters, placing any characters that fall of the end at the beginning.
     *
     * For example:
     *
     * Text "ABCD"
     *
     * Rotated 2 spaces would look like
     *
     * CDAB
     *
     * @param theString
     * @param characters
     * @return
     */
    public static String rotateString(String theString, int characters)
    {
        int a = characters;

        //reduce the number to less than the string length.
        while (a > theString.length())
        {
            a = a - theString.length();
        }

        return theString.substring(a, theString.length()) + theString.substring(0, a);
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

    public static File getOrCreateFile(File file)
        throws Exception
    {
        Objects.requireNonNull(file);

        if (!Files.exists(file.toPath(), LinkOption.NOFOLLOW_LINKS))
        {
            File folder = file.getParentFile();

            // Create folder to contain the settings file if it doesn't exist.
            if (!folder.exists())
            {
                Files.createDirectory(folder.toPath()).toFile();
            }

            file = Files.createFile(file.toPath()).toFile();
        }

        return file;
    }

    public static String compressString(String string, int length)
    {
        if (string.length()<=length)
        {
            return string;
        }

        int borders = (length/2)-3;

        if (borders<=0)
        {
            borders = 1;
        }

        String res = string.substring(0, borders);
        res = res.concat("...");
        res = res.concat(string.substring(string.length()-1-borders, string.length()));

        return res;
    }

}

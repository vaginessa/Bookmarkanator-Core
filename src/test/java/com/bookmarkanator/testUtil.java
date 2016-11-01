package com.bookmarkanator;

import java.util.*;
import com.bookmarkanator.util.*;
import org.junit.*;

public class testUtil {

    @Ignore
    @Test
    public void testGetWords()
    {
        String theString = "Hello I am here, \n again...";

        Set<String> words = Util.getWords(theString);

        Assert.assertTrue(words.size()==5);


        theString = "Hello";

        words = Util.getSubstrings(theString);

        words = Util.getAllStringRotations(theString);


        System.out.println();
    }
}

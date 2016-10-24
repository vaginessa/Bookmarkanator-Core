package com.bookmarkanator;

import java.util.*;
import org.junit.*;

public class testUtil {

    @Test
    public void testGetWords()
    {
        String theString = "Hello I am here, \n again...";

        Set<String> words = Util.getWords(theString);

        Assert.assertTrue(words.size()==5);

    }
}

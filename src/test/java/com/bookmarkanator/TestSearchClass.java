package com.bookmarkanator;

import java.util.*;
import com.bookmarkanator.util.*;
import org.junit.*;

public class TestSearchClass
{

//    @Ignore
    @Test
    public void testTextAssociator()
    {
        String a = "a";
        String b = "b";
        String c = "c";
        String d = "d";
        String e = "e";
        String f = "f";

        TextAssociator<Integer> textAssociator = new TextAssociator<>();

        textAssociator.add(0, a);
        textAssociator.add(0, b);
        textAssociator.add(0, c);

        textAssociator.add(1,d);
        textAssociator.add(1,e);
        textAssociator.add(1, c);
        textAssociator.add(2, f);

        Set<String> theSet = textAssociator.getAllWords();
        Assert.assertTrue(theSet.size()==6);

        theSet = textAssociator.getWords(0);

        Assert.assertTrue(theSet.contains(a));
        Assert.assertTrue(theSet.contains(b));
        Assert.assertTrue(theSet.contains(c));
        Assert.assertFalse(theSet.contains(d));
        Assert.assertFalse(theSet.contains(e));
        Assert.assertFalse(theSet.contains(f));

        theSet = textAssociator.getWords(1);

        Assert.assertTrue(theSet.contains(d));
        Assert.assertTrue(theSet.contains(e));
        Assert.assertTrue(theSet.contains(c));
        Assert.assertFalse(theSet.contains(a));
        Assert.assertFalse(theSet.contains(b));
        Assert.assertFalse(theSet.contains(f));

        theSet = textAssociator.getWords(2);

        Assert.assertTrue(theSet.contains(f));
        Assert.assertFalse(theSet.contains(a));
        Assert.assertFalse(theSet.contains(b));
        Assert.assertFalse(theSet.contains(c));
        Assert.assertFalse(theSet.contains(d));
        Assert.assertFalse(theSet.contains(e));

        Set<Integer> ids = textAssociator.getAllIds();

        Assert.assertEquals(3, ids.size());

        ids = textAssociator.getIDs(a);

        Assert.assertTrue(ids.contains(0));
        Assert.assertFalse(ids.contains(1));
        Assert.assertFalse(ids.contains(2));

        ids = textAssociator.getIDs(b);

        Assert.assertTrue(ids.contains(0));
        Assert.assertFalse(ids.contains(1));
        Assert.assertFalse(ids.contains(2));

        ids = textAssociator.getIDs(c);

        Assert.assertTrue(ids.contains(0));
        Assert.assertTrue(ids.contains(1));
        Assert.assertFalse(ids.contains(2));

        ids = textAssociator.getIDs(d);

        Assert.assertTrue(ids.contains(1));
        Assert.assertFalse(ids.contains(0));
        Assert.assertFalse(ids.contains(2));

        ids = textAssociator.getIDs(e);

        Assert.assertTrue(ids.contains(1));
        Assert.assertFalse(ids.contains(0));
        Assert.assertFalse(ids.contains(2));

        ids = textAssociator.getIDs(f);

        Assert.assertTrue(ids.contains(2));
        Assert.assertFalse(ids.contains(0));
        Assert.assertFalse(ids.contains(1));

        textAssociator.remove(f);

        ids = textAssociator.getAllIds();

        Assert.assertFalse(ids.contains(2));
        Assert.assertTrue(ids.contains(0));
        Assert.assertTrue(ids.contains(1));

        textAssociator.remove(1);

        ids = textAssociator.getAllIds();
        theSet = textAssociator.getAllWords();

        Assert.assertTrue(theSet.contains(a));
        Assert.assertTrue(theSet.contains(b));
        Assert.assertTrue(theSet.contains(c));
        Assert.assertFalse(theSet.contains(d));
        Assert.assertFalse(theSet.contains(e));
        Assert.assertFalse(theSet.contains(f));

        Assert.assertTrue(ids.contains(0));
        Assert.assertFalse(ids.contains(1));
        Assert.assertFalse(ids.contains(2));
    }

    @Test
    public void testSearch()
    {
        String a = "Micah";
        String b = "hello I am you";
        String c = "tag1, tag2, tag3";
        String d = "ABCDEF";

        Search<Integer> search = new Search<>();

        search.add(0, a);
        search.add(1, b);
        search.add(2, c);
        search.add(0, d);

        for (int count=0;count<100;count++)
        {
            search.add(count, ((char)(count+65))+"");
        }



        LinkedHashSet<Integer> results = search.searchAll("am", 10);
        System.out.println();
        results = search.searchAll("I", 10);
        System.out.println();
        results = search.searchAll("tag", 10);
        System.out.println();
        results = search.searchAll("M", 10);
        System.out.println();
        results = search.searchAll("a", 10);
        System.out.println();
        results = search.searchAll("d", 10);
        System.out.println();

        //TODO Add in better search tests and add in add and remove item with search after the operations.
    }
}

package com.bookmarkanator.ui.fxui;

import java.util.*;

public class SimilarItemIterator
{
    private List<String> itemsList;
    private int pos;

    public SimilarItemIterator(String original, Collection<String> similarItems)
    {
        Objects.requireNonNull(original);
        Objects.requireNonNull(similarItems);

        this.itemsList = new ArrayList<>(similarItems.size() + 1);
        pos = 0;

        itemsList.add(original);

        fillList(original, similarItems);
    }

    public String getNextSimilar()
    {
        incrementPos();

        return itemsList.get(pos);
    }

    private void fillList(String original, Collection<String> similarItems)
    {
        Map<Integer, Set<String>> map = new TreeMap<>();

        for (String s : similarItems)
        {
            int a = lengthDiff(original, s);

            Set<String> tmpSet = map.get(a);

            if (tmpSet == null)
            {
                tmpSet = new HashSet<>();
                map.put(a, tmpSet);
            }
            tmpSet.add(s);
        }

        for (Collection<String> collection : map.values())
        {
            for (String s : collection)
            {
                itemsList.add(s);
            }
        }
    }

    private int lengthDiff(String string1, String string2)
    {
        if (string1 == null || string2 == null)
        {
            return -1;
        }
        return Math.abs(string1.length() - string2.length());
    }

    private void incrementPos()
    {
        if (itemsList.size() == 1)
        {
            pos = 0;
        }
        else
        {
            if (pos >= itemsList.size() - 1)
            {
                pos = 0;
            }
            else
            {
                pos++;
            }
        }
    }
}

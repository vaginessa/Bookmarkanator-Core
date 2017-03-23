package com.bookmarkanator.util;

public class GlobalSearch
{
    private static SearchParam searchParam;

    public static SearchParam use()
    {
        if (searchParam==null)
        {
            searchParam = new SearchParam();
        }
        return searchParam;
    }
}

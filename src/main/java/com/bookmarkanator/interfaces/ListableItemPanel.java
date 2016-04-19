package com.bookmarkanator.interfaces;

import java.util.*;

public interface ListableItemPanel
{
    List<ListableItem> getItems();
    List<ListableItem> getCurrentlyDisplayedItems();
    Set<String> getItemNames();
    Map<String, List<ListableItem>> getItemsSearchMap();
    void refresh();
}

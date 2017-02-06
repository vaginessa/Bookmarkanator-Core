package com.bookmarkanator.ui.depreciated;

import java.util.*;

public interface UIControllerInterface
{
    //General
    void addAction(UIAction uiAction);
    UIAction getLatestUIAction(UIInterface item);
    List<UIAction> getUIActions();
    void undo();
    void redo();

    //Search
    void enterSearchTerm(String searchTerm);
    void setSearchOptions(Map<String, Boolean> searchOptions);
    Map<String, Boolean> getSearchOptions();
    void setNumSearchResults(int numberSearchResults);
    void setSearchInterface(SearchInterface searchInterface);
    SearchInterface getSearchInterface();

    //Edit mode
    void enterEditMode();
    void exitEditMode();
    boolean isEditMode();


}

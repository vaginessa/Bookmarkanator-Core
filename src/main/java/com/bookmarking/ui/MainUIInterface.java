package com.bookmarking.ui;

public interface MainUIInterface extends UIInterface
{
    UpdateUIInterface getUpdateUIInterface();
    IOUIInterface getIOUIInterface();
    InitUIInterface getInitUIInterface();
}

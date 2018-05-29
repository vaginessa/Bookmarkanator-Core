package com.bookmarking;

import com.bookmarking.io.*;
import com.bookmarking.ui.*;
import com.bookmarking.update.*;

public interface MainUIInterface extends UIInterface
{
    UpdateUIInterface getUpdateUIInterface();
    IOUIInterface getIOUIInterface();
}

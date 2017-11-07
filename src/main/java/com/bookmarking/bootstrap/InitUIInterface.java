package com.bookmarking.bootstrap;

import com.bookmarking.io.*;
import com.bookmarking.ui.*;

public interface InitUIInterface extends UIInterface
{
    IOUIInterface getIOUIInterface();
    void setIOUIInterface(IOUIInterface iouiInterface);
}

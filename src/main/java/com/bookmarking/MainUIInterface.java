package com.bookmarking;

import com.bookmarking.io.*;
import com.bookmarking.update.*;

public interface MainUIInterface
{
    UpdateUIInterface getUpdateUIInterface();
    IOUIInterface getIOUIInterface();
}

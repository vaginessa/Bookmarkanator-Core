package com.bookmarking;

import com.bookmarking.bootstrap.*;
import com.bookmarking.update.*;

public interface MainUIInterface
{
    UpdateUIInterface getUpdateUIInterface();
    InitUIInterface getInitUIInterface();
}

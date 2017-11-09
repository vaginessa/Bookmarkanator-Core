package com.bookmarking.bootstrap;

import com.bookmarking.settings.*;
import com.bookmarking.util.*;

public interface InitInterface
{
    String OVERRIDDEN_CLASSES_GROUP = "overridden-classes";

    void init() throws Exception;
    void init(Settings settings) throws Exception;
    void exit() throws Exception;
    void setInitUIInterface(InitUIInterface initUIInterface);
    InitUIInterface getInitUIInterface();
    Version getVersion();
}

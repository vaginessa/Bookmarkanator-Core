package com.bookmarking.bootstrap;

import com.bookmarking.settings.*;

public interface InitInterface
{
    void init() throws Exception;
    void init(Settings settings) throws Exception;
    void exit() throws Exception;
    void setInitUIInterface(InitUIInterface initUIInterface);
    InitUIInterface getInitUIInterface();
}

package com.bookmarkanator.interfaces;

import java.util.*;

public interface StringPanelInterface<E>
{
    void prepareListeners();

    String getType();

    void setType(String type);

    String getText();

    void setText(String text);

    E getItem();

    void setItem(E item);

    Observer getObserver();

    void setObserver(Observer observer);

    StringPanelInterface getNew(StringPanelInterface spi);

    StringPanelInterface getNew(E item, Observer observer, String type);
}

package com.bookmarkanator.ui.panel.itempanel;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.interfaces.*;

public class BookmarkPanel<E> extends StringPanel
{
    private JLabel bookmarkType;

    public BookmarkPanel()
    {
        bookmarkType = new JLabel();
    }

    public BookmarkPanel(E item, Observer observer, String type)
    {
        super(item, observer, type);

        if (item instanceof Bookmark)
        {
            Bookmark b = (Bookmark)item;
            bookmarkType = new JLabel();
            bookmarkType.setText(getBookmarkType(b));
            bookmarkType.setBorder(BorderFactory.createLineBorder(Color.blue));

            this.add(bookmarkType, 0);
        }
    }

    private String getBookmarkType(Bookmark bm)
    {
        return "BM";
    }

    @Override
    public StringPanel getNew(StringPanelInterface spi)
    {
        return new BookmarkPanel<>(spi.getItem(),spi.getObserver(), spi.getType());
    }

    @Override
    public StringPanel getNew(Object item, Observer observer, String type)
    {
        return new BookmarkPanel<>(item, observer, type);
    }
}

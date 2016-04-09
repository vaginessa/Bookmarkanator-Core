package com.bookmarkanator.bookmarks;

import com.bookmarkanator.interfaces.XMLWritable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micah on 4/9/16.
 */
public class Bookmarks implements XMLWritable {
    private String version;
    private List<Bookmark> bookmarkList;


    public Bookmarks() {
        bookmarkList = new ArrayList<>();
    }

    public List<Bookmark> getBookmarkList() {
        return bookmarkList;
    }

    public void setBookmarkList(List<Bookmark> bookmarkList) {
        this.bookmarkList = bookmarkList;
    }

    public void addBookmark(Bookmark bookmark)
    {
        bookmarkList.add(bookmark);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public void toXML(StringBuilder sb, String prependTabs) {
        sb.append("\n<bookmarks program-version=\"");
        sb.append(getVersion());
        sb.append("\">\n");

        for (Bookmark bookmark: getBookmarkList())
        {
            bookmark.toXML(sb,"\t");
            sb.append("\n");
        }
        sb.append("</bookmarks>");
    }
}

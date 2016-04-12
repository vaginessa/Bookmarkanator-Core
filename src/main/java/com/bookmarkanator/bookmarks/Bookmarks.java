package com.bookmarkanator.bookmarks;

import com.bookmarkanator.interfaces.XMLWritable;

import java.util.ArrayList;
import java.util.List;

public class Bookmarks implements XMLWritable {
    private String version;
    private List<Bookmark> bookmarkList;


    public Bookmarks() {
        bookmarkList = new ArrayList<>();
        version = "1.0.0-1";
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

    @Override
    public int hashCode() {
        return getBookmarkList().hashCode()+getVersion().hashCode();
    }

    /**
     * Equal if and only if all child bookmarks are equal. Child bookmark equality is compared
     * using UUID's.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj!=null)
        {
            if (obj instanceof Bookmarks)
            {
                Bookmarks b = (Bookmarks)obj;

                if (b.getVersion().equals(getVersion()))
                {
                    if (b.getBookmarkList().size()!=getBookmarkList().size())
                    {
                        return false;
                    }
                    for (Bookmark bm : b.getBookmarkList())
                    {//deep equals
                        int a = getBookmarkList().indexOf(bm);
                        if (a<0)
                        {
                            return false;
                        }
                        if (!bm.equals(getBookmarkList().get(a)))
                        {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}

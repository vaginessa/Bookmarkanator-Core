package com.bookmarking.file;

import java.util.*;
import com.bookmarking.bookmark.*;
import org.w3c.dom.*;

/**
 * An object used to contain parsed and non parsed bookmarks.
 */
public class ParsedBookmarks
{
    // ============================================================
    // Fields
    // ============================================================

    private Map<Class, Set<AbstractBookmark>> loadedBookmarkSetsMap;
    private Map<UUID, AbstractBookmark> loadedBookmarkMap;
    private List<Node> errorTexts;

    // ============================================================
    // Constructors
    // ============================================================

    public ParsedBookmarks()
    {
        loadedBookmarkSetsMap = new HashMap<>();
        loadedBookmarkMap = new HashMap<>();
        errorTexts = new ArrayList<>();
    }

    // ============================================================
    // Methods
    // ============================================================

    public List<Node> getErrorTexts()
    {
        return errorTexts;
    }

    public void addErrorText(Node errorItem)
    {
        errorTexts.add(errorItem);
    }

    public Set<AbstractBookmark> getLoadedByClass(Class clazz)
    {
        return loadedBookmarkSetsMap.get(clazz);
    }

    public AbstractBookmark getLoadedById(UUID bookmarkId)
    {
        return loadedBookmarkMap.get(bookmarkId);
    }

    public void addLoaded(AbstractBookmark abstractBookmark)
    {
        addToSystem(abstractBookmark, loadedBookmarkMap, loadedBookmarkSetsMap);
    }

    public AbstractBookmark removeLoaded(UUID bookmarkId)
    {
       return removeFromSystem(bookmarkId, loadedBookmarkMap, loadedBookmarkSetsMap);
    }

    public Collection<AbstractBookmark> getLoadedBookmarks()
    {
        return Collections.unmodifiableCollection(loadedBookmarkMap.values());
    }

    public Set<UUID> getLoadedBookmarkIds()
    {
        return Collections.unmodifiableSet(loadedBookmarkMap.keySet());
    }

    public boolean contains(UUID bookmarkId)
    {
        return loadedBookmarkMap.containsValue(bookmarkId);
    }

    // ============================================================
    // Private Methods
    // ============================================================

    private void addToSystem(AbstractBookmark abstractBookmark, Map<UUID, AbstractBookmark> map, Map<Class, Set<AbstractBookmark>> mapSet)
    {
        Set<AbstractBookmark> s = mapSet.get(abstractBookmark.getClass());

        if (s==null)
        {
            s = new HashSet<>();
            mapSet.put(abstractBookmark.getClass(), s);
        }

        s.add(abstractBookmark);

        map.put(abstractBookmark.getId(), abstractBookmark);
    }

    private AbstractBookmark removeFromSystem(UUID uuid, Map<UUID, AbstractBookmark> map, Map<Class, Set<AbstractBookmark>> mapSet)
    {
        AbstractBookmark abstractBookmark = map.remove(uuid);

        if (abstractBookmark==null)
        {
            return null;
        }

        Set<AbstractBookmark> s = mapSet.get(abstractBookmark.getClass());

        if (s!=null)
        {
            s.remove(abstractBookmark);

            if (s.isEmpty())
            {
                mapSet.remove(s);
            }
        }

        return abstractBookmark;
    }


}

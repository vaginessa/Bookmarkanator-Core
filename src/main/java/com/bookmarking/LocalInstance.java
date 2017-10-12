package com.bookmarking;

import java.text.*;
import java.util.*;
import com.bookmarking.action.*;
import com.bookmarking.bookmark.*;
import com.bookmarking.bootstrap.*;
import com.bookmarking.io.*;
import com.bookmarking.search.*;
import com.bookmarking.settings.*;
import com.bookmarking.util.*;

public class LocalInstance implements BKInterface
{

    private static LocalInstance localInstance;

    @Override
    public void init()
    {
        Bootstrap.use();
    }

    @Override
    public void init(Settings settings)
    {
        Bootstrap.use(settings);
    }

    @Override
    public void init(BootstrapUIInterface bookmarkUIInterface)
    {
        Bootstrap.use(bookmarkUIInterface);
    }

    @Override
    public void exit()
        throws Exception
    {
        Bootstrap.use().exit();
    }

    @Override
    public BootstrapSettings getSettings()
    {
        return Bootstrap.use().getSettings();
    }

    @Override
    public BootstrapSettings setSettings(BootstrapSettings settings)
    {
        Bootstrap.use().setSettings(settings);
        return Bootstrap.use().getSettings();
    }

    @Override
    public void setBootstrapUIInterface(BootstrapUIInterface bootstrapUIInterface)
    {
        Bootstrap.use().setUiInterface(bootstrapUIInterface);
    }

    @Override
    public BootstrapUIInterface getBootstrapUIInterface()
    {
        return Bootstrap.use().getUiInterface();
    }

    @Override
    public IOInterface getIOInterface()
    {
        return Bootstrap.use().getIOInterface();
    }

    @Override
    public void setIOUIInterface(IOUIInterface uiInterface)
    {
        Bootstrap.use().getIOInterface().setUIInterface(uiInterface);
        Bootstrap.use().getUiInterface().setIOUIInterface(uiInterface);
    }

    @Override
    public Set<String> getSearchWords(Collection<UUID> bookmarkIds)
        throws Exception
    {
        return Bootstrap.use().getIOInterface().getSearchWords(bookmarkIds);
    }

    @Override
    public Set<String> getAllSearchWords()
        throws Exception
    {
        return Bootstrap.use().getIOInterface().getAllSearchWords();
    }

    @Override
    public List<AbstractBookmark> applySearchOptions(SearchOptions options)
        throws ParseException
    {
        return Bootstrap.use().getIOInterface().applySearchOptions(options);
    }

    @Override
    public List<AbstractBookmark> applySearchOptions(Collection<AbstractBookmark> bookmarks, SearchOptions options)
        throws ParseException
    {
        return Bootstrap.use().getIOInterface().applySearchOptions(bookmarks, options);
    }

    @Override
    public Set<AbstractBookmark> getAllBookmarks()
    {
        return Bootstrap.use().getIOInterface().getAllBookmarks();
    }

    @Override
    public Collection<String> getAllBookmarkNames()
    {
        return Bootstrap.use().getIOInterface().getAllBookmarkNames();
    }

    @Override
    public Set<Class> getAllBookmarkClasses()
    {
        return Bootstrap.use().getIOInterface().getAllBookmarkClasses();
    }

    @Override
    public AbstractBookmark getBookmark(UUID bookmarkId)
    {
        return Bootstrap.use().getIOInterface().getBookmark(bookmarkId);
    }

    @Override
    public Set<String> getBookmarkNames(Collection<UUID> bookmarkIds)
    {
        return Bootstrap.use().getIOInterface().getBookmarkNames(bookmarkIds);
    }

    @Override
    public Set<Class> getBookmarkClasses(Collection<UUID> bookmarkIds)
    {
        return Bootstrap.use().getIOInterface().getBookmarkClasses(bookmarkIds);
    }

    @Override
    public void addBookmark(AbstractBookmark bookmark)
        throws Exception
    {
        Bootstrap.use().getIOInterface().addBookmark(bookmark);
    }

    @Override
    public void addAllBookmarks(Collection<AbstractBookmark> bookmarks)
        throws Exception
    {
        Bootstrap.use().getIOInterface().addAllBookmarks(bookmarks);
    }

    @Override
    public void updateBookmark(AbstractBookmark bookmark)
        throws Exception
    {
        Bootstrap.use().getIOInterface().updateBookmark(bookmark);
    }

    @Override
    public void updateAll(Collection<AbstractBookmark> bookmarks)
        throws Exception
    {
        Bootstrap.use().getIOInterface().updateAll(bookmarks);
    }

    @Override
    public AbstractBookmark deleteBookmark(UUID bookmarkId)
    {
        return Bootstrap.use().getIOInterface().deleteBookmark(bookmarkId);
    }

    @Override
    public AbstractBookmark deleteBookmark(AbstractBookmark bookmark)
    {
        return Bootstrap.use().getIOInterface().deleteBookmark(bookmark);
    }

    @Override
    public Set<String> extractTags(Collection<UUID> bookmarkIds)
    {
        return Bootstrap.use().getIOInterface().extractTags(bookmarkIds);
    }

    @Override
    public Set<String> extractTags(List<AbstractBookmark> bookmarks)
    {
        return Bootstrap.use().getIOInterface().extractTags(bookmarks);
    }

    @Override
    public Set<String> getAllTags()
    {
        return Bootstrap.use().getIOInterface().getAllTags();
    }

    @Override
    public List<AbstractBookmark> renameTag(String originalTagName, String newTagName)
        throws Exception
    {
        return Bootstrap.use().getIOInterface().renameTag(originalTagName, newTagName);
    }

    @Override
    public List<AbstractBookmark> replaceTags(String replacement, Set<String> tagsToReplace)
        throws Exception
    {
        return Bootstrap.use().getIOInterface().replaceTags(replacement, tagsToReplace);
    }

    @Override
    public Set<AbstractBookmark> deleteTag(String tagToDelete)
    {
        return Bootstrap.use().getIOInterface().deleteTag(tagToDelete);
    }

    @Override
    public Set<AbstractBookmark> deleteTags(Set<String> tagsToDelete)
    {
        return Bootstrap.use().getIOInterface().deleteTags(tagsToDelete);
    }

    @Override
    public Set<String> extractTypeNames(Collection<UUID> bookmarkIds)
    {
        return Bootstrap.use().getIOInterface().extractTypeNames(bookmarkIds);
    }

    @Override
    public Set<String> getAllTypeNames()
    {
        return Bootstrap.use().getIOInterface().getAllTypeNames();
    }

    @Override
    public List<Class> getActionsPresent()
    {
        // Sort the actions and return them.
        List<ComparableClass> sort = new ArrayList<>();

        for (Class clazz: ModuleLoader.use().getClassesFound(AbstractAction.class))
        {
            sort.add(new ComparableClass(clazz));
        }

        Collections.sort(sort);

        List<Class> res = new ArrayList<>();

        for (ComparableClass comparableClass: sort)
        {
            res.add(comparableClass.getClazz());
        }

        return res;
    }

    public static LocalInstance use()
    {
        if (localInstance == null)
        {
            localInstance = new LocalInstance();
            localInstance.init();
        }
        return localInstance;
    }

    public static LocalInstance use(Settings settings)
    {
        if (localInstance == null)
        {
            localInstance = new LocalInstance();
            localInstance.init(settings);
        }
        return localInstance;
    }

    public static LocalInstance use(BootstrapUIInterface bootstrapUIInterface)
    {
        if (localInstance == null)
        {
            localInstance = new LocalInstance();
            localInstance.init(bootstrapUIInterface);
        }
        return localInstance;
    }
}

package com.bookmarking.file;

import java.io.*;
import java.text.*;
import java.util.*;
import com.bookmarking.*;
import com.bookmarking.bookmark.*;
import com.bookmarking.fileservice.*;
import com.bookmarking.io.*;
import com.bookmarking.module.*;
import com.bookmarking.search.*;
import com.bookmarking.settings.*;
import com.bookmarking.stats.*;
import com.bookmarking.ui.*;
import org.apache.logging.log4j.*;

/**
 * A file system implementation of the IOInterface. This implementation loads/saves only from the local file system.
 */
public class FileIO implements IOInterface
{
    private static final Logger logger = LogManager.getLogger(FileIO.class.getCanonicalName());

    // ============================================================
    // Fields
    // ============================================================
    private IOUIInterface ioUIInterface;
    private File file;
    private Settings fileIOSettings;
    //    private Map<UUID, AbstractBookmark> bookmarks;
    private ParsedBookmarks parsedBookmarks;
    private SearchGroup searchGroup;
    private SettingsIOInterface settingsIOInterface;

    private boolean isDirty;

    public FileIO()
    {
        parsedBookmarks = new ParsedBookmarks();
    }

    public boolean isDirty()
    {
        return isDirty;
    }

    public void setDirty(boolean dirty)
    {
        isDirty = dirty;
    }

    public ParsedBookmarks getParsedBookmarks()
    {
        return parsedBookmarks;
    }

    public void setParsedBookmarks(ParsedBookmarks parsedBookmarks)
    {
        this.parsedBookmarks = parsedBookmarks;
    }

    /**
     * Initiates FileIO which means use the settingsIOInterface to access configs necessary to init it self.
     * Specifically FileIO
     * @param settingsIOInterface  The fileIOSettings interface used by a particular implementation to pull init fileIOSettings from.
     * @throws Exception
     */
    @Override
    public void init(SettingsIOInterface settingsIOInterface)
        throws Exception
    {
        this.settingsIOInterface = settingsIOInterface;
        //TODO Figure out what to do about the bookmark.xml file getting deleted if the program has an error. Possibly create a temporary file to read from while it is running?
        this.fileIOSettings = this.getSettings();

        // Using regular settings here instead of having a separate settings file.

//        File settingsFile = LocalInstance.use().getFileService().getFileSync(Defaults.SETTINGS_FILE_CONTEXT).getFile();
//        Objects.requireNonNull(settingsFile, "Settings file is not set.");

//        String parent = settingsFile.getParent();

        File directory = settingsIOInterface.getSettings().getFileSetting(Defaults.DIRECTORIES_GROUP, Defaults.SELECTED_FILE_LOCATION_KEY);
        directory = new File(directory.getParent());
        Objects.requireNonNull(directory);

        String bookmarkFileName = directory.getCanonicalPath() + File.separatorChar + Defaults.BOOKMARKS_FILE_NAME;
        logger.trace("Bookmarks file inferred from fileIOSettings file is \"" + bookmarkFileName + "\"");
        file = new File(bookmarkFileName);

        // Create bookmark file
        BookmarksXMLParser parser = new BookmarksXMLParser();
        parser.setObject(this);
        FileSync<FileIO> fileSync = new FileSync<>(new BookmarksXMLWriter(),parser, file);
        FileService.use().addFile(fileSync, Defaults.BOOKMARKS_FILE_SYNC_CONTEXT);

        // Create file io fileIOSettings file
//        File fileIOSettings = createSettingsFile(file);
//        FileSync<Settings> fileSync2 = new FileSync<>(new SettingsXMLWriter(), new SettingsXMLParser(), settingsFile);
//        FileService.use().addFile(fileSync2, Defaults.FILE_IO_SETTINGS_KEY);

        //        File searchSettingsFile = new File(file.getParent()+File.separatorChar+DEFAULT_SEARCH_SETTINGS_FILE_NAME);
        //        FileSync<SearchGroup> searchGroupFileSync = new FileSync<>(new SearchSettingsWriter(), new SearchSettingsReader(), searchSettingsFile);
        //        FileService.use().addFile(searchGroupFileSync,FILE_SEARCH_SETTINGS_KEY);

        // Load files in.
//        load();
        fileSync.readFromDisk();

        searchGroup = new SearchGroup();

        for (AbstractBookmark abstractBookmark : parsedBookmarks.getLoadedBookmarks())
        {
            searchGroup.addBookmark(abstractBookmark);
        }

        searchGroup.setNumSearchResults(parsedBookmarks.getLoadedBookmarkIds().size());
    }

    @Override
    public void init(SettingsIOInterface settingsIOInterface, IOUIInterface iouiInterface)
        throws Exception
    {
        this.ioUIInterface = iouiInterface;
        this.init(settingsIOInterface);
    }

    @Override
    public synchronized void save()
        throws Exception
    {
        Settings mainSettings = this.settingsIOInterface.getSettings();
        mainSettings.importSettings(this.fileIOSettings);
        this.settingsIOInterface.setSettings(mainSettings);
        this.settingsIOInterface.save();

        setDirty(false);
    }

    @Override
    public void exit()
        throws Exception
    {
        // Do nothing here.
    }

    @Override
    public Settings getSettings()
    {
        if (fileIOSettings == null)
        {
            fileIOSettings = new Settings();
        }
        return fileIOSettings;
    }

    @Override
    public AbstractBookmark getBookmark(UUID bookmarkId)
    {
        return parsedBookmarks.getLoadedById(bookmarkId);
    }

    @Override
    public Set<AbstractBookmark> getBookmarks(Set<UUID> bookmarkIds)
    {
        Set<AbstractBookmark> res = new HashSet<>();

        bookmarkIds.stream().forEach(id -> res.add(parsedBookmarks.getLoadedById(id)));

        return res;
    }

    @Override
    public Set<String> getSearchWords(Collection<UUID> bookmarkIds)
        throws Exception
    {
        Set<String> res = new HashSet<>();
        for (UUID uuid : bookmarkIds)
        {
            AbstractBookmark bookmark = getBookmark(uuid);
            Objects.requireNonNull(bookmark);
            res.addAll(bookmark.getSearchWords());
        }

        return res;
    }

    @Override
    public Set<String> extractTypeNames(Collection<UUID> bookmarkIds)
    {
        Set<String> res = new HashSet<>();
        for (UUID uuid : bookmarkIds)
        {
            AbstractBookmark bookmark = getBookmark(uuid);
            Objects.requireNonNull(bookmark);
            res.add(bookmark.getTypeName());
        }

        return res;
    }

    @Override
    public List<String> getBookmarkNames(Collection<UUID> bookmarkIds)
    {
        List<String> res = new ArrayList<>();
        for (UUID uuid : bookmarkIds)
        {
            AbstractBookmark bookmark = getBookmark(uuid);
            Objects.requireNonNull(bookmark);
            res.add(bookmark.getName());
        }

        return res;
    }

    @Override
    public Set<Class> getBookmarkClasses(Collection<UUID> bookmarkIds)
    {
        Set<Class> res = new HashSet<>();

        for (UUID uuid : bookmarkIds)
        {
            AbstractBookmark bk = getBookmark(uuid);
            res.add(bk.getClass());
        }

        return res;
    }

    @Override
    public Set<AbstractBookmark> getAllBookmarks()
    {
        return new HashSet<>(parsedBookmarks.getLoadedBookmarks());
    }

    @Override
    public Set<String> getAllTags()
    {
        return searchGroup.getBookmarkTags().getFullTextWords();
    }

    @Override
    public Set<String> getAllSearchWords()
        throws Exception
    {
        return getSearchWords(parsedBookmarks.getLoadedBookmarkIds());
    }

    @Override
    public Set<String> getAllTypeNames()
    {
        return extractTypeNames(parsedBookmarks.getLoadedBookmarkIds());
    }

    @Override
    public int getInstanceCount(Class clazz)
    {
        Set<AbstractBookmark> s = parsedBookmarks.getLoadedByClass(clazz);

        if (s != null)
        {
            return s.size();
        }

        return 0;
    }

    @Override
    public List<String> getAllBookmarkNames()
    {
        return getBookmarkNames(parsedBookmarks.getLoadedBookmarkIds());
    }

    @Override
    public Set<Class> getAllBookmarkClasses()
    {
        return ModuleLoader.use().getClassesFound(AbstractBookmark.class);
    }

    @Override
    public void setSettings(Settings settings)
    {
        this.fileIOSettings = settings;
        setDirty(true);
    }

    @Override
    public void addBookmark(AbstractBookmark bookmark)
        throws Exception
    {
        //TODO Add/update to search methods
        Objects.requireNonNull(bookmark);

        if (parsedBookmarks.contains(bookmark.getId()))
        {
            throw new Exception("Duplicate bookmark present");
        }

        parsedBookmarks.addLoaded(bookmark);
        setDirty(true);
    }

    @Override
    public void addAllBookmarks(Collection<AbstractBookmark> bookmarks)
        throws Exception
    {
        //TODO Add/update to search methods
        for (AbstractBookmark bookmark : bookmarks)
        {
            addBookmark(bookmark);
        }
        setDirty(true);
    }

    @Override
    public void updateBookmark(AbstractBookmark bookmark)
        throws Exception
    {
        //TODO Add/update to search methods
        Objects.requireNonNull(bookmark);
        if (!parsedBookmarks.contains(bookmark.getId()))
        {
            throw new Exception("Bookmark " + bookmark.getId() + " not found.");
        }
        setDirty(true);
    }

    @Override
    public void updateAll(Collection<AbstractBookmark> bookmarks)
        throws Exception
    {
        //TODO Add/update to search methods
        for (AbstractBookmark bookmark : bookmarks)
        {
            updateBookmark(bookmark);
        }
        setDirty(true);
    }

    @Override
    public List<AbstractBookmark> renameTag(String originalTagName, String newTagName)
        throws Exception
    {
        //TODO Add/update to search methods
        SearchOptions searchOptions = new SearchOptions();
        Set<String> tags = new HashSet<>();
        tags.add(originalTagName);
        searchOptions.add(Operation.TagOptions.ANY_TAG, tags);
        List<AbstractBookmark> bookmarks = applySearchOptions(searchOptions);

        for (AbstractBookmark bookmark : bookmarks)
        {
            bookmark.removeTag(originalTagName);
            bookmark.addTag(newTagName);
            updateBookmark(bookmark);
        }

        setDirty(true);

        return bookmarks;
    }

    @Override
    public List<AbstractBookmark> replaceTags(String replacement, Set<String> tagsToReplace)
        throws Exception
    {
        //TODO Add/update to search methods
        SearchOptions searchOptions = new SearchOptions();
        searchOptions.add(Operation.TagOptions.ANY_TAG, tagsToReplace);
        List<AbstractBookmark> bookmarks = applySearchOptions(searchOptions);

        for (AbstractBookmark bookmark : bookmarks)
        {
            bookmark.getTags().removeAll(tagsToReplace);
            bookmark.addTag(replacement);
            updateBookmark(bookmark);
        }

        setDirty(true);

        return bookmarks;
    }

    @Override
    public AbstractBookmark deleteBookmark(UUID bookmarkId)
    {
        //TODO Add/update to search methods

        setDirty(true);

        return null;
    }

    @Override
    public AbstractBookmark deleteBookmark(AbstractBookmark bookmark)
    {
        //TODO Add/update to search methods

        setDirty(true);

        return null;
    }

    @Override
    public Set<AbstractBookmark> deleteTag(String tagToDelete)
    {
        //TODO Add/update to search methods

        setDirty(true);

        return null;
    }

    @Override
    public Set<AbstractBookmark> deleteTags(Set<String> tagsToDelete)
    {
        //TODO Add/update to search methods

        setDirty(true);

        return null;
    }

    @Override
    public List<AbstractBookmark> applySearchOptions(SearchOptions options)
        throws ParseException
    {
        Set<AbstractBookmark> bks;

        if (options.getSearchTerm() == null || options.getSearchTerm().trim().isEmpty())
        {
            bks = new HashSet<>();
            bks.addAll(parsedBookmarks.getLoadedBookmarks());
        }
        else
        {
            Set<UUID> bkIds = searchGroup.applySearchOptions(options);
            bks = getBookmarks(bkIds);
        }

        Filter filter = Filter.use(bks);
        filter.filterBySearchOptions(options);

        return filter.results();
    }

    @Override
    public List<AbstractBookmark> applySearchOptions(Collection<AbstractBookmark> bookmarks, SearchOptions options)
        throws ParseException
    {
        Filter filter = Filter.use(bookmarks);
        filter.filterBySearchOptions(options);

        return filter.results();
    }

    @Override
    public IOUIInterface getIOUIInterface()
    {
        return ioUIInterface;
    }

    @Override
    public void setIOUIInterface(IOUIInterface uiInterface)
    {
        this.ioUIInterface = uiInterface;
    }

//    @Override
//    public void undo()
//        throws Exception
//    {
//        // TODO undo IOInterface
//        // TODO undo Main Settings changes.
//    }
//
//    @Override
//    public void redo()
//        throws Exception
//    {
//        // TODO redo IOInterface
//        // TODO redo Main Settings changes.
//    }
//
//    @Override
//    public void clearUndoRedoStack()
//        throws Exception
//    {
//        // TODO Implement stack clearOperationsList.
//    }

    @Override
    public StatsInterface getStatsInterface()
    {
        return null;
    }

    // ----------
    // private
    // ----------
//
//    private void load()
//        throws Exception
//    {
//       FileSync fileSync =  FileService.use().getFileSync(Defaults.BOOKMARKS_FILE_SYNC_CONTEXT);
//       fileSync.readFromDisk();
//    }
//
//    private File createSettingsFile(File input)
//    {
//        String path = input.getParent();
//        return new File(path + File.separatorChar + Defaults.DEFAULT_SETTINGS_FILE_NAME);
//    }
}

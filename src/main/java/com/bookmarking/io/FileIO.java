package com.bookmarking.io;

import java.io.*;
import java.text.*;
import java.util.*;
import com.bookmarking.*;
import com.bookmarking.bookmark.*;
import com.bookmarking.fileservice.*;
import com.bookmarking.search.*;
import com.bookmarking.settings.*;
import com.bookmarking.ui.*;
import com.bookmarking.xml.*;
import org.apache.logging.log4j.*;

public class FileIO implements IOInterface
{


    // Static fields
    private static final Logger logger = LogManager.getLogger(FileIO.class.getCanonicalName());
    private static final String FILE_IO_KEY = "FILE_IO";
    private static final String DEFAULT_BOOKMARKS_FILE_NAME = "bookmarks.xml";
    private static final String FILE_IO_SETTINGS_KEY = "FILE_IO_SETTINGS";
    private static final String DEFAULT_SETTINGS_FILE_NAME = "file-io-settings.xml";

    // Fields

    private IOUIInterface uiInterface;
    private File file;
    private Settings settings;
    private Map<UUID, AbstractBookmark> bookmarks;
    private Search<UUID> bookmarkNames;
    private Search<UUID> bookmarkTypeNames;//Such as text, web, terminal, mapping, whatever...
    //    private Search<UUID> bookmarkText;//The text the bookmark contains.
    private Map<String, Set<UUID>> fullTextSearchMap;//The map used to search only words of the bookmark.
    private Search<UUID> bookmarkTags;
    private int numSearchResults;//How many search results to return.
    private IOInterface IOInterface;// Currently this is FileIo, but it could be any interface. For instance it could point to a database, or web service.

    public FileIO()
    {
        bookmarks = new HashMap<>();
        bookmarkNames = new Search<>();
        bookmarkTypeNames = new Search<>();
        //        bookmarkText = new Search<>();
        bookmarkTags = new Search<>();
        fullTextSearchMap = new HashMap<>();
        numSearchResults = 20;
    }

    @Override
    public void init(String config)
        throws Exception
    {
        logger.trace("Entering init method with config \"" + config + "\"");
        //TODO Figure out what to do about the bookmark.xml file getting deleted if the program has an error. Possibly create a temporary file to read from while it is running?
        this.settings = this.getSettings();

        if (config == null || config.trim().isEmpty())
        {//
            logger.trace("No file location sent in. Inferring location from settings file used.");
            File settingsFile = GlobalSettings.use().getFile();
            Objects.requireNonNull(settingsFile, "Settings file is not set.");

            String parent = settingsFile.getParent();

            String bookmarkFileName = parent + File.separatorChar + DEFAULT_BOOKMARKS_FILE_NAME;
            logger.trace("Bookmarks file inferred from settings file is \"" + bookmarkFileName + "\"");
            file = new File(bookmarkFileName);
        }
        else
        {// Using file sent in.
            file = new File(config);
        }

        // Create bookmark file
        FileSync<IOInterface> fileSync = new FileSync<>(new BookmarksXMLWriter(), new BookmarksXMLParser(), file);
        FileService.use().addFile(fileSync, FILE_IO_KEY);

        // Create file io settings file
        File settingsFile = createSettingsFile(file);
        FileSync<Settings> fileSync2 = new FileSync<>(new SettingsXMLWriter2(), new SettingsXMLParser2(), settingsFile);
        FileService.use().addFile(fileSync2, FILE_IO_SETTINGS_KEY);

        // Load both files in.
        load();
    }

    @Override
    public void save()
        throws Exception
    {
        FileSync<IOInterface> fileSync = FileService.use().getFile(FILE_IO_KEY);
        fileSync.setObjectToWrite(this);
        fileSync.writeToDisk();

        FileSync<Settings> fileSync2 = FileService.use().getFile(FILE_IO_SETTINGS_KEY);

//       TODO figure out why the settings are null.....
        fileSync2.setObjectToWrite(settings);
        fileSync2.writeToDisk();
    }

    @Override
    public void save(String config)
        throws Exception
    {
        FileSync<IOInterface> fileSync = FileService.use().getFile(FILE_IO_KEY);
        File file = new File(config);
        fileSync.setFile(file);
        fileSync.setObjectToWrite(this);
        fileSync.writeToDisk();

        // Get settings file from current file location
        File settingsFile = createSettingsFile(file);
        FileSync<Settings> fileSync2 = FileService.use().getFile(FILE_IO_SETTINGS_KEY);
        fileSync2.setFile(settingsFile);
        fileSync2.setObjectToWrite(settings);
        fileSync2.writeToDisk();
    }

    @Override
    public void close()
        throws Exception
    {

    }

    @Override
    public AbstractBookmark getBookmark(String params, Object... obj)
    {
        throw new UnsupportedOperationException("Not supported at this time");
    }

    @Override
    public List<AbstractBookmark> getBookmarkList(String params, Object... obj)
    {
        throw new UnsupportedOperationException("Not supported at this time");
    }

    @Override
    public String getOther(String params, Object... obj)
    {
        throw new UnsupportedOperationException("Not supported at this time");
    }

    @Override
    public List<String> getOtherList(String params, Object... obj)
    {
        throw new UnsupportedOperationException("Not supported at this time");
    }

    @Override
    public Settings getSettings()
    {
        if (settings == null)
        {
            settings = new Settings();
        }
        return settings;
    }

    @Override
    public AbstractBookmark getBookmark(UUID bookmarkId)
    {
        return bookmarks.get(bookmarkId);
    }

    @Override
    public Set<String> getTags(Collection<UUID> bookmarkIds)
    {
        Set<String> res = new HashSet<>();
        for (UUID uuid: bookmarkIds)
        {
            AbstractBookmark bookmark = getBookmark(uuid);
            Objects.requireNonNull(bookmark);
            res.addAll(bookmark.getTags());
        }

        return res;
    }

    @Override
    public Set<String> getSearchWords(Collection<UUID> bookmarkIds)
        throws Exception
    {
        Set<String> res = new HashSet<>();
        for (UUID uuid: bookmarkIds)
        {
            AbstractBookmark bookmark = getBookmark(uuid);
            Objects.requireNonNull(bookmark);
            res.addAll(bookmark.getSearchWords());
        }

        return res;
    }

    @Override
    public Set<String> getTypeNames(Collection<UUID> bookmarkIds)
    {
        Set<String> res = new HashSet<>();
        for (UUID uuid: bookmarkIds)
        {
            AbstractBookmark bookmark = getBookmark(uuid);
            Objects.requireNonNull(bookmark);
            res.add(bookmark.getTypeName());
        }

        return res;
    }

    @Override
    public Set<String> getBookmarkNames(Collection<UUID> bookmarkIds)
    {
        Set<String> res = new HashSet<>();
        for (UUID uuid: bookmarkIds)
        {
            AbstractBookmark bookmark = getBookmark(uuid);
            Objects.requireNonNull(bookmark);
            res.add(bookmark.getName());
        }

        return res;
    }

    @Override
    public Set<String> getClassNames(Collection<UUID> bookmarkIds)
    {
        Set<String> res = new HashSet<>();
        for (UUID uuid: bookmarkIds)
        {
            AbstractBookmark bookmark = getBookmark(uuid);
            Objects.requireNonNull(bookmark);
            res.add(bookmark.getClass().getCanonicalName());
        }

        return res;
    }

    @Override
    public Set<AbstractBookmark> getWithinDateRange(Date includeIfAfter, Date includeIfBefore)
    {
        Set<AbstractBookmark> res = new HashSet<>();

        for (AbstractBookmark bk : getAllBookmarks())
        {
            if (bk.getCreationDate().after(includeIfAfter) && bk.getCreationDate().before(includeIfBefore))
            {
                res.add(bk);
            }
        }

        return res;
    }

    @Override
    public Set<AbstractBookmark> getWithinLastAccessedDateRange(Date includeIfAfter, Date includeIfBefore)
    {
        Set<AbstractBookmark> res = new HashSet<>();

        for (AbstractBookmark bk : getAllBookmarks())
        {
            if (bk.getLastAccessedDate().after(includeIfAfter) && bk.getLastAccessedDate().before(includeIfBefore))
            {
                res.add(bk);
            }
        }

        return res;
    }

    @Override
    public Set<AbstractBookmark> getAllBookmarks()
    {
        return new HashSet<>(bookmarks.values());
    }

    @Override
    public Set<String> getAllTags()
    {
       return getTags(bookmarks.keySet());
    }

    @Override
    public Set<String> getAllSearchWords()
        throws Exception
    {
        return getSearchWords(bookmarks.keySet());
    }

    @Override
    public Set<String> getAllTypeNames()
    {
        return getTypeNames(bookmarks.keySet());
    }

    @Override
    public Set<String> getAllBookmarkNames()
    {
        return getBookmarkNames(bookmarks.keySet());
    }

    @Override
    public Set<String> getAllClassNames()
    {
        return getClassNames(bookmarks.keySet());
    }

    @Override
    public void setSettings(Settings settings)
    {
        this.settings = settings;
    }

    @Override
    public void addBookmark(AbstractBookmark bookmark)
        throws Exception
    {
        //TODO Add/update to search methods
        Objects.requireNonNull(bookmark);

        if (bookmarks.containsKey(bookmark.getId()))
        {
            throw new Exception("Duplicate bookmark present");
        }

        bookmarks.put(bookmark.getId(), bookmark);
    }

    @Override
    public void addAllBookmarks(Collection<AbstractBookmark> bookmarks)
        throws Exception
    {
        //TODO Add/update to search methods
        for (AbstractBookmark bookmark: bookmarks)
        {
            addBookmark(bookmark);
        }
    }

    @Override
    public void updateBookmark(AbstractBookmark bookmark)
        throws Exception
    {
        //TODO Add/update to search methods
        Objects.requireNonNull(bookmark);
        if (!bookmarks.containsKey(bookmark.getId()))
        {
            throw new Exception("Bookmark "+bookmark.getId()+" not found.");
        }
    }

    @Override
    public void updateAll(Collection<AbstractBookmark> bookmarks)
        throws Exception
    {
        //TODO Add/update to search methods
        for (AbstractBookmark bookmark: bookmarks)
        {
            updateBookmark(bookmark);
        }
    }

    @Override
    public List<AbstractBookmark> renameTag(String originalTagName, String newTagName)throws Exception
    {
        //TODO Add/update to search methods
        SearchOptions searchOptions = new SearchOptions();
        Set<String> tags = new HashSet<>();
        tags.add(originalTagName);
        searchOptions.addTags(TagsInfo.TagOptions.ANY_TAG,tags);
        List<AbstractBookmark> bookmarks = applySearchOptions(searchOptions);

        for (AbstractBookmark bookmark: bookmarks)
        {
            bookmark.removeTag(originalTagName);
            bookmark.addTag(newTagName);
            updateBookmark(bookmark);
        }

        return bookmarks;
    }

    @Override
    public List<AbstractBookmark> replaceTags(String replacement, Set<String> tagsToReplace)throws Exception
    {
        //TODO Add/update to search methods
        SearchOptions searchOptions = new SearchOptions();
        searchOptions.addTags(TagsInfo.TagOptions.ANY_TAG,tagsToReplace);
        List<AbstractBookmark> bookmarks = applySearchOptions(searchOptions);

        for (AbstractBookmark bookmark: bookmarks)
        {
            bookmark.getTags().removeAll(tagsToReplace);
            bookmark.addTag(replacement);
            updateBookmark(bookmark);
        }

        return bookmarks;
    }

    @Override
    public AbstractBookmark deleteBookmark(UUID bookmarkId)
    {
        //TODO Add/update to search methods
        return null;
    }

    @Override
    public AbstractBookmark deleteBookmark(AbstractBookmark bookmark)
    {
        //TODO Add/update to search methods
        return null;
    }

    @Override
    public Set<AbstractBookmark> deleteTag(String tagToDelete)
    {
        //TODO Add/update to search methods
        return null;
    }

    @Override
    public Set<AbstractBookmark> deleteTags(Set<String> tagsToDelete)
    {
        //TODO Add/update to search methods
        return null;
    }

    @Override
    public List<AbstractBookmark> applySearchOptions(SearchOptions options)
        throws ParseException
    {
        Filter filter = Filter.use(getAllBookmarks());
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
    public IOUIInterface getUIInterface()
    {
        return uiInterface;
    }

    @Override
    public void setUIInterface(IOUIInterface uiInterface)
    {
        this.uiInterface = uiInterface;
    }

    @Override
    public Set<String> getSettingsKeys()
    {
        //TODO Implement
        return null;
    }

    // ----------
    // private
    // ----------

    private void load()
        throws Exception
    {
        // Load bookmark
        FileSync<FileIO> fileSync = FileService.use().getFile(FILE_IO_KEY);
        fileSync.injectParsingObject(this);
        fileSync.readFromDisk();

        logger.info("Calling systemInit() on the bookmark.");
        Set<AbstractBookmark> bks = getAllBookmarks();
        for (AbstractBookmark abs : bks)
        {
            abs.systemInit();
        }
        logger.trace("Done.");

        // Load settings
        FileSync<Settings> fileSync2 = FileService.use().getFile(FILE_IO_SETTINGS_KEY);
        fileSync2.readFromDisk();
        settings = fileSync2.getObject();
    }

    private File createSettingsFile(File input)
    {
        String path = input.getParent();
        return new File(path + File.separatorChar + DEFAULT_SETTINGS_FILE_NAME);
    }

}

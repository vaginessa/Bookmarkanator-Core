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
import org.apache.logging.log4j.*;

/**
 * A file system implementation of the IOInterface. This implementation loads/saves only from the local file system.
 */
public class FileIO implements IOInterface
{
    // Static fields
    private static final Logger logger = LogManager.getLogger(FileIO.class.getCanonicalName());
    private static final String FILE_IO_KEY = "FILE_IO";
    private static final String DEFAULT_BOOKMARKS_FILE_NAME = "bookmarks.xml";
    private static final String FILE_IO_SETTINGS_KEY = "FILE_IO_SETTINGS";
    private static final String DEFAULT_SETTINGS_FILE_NAME = "file-io-settings.xml";
    private static final String FILE_SEARCH_SETTINGS_KEY = "FILE_SEARCH_SETTINGS";
    private static final String DEFAULT_SEARCH_SETTINGS_FILE_NAME = "file-search-settings.xml";

    // Fields
    private IOUIInterface uiInterface;
    private File file;
    private Settings settings;
//    private Map<UUID, AbstractBookmark> bookmarks;
    private ParsedBookmarks parsedBookmarks;
    private SearchGroup searchGroup;


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

    @Override
    public void init(String config)
        throws Exception
    {
        logger.trace("Entering use method with config \"" + config + "\"");
        //TODO Figure out what to do about the bookmark.xml file getting deleted if the program has an error. Possibly create a temporary file to read from while it is running?
        this.settings = this.getSettings();

        if (config == null || config.trim().isEmpty())
        {//
            logger.trace("No file location sent in. Inferring location from settings file used.");
            File settingsFile = LocalInstance.use().getFileService().getFileSync(MainInterface.SETTINGS_FILE_CONTEXT).getFile();
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
        FileSync<FileIO> fileSync = new FileSync<>(new BookmarksXMLWriter(), new BookmarksXMLParser(), file);
        FileService.use().addFile(fileSync, FILE_IO_KEY);

        // Create file io settings file
        File settingsFile = createSettingsFile(file);
        FileSync<Settings> fileSync2 = new FileSync<>(new SettingsXMLWriter(), new SettingsXMLParser(), settingsFile);
        FileService.use().addFile(fileSync2, FILE_IO_SETTINGS_KEY);

//        File searchSettingsFile = new File(file.getParent()+File.separatorChar+DEFAULT_SEARCH_SETTINGS_FILE_NAME);
//        FileSync<SearchGroup> searchGroupFileSync = new FileSync<>(new SearchSettingsWriter(), new SearchSettingsReader(), searchSettingsFile);
//        FileService.use().addFile(searchGroupFileSync,FILE_SEARCH_SETTINGS_KEY);

        // Load files in.
        load();

        searchGroup = new SearchGroup();

        for (AbstractBookmark abstractBookmark: parsedBookmarks.getLoadedBookmarks())
        {
            searchGroup.addBookmark(abstractBookmark);
        }
    }

    public ParsedBookmarks getParsedBookmarks()
    {
        return parsedBookmarks;
    }

    public void setParsedBookmarks(ParsedBookmarks parsedBookmarks)
    {
        this.parsedBookmarks = parsedBookmarks;
    }

    @Override
    public synchronized void save()
        throws Exception
    {
        FileSync<IOInterface> fileSync = FileService.use().getFileSync(FILE_IO_KEY);
        fileSync.setObjectToWrite(this);
        fileSync.writeToDisk();

        FileSync<Settings> fileSync2 = FileService.use().getFileSync(FILE_IO_SETTINGS_KEY);

        fileSync2.setObjectToWrite(settings);
        fileSync2.writeToDisk();

//        FileSync<SearchGroup> fileSync3 = FileService.use().getFileSync(FILE_SEARCH_SETTINGS_KEY);
//        fileSync3.setObjectToWrite(searchGroup);
//        fileSync3.writeToDisk();

        setDirty(false);
    }

    @Override
    public synchronized void save(String config)
        throws Exception
    {
        FileSync<IOInterface> fileSync = FileService.use().getFileSync(FILE_IO_KEY);
        File file = new File(config);
        fileSync.setFile(file);
        fileSync.setObjectToWrite(this);
        fileSync.writeToDisk();

        // Get settings file from current file location
        File settingsFile = createSettingsFile(file);
        FileSync<Settings> fileSync2 = FileService.use().getFileSync(FILE_IO_SETTINGS_KEY);
        fileSync2.setFile(settingsFile);
        fileSync2.setObjectToWrite(settings);
        fileSync2.writeToDisk();

//        File searchSettingsFile = new File(file.getParent()+File.separatorChar+DEFAULT_SEARCH_SETTINGS_FILE_NAME);
//        FileSync<SearchGroup> fileSync3 = FileService.use().getFileSync(FILE_SEARCH_SETTINGS_KEY);
//        fileSync3.setFile(searchSettingsFile);
//        fileSync3.setObjectToWrite(searchGroup);
//        fileSync3.writeToDisk();
        
        setDirty(false);
    }

    @Override
    public void close()
        throws Exception
    {

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
        for (UUID uuid: bookmarkIds)
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
        for (UUID uuid: bookmarkIds)
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
        for (UUID uuid: bookmarkIds)
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

        for (UUID uuid: bookmarkIds)
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
       return extractTags(parsedBookmarks.getLoadedBookmarks());
    }

    @Override
    public SortedMap<String, Set<String>> getAllTagsAlphabetical(Set<String> tagsToRemove)
    {
        Set<String> allTags = getAllTags();

        if (tagsToRemove!=null)
        {
            allTags.removeAll(tagsToRemove);
        }

        return orderTagsAlphabetical(allTags);
    }

    @Override
    public SortedMap<String, Set<String>> orderTagsAlphabetical(Set<String> tags)
    {
        Map<String, Set<String>> tagsMap = new LinkedHashMap<>();

        // Add available tags to map by first letter.
        for (String tag: tags)
        {
            if (!tag.isEmpty())
            {
                String s = tag.charAt(0)+"";
                s = s.toUpperCase();

                Set<String> set = tagsMap.get(s);

                if (set==null)
                {
                    set = new HashSet<>();
                    tagsMap.put(s, set);
                }

                set.add(tag);
            }
        }

        List<String> tagsMapKeyset = new ArrayList<>(tagsMap.keySet());
        Collections.sort(tagsMapKeyset);

        SortedMap<String, Set<String>> res = new TreeMap<>();

        for (String key: tagsMapKeyset)
        {
            res.put(key, tagsMap.get(key));
        }

        return res;
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
    public int numberOfType(Class clazz)
    {
        Set<AbstractBookmark> s = parsedBookmarks.getLoadedByClass(clazz);

        if (s!=null)
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
        this.settings = settings;
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
        for (AbstractBookmark bookmark: bookmarks)
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
            throw new Exception("Bookmark "+bookmark.getId()+" not found.");
        }
        setDirty(true);
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
        setDirty(true);
    }

    @Override
    public List<AbstractBookmark> renameTag(String originalTagName, String newTagName)throws Exception
    {
        //TODO Add/update to search methods
        SearchOptions searchOptions = new SearchOptions();
        Set<String> tags = new HashSet<>();
        tags.add(originalTagName);
        searchOptions.add(Operation.TagOptions.ANY_TAG,tags);
        List<AbstractBookmark> bookmarks = applySearchOptions(searchOptions);

        for (AbstractBookmark bookmark: bookmarks)
        {
            bookmark.removeTag(originalTagName);
            bookmark.addTag(newTagName);
            updateBookmark(bookmark);
        }

        setDirty(true);

        return bookmarks;
    }

    @Override
    public List<AbstractBookmark> replaceTags(String replacement, Set<String> tagsToReplace)throws Exception
    {
        //TODO Add/update to search methods
        SearchOptions searchOptions = new SearchOptions();
        searchOptions.add(Operation.TagOptions.ANY_TAG,tagsToReplace);
        List<AbstractBookmark> bookmarks = applySearchOptions(searchOptions);

        for (AbstractBookmark bookmark: bookmarks)
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


        if (options.getSearchTerm()==null || options.getSearchTerm().trim().isEmpty())
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
    public void undo()
        throws Exception
    {
        // TODO undo IOInterface
        // TODO undo Main Settings changes.
    }

    @Override
    public void redo()
        throws Exception
    {
        // TODO redo IOInterface
        // TODO redo Main Settings changes.
    }

    @Override
    public void clearUndoRedoStack()
        throws Exception
    {
        // TODO Implement stack clearOperationsList.
    }

    // ----------
    // private
    // ----------

    private void load()
        throws Exception
    {
        // Load bookmark
        FileSync<FileIO> fileSync = FileService.use().getFileSync(FILE_IO_KEY);
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
        FileSync<Settings> fileSync2 = FileService.use().getFileSync(FILE_IO_SETTINGS_KEY);
        fileSync2.readFromDisk();
        settings = fileSync2.getObject();

        // Load search settings
//        FileSync<SearchGroup> fileSync3 = FileService.use().getFileSync(FILE_SEARCH_SETTINGS_KEY);
//        fileSync3.readFromDisk();
//        searchGroup = fileSync3.getObject();
    }

    private File createSettingsFile(File input)
    {
        String path = input.getParent();
        return new File(path + File.separatorChar + DEFAULT_SETTINGS_FILE_NAME);
    }
}

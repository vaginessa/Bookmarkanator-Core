package com.bookmarking;

import java.util.*;
import com.bookmarking.bootstrap.*;
import com.bookmarking.fileservice.*;
import com.bookmarking.io.*;
import com.bookmarking.module.*;
import com.bookmarking.settings.*;
import com.bookmarking.ui.*;

public class LocalInstance implements MainInterface
{
    // ============================================================
    // Fields
    // ============================================================

    private static LocalInstance localInstance;

    // Interfaces
    private Bootstrap initInterface;
    private IOInterface ioInterface;
    private UIInterface uiInterface;

    // Other
    private MainSettings mainSettings;

    @Override
    public void init()
        throws Exception
    {
        initInterface = new Bootstrap();
        initInterface.init();
    }

    @Override
    public void init(Settings settings)
        throws Exception
    {
        initInterface = new Bootstrap();
        initInterface.init(settings);
    }

    @Override
    public void init(InitUIInterface initUIInterface)
        throws Exception
    {
        initInterface = new Bootstrap();
        initInterface.init();
        initInterface.setInitUIInterface(initUIInterface);
    }

    @Override
    public void init(Settings settings, InitUIInterface initUIInterface)
        throws Exception
    {
        initInterface = new Bootstrap(settings);
        initInterface.init();
        initInterface.setInitUIInterface(initUIInterface);
    }

    @Override
    public void exit()
        throws Exception
    {
        this.saveSettings();
    }

    @Override
    public InitInterface getInitInterface()
    {
        return initInterface;
    }

    @Override
    public ModuleLoader getModuleLoader()
    {
        return ModuleLoader.use();
    }

    @Override
    public FileService getFileService()
    {
        return FileService.use();
    }

    @Override
    public IOInterface getIOInterface()
    {
        return this.initInterface.getIOInterface();
    }

    @Override
    public MainSettings getSettings()
    {
        if (mainSettings == null)
        {
            MainSettings mSettings = new MainSettings();
            mSettings.setMainSettings((Settings) getFileService().getFileSync(MainInterface.SETTINGS_FILE_CONTEXT).getObject());
            if (this.ioInterface != null)
            {
                mSettings.setIoSettings((Settings) getFileService().getFileSync(IOInterface.SETTINGS_FILE_CONTEXT).getObject());
            }
            this.mainSettings = mSettings;
        }

        return this.mainSettings;
    }

    @Override
    public void setSettings(MainSettings mainSettings)
        throws Exception
    {
        this.mainSettings = mainSettings;
        this.getFileService().getFileSync(MainInterface.SETTINGS_FILE_CONTEXT).setObjectToWrite(mainSettings.getMainSettings());
        if (this.getIOInterface() != null && mainSettings.getIoSettings() != null)
        {
            getIOInterface().setSettings(mainSettings.getIoSettings());
        }
    }

    @Override
    public MainSettings saveSettings()
        throws Exception
    {
        if (mainSettings!=null)
        {
            FileSync<Settings> mainS = getFileService().getFileSync(MainInterface.SETTINGS_FILE_CONTEXT);
            mainS.writeToDisk();
            mainSettings.setMainSettings(mainS.getObject());

            if (getIOInterface()!=null)
            {
                getIOInterface().setSettings(mainSettings.getIoSettings());
                getIOInterface().save();
                this.mainSettings.setIoSettings(getIOInterface().getSettings());
            }
        }

        return this.mainSettings;
    }

    @Override
    public MainSettings saveSettings(MainSettings mainSettings)
        throws Exception
    {
        Objects.requireNonNull(mainSettings);
        this.mainSettings = mainSettings;
        return saveSettings();
    }

    @Override
    public void setUIInterface(UIInterface uiInterface)
        throws Exception
    {
        Objects.requireNonNull(uiInterface);
        this.uiInterface = uiInterface;
    }

    @Override
    public UIInterface getUIInterface()
    {
        return this.uiInterface;
    }

    //
    //    @Override
    //    public void exit()
    //        throws Exception
    //    {
    //        Bootstrap.use().exit();
    //    }
    //
    //    @Override
    //    public MainSettings getSettings()
    //    {
    //        return Bootstrap.use().getSettings();
    //    }
    //
    //    @Override
    //    public MainSettings setSettings(MainSettings settings)
    //    {
    //        Bootstrap.use().setSettings(settings);
    //        return Bootstrap.use().getSettings();
    //    }
    //
    //    @Override
    //    public boolean undo()
    //    {
    //        return false;
    //    }
    //
    //    @Override
    //    public boolean redo()
    //    {
    //        return false;
    //    }
    //
    //    @Override
    //    public void setBootstrapUIInterface(BootstrapUIInterface bootstrapUIInterface)
    //    {
    //        Bootstrap.use().setUiInterface(bootstrapUIInterface);
    //    }
    //
    //    @Override
    //    public BootstrapUIInterface getBootstrapUIInterface()
    //    {
    //        return Bootstrap.use().getUiInterface();
    //    }
    //
    //    @Override
    //    public IOInterface _getIOInterface()
    //    {
    //        return Bootstrap.use().getIOInterface();
    //    }
    //
    //    @Override
    //    public void setIOUIInterface(IOUIInterface uiInterface)
    //    {
    //        Bootstrap.use().getIOInterface().setUIInterface(uiInterface);
    //        Bootstrap.use().getUiInterface().setIOUIInterface(uiInterface);
    //    }
    //
    //    @Override
    //    public Set<String> getSearchWords(Collection<UUID> bookmarkIds)
    //        throws Exception
    //    {
    //        return Bootstrap.use().getIOInterface().getSearchWords(bookmarkIds);
    //    }
    //
    //    @Override
    //    public Set<String> getAllSearchWords()
    //        throws Exception
    //    {
    //        return Bootstrap.use().getIOInterface().getAllSearchWords();
    //    }
    //
    //    @Override
    //    public List<AbstractBookmark> applySearchOptions(SearchOptions options)
    //        throws ParseException
    //    {
    //        return Bootstrap.use().getIOInterface().applySearchOptions(options);
    //    }
    //
    //    @Override
    //    public List<AbstractBookmark> applySearchOptions(Collection<AbstractBookmark> bookmarks, SearchOptions options)
    //        throws ParseException
    //    {
    //        return Bootstrap.use().getIOInterface().applySearchOptions(bookmarks, options);
    //    }
    //
    //    @Override
    //    public Set<AbstractBookmark> getAllBookmarks()
    //    {
    //        return Bootstrap.use().getIOInterface().getAllBookmarks();
    //    }
    //
    //    @Override
    //    public Collection<String> getAllBookmarkNames()
    //    {
    //        return Bootstrap.use().getIOInterface().getAllBookmarkNames();
    //    }
    //
    //    @Override
    //    public Set<Class> getAllBookmarkClasses()
    //    {
    //        return Bootstrap.use().getIOInterface().getAllBookmarkClasses();
    //    }
    //
    //    @Override
    //    public AbstractBookmark getBookmark(UUID bookmarkId)
    //    {
    //        return Bootstrap.use().getIOInterface().getBookmark(bookmarkId);
    //    }
    //
    //    @Override
    //    public List<String> getBookmarkNames(Collection<UUID> bookmarkIds)
    //    {
    //        return Bootstrap.use().getIOInterface().getBookmarkNames(bookmarkIds);
    //    }
    //
    //    @Override
    //    public Set<Class> getBookmarkClasses(Collection<UUID> bookmarkIds)
    //    {
    //        return Bootstrap.use().getIOInterface().getBookmarkClasses(bookmarkIds);
    //    }
    //
    //    @Override
    //    public void addBookmark(AbstractBookmark bookmark)
    //        throws Exception
    //    {
    //        Bootstrap.use().getIOInterface().addBookmark(bookmark);
    //    }
    //
    //    @Override
    //    public void addAllBookmarks(Collection<AbstractBookmark> bookmarks)
    //        throws Exception
    //    {
    //        Bootstrap.use().getIOInterface().addAllBookmarks(bookmarks);
    //    }
    //
    //    @Override
    //    public void updateBookmark(AbstractBookmark bookmark)
    //        throws Exception
    //    {
    //        Bootstrap.use().getIOInterface().updateBookmark(bookmark);
    //    }
    //
    //    @Override
    //    public void updateAll(Collection<AbstractBookmark> bookmarks)
    //        throws Exception
    //    {
    //        Bootstrap.use().getIOInterface().updateAll(bookmarks);
    //    }
    //
    //    @Override
    //    public AbstractBookmark deleteBookmark(UUID bookmarkId)
    //    {
    //        return Bootstrap.use().getIOInterface().deleteBookmark(bookmarkId);
    //    }
    //
    //    @Override
    //    public AbstractBookmark deleteBookmark(AbstractBookmark bookmark)
    //    {
    //        return Bootstrap.use().getIOInterface().deleteBookmark(bookmark);
    //    }
    //
    //    @Override
    //    public Set<String> extractTags(List<AbstractBookmark> bookmarks)
    //    {
    //        return Bootstrap.use().getIOInterface().extractTags(bookmarks);
    //    }
    //
    //    @Override
    //    public Set<String> getAllTags()
    //    {
    //        return Bootstrap.use().getIOInterface().getAllTags();
    //    }
    //
    //    @Override
    //    public List<AbstractBookmark> renameTag(String originalTagName, String newTagName)
    //        throws Exception
    //    {
    //        return Bootstrap.use().getIOInterface().renameTag(originalTagName, newTagName);
    //    }
    //
    //    @Override
    //    public List<AbstractBookmark> replaceTags(String replacement, Set<String> tagsToReplace)
    //        throws Exception
    //    {
    //        return Bootstrap.use().getIOInterface().replaceTags(replacement, tagsToReplace);
    //    }
    //
    //    @Override
    //    public Set<AbstractBookmark> deleteTag(String tagToDelete)
    //    {
    //        return Bootstrap.use().getIOInterface().deleteTag(tagToDelete);
    //    }
    //
    //    @Override
    //    public Set<AbstractBookmark> deleteTags(Set<String> tagsToDelete)
    //    {
    //        return Bootstrap.use().getIOInterface().deleteTags(tagsToDelete);
    //    }
    //
    //    @Override
    //    public Set<String> extractTypeNames(Collection<UUID> bookmarkIds)
    //    {
    //        return Bootstrap.use().getIOInterface().extractTypeNames(bookmarkIds);
    //    }
    //
    //    @Override
    //    public Set<String> getAllTypeNames()
    //    {
    //        return Bootstrap.use().getIOInterface().getAllTypeNames();
    //    }
    //
    //    @Override
    //    public List<Class> getActionsPresent()
    //    {
    //        // Sort the actions and return them.
    //        List<ComparableClass> sort = new ArrayList<>();
    //
    //        for (Class clazz: ModuleLoader.use().getClassesFound(AbstractAction.class))
    //        {
    //            sort.add(new ComparableClass(clazz));
    //        }
    //
    //        Collections.sort(sort);
    //
    //        List<Class> res = new ArrayList<>();
    //
    //        for (ComparableClass comparableClass: sort)
    //        {
    //            res.add(comparableClass.getClazz());
    //        }
    //
    //        return res;
    //    }

    // ============================================================
    // Static Methods
    // ============================================================

    public static LocalInstance use()
        throws Exception
    {
        if (localInstance == null)
        {
            localInstance = new LocalInstance();
            localInstance.init();
        }
        return localInstance;
    }

    public static LocalInstance use(Settings settings)
        throws Exception
    {
        if (localInstance == null)
        {
            localInstance = new LocalInstance();
            localInstance.init(settings);
        }
        return localInstance;
    }

    public static LocalInstance use(InitUIInterface initUIInterface)
        throws Exception
    {
        if (localInstance == null)
        {
            localInstance = new LocalInstance();
            localInstance.init(initUIInterface);
        }
        return localInstance;
    }

    public static LocalInstance use(Settings settings, InitUIInterface initUIInterface)
        throws Exception
    {
        if (localInstance == null)
        {
            localInstance = new LocalInstance();
            localInstance.init(settings, initUIInterface);
        }
        return localInstance;
    }
}

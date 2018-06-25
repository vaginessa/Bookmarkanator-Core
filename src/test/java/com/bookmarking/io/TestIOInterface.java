package com.bookmarking.io;

import java.text.*;
import java.util.*;
import com.bookmarking.bookmark.*;
import com.bookmarking.search.*;
import com.bookmarking.settings.*;
import com.bookmarking.stats.*;
import com.bookmarking.ui.*;

public class TestIOInterface implements IOInterface
{
    private SettingsIOInterface settingsIOInterface;
    private IOUIInterface iouiInterface;

    @Override
    public void init(SettingsIOInterface settingsIOInterface)
        throws Exception
    {
        this.settingsIOInterface = settingsIOInterface;
    }

    @Override
    public void init(SettingsIOInterface settingsIOInterface, IOUIInterface iouiInterface)
        throws Exception
    {
        this.settingsIOInterface = settingsIOInterface;
        this.iouiInterface = iouiInterface;
    }

    @Override
    public void save()
        throws Exception
    {
        this.iouiInterface.setStatus("Saving...");
        settingsIOInterface.save();
        this.iouiInterface.setStatus("Done.");
    }

    @Override
    public void exit()
        throws Exception
    {
        this.iouiInterface.setStatus("Closing...");
    }

    @Override
    public IOUIInterface getIOUIInterface()
    {
        return iouiInterface;
    }

    @Override
    public void setIOUIInterface(IOUIInterface uiInterface)
    {
        this.iouiInterface = uiInterface;
    }

    @Override
    public List<AbstractBookmark> applySearchOptions(SearchOptions options)
        throws ParseException
    {
        return null;
    }

    @Override
    public List<AbstractBookmark> applySearchOptions(Collection<AbstractBookmark> bookmarks, SearchOptions options)
        throws ParseException
    {
        return null;
    }

    @Override
    public Set<AbstractBookmark> getAllBookmarks()
    {
        return null;
    }

    @Override
    public AbstractBookmark getBookmark(UUID bookmarkId)
    {
        return null;
    }

    @Override
    public void addBookmark(AbstractBookmark bookmark)
        throws Exception
    {

    }

    @Override
    public void updateBookmark(AbstractBookmark bookmark)
        throws Exception
    {

    }

    @Override
    public AbstractBookmark deleteBookmark(AbstractBookmark bookmark)
    {
        return null;
    }

    @Override
    public Settings getSettings()
    {
        return settingsIOInterface.getSettings();
    }

    @Override
    public void setSettings(Settings settings)
    {
        settingsIOInterface.setSettings(settings);
    }

    @Override
    public void undo()
        throws Exception
    {

    }

    @Override
    public void redo()
        throws Exception
    {

    }

    @Override
    public void clearUndoRedoStack()
        throws Exception
    {

    }

    @Override
    public StatsInterface getStatsInterface()
    {
        return null;
    }
}

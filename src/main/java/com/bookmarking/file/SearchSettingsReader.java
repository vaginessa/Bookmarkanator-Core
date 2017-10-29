package com.bookmarking.file;

import java.io.*;
import javax.xml.bind.*;
import com.bookmarking.fileservice.*;

public class SearchSettingsReader implements FileReaderInterface<SearchGroup>
{
    private SearchGroup searchGroup;

    public SearchSettingsReader()
    {
        searchGroup = new SearchGroup();
    }

    @Override
    public SearchGroup parse(InputStream inputStream)
        throws Exception
    {
        JAXBContext jaxbContext = JAXBContext.newInstance(SearchGroup.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        SearchGroup searchGroup = (SearchGroup) jaxbUnmarshaller.unmarshal(inputStream);
        this.searchGroup = searchGroup;
        return searchGroup;
    }

    @Override
    public void setObject(SearchGroup obj)
    {
        searchGroup = obj;
    }

    @Override
    public SearchGroup getObject()
    {
        return  searchGroup;
    }

    @Override
    public void validate(InputStream inputStream)
        throws Exception
    {
        // Do nothing for now. Parsing is where errors will be intercepted.
    }

    @Override
    public FileSync.InvalidFilePolicy getInvalidFilePolicy()
    {
        return FileSync.InvalidFilePolicy.markBadAndContinue;
    }

    @Override
    public FileSync.MissingFilePolicy getMissingFilePolicy()
    {
        return FileSync.MissingFilePolicy.createNew;
    }

    @Override
    public FileSync.FileBackupPolicy getFileBackupPolicy()
    {
        return FileSync.FileBackupPolicy.NO_BACKUP;
    }
}

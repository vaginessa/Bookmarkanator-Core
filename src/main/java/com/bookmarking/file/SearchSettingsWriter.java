package com.bookmarking.file;

import java.io.*;
import javax.xml.bind.*;
import com.bookmarking.fileservice.*;

public class SearchSettingsWriter implements FileWriterInterface<SearchGroup>
{
    @Override
    public void write(SearchGroup object, OutputStream out)
        throws Exception
    {
        JAXBContext jaxbContext = JAXBContext.newInstance(SearchGroup.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(object, out);
    }

    @Override
    public void writeInitial(OutputStream outputStream)
        throws Exception
    {
        SearchGroup searchGroup = new SearchGroup();

        JAXBContext jaxbContext = JAXBContext.newInstance(SearchGroup.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(searchGroup, outputStream);
    }

    @Override
    public FileSync.FileBackupPolicy getFileBackupPolicy()
    {
        return FileSync.FileBackupPolicy.NO_BACKUP;
    }
}

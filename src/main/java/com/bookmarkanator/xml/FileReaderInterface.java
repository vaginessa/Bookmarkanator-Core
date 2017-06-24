package com.bookmarkanator.xml;

import java.io.*;
import javax.xml.parsers.*;

public interface FileReaderInterface <T>
{

    T parse(InputStream inputStream) throws ParserConfigurationException, Exception;
    void validate(InputStream inputStream) throws Exception;

    FileSync.InvalidFilePolicy getInvalidFilePolicy();
    FileSync.MissingFilePolicy getMissingFilePolicy();
    FileSync.FileBackupPolicy getFileBackupPolicy();
}

package com.bookmarkanator.xml;

import java.io.*;

public interface FileReaderInterface <T>
{

    T parse(InputStream inputStream) throws Exception;
    void setObject(T obj);
    void validate(InputStream inputStream) throws Exception;

    FileSync.InvalidFilePolicy getInvalidFilePolicy();
    FileSync.MissingFilePolicy getMissingFilePolicy();
    FileSync.FileBackupPolicy getFileBackupPolicy();
}

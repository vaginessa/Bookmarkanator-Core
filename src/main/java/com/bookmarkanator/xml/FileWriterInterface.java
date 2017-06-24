package com.bookmarkanator.xml;

import java.io.*;

public interface FileWriterInterface <T>
{
    void write(T object, OutputStream out) throws Exception;
    void writeInitial(OutputStream outputStream) throws Exception;

    FileSync.FileBackupPolicy getFileBackupPolicy();
}

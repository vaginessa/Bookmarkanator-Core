package com.bookmarkanator.xml;

import com.bookmarkanator.core.Settings;

import java.io.OutputStream;

public interface FileWriterInterface <T>
{
    enum FileBackupPolicy
    {
        createBackupOnWrite,
        noBackup
    }

    void write(T object, OutputStream out) throws Exception;
    void writeInitial(OutputStream outputStream) throws Exception;

    FileBackupPolicy getFileBackupPolicy();
}

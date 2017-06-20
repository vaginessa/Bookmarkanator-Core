package com.bookmarkanator.xml;

import java.io.*;
import java.util.*;

public interface FileReaderInterface <T>
{
    enum InvalidFilePolicy
    {
        markBadAndContinue,
        thowError,
    }

    enum InvalidFilePolicy
    {
        markBadAndContinue,
        thowError,
    }

    enum FileBackupPolicy
    {
        createBackupOnRead,
        noBackup,
        createBackupOnWrite
    }

    void parse(InputStream inputStream);
    void validate(InputStream inputStream);

    T getObject();

    InvalidFilePolicy getInvalidFilePolicy();
    FileBackupPolicy getFileBackupPolicy();

}

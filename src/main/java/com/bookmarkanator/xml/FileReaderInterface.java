package com.bookmarkanator.xml;

import java.io.*;
import java.util.*;

public interface FileReaderInterface
{
    enum InvalidFilePolicy
    {
        markBadAndContinue,
        thowError,
    }

    enum FileBackupPolicy
    {
        createBackupOnRead,
        noBackup
    }

    void parse();
    void validate(InputStream inputStream);
    InvalidFilePolicy getInvalidFilePolicy();
    FileBackupPolicy getFileBackupPolicy();
    Set<File> getFileLocations();

}

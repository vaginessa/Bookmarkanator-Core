package com.bookmarkanator.xml;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

public interface FileReaderInterface <T>
{
    enum InvalidFilePolicy
    {
        markBadAndContinue,
        thowError,
    }

    enum MissingFilePolicy
    {
        createNew,
        thowError,
    }

    enum FileBackupPolicy
    {
        createBackupOnRead,
        noBackup
    }

    T parse(InputStream inputStream) throws ParserConfigurationException, Exception;
    void validate(InputStream inputStream) throws Exception;


    InvalidFilePolicy getInvalidFilePolicy();
    MissingFilePolicy getMissingFilePolicy();
    FileBackupPolicy getFileBackupPolicy();
}

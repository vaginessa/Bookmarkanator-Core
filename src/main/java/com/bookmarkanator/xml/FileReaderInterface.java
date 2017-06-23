package com.bookmarkanator.xml;

import java.io.*;
import javax.xml.parsers.*;

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
        singleBackupOnRead,
        rollingBackupOnRead,
        noBackup
    }

    T parse(InputStream inputStream) throws ParserConfigurationException, Exception;
    void validate(InputStream inputStream) throws Exception;


    InvalidFilePolicy getInvalidFilePolicy();
    MissingFilePolicy getMissingFilePolicy();
    FileBackupPolicy getFileBackupPolicy();
}

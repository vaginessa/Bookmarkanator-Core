package com.bookmarking.settings;

import java.io.*;
import com.bookmarking.structure.*;

public class FileSetting extends AbstractSetting<File>
{
    public FileSetting()
    {
    }

    public FileSetting(String key)
    {
        super(key);
    }

    public FileSetting(String group, String key, File value)
    {
        super(group, key, value);
    }
}

package com.bookmarking.settings.types;

import java.net.*;

public class URLSetting extends AbstractSetting<URL>
{
    public URLSetting()
    {
    }

    public URLSetting(String key)
    {
        super(key);
    }

    public URLSetting(String group, String key, URL value)
    {
        super(group, key, value);
    }
}

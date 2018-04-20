package com.bookmarking;

import java.io.*;
import com.bookmarking.update.config.*;
import org.junit.*;

public class TestUpdateConfig
{
//    @Ignore
    @Test
    public void testWritingAndParsing()
        throws Exception
    {
        UpdateConfigEntry config = new UpdateConfigEntry();

        config.setResourceKey("desktop");
        config.setCurrentVersion("0.0.0-2");
        config.setCurrentResource(new File(".").toURI().toURL());

        String s = config.toXML();
        System.out.println(s);

        UpdateConfigEntry config2 = UpdateConfigEntry.parse(s);

        Assert.assertTrue(config.equals(config2));
        Assert.assertTrue(config2.equals(config));
    }
}

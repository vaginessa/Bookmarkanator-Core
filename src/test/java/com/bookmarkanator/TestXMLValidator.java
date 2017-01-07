package com.bookmarkanator;

import java.io.*;
import com.bookmarkanator.xml.*;
import org.junit.*;

public class TestXMLValidator
{
    @Test
    public void testBookmarksXMLValidator()
        throws Exception
    {

        InputStream xsd = this.getClass().getResourceAsStream("/com.bookmarkanator.xml/BookmarksStructure.xsd");
        InputStream xml = this.getClass().getResourceAsStream("/com.bookmarkanator.xml/BookmarksXMLTemplate.xml");

        XMLValidator.validate(xml, xsd);
        System.out.println();
    }

    @Test
    public void testSettingsXMLValidator()
        throws Exception
    {

        InputStream xsd = this.getClass().getResourceAsStream("/com.bookmarkanator.xml/SettingsStructure.xsd");
        InputStream xml = this.getClass().getResourceAsStream("/com.bookmarkanator.xml/SettingsXMLTemplate.xml");

        XMLValidator.validate(xml, xsd);
        System.out.println();
    }
}

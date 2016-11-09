package com.bookmarkanator;

import java.io.*;
import com.bookmarkanator.xml.*;
import org.junit.*;

public class TestXMLValidator
{
    @Test
    public void testXMLValidator()
        throws Exception
    {

        InputStream xsd = this.getClass().getResourceAsStream("/com.bookmarkanator.xml/BookmarksStructure.xsd");
//        String s = IOUtils.toString(xsd, Charset.defaultCharset());
//        System.out.println(s);
        InputStream xml = this.getClass().getResourceAsStream("/com.bookmarkanator.xml/BookmarksXMLTemplate.xml");

        XMLValidator.validate(xml, xsd);
        System.out.println();
    }
}

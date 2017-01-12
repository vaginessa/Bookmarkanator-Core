package com.bookmarkanator;

import java.io.*;
import java.util.*;
import com.bookmarkanator.io.*;
import com.bookmarkanator.util.*;
import com.bookmarkanator.xml.*;
import org.junit.*;

public class TestParsing
{
    @Test
    public void testParsingBookmarks()
        throws Exception
    {
        FileIO fileIO = new FileIO();
        fileIO.init("/Users/lloyd1/Projects/Bookmark-anator/src/main/resources/com.bookmarkanator.xml/BookmarksXMLTemplate.xml");
        ContextInterface fileContext = fileIO.getContext();
//        fileContext.getBookmark(UUID.fromString("94219EF5-6C6E-4A55-8DA7-5107ED80D0A9")).action(fileContext);

        //TODO Fix problem where if the program exits before fileIO.save() is called then all the file contents are removed.


        fileIO.save();
        System.out.println();
        List<String> exclusions = new ArrayList<>();
        exclusions.add("A");
        exclusions.add("B");

        List res = Filter.use(fileContext.getBookmarks()).excludeNamesWithText(exclusions).results();
        System.out.println();
    }

    @Test
    public void testParsingSettings()
        throws Exception
    {
        String config = "/Users/lloyd1/Projects/Bookmark-anator/src/main/resources/com.bookmarkanator.xml/SettingsXMLTemplate.xml";
        FileInputStream fin = new FileInputStream(new File(config));
        validateXML(fin);
        fin = new FileInputStream(new File(config));

        SettingsXMLParser parser = new SettingsXMLParser(fin);
        Map<String, List<String>> settings = parser.parse();

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        SettingsXMLWriter writer = new SettingsXMLWriter(settings,bout );
        writer.write();

        String s = bout.toString();

        System.out.println();
    }

    private void validateXML(InputStream inputStream)
        throws Exception
    {
        InputStream xsd = this.getClass().getResourceAsStream("/com.bookmarkanator.xml/SettingsStructure.xsd");
        XMLValidator.validate(inputStream, xsd);
    }
}

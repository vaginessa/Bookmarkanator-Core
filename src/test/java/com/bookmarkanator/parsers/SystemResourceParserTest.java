package com.bookmarkanator.parsers;

import java.io.*;
import java.util.*;
import com.bookmarkanator.resourcetypes.*;
import com.bookmarkanator.settings.*;
import com.bookmarkanator.writers.Writer;
import org.junit.*;

public class SystemResourceParserTest
{
    private File file;
    @Before
    public void setUp()
        throws Exception
    {
        file = new File("");
    }

    @After
    public void tearDown()
        throws Exception
    {
        file.delete();
    }

    @Test
    public void testParseMethod()
        throws Exception
    {
        File b = new File("");
        System.out.println(b.getCanonicalPath());
        File f = new File("/media/micah/Backup/Programming/Java/myprojects/Bookmark-anator/Bookmark-anator/system_resource_settings_test.xml");
        Writer sw = new Writer();
        Settings s1 = generateSystemTypes();
        sw.writeSettings(s1, f);


//
//        SystemResourceParser p = new SystemResourceParser();
//
//
//
//
//        try
//        {
//            p.parse(f);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
    }

    private Settings generateSystemTypes()
    {
        Settings settings = new Settings();

        List<SystemType> systems = new ArrayList<SystemType>();
        SystemType sysType = new SystemType();

        BasicResource basic = new BasicResource();
        basic.setName("Basic resource name");
        basic.setText("Show this basic text!");

        DefaultSystemResource defSysWeb = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
        defSysWeb.setName("Yahoo");
        defSysWeb.setText("http://www.yahoo.com");

        DefaultSystemResource defSysEdit = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_EDITOR);
        defSysWeb.setName("Applications");
        defSysWeb.setText("/Users/lloyd1/applications.txt");

        DefaultSystemResource defSysFile = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_BROWSER);
        defSysWeb.setName("Home");
        defSysWeb.setText("~");

        TerminalResource tr = new TerminalResource();
        tr.setName("OpenOffice");
        tr.setText("-writer");
        tr.setPreCommand("xterm soffice");
        tr.setPostCommand("hello.odt");

        FileFilterResource ff = new FileFilterResource();
        ff.setName("CSV filter");
        ff.setText("Filters a file based on comma separated values.");
        ff.setCommentIdentifier("#");
        ff.setEscapeString("%");
        ff.setKeyValuePairSeparator(",");
        ff.setKeyValueSeparator("=");

        CustomFileFilter cf = new CustomFileFilter();
        cf.setName("Bob file filter 1");
        cf.setText("Text describing the custom file filter");


        sysType.getResourceList().add(basic);
        sysType.getResourceList().add(defSysEdit);
        sysType.getResourceList().add(defSysFile);
        sysType.getResourceList().add(defSysWeb);
        sysType.getResourceList().add(tr);
        sysType.getResourceList().add(ff);
        sysType.getResourceList().add(cf);

        systems.add(sysType);

        settings.setSystemTypes(systems);
        return settings;
    }
}
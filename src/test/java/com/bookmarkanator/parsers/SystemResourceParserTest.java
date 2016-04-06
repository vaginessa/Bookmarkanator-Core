package com.bookmarkanator.parsers;

import java.io.*;
import java.util.*;
import com.bookmarkanator.resourcetypes.*;
import com.bookmarkanator.settings.*;
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
    {
        SystemResourceParser p = new SystemResourceParser();

        File f = new File("/Users/lloyd1/Projects/Bookmark-anator/src/main/system_resource_settings.xml");
        try
        {
            p.parse(f);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private List<SystemType> generateSystemTypes()
    {
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

        DefaultSystemResource defSysFile = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_EDITOR);
        defSysWeb.setName("Home");
        defSysWeb.setText("~");

        sysType.getResourceList().add(basic);
        sysType.getResourceList().add(defSysEdit);
        sysType.getResourceList().add(defSysFile);
        sysType.getResourceList().add(defSysWeb);

        systems.add(sysType);

        return systems;
    }
}
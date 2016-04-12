package com.bookmarkanator.parsers;

import java.io.*;
import java.util.*;

import com.bookmarkanator.customClass.*;
import com.bookmarkanator.resourcetypes.*;
import com.bookmarkanator.settings.*;
import com.bookmarkanator.writers.Writer;
import org.junit.*;

public class SystemResourceParserTest {
    private File file;

    @Before
    public void setUp()
            throws Exception {
        file = new File("src/test/java/com/bookmarkanator/parsers/system_resource_settings_test.xml");
    }

    @After
    public void tearDown()
            throws Exception {
//        file.delete();
    }

    @Test
    public void testParseMethod()
            throws Exception {
        System.out.println("Enter testParseMethod in SystemResourceParserTest");
        Writer sw = new Writer();
        Settings s1 = generateSystemTypes();
        sw.writeSettings(s1, file);

        SystemResourceParser p = new SystemResourceParser();

        try {
            Settings parsedS = p.parse(file);
            Assert.assertTrue(s1.equals(parsedS));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Exit testParseMethod in SystemResourceParserTest");
    }

    private Settings generateSystemTypes() {
        Settings settings = new Settings();
        settings.setVersion("1.0");


        List<SystemType> systems = new ArrayList<SystemType>();
        SystemType sysType = new SystemType();
        sysType.setSystemName("OSX");
        sysType.setSystemVersion("6.9");
        sysType.setSystemVersionName("Bob");

        QuickAccessTagGroup q = new QuickAccessTagGroup();
        q.addTag("hello");
        q.addTag("bye");
        q.addTag("your mom");
        q.setIndex(1);
        q.setLabel("My favorite tag group");

        sysType.addQuickAccessTagGroup(q);

        BasicResource basic = new BasicResource();
        basic.setName("Basic resource name");
        basic.setText("Show this basic text!");

        DefaultSystemResource defSysWeb = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
        defSysWeb.setName("Yahoo");
        defSysWeb.setText("http://www.yahoo.com");

        DefaultSystemResource defSysEdit = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_EDITOR);
        defSysEdit.setName("Applications");
        defSysEdit.setText("/Users/lloyd1/applications.txt");
        defSysEdit.setPreCommand("nano ");

        DefaultSystemResource defSysFile = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_BROWSER);
        defSysFile.setName("Home");
        defSysFile.setText("~");

        TerminalResource tr = new TerminalResource();
        tr.setName("OpenOffice");
        tr.setText("-writer");
        tr.setPreCommand("xterm soffice");
        tr.setPostCommand("hello.odt");

        CustomFileFilter cf = new CustomFileFilter();
        cf.setName("Bob file filter 1");
        cf.setText("Text describing the custom file filter");
        List<CustomClassParameter> params = new ArrayList<>();

        CustomClassParameter p = new CustomClassParameter();
        p.setKey("key-value-separator");
        p.setDescription("defines the separator between key value pairs");
        p.setOverridden(true);
        p.setRequired(true);
        p.setValue("=");

        params.add(p);

        p = new CustomClassParameter();
        p.setKey("key-value-pair-separator");
        p.setDescription("defines the separator of the key value pairs");
        p.setOverridden(true);
        p.setRequired(true);
        p.setValue(",");

        params.add(p);

        p = new CustomClassParameter();
        p.setKey("optional param 1");
        p.setDescription("defines optional param 1");
        p.setOverridden(false);
        p.setRequired(true);
        p.setValue("option!");

        params.add(p);

        cf.setParameters(params);

        sysType.getResourceList().add(basic);
        sysType.getResourceList().add(defSysEdit);
        sysType.getResourceList().add(defSysFile);
        sysType.getResourceList().add(defSysWeb);
        sysType.getResourceList().add(tr);
        sysType.getResourceList().add(cf);


        systems.add(sysType);

        sysType = new SystemType();
        sysType.setSystemName("Linux mint");
        sysType.setSystemVersion("17.3");
        sysType.setSystemVersionName("Rosa");

        sysType.getResourceList().add(basic);
        sysType.getResourceList().add(defSysEdit);
        sysType.getResourceList().add(defSysFile);
        sysType.getResourceList().add(defSysWeb);
        sysType.getResourceList().add(tr);
        sysType.getResourceList().add(cf);

        systems.add(sysType);
        settings.setSystemTypes(systems);
        return settings;
    }
}
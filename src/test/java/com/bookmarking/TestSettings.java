package com.bookmarking;

import java.io.*;
import java.util.*;
import com.bookmarking.settings.*;
import com.bookmarking.xml.*;
import org.junit.*;

public class TestSettings
{

    @Test
    public void testParsing()
        throws Exception
    {
        Settings settings = genSettings();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        SettingsXMLWriter2 writer = new SettingsXMLWriter2();
        writer.write(settings, bout);
        System.out.println(bout.toString());
        bout.flush();
        bout.close();

        InputStream xsd = this.getClass().getResourceAsStream("/com.bookmarkanator.xml/SettingsStructure.xsd");
        InputStream xml = new ByteArrayInputStream(bout.toByteArray());

        XMLValidator.validate(xml, xsd);

        SettingsXMLParser2 parser = new SettingsXMLParser2();
        ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
        Settings settings2 = parser.parse(bin);
        bin.close();

        bout = new ByteArrayOutputStream();
        writer = new SettingsXMLWriter2();
        writer.write(settings2, bout);
        System.out.println(bout.toString());
        bout.flush();
        bout.close();

        Map<String, SettingsGroup> settingsGroupMap = settings.getGroups();

        for (String s: settingsGroupMap.keySet())
        {
            SettingsGroup settingsGroup = settingsGroupMap.get(s);
            Assert.assertNotNull(settingsGroup);

            SettingsGroup settingsGroup2 = settings2.getGroups().get(s);
            Assert.assertNotNull(settingsGroup2);

            for (String ss: settingsGroup.getSettings().keySet())
            {
                AbstractSetting abstractSetting = settingsGroup.getSettings().get(ss);
                Assert.assertNotNull(abstractSetting);

                AbstractSetting abstractSetting2 = settingsGroup2.getSettings().get(ss);
                Assert.assertNotNull(abstractSetting2);

                Assert.assertEquals(abstractSetting.getGroup(), abstractSetting2.getGroup());
                Assert.assertEquals(abstractSetting.getKey(), abstractSetting2.getKey());
                Assert.assertEquals(abstractSetting.getValue(), abstractSetting2.getValue());
            }
        }
    }

    private Settings genSettings()
        throws Exception
    {
        Settings settings = new Settings();

        FileSetting fileSetting = new FileSetting("A", "AA", new File("File!"));
        settings.putSetting(fileSetting);

        fileSetting = new FileSetting("A", "AAA", new File("Another file!"));
        settings.putSetting(fileSetting);

        BooleanSetting booleanSetting = new BooleanSetting("A", "BB", true);
        settings.putSetting(booleanSetting);

        booleanSetting = new BooleanSetting("A", "BBB", false);
        settings.putSetting(booleanSetting);

        ClassSetting classSetting = new ClassSetting("A", "CC", String.class);
        settings.putSetting(classSetting);

        classSetting = new ClassSetting("A", "CCC", File.class);
        settings.putSetting(classSetting);

        FloatSetting floatSetting = new FloatSetting("A", "DD", 25.678f);
        settings.putSetting(floatSetting);

        floatSetting = new FloatSetting("A", "DDD", 55555.9087f);
        settings.putSetting(floatSetting);

        IntegerSetting integerSetting = new IntegerSetting("A", "EE", 75);
        settings.putSetting(integerSetting);

        integerSetting = new IntegerSetting("A", "EEE", 750);
        settings.putSetting(integerSetting);

        StringSetting stringSetting = new StringSetting("A", "FF", "Hello World");
        settings.putSetting(stringSetting);

        stringSetting = new StringSetting("A", "FFF", "Hello World Again");
        settings.putSetting(stringSetting);

        fileSetting = new FileSetting("B", "AA", null);
        settings.putSetting(fileSetting);

        return settings;
    }
}
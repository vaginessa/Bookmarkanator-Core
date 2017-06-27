package com.bookmarkanator.bookmarks;

import com.fasterxml.jackson.xml.XmlMapper;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EncryptedBookmarkTest {

    @Test
    public void encryptionDecryptionTest() throws Exception {
        String testString = "You can't handle the truth";
        EncryptedBookmark encryptedBookmark = new EncryptedBookmark("Test Encryption Key");
        encryptedBookmark.setContent(testString);

        encryptedBookmark.runAction("Encrypt");
        assertNotEquals(testString, encryptedBookmark.getContent());
        System.out.println(encryptedBookmark.getContent());

        encryptedBookmark.runAction("Decrypt");
        assertEquals(testString, encryptedBookmark.getContent());
        System.out.println(encryptedBookmark.getContent());
    }

    @Test
    public void serializationTest() throws Exception {
        String testString = "You can't handle the truth";
        EncryptedBookmark encryptedBookmark = new EncryptedBookmark("Test Encryption Key");
        encryptedBookmark.setContent(testString);

        XmlMapper mapper = new XmlMapper();

        FileOutputStream outputStream = new FileOutputStream(new File("test.xml"));
        mapper.writeValue(outputStream, encryptedBookmark);

        outputStream.close();

    }

    @Test
    public void deserializationTest() throws Exception {
        XmlMapper mapper = new XmlMapper();
        String xml = inputStreamToString(new FileInputStream(new File("test.xml")));
        EncryptedBookmark encryptedBookmark = mapper.readValue(xml, EncryptedBookmark.class);

        assertEquals(encryptedBookmark.getContent(), "You can't handle the truth");
    }

    private static String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

}

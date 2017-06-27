package com.bookmarkanator.bookmarks;

import org.junit.Test;

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
}

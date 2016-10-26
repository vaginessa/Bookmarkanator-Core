package com.bookmarkanator.bookmarks;

import com.bookmarkanator.core.*;

public class EncryptedBookmark extends AbstractBookmark <String>{
    private String passwordHash;
    private long valid;//stores how long after entering this password, until the bookmark text is encrypted again.

    /**
     * Add field to the context if this bookmark is unencrypted so that the user doesn't have to unencrypt it each time.
     */

    @Override
    public String getTypeName() {
        return "encrypted";
    }

    @Override
    public String action(Context context) {
        //check if the password has been entered before, and if so return the unencrypted string.
        return null;
    }
}

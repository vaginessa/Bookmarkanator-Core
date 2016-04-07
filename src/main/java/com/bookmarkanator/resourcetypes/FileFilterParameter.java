package com.bookmarkanator.resourcetypes;

import javax.swing.*;

/**
 * Created by micah on 4/6/16.
 */
public class FileFilterParameter {
    private String key;
    private String description;
    private String value;
    private InputVerifier inputVerifier;
    private boolean required;//required parameters must be supplied before the class can be run.

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public InputVerifier getInputVerifier() {
        return inputVerifier;
    }

    public void setInputVerifier(InputVerifier inputVerifier) {
        this.inputVerifier = inputVerifier;
    }
}

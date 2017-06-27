package com.bookmarkanator.bookmarks;

import java.security.*;
import java.security.spec.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class EncryptedBookmark extends AbstractBookmark
{

    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private String secretKey;
    private String content;
    private byte[] contentAsBytes;
    private byte[] salt;
    private byte[] iv;
    private String encryptionKey;
    private boolean encrypted;
    private SecretKey secret;

    //Needed for serialization
    EncryptedBookmark()
    {
        supportedActions.add("Encrypt");
        supportedActions.add("Decrypt");
    }

    public EncryptedBookmark(String encryptionKey)
    {
        supportedActions.add("Encrypt");
        supportedActions.add("Decrypt");
        this.encryptionKey = encryptionKey;
        this.salt = SecureRandom.getSeed(8);

        try
        {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(encryptionKey.toCharArray(), salt, 65536, 128);
            SecretKey tmp = factory.generateSecret(spec);
            secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean setSecretKey(final String secretKey)
    {
        if (this.secretKey == null && secretKey != null)
        {
            this.secretKey = secretKey;
            return true;
        }
        return false;
    }

    @Override
    public void notifyBeforeAction(Object source, String actionString)
    {

    }

    @Override
    public void notifyAfterAction(Object source, String actionString)
    {

    }

    @Override
    protected String runAction(final String action)
        throws Exception
    {
        switch (action)
        {
            case "Encrypt":
                encryptText();
                break;
            case "Decrypt":
                decryptText();
                break;
            default:
                return "Invalid Action.  Must be either Encrypt or Decrypt";
        }
        return "Done";
    }

    @Override
    public String getTypeName()
    {
        return "Encrypted";
    }

    @Override
    public List<String> getTypeLocation()
    {
        return null;
    }

    @Override
    public void destroy()
        throws Exception
    {
        // Do nothing
    }

    @Override
    public AbstractBookmark getNew()
    {
        return null;
    }

    @Override
    public String getContent()
        throws Exception
    {
        return content;
    }

    @Override
    public void setContent(final String content)
        throws Exception
    {
        this.content = content;
    }

    @Override
    public Set<String> getSearchWords()
        throws Exception
    {
        Set<String> strings = new HashSet<>();
        if (!encrypted)
        {
            String content = getContent();
            if (content != null)
            {
                for (String s : getContent().split("[\\s\\\\\"'\\./-]"))
                {
                    strings.add(s);
                }
            }
        }

        return strings;
    }

    @Override
    public void systemInit()
    {
        // Do nothing
    }

    @Override
    public void systemShuttingDown()
    {
        if (!encrypted)
        {
            encryptText();
        }
    }

    @Override
    public HandleData canHandle(final String content)
    {
        return null;
    }

    @Override
    public int compareTo(final AbstractBookmark o)
    {
        return 0;
    }

    private void encryptText()
    {
        try
        {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = cipher.getParameters();
            iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            contentAsBytes = cipher.doFinal(content.getBytes("UTF-8"));
            content = new String(contentAsBytes);
            encrypted = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void decryptText()
    {
        try
        {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
            content = new String(cipher.doFinal(contentAsBytes), "UTF-8");
            encrypted = false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Getters and setters for serialization

    // ** There should be no public access to the secret key **
    //    public String getSecretKey() {
    //        return secretKey;
    //    }

    public byte[] getContentAsBytes()
    {
        return contentAsBytes;
    }

    public void setContentAsBytes(final byte[] contentAsBytes)
    {
        this.contentAsBytes = contentAsBytes;
    }

    public byte[] getSalt()
    {
        return salt;
    }

    public void setSalt(final byte[] salt)
    {
        this.salt = salt;
    }

    public byte[] getIv()
    {
        return iv;
    }

    public void setIv(final byte[] iv)
    {
        this.iv = iv;
    }

    public String getEncryptionKey()
    {
        return encryptionKey;
    }

    public void setEncryptionKey(final String encryptionKey)
    {
        this.encryptionKey = encryptionKey;
    }

    public boolean isEncrypted()
    {
        return encrypted;
    }

    public void setEncrypted(final boolean encrypted)
    {
        this.encrypted = encrypted;
    }

    public SecretKey getSecret()
    {
        return secret;
    }

    public void setSecret(final SecretKey secret)
    {
        this.secret = secret;
    }
}

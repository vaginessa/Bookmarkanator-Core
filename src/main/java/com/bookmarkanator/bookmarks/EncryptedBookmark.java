package com.bookmarkanator.bookmarks;

import com.bookmarkanator.io.BKIOInterface;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
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
    public EncryptedBookmark()
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
        if (source instanceof BKIOInterface && actionString.equals(BKIOInterface.Actions.SAVING) && !encrypted)
        {
            this.encryptText();
        }
    }

    @Override
    public void notifyAfterAction(Object source, String actionString)
    {
        if (source instanceof BKIOInterface && actionString.equals(BKIOInterface.Actions.COMPLETE) && encrypted)
        {
            this.decryptText();
        }
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
}

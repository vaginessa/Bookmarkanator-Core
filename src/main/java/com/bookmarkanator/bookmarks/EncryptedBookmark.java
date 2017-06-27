package com.bookmarkanator.bookmarks;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.List;
import java.util.Set;

public class EncryptedBookmark extends AbstractBookmark {

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
    EncryptedBookmark() {
        supportedActions.add("Encrypt");
        supportedActions.add("Decrypt");
    }

    public EncryptedBookmark(String encryptionKey) {
        supportedActions.add("Encrypt");
        supportedActions.add("Decrypt");
        this.encryptionKey = encryptionKey;
        this.salt = SecureRandom.getSeed(8);

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(encryptionKey.toCharArray(), salt, 65536, 128);
            SecretKey tmp = factory.generateSecret(spec);
            secret = new SecretKeySpec(tmp.getEncoded(), "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean setSecretKey(final String secretKey) {
        if (this.secretKey == null && secretKey != null) {
            this.secretKey = secretKey;
            return true;
        }
        return false;
    }

    @Override
    public void notifyBeforeAction(final AbstractBookmark source, final String actionString) {

    }

    @Override
    public void notifyAfterAction(final AbstractBookmark source, final String actionString) {

    }

    @Override
    protected String runAction(final String action) throws Exception {
        switch (action) {
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
    public String getTypeName() {
        return "Encrypted";
    }

    @Override
    public List<String> getTypeLocation() {
        return null;
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public AbstractBookmark getNew() {
        return null;
    }

    @Override
    public String getContent() throws Exception {
        return content;
    }

    @Override
    public void setContent(final String content) throws Exception {
        this.content = content;
    }

    @Override
    public Set<String> getSearchWords() throws Exception {
        return null;
    }

    @Override
    public void systemInit() {

    }

    @Override
    public void systemShuttingDown() {

    }

    @Override
    public HandleData canHandle(final String content) {
        return null;
    }

    @Override
    public int compareTo(final AbstractBookmark o) {
        return 0;
    }

    private void encryptText() {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = cipher.getParameters();
            iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            contentAsBytes = cipher.doFinal(content.getBytes("UTF-8"));
            content = new String(contentAsBytes);
            encrypted = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decryptText() {
        try  {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
            content = new String(cipher.doFinal(contentAsBytes), "UTF-8");
            encrypted = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getters and setters for serialization

    public String getSecretKey() {
        return secretKey;
    }

    public byte[] getContentAsBytes() {
        return contentAsBytes;
    }

    public void setContentAsBytes(final byte[] contentAsBytes) {
        this.contentAsBytes = contentAsBytes;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(final byte[] salt) {
        this.salt = salt;
    }

    public byte[] getIv() {
        return iv;
    }

    public void setIv(final byte[] iv) {
        this.iv = iv;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(final String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(final boolean encrypted) {
        this.encrypted = encrypted;
    }

    public SecretKey getSecret() {
        return secret;
    }

    public void setSecret(final SecretKey secret) {
        this.secret = secret;
    }
}

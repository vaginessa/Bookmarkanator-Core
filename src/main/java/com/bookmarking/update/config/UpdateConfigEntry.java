package com.bookmarking.update.config;

import java.io.*;
import java.net.*;
import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
import com.bookmarking.util.*;

/**
 * An UpdateConfigEntry represents a single item that needs to be checked for updates.
 */
@XmlRootElement
public class UpdateConfigEntry
{
    private String currentVersion;
    private URL currentResource;
    private String resourceKey;

    public UpdateConfigEntry()
    {
    }

    public UpdateConfigEntry(UpdateConfigEntry updateConfigEntry)
    {
        this.setCurrentResource(updateConfigEntry.getCurrentResource());
        this.setCurrentVersion(updateConfigEntry.getCurrentVersion());
        this.setResourceKey(updateConfigEntry.getResourceKey());
    }

    public String getCurrentVersion()
    {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion)
    {
        this.currentVersion = currentVersion;
    }

    public URL getCurrentResource()
    {
        return currentResource;
    }

    public void setCurrentResource(URL currentResource)
    {
        this.currentResource = currentResource;
    }

    public String getResourceKey()
    {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey)
    {
        this.resourceKey = resourceKey;
    }

    public String toXML()
        throws Exception
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        JAXBContext jaxbContext = JAXBContext.newInstance(UpdateConfigEntry.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.marshal(this, bout);
        String s = new String(bout.toByteArray());
        bout.flush();
        bout.close();
        return s;
    }

    public static UpdateConfigEntry parse(String xmlIn)
        throws Exception
    {
        ByteArrayInputStream bin = new ByteArrayInputStream(xmlIn.getBytes());
        UpdateConfigEntry res = Util.convertStreamToObject(UpdateConfigEntry.class, bin);
        bin.close();
        return res;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UpdateConfigEntry that = (UpdateConfigEntry) o;

        if (getCurrentVersion() != null ? !getCurrentVersion().equals(that.getCurrentVersion()) : that.getCurrentVersion() != null)
            return false;
        if (getCurrentResource() != null ? !getCurrentResource().equals(that.getCurrentResource()) : that.getCurrentResource() != null)
            return false;
        return getResourceKey() != null ? getResourceKey().equals(that.getResourceKey()) : that.getResourceKey() == null;
    }

    @Override
    public int hashCode()
    {
        int result = getCurrentVersion() != null ? getCurrentVersion().hashCode() : 0;
        result = 31 * result + (getCurrentResource() != null ? getCurrentResource().hashCode() : 0);
        result = 31 * result + (getResourceKey() != null ? getResourceKey().hashCode() : 0);
        return result;
    }
}

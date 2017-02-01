package com.bookmarkanator.xml;

import java.io.*;
import javax.xml.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;

public class XMLValidator
{
    public static void validate(InputStream xmlIn, InputStream xsdIn)
        throws Exception
    {
        SchemaFactory factory =
            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(xsdIn));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(xmlIn));
    }

    public static void validate(File xmlFile, File xsdFile)
        throws Exception
    {
        validate(new FileInputStream(xmlFile), new FileInputStream(xsdFile));
    }
}

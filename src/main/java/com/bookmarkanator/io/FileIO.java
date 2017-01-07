package com.bookmarkanator.io;

import java.io.*;
import java.util.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.xml.*;
import org.apache.commons.io.*;

public class FileIO implements BKIOInterface
{
    //Note: Default file locations are called from the users home directory.
    public static final String defaultBookmarksFileName = "bookmarks.xml";
    public static final String defaultBookmarksDirectory = "Bookmark-anator";
    public static final String defaultBookmarkAddonsDirectory = "Bookmark-anator"+File.separatorChar+"addons";
    private FileContext context;

    @Override
    public void init()
        throws Exception
    {
        FileInputStream fin = new FileInputStream(getDefaultBookmarksFile());
        validateXML(fin);
        fin = new FileInputStream(
            getDefaultBookmarksFile());//need to open the file stream again because for some reason the validator closes the stream when it validates the xml.

        loadStandardBookmarks(fin);
        fin.close();

//        loadModules(getDefaultBookmarksFile());
    }

    @Override
    public void init(String config)
        throws Exception
    {
        FileInputStream fin = new FileInputStream(new File(config));
        validateXML(fin);
        fin = new FileInputStream(new File(config));

        loadStandardBookmarks(fin);
        fin.close();

//        loadModules(config);
    }

    @Override
    public void save()
        throws Exception
    {
        FileOutputStream fout = new FileOutputStream(getDefaultBookmarksFile());
        BookmarksXMLWriter writer = new BookmarksXMLWriter(context, fout);
        writer.write();
        fout.flush();
        fout.close();
    }

    @Override
    public void save(String config)
        throws Exception
    {
        FileOutputStream fout = new FileOutputStream(new File(config));
        BookmarksXMLWriter writer = new BookmarksXMLWriter(context, fout);
        writer.write();
        fout.flush();
        fout.close();
    }

    @Override
    public void close()
    {
        //close any open file sources.
    }

    private void loadStandardBookmarks(InputStream inputStream)
        throws Exception
    {
        context = new FileContext();
        context.setBKIOInterface(this);
        BookmarksXMLParser parser = new BookmarksXMLParser(context, inputStream);
        parser.parse();
    }

    private void validateXML(InputStream inputStream)
        throws Exception
    {
        InputStream xsd = this.getClass().getResourceAsStream("/com.bookmarkanator.xml/BookmarksStructure.xsd");
        XMLValidator.validate(inputStream, xsd);
    }

    @Override
    public ContextInterface getContext()
    {
        if (this.context == null)
        {
            this.context = new FileContext();
            this.context.setBKIOInterface(this);
        }

        return context;
    }

    private File getDefaultBookmarksFile()
        throws IOException
    {
        String usersHome = System.getProperty("user.home");
        String directory = usersHome + File.separatorChar + FileIO.defaultBookmarksDirectory;
        String path = directory + File.separatorChar + FileIO.defaultBookmarksFileName;

        File file = new File(path);

        if (!file.exists())
        {
            if (file.getParentFile().mkdir())
            {
                file.createNewFile();
            }
            else
            {
                throw new IOException("Failed to create directory " + file.getParent());
            }
        }

        //TODO handle empty bookmarks file.

        return file;
    }

//    public void loadModules(String basePath)
//    {
//        //TODO: Load all jars in the basePath directory.
//        //TODO: Load all bookmarks from those jars.
//    }

    //Copied from:
    //http://stackoverflow.com/questions/19776063/java-list-files-recursively-in-subdirectories-with-apache-commons-io-2-4
    public Collection listFiles(String directoryBase)

    {
        final String[] SUFFIX = {"jar"};  // use the suffix to filter
        File rootDir = new File(directoryBase);
        Collection<File> files = FileUtils.listFiles(rootDir, SUFFIX, true);
        return files;
    }



//    public static void addPath(String s) throws Exception {
//        File f = new File(s);
//        URL url = new URL(f.toURI().toString());
//        ClassLoader loader = URLClassLoader.newInstance(
//            new URL[] { url },
//            getClass().getClassLoader()
//        );
//        Class<?> clazz = Class.forName("mypackage.MyClass", true, loader);
//        Class<? extends Runnable> runClass = clazz.asSubclass(Runnable.class);
//        // Avoid Class.newInstance, for it is evil.
//        Constructor<? extends Runnable> ctor = runClass.getConstructor();
//        Runnable doRun = ctor.newInstance();
//        doRun.run();
//    }


}

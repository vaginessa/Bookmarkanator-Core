package com.micah.bookmarking;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        try {
			URI uri = new URI("www.yahoo.com");
			
			File f = new File(uri.toString());
			if (f.exists())
			{
				System.out.println("file exists "+f.getCanonicalPath());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

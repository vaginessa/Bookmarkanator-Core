package com.bookmarkanator;

import java.io.*;
import com.bookmarkanator.parsers.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
		throws Exception
	{
		SystemResourceParser p = new SystemResourceParser();
		File t = new File("");
		try
		{
			System.out.println(t.getCanonicalPath().toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		File f = new File("/Users/lloyd1/Projects/Bookmark-anator/src/main/system_resource_settings.xml");
		try
		{
			p.parse(f);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

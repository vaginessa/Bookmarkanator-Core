package com.bookmarkanator;

import java.io.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
        throws IOException
    {
        String args = "open -a Terminal";//opens blank terminal on mac osx
        Runtime rt = Runtime.getRuntime();
//        Process pr = rt.exec(args);
//        BufferedOutputStream br = new BufferedOutputStream(pr.getOutputStream());
//        br.write("a;lskdjf;aslkjdf;laksjf;adskjf ".getBytes());
//        br.write("bytererer ".getBytes());
//        br.flush();
//        br.close();

//        ProcessBuilder builder = new ProcessBuilder();
//        builder.redirectErrorStream(true);
//        builder.command(args);

//        try {
//            Process p = rt.exec(args);
//            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
//            PrintWriter output = new PrintWriter(p.getOutputStream());
//            for (int c=0;c<100;c++)
//            {
//                output.print("hello");
//            }
//        } catch (IOException e) {
//            System.out.println(e);
//            return;
//        }

        try {
            // create a new process
            System.out.println("Creating Process...");
            Process p = Runtime.getRuntime().exec("TextEditor");

            // get the output stream
            OutputStream out = p.getOutputStream();

            // close the output stream
            System.out.println("Closing the output stream...");
            out.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}

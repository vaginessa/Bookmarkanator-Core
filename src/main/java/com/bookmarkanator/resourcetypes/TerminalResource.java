package com.bookmarkanator.resourcetypes;

import java.io.*;

/**
 * Represents a resource that will can be called from the command prompt or terminal.
 * <p>
 * This class enables custom commands to be mapped, and to show up as system resources when the user is creating new bookmarks.
 * <p>
 * <p>
 * For instance a bookmark could be added that calls a grep command with specific flags, or other parameters. When the user creates a bookmark they
 * will only need to specify the text inserted into the bookmark, and be placed inbetween the preCommand, and postCommand strings.
 * </p>
 */
public class TerminalResource extends BasicResource
{
    private String preCommand;
    private String postCommand;

    public String getPreCommand()
    {
        return preCommand;
    }

    public void setPreCommand(String preCommand)
    {
        this.preCommand = preCommand;
    }

    public String getPostCommand()
    {
        return postCommand;
    }

    public void setPostCommand(String postCommand)
    {
        this.postCommand = postCommand;
    }

    @Override
    public String execute()
        throws Exception
    {
        //        String command= "gnome-terminal -x \"ping www.yahoo.com\"";
        //Must use the following on linux mint:
        //gnome-terminal -e 'bash -c "cd /etc";"exec bash"'
//        System.out.println("gnome-terminal -e 'bash -c \"" + getText() + "\";\"exec bash\"'");
//        //        String args = "gnome-terminal -e 'bash -c \"cd /etc\";\"exec bash\"'";
//        //        String args = "gnome-terminal -e 'bash -c \""+getText()+"\";\"exec bash\"'";
        String args = "open -a Terminal";//opens blank terminal on mac osx
//        //See:
//        //http://askubuntu.com/questions/484993/run-command-on-anothernew-terminal-window
//        Runtime rt = Runtime.getRuntime();
//        Process pr = rt.exec(args);
//        BufferedOutputStream br = new BufferedOutputStream(pr.getOutputStream());
//        br.write(getText().getBytes());
//        br.write("bytererer ".getBytes());
//        br.flush();
//        br.close();
//        //        String command = "gnome-terminal ping -c 3 www.google.com";
        //
        //        Process proc = Runtime.getRuntime().exec(command);
        //see:
        //http://stackoverflow.com/questions/3643939/java-process-with-input-output-stream
        //http://stackoverflow.com/questions/11573457/java-processbuilder-input-output-stream
        //http://www.javaworld.com/article/2071275/core-java/when-runtime-exec---won-t.html?page=2
        //http://illegalargumentexception.blogspot.com/2010/09/java-systemconsole-ides-and-testing.html
//        Console console;
//
//
//        String command=getText();
//        try {
//            Process process = Runtime.getRuntime().exec(command);
////            ProcessBuilder pb = new ProcessBuilder();
////            pb.command(command);
////            pb.redirectErrorStream(true);
////            Process process = pb.start();
//            System.out.println("the output stream is "+process.getOutputStream());
////            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
////            writer.write("mkdir hello2");
////            writer.flush();
////            writer.close();
//
//            BufferedReader reader=new BufferedReader( new InputStreamReader(process.getInputStream()));
//
//
//            String s;
//            while ((s = reader.readLine()) != null){
//                System.out.println("The inout stream is " + s);
//            }

//        InputStream inp = System.in;
////            Console con = System.console();
////        con.writer().write("hello");
//OutputStream out = System.out;
//
//        out.write("hello".getBytes());
//
//            while (inp.available()>0)
//            {
//                System.out.println(inp.read());
//            }

//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return getText();
    }

    @Override
    public void toXML(StringBuilder sb, String prependTabs)
    {
        sb.append(prependTabs + "<terminal-resource>");
        sb.append("\n");
        sb.append(prependTabs + "\t<name>");
        sb.append(getName());
        sb.append("</name>");
        sb.append("\n");
        sb.append(prependTabs + "\t<text>");
        sb.append(getText());
        sb.append("</text>");
        sb.append("\n");
        sb.append(prependTabs + "\t<pre-command>");
        sb.append(getPreCommand());
        sb.append("</pre-command>");
        sb.append("\n");
        sb.append(prependTabs + "\t<post-command>");
        sb.append(getPostCommand());
        sb.append("</post-command>");
        sb.append("\n");
        sb.append(prependTabs + "</terminal-resource>");
    }

    @Override
    public int hashCode()
    {
        return super.hashCode() + getPreCommand().hashCode() + getPostCommand().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj != null)
        {
            if (obj instanceof DefaultSystemResource)
            {
                DefaultSystemResource d = (DefaultSystemResource) obj;

                if (d.getName().equals(getName()))
                {
                    if (d.getText().equals(getText()))
                    {
                        if (d.getPreCommand().equals(getPreCommand()))
                        {
                            if (d.getPostCommand().equals(getPostCommand()))
                            {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


}

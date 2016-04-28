package com.bookmarkanator.resourcetypes;

import java.awt.*;
import java.awt.datatransfer.*;
import javax.swing.*;
import com.bookmarkanator.settings.*;

/**
 * Represents a resource that will can be called from the command prompt or terminal.
 * <p>
 * This class enables custom commands to be mapped, and to show up as system resources when the user is creating new bookmarks.
 * <p>
 * <p>
 * For instance a bookmark could be added that calls a grep command with specific flags, or other parameters. When the user creates a bookmark they
 * will only need to specify the text inserted into the bookmark, and be placed inbetween the preCommand, and postCommand strings.
 * </P>
 * <p>
 * Note because of the limitations of Java's terminal access on different systems, it was decided to simply open a text window, and a terminal window
 * when the execute method is called. The user can then copy the command in the text window into the open terminal. This is made easier by automatically
 * inserting the text of the node into the system clipboard. This has an unexpected security advantage though because the program won't be able to
 * automatically run dangerous commands. The user will have to manually place them in the terminal and run them.
 * </p>
 */
public class TerminalResource extends BasicResource
{
    public static final int OPEN_TERMINAL_ONLY = 0;//opens the terminal to paste any command one chooses.
    public static final int RUN_PROGRAM_WO_TERMINAL = 1;//can only be used to run a program not run commands such as ls, sudo, mkdir etc...

    private String preCommand;
    private String postCommand;
    private int type;

    public TerminalResource(int type)
    {
        this.type = type;
        preCommand = new String();
        postCommand = new String();
    }

    public TerminalResource()
    {
        this.type = OPEN_TERMINAL_ONLY;
        preCommand = new String();
        postCommand = new String();
    }

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

    public int getType()
    {
        return type;
    }

    @Override
    public String execute()
        throws Exception
    {
        if (getType() == TerminalResource.OPEN_TERMINAL_ONLY)
        {
            StringSelection stringSelection = new StringSelection(this.getText());
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents(stringSelection, null);

            JTextArea textArea = new JTextArea(
                "The following text has already been copied to your clipboard for your convenience. Paste it int the terminal after closing this " +
                    "window if you wish to run it.\n\n\n\"" +
                    this.getText() + "\"");
            JScrollPane scrollPane = new JScrollPane(textArea);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            scrollPane.setPreferredSize(new Dimension(500, 500));
            JOptionPane.showMessageDialog(null, scrollPane, "Copy this text and insert it into the terminal please.", JOptionPane.YES_NO_OPTION);

            Process process = SettingsUtil.openTerminal();
            //            StringBuilder sb = new StringBuilder();
            //            BufferedReader reader=new BufferedReader( new InputStreamReader(process.getInputStream()));
            //
            //            String s;
            //            while ((s = reader.readLine()) != null) {
            //                System.out.println("The inout stream is " + s);
            //            }
            //
            ////            if (process.exitValue()!=0)
            ////            {
            //                System.out.println("error "+process.exitValue());
            //                System.out.println();
            ////            }
        }
        else
        {
            //        String command= "gnome-terminal -x \"ping www.yahoo.com\"";
            //Must use the following on linux mint:
            //gnome-terminal -e 'bash -c "cd /etc";"exec bash"'
            //        System.out.println("gnome-terminal -e 'bash -c \"" + getText() + "\";\"exec bash\"'");
            //        //        String args = "gnome-terminal -e 'bash -c \"cd /etc\";\"exec bash\"'";
            //        //        String args = "gnome-terminal -e 'bash -c \""+getText()+"\";\"exec bash\"'";
            //        StringSelection selection = new StringSelection(theString);
            //        String myString =
            //        String args = "open -a Terminal";//opens blank terminal on mac osx
            //        //See:
            //        //http://askubuntu.com/questions/484993/run-command-on-anothernew-terminal-window

            //        System.out.println(SettingsUtil.getOSName());

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
        }
        return getText();
    }

    @Override
    public String getTypeString()
    {
        return "Term";
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

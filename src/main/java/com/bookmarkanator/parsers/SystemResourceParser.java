package com.bookmarkanator.parsers;

import java.io.*;
import java.util.*;
import javax.xml.stream.*;

import com.bookmarkanator.resourcetypes.BasicResource;
import com.bookmarkanator.resourcetypes.CustomFileFilter;
import com.bookmarkanator.resourcetypes.DefaultSystemResource;
import com.bookmarkanator.resourcetypes.TerminalResource;
import com.bookmarkanator.settings.*;

public class SystemResourceParser
{
    private enum Tags
    {
        root("root"),
        settings("settings"),
        system("system"),
        basicresource("basic-resource"),
        fileeditor("file-editor"),
        filebrowser("file-browser"),
        web("web"),
        name("name"),
        text("text"),
        key("key"),
        value("value"),
        description("description"),
        classpointer("class-pointer"),
        parameter("parameter"),
        terminalresource("terminal-resource"),
        customclass("custom-class"),
        precommand("pre-command"),
        postcommand("post-command"),
        unknown("_unknown_");

        private String tag;
        private static Map<String, Tags> tagsMap;

        static {
            tagsMap = new HashMap<String, Tags>();
            for (Tags tag: Tags.values())
            {
                tagsMap.put(tag.tagText(), tag);
            }
        }

        public String tagText()
        {
            return this.tag;
        }

        public static Map<String, Tags> getTagsMap()
        {
            return tagsMap;
        }

        Tags(String tag)
        {
            this.tag = tag;
        }

    }

    private Settings currentSettings;
    private SystemType currentSystem;
    private BasicResource currentBasicResource;
    private DefaultSystemResource currentDefaultSystemResource;
    private CustomFileFilter currentCustomFileFilter;
    private TerminalResource currentTerminalResource;
    private XMLStreamReader reader;
    private Stack<Tags> stateStack;
    private Stack<StringBuilder> charsStack;

    public void parse(File file)
        throws Exception
    {
        Reader in = new FileReader(file);
        try
        {
            parse(in);
        }
        finally
        {
            in.close();
        }
    }

    public Settings parse(Reader in)
        throws XMLStreamException
    {
        XMLInputFactory xif = XMLInputFactory.newInstance();
        xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
        reader = xif.createXMLStreamReader(in);


        stateStack = new Stack<Tags>();
        charsStack = new Stack<StringBuilder>();
        stateStack.push(Tags.root);

        while (reader.hasNext())
        {
            int event = reader.next();

            switch (event)
            {
                case XMLStreamConstants.START_ELEMENT:
                    startTag(reader.getLocalName());
                    break;

                case XMLStreamConstants.CHARACTERS:
                    charsStack.peek().append(reader.getText());
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    endTag();
                    break;
            }
        }

        return currentSettings;
    }

    public void startTag(String currentTag)
    {
        Tags prevTag = stateStack.peek();
        Tags state = Tags.unknown;

        switch (prevTag)
        {
            case root:
                state =match(state, Tags.settings, currentTag);
                if (state==Tags.settings)
                {
                    currentSettings = new Settings();
                    //todo set attributes
                }
                break;
            case settings:
                state =match(state, Tags.system, currentTag);
                if (state==Tags.system)
                {
                    currentSystem = new SystemType();
                    //todo set attributes
                }
                break;
            case system:
                state =match(state, Tags.basicresource, currentTag);
                state =match(state, Tags.fileeditor, currentTag);
                state =match(state, Tags.filebrowser, currentTag);
                state =match(state, Tags.web, currentTag);
                state =match(state, Tags.terminalresource, currentTag);
                state =match(state, Tags.customclass, currentTag);
                if (state==Tags.basicresource)
                {
                    currentBasicResource = new BasicResource();
                    //todo set attributes
                }
                else if (state==Tags.filebrowser)
                {

                }
                else if (state==Tags.fileeditor)
                {

                }
                else if (state==Tags.web)
                {

                }
                else if (state==Tags.terminalresource)
                {

                }
                else if (state==Tags.customclass)
                {

                }
                break;
            case basicresource:
                state =match(state, Tags.name, currentTag);
                state =match(state, Tags.text, currentTag);
                break;
            case customclass:
                state =match(state, Tags.classpointer, currentTag);
                state =match(state, Tags.parameter, currentTag);
            case terminalresource:
                state =match(state, Tags.name, currentTag);
                state =match(state, Tags.text, currentTag);
                state =match(state, Tags.precommand, currentTag);
                state =match(state, Tags.postcommand, currentTag);
                break;
            case web:
                state =match(state, Tags.name, currentTag);
                state =match(state, Tags.text, currentTag);
                state =match(state, Tags.precommand, currentTag);
                state =match(state, Tags.postcommand, currentTag);
                break;
            case fileeditor:
                state =match(state, Tags.name, currentTag);
                state =match(state, Tags.text, currentTag);
                state =match(state, Tags.precommand, currentTag);
                state =match(state, Tags.postcommand, currentTag);
                break;
            case filebrowser:
                state =match(state, Tags.name, currentTag);
                state =match(state, Tags.text, currentTag);
                state =match(state, Tags.precommand, currentTag);
                state =match(state, Tags.postcommand, currentTag);
                break;
            case parameter:
                state =match(state, Tags.key, currentTag);
                state =match(state, Tags.value, currentTag);
                state =match(state, Tags.description, currentTag);
                break;
        }

        if (state == Tags.unknown)
        {
            throw new RuntimeException("Unknown [" + prevTag + "][" + currentTag + "]");
        }

        stateStack.push(state);
        charsStack.push(new StringBuilder());
    }

    public void textTag()
    {

    }

    public void endTag()
    {
        Tags state = stateStack.pop();
        StringBuilder chars = charsStack.pop();
    }

    public static Tags match(Tags unchangedState, Tags state, String tag)
    {
        Tags theTag = Tags.getTagsMap().get(tag);

        if (theTag!=null && theTag==state)
        {
            return state;
        }

        return unchangedState;
    }
}

package com.bookmarkanator.parsers;

import java.io.*;
import java.util.*;
import javax.xml.stream.*;

import com.bookmarkanator.customClass.*;
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
        quickaccesstags("quick-access-tag-group"),
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
    private CustomClassParameter currentParameter;
    private XMLStreamReader reader;
    private Stack<Tags> stateStack;
    private Stack<StringBuilder> charsStack;
    private QuickAccessTagGroup currentQuickAccessTagGroup;

    public Settings parse(File file)
        throws Exception
    {
        Reader in = new FileReader(file);
        Settings s;
        try
        {
            s = parse(in);
        }
        finally
        {
            in.close();
        }
        return s;
    }

    public Settings parse(Reader in)
            throws Exception
    {
        XMLInputFactory xif = XMLInputFactory.newInstance();
        xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
        reader = xif.createXMLStreamReader(in);


        stateStack = new Stack<>();
        charsStack = new Stack<>();
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
                    charsStack.peek().append(reader.getText().trim());
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    endTag();
                    break;
            }
        }

        return currentSettings;
    }

    public void startTag(String currentTag) throws Exception {
        Tags prevTag = stateStack.peek();
        Tags state = Tags.unknown;
        String attr;

        switch (prevTag)
        {
            case root:
                state =match(state, Tags.settings, currentTag);
                if (state==Tags.settings)
                {
                    currentSettings = new Settings();
                    attr = ParserUtil.getStartElementAttribute(reader, "version");
                    if (attr==null)
                    {
                        throw new Exception("Settings version attribute missing");
                    }
                    currentSettings.setVersion(attr);
                }
                break;
            case settings:
                state =match(state, Tags.system, currentTag);
                if (state==Tags.system)
                {
                    currentSystem = new SystemType();
                    attr = ParserUtil.getStartElementAttribute(reader, "name");
                    if (attr==null)
                    {
                        throw new Exception("System type name attribute missing");
                    }
                    currentSystem.setSystemName(attr);
                    attr = ParserUtil.getStartElementAttribute(reader, "version");
                    if (attr==null)
                    {
                        throw new Exception("System type version attribute missing");
                    }
                    currentSystem.setSystemVersion(attr);
                    attr = ParserUtil.getStartElementAttribute(reader, "versionname");
                    if (attr==null)
                    {
                        throw new Exception("System type versionname attribute missing");
                    }
                    currentSystem.setSystemVersionName(attr);
                }
                break;
            case system:
                state =match(state, Tags.basicresource, currentTag);
                state =match(state, Tags.fileeditor, currentTag);
                state =match(state, Tags.filebrowser, currentTag);
                state =match(state, Tags.web, currentTag);
                state =match(state, Tags.terminalresource, currentTag);
                state =match(state, Tags.customclass, currentTag);
                state =match(state, Tags.quickaccesstags, currentTag);

                if (state == Tags.quickaccesstags)
                {
                    currentQuickAccessTagGroup = new QuickAccessTagGroup();

                    attr = ParserUtil.getStartElementAttribute(reader, "label");
                    if (attr!=null)
                    {
                        currentQuickAccessTagGroup.setLabel(attr);
                    }
                    else
                    {
                        throw new Exception("Quick access tag encountered with out label attribute.");
                    }
                    attr = ParserUtil.getStartElementAttribute(reader, "index");
                    if (attr!=null)
                    {
                        currentQuickAccessTagGroup.setIndex(Integer.parseInt(attr));
                    }
                    else
                    {
                        throw new Exception("Quick access tag encountered with out index attribute.");
                    }
                    attr = ParserUtil.getStartElementAttribute(reader, "tags");
                    if (attr!=null)
                    {
                        String[] s = attr.split(",");

                        for (String st: s)
                        {
                            currentQuickAccessTagGroup.addTag(st);
                        }
                    }
                    else
                    {
                        throw new Exception("Quick access tag encountered with out tags attribute.");
                    }
                }
                else if (state==Tags.basicresource)
                {
                    currentBasicResource = new BasicResource();
                }
                else if (state==Tags.filebrowser || state==Tags.fileeditor || state==Tags.web)
                {
                    if (currentDefaultSystemResource!=null)
                    {
                        throw new Exception("Improperly nested DefaultSystemResource tags encountered");
                    }
                    else
                    {
                        switch (state)
                        {
                            case filebrowser:
                                currentDefaultSystemResource = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_BROWSER);
                                break;
                            case fileeditor:
                                currentDefaultSystemResource = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_EDITOR);
                                break;
                            case web:
                                currentDefaultSystemResource = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
                                break;
                        }
                    }
                }
                else if (state==Tags.terminalresource)
                {
                    if (currentTerminalResource!=null)
                    {
                        throw new Exception("Improperly nested TerminalResource tags encountered");
                    }
                    else
                    {
                        currentTerminalResource = new TerminalResource();
                    }
                }
                else if (state==Tags.customclass)
                {
                    if (currentCustomFileFilter!=null)
                    {
                        throw new Exception("Improperly nested CustomFileFilterResource tags encountered");
                    }
                    else
                    {
                        currentCustomFileFilter = new CustomFileFilter();
                    }
                }
                break;
            case basicresource:
                state =match(state, Tags.name, currentTag);
                state =match(state, Tags.text, currentTag);
                break;
            case customclass:
                state =match(state, Tags.classpointer, currentTag);
                state =match(state, Tags.parameter, currentTag);
                if (state==Tags.parameter)
                {
                    currentParameter = new CustomClassParameter();
                    attr = ParserUtil.getStartElementAttribute(reader, "required");
                    if (attr==null)
                    {
                        currentParameter.setRequired(false);
                    }
                    else
                    {
                        currentParameter.setRequired(Boolean.parseBoolean(attr));
                    }
                }
            case terminalresource:
                state =match(state, Tags.name, currentTag);
                state =match(state, Tags.text, currentTag);
                state =match(state, Tags.precommand, currentTag);
                state =match(state, Tags.postcommand, currentTag);
                break;

            case web:
            case fileeditor:
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

    public void endTag()
    {
        Tags prevTag = stateStack.pop();
        String text = charsStack.pop().toString();

        switch (prevTag)
        {
            case filebrowser:
            case fileeditor:
            case web:
                currentSystem.addResource(currentDefaultSystemResource);
                currentDefaultSystemResource = null;
                break;
            case system:
                currentSettings.addSystemType(currentSystem);
                break;
            case basicresource:
                currentSystem.addResource(currentBasicResource);
                break;
            case terminalresource:
                currentSystem.addResource(currentTerminalResource);
                currentTerminalResource = null;
                break;
            case customclass:
                currentSystem.addResource(currentCustomFileFilter);
                currentCustomFileFilter = null;
                break;
            case parameter:
                currentCustomFileFilter.addParameter(currentParameter);
                break;
            case key:
                currentParameter.setKey(text);
                break;
            case value:
                currentParameter.setValue(text);
                break;
            case description:
                currentParameter.setDescription(text);
                break;
            case name:
                switch (stateStack.peek())
                {
                    case basicresource:
                        currentBasicResource.setName(text);
                        break;
                    case fileeditor:
                    case filebrowser:
                    case web:
                        currentDefaultSystemResource.setName(text);
                        break;
                    case terminalresource:
                        currentTerminalResource.setName(text);
                        break;
                    case customclass:
                        currentCustomFileFilter.setName(text);
                        break;
                }
                break;
            case text:
                switch (stateStack.peek())
                {
                    case basicresource:
                        currentBasicResource.setText(text);
                        break;
                    case fileeditor:
                    case filebrowser:
                    case web:
                        currentDefaultSystemResource.setText(text);
                        break;
                    case terminalresource:
                        currentTerminalResource.setText(text);
                        break;
                    case customclass:
                        currentCustomFileFilter.setText(text);
                        break;
                }
                break;
            case precommand:
                switch (stateStack.peek())
                {
                    case fileeditor:
                    case filebrowser:
                    case web:
                        currentDefaultSystemResource.setPreCommand(text);
                        break;
                    case terminalresource:
                        currentTerminalResource.setPreCommand(text);
                        break;
                }
                break;
            case postcommand:
                switch (stateStack.peek())
                {
                    case fileeditor:
                    case filebrowser:
                    case web:
                        currentDefaultSystemResource.setPostCommand(text);
                        break;
                    case terminalresource:
                        currentTerminalResource.setPostCommand(text);
                        break;
                }
                break;
            case quickaccesstags:
                currentSystem.addQuickAccessTagGroup(currentQuickAccessTagGroup);
                break;

        }
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

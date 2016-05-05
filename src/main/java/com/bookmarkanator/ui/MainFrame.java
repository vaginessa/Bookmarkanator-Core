package com.bookmarkanator.ui;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.interfaces.*;
import com.bookmarkanator.resourcetypes.*;
import com.bookmarkanator.ui.panel.*;
import com.bookmarkanator.ui.panel.itempanel.*;
import com.bookmarkanator.ui.panel.listpanel.*;

public class MainFrame implements Observer
{
    public static final String SELECTED_TAG = "Selected Tag";
    public static final String SELECTABLE_TAG = "Selectable Tag";
    public static final String BOOKMARK_TYPE = "Bookmark Type";
    public static final String BOOKMARK = "Bookmark";

    private JFrame frame;
    private GridBagConstraints con;
    private StringsPanel<Bookmark> bookmarksPan;
    private StringsPanel<String> tagsSelectionPan;
    private StringsPanel<String> selectedTags;
    private StringsPanel<BasicResource> bookmarkTypes;
    private List<Bookmark> bookmarks;

    public MainFrame()
    {
        frame = new JFrame();
        frame.setPreferredSize(new Dimension(800, 800));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridBagLayout());
        JFrame.setDefaultLookAndFeelDecorated(true);

        init();

        //TODO Replace separate panels with splitpanes.

        frame.getContentPane().doLayout();
        frame.pack();
        frame.setVisible(true);
    }

    private void init()
    {
        //        JSplitPane
        con = new GridBagConstraints();
        con.fill = GridBagConstraints.BOTH;
        con.weightx = .20;
        con.weighty = 1.0;
        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 1;
        con.gridheight = 2;
        bookmarkTypes = new StringsPanel<>(this, MainFrame.BOOKMARK_TYPE, new ToggleButtonPanel<BasicResource>());
        bookmarkTypes.getSearch().setVisible(false);
        bookmarkTypes.setMinimumSize(new Dimension(80, 500));
        bookmarkTypes.setMaximumSize(new Dimension(200, 500));

        frame.add(bookmarkTypes, con);

        con.fill = GridBagConstraints.BOTH;
        con.weightx = .70;
        con.gridheight = 1;
        con.gridx = 1;
        con.gridy = 0;
        selectedTags = new StringsPanel<>(this, MainFrame.SELECTED_TAG, new XButtonPanel());
        frame.add(selectedTags, con);

        con.gridy = 1;
        con.gridx = 1;
        tagsSelectionPan = new StringsPanel<String>(this, MainFrame.SELECTABLE_TAG, new StringPanel());
        tagsSelectionPan.setPreferredSize(new Dimension(500, 500));
        frame.add(tagsSelectionPan, con);

        con.weightx = 1;
        con.gridx = 2;
        con.gridy = 0;
        con.gridheight = 2;
        bookmarksPan = getTestBookmarks();
        bookmarksPan.setPreferredSize(new Dimension(200, 600));
        frame.add(bookmarksPan, con);

        con.gridx = 0;
        con.gridy = 2;
        con.gridwidth = 3;
        con.gridheight = 1;
        con.weighty = .20;
        OptionsPanel options = new OptionsPanel();
        options.setMinimumSize(new Dimension(-1, 80));
        frame.add(options, con);

        addTagsToSelectableTagsPanel(null);
        addBookmarkTypesToPanel();
    }

    private void addBookmarkTypesToPanel()
    {
        Set<BasicResource> types = BookmarksUtil.getUniqueResources(bookmarks);
        List<BasicResource> res = new ArrayList<>(types);

        bookmarkTypes.setLabels(res);
    }

    private void addTagsToSelectableTagsPanel(List<String> withoutThese)
    {
        List<String> labels = new ArrayList<>();
        Set<String> s = BookmarksUtil.getTags(bookmarksPan.getLabels());
        labels.addAll(s);

        if (withoutThese != null)
        {
            for (String st : withoutThese)
            {
                labels.remove(st);
            }
        }

        tagsSelectionPan.setLabels(labels);
    }

    private StringsPanel getTestBookmarks()
    {
        StringsPanel bookmarksPan = new StringsPanel(this, MainFrame.BOOKMARK, new BookmarkPanel<Bookmark>());

        Bookmark web = new Bookmark();
        web.setName("yahoo.com");
        DefaultSystemResource dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
        dsr.setName("yahoo.com");
        dsr.setText("http://www.yahoo.com");
        web.setResource(dsr);
        web.addTag("yahoo");
        web.addTag("internet");
        web.addTag("web");
        web.addTag("social");

        Bookmark web1 = new Bookmark();
        web1.setName("google.com");
        dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
        dsr.setName("google.com");
        dsr.setText("http://www.google.com");
        web1.setResource(dsr);
        web1.addTag("web");
        web1.addTag("search");
        web1.addTag("google");
        web1.addTag("internet");

        Bookmark web2 = new Bookmark();
        web2.setName("msn");
        dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
        dsr.setName("msn");
        dsr.setText("http://www.msn.com");
        web2.setResource(dsr);
        web2.addTag("web");
        web2.addTag("msn");
        web2.addTag("social");
        web2.addTag("internet");

        Bookmark web3 = new Bookmark();
        web3.setName("acronymfinder");
        dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
        dsr.setName("acronymfinder");
        dsr.setText("http://www.acronymfinder.com");
        web3.setResource(dsr);
        web3.addTag("web");
        web3.addTag("acronym");
        web3.addTag("search");

        Bookmark terminal = new Bookmark();
        terminal.setName("pwd");
        TerminalResource tr = new TerminalResource(TerminalResource.OPEN_TERMINAL_ONLY);
        tr.setText("pwd");
        terminal.setResource(tr);
        terminal.addTag("print");
        terminal.addTag("directory");
        terminal.addTag("terminal");
        terminal.addTag("prompt");
        terminal.addTag("run");

        Bookmark terminal2 = new Bookmark();
        terminal2.setName("change java");
        tr = new TerminalResource(TerminalResource.OPEN_TERMINAL_ONLY);
        tr.setText("sudo update-alternatives --config java");
        terminal2.setResource(tr);
        terminal2.addTag("java");
        terminal2.addTag("change");
        terminal2.addTag("version");
        terminal2.addTag("Java 8");

        Bookmark fileOpen = new Bookmark();
        fileOpen.setName("open home");
        dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_BROWSER);
        dsr.setText("/home");
        fileOpen.setResource(dsr);
        fileOpen.addTag("file");
        fileOpen.addTag("open");
        fileOpen.addTag("sys home");

        bookmarks = new ArrayList<>();
        bookmarks.add(web);
        bookmarks.add(web1);
        bookmarks.add(web2);
        bookmarks.add(web3);
        bookmarks.add(terminal);
        bookmarks.add(terminal2);
        bookmarks.add(fileOpen);

        for (int c = 0; c < 100; c++)
        {
            fileOpen = new Bookmark();
            fileOpen.setName("open home " + c);
            dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_BROWSER);
            dsr.setText("/home");
            //            fileOpen.addObserver(this);
            fileOpen.setResource(dsr);
            fileOpen.addTag("file");
            fileOpen.addTag("open");
            fileOpen.addTag("sys home");
            bookmarks.add(fileOpen);
        }

        Bookmark gitignore = new Bookmark();
        gitignore.setName("gitignore");
        dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_EDITOR);
        dsr.setName("gitignore");
        dsr.setText("/users/lloyd1/.gitignore_global");
        gitignore.setResource(dsr);
        gitignore.addTag("Bob");

        bookmarks.add(gitignore);

        bookmarksPan.setLabels(bookmarks);

        return bookmarksPan;
    }

    @Override
    public void update(Observable o, Object arg)
    {

        if (arg instanceof StringPanelInterface)
        {
            StringPanel sp = (StringPanel) arg;
            System.out.println("String panel clicked " + sp.getType() + " " + sp.getText() + " " + sp.getItem().getClass().getName());

            if (sp.getType().equals(MainFrame.SELECTABLE_TAG))
            {
                selectedTags.add(sp);
                selectedTags.updateUI();

                List<Bookmark> li = new ArrayList<>();
                li.addAll(bookmarksPan.getLabels().values());
                List<Bookmark> panelBookmarks = BookmarksUtil
                    .getBookmarksWithAllOfTheseTagsOnly(new HashSet<>(BookmarksUtil.filterBookmarksByResourceTypeName(li, getSelectedBookmarkTypes())), selectedTags.getLabels().keySet());
                bookmarksPan.setLabels(panelBookmarks);
                bookmarksPan.updateUI();

                List<String> sl = new ArrayList<>();
                sl.addAll(selectedTags.getLabels().keySet());

                addTagsToSelectableTagsPanel(sl);

                bookmarksPan.getSearch().getEditor().setItem("");
                selectedTags.getSearch().getEditor().setItem("");
                tagsSelectionPan.getSearch().getEditor().setItem("");

            }
            else if (sp.getType().equals(MainFrame.SELECTED_TAG))
            {
                selectedTags.remove(sp);
                selectedTags.updateUI();

                System.out.println("S2 " + selectedTags.getLabels().size());

                List<Bookmark> panelBookmarks;
                if (selectedTags.getLabels().isEmpty())
                {
                    bookmarksPan.setLabels(BookmarksUtil.filterBookmarksByResourceTypeName(bookmarks, getSelectedBookmarkTypes()));
                }
                else
                {
                    panelBookmarks = BookmarksUtil.getBookmarksWithAllOfTheseTagsOnly(new HashSet<>(BookmarksUtil.filterBookmarksByResourceTypeName(bookmarks, getSelectedBookmarkTypes())),
                        selectedTags.getLabels().keySet());
                    bookmarksPan.setLabels(panelBookmarks);
                }

                bookmarksPan.updateUI();

                List<String> sl = new ArrayList<>();
                sl.addAll(selectedTags.getLabels().keySet());

                addTagsToSelectableTagsPanel(sl);
                bookmarksPan.getSearch().getEditor().setItem("");
                selectedTags.getSearch().getEditor().setItem("");
                tagsSelectionPan.getSearch().getEditor().setItem("");

            }
            else if (sp.getItem() instanceof Bookmark)
            {
                try
                {
                    Bookmark b = (Bookmark) sp.getItem();

                    b.execute();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (sp instanceof ToggleButtonPanel)
            {
                System.out.println("Toggle button clicked ");


                bookmarksPan.setLabels(BookmarksUtil.filterBookmarksByResourceTypeName(bookmarks, getSelectedBookmarkTypes()));
                bookmarksPan.refresh();

                //adding bookmark tags to tag selection panel
                List<String> labels = new ArrayList<>();
                Set<String> s = BookmarksUtil.getTags(bookmarksPan.getLabels());
                labels.addAll(s);
                tagsSelectionPan.setLabels(labels);

                selectedTags.setLabels(new ArrayList());

                bookmarksPan.getSearch().getEditor().setItem("");
                selectedTags.getSearch().getEditor().setItem("");
                tagsSelectionPan.getSearch().getEditor().setItem("");
            }
            else
            {
                System.out.println("Encountered string panel with unknown type");
            }
        }
        else
        {
            System.out.println("Unspecified object action " + arg.getClass().getName());
        }
    }

    public List getSelectedBookmarkTypes()
    {
        ArrayList selected = new ArrayList();

        for (Component com: bookmarkTypes.getPan().getComponents())
        {//getting selected tag types.
            if (com instanceof ToggleButtonPanel)
            {
                ToggleButtonPanel tg = (ToggleButtonPanel)com;
                if (!tg.getButton().isSelected())
                {
                    selected.add(tg.getItem());
                }
            }
        }
        return selected;
    }
}

package com.bookmarkanator.ui  ;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.interfaces.*;
import com.bookmarkanator.resourcetypes.*;

public class MainFrame implements Observer {
    private JFrame frame;
    private GridBagConstraints con;
    private ListableItemsPanel bookmarksPan;
    private ListableItemsPanel tagsSelectionPan;
    private SelectedTagsPanel selectedTags;
    private ListableItemsPanel bookmarkTypes;
    private List<Bookmark> bookmarks;

    public MainFrame()
    {
        frame = new JFrame();
        frame.setPreferredSize(new Dimension(800,800));
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
        bookmarkTypes = new ListableItemsPanel();
        bookmarkTypes.getSearch().setVisible(false);
        bookmarkTypes.setMinimumSize(new Dimension(80,500));
        bookmarkTypes.setMaximumSize(new Dimension(200,500));

        frame.add(bookmarkTypes, con);

        con.fill = GridBagConstraints.BOTH;
        con.weightx = .70;
        con.gridheight = 1;
        con.gridx = 1;
        con.gridy = 0;
        selectedTags = new SelectedTagsPanel();
        frame.add(selectedTags, con);

        con.gridy = 1;
        con.gridx = 1;
        tagsSelectionPan = new ListableItemsPanel();
        tagsSelectionPan.setPreferredSize(new Dimension(500,500));
        frame.add(tagsSelectionPan,con);

        con.weightx = 1;
        con.gridx = 2;
        con.gridy = 0;
        con.gridheight = 2;
        bookmarksPan = getTestBookmarks();
        bookmarksPan.setPreferredSize(new Dimension(200, 600));
        frame.add(bookmarksPan,con);

        con.gridx = 0;
        con.gridy = 2;
        con.gridwidth = 3;
        con.gridheight = 1;
        con.weighty = .20;
        OptionsPanel options = new OptionsPanel();
        options.setMinimumSize(new Dimension(-1,80));
        frame.add(options, con);

        getBookmarkTags();
        addBookmarkTypesToPanel();
    }

    private void addBookmarkTypesToPanel()
    {
        List<ListableItem> b = bookmarksPan.getItemsList();
        Set<String> strings = new HashSet<>();
        List<ListableItem> b2 = new ArrayList<>();

        for (ListableItem l: b)
        {
            Bookmark bk = ((Bookmark)l);
            strings.add(bk.getResource().getTypeString());
        }

        for (String s: strings)
        {
            BookmarkType st = new BookmarkType(s);
            st.addObserver(this);
            b2.add(st);
        }

        bookmarkTypes.setItemsList(b2);
    }

    private void getBookmarkTags()
    {
        List<String> li = BookmarksUtil.getSortedList(BookmarksUtil.getTags(bookmarks));

        List<ListableItem> li2 = new ArrayList<>(li.size());

        for (String s:li)
        {
            SelectableTag st = new SelectableTag(s);
            st.addObserver(this);
            li2.add(st);
        }

        tagsSelectionPan.setItemsList(li2);
    }

    private List<ListableItem> convertToSelectedTags(List<String> strings){
        List<ListableItem> res = new ArrayList<>();
        for (String s: strings)
        {
           SelectedTag st = new SelectedTag(s);
            st.addObserver(this);
            res.add(st);
        }
        return res;
    }

    private List<ListableItem> convertToSelectableTags(List<String> strings){
        List<ListableItem> res = new ArrayList<>();
        for (String s: strings)
        {
            SelectableTag st = new SelectableTag(s);
            st.addObserver(this);
            res.add(st);
        }
        return res;
    }

    private ListableItemsPanel getTestBookmarks()
    {
        ListableItemsPanel bookmarksPan = new ListableItemsPanel();

        Bookmark web = new Bookmark();
        web.setName("yahoo.com");
        DefaultSystemResource dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_WEB_BROWSER);
        dsr.setName("yahoo.com");
        dsr.setText("http://www.yahoo.com");
        web.addObserver(this);
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
        web1.addObserver(this);
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
        web2.addObserver(this);
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
        web3.addObserver(this);
        web3.setResource(dsr);
        web3.addTag("web");
        web3.addTag("acronym");
        web3.addTag("search");


        Bookmark terminal = new Bookmark();
        terminal.setName("pwd");
        TerminalResource tr = new TerminalResource(TerminalResource.OPEN_TERMINAL_ONLY);
        tr.setText("pwd");
        terminal.addObserver(this);
        terminal.setResource(tr);
        terminal.addTag("print");
        terminal.addTag("directory");
        terminal.addTag("terminal");
        terminal.addTag("prompt");
        terminal.addTag("run");

        Bookmark fileOpen = new Bookmark();
        fileOpen.setName("open home");
        dsr = new DefaultSystemResource(DefaultSystemResource.RESOURCE_TYPE_DEFAULT_FILE_BROWSER);
        dsr.setText("/home");
        fileOpen.addObserver(this);
        fileOpen.setResource(dsr);
        fileOpen.addTag("file");
        fileOpen.addTag("open");
        fileOpen.addTag("sys home");


        bookmarks= new ArrayList<>();
        bookmarks.add(web);
        bookmarks.add(web1);
        bookmarks.add(web2);
        bookmarks.add(web3);
        bookmarks.add(terminal);
        bookmarks.add(fileOpen);

        for (int c=0;c<10;c++)
        {
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

        bookmarksPan.setItemsList((List<ListableItem>)(Object)bookmarks);
        return bookmarksPan;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof SelectableTag)
        {
            System.out.println("selectable tag clicked");

            String s = ((SelectableTag)o).getText();
            Set<String> a = selectedTags.getItemNames();
            int length = a.size();

            a.add(s);

            if (a.size()>length)
            {//it added an item. Update the view.
                selectedTags.setItemsList(convertToSelectedTags(BookmarksUtil.getSortedList(a)));

                //update bookmarks
                bookmarksPan.setItemsList((List<ListableItem>)(Object)BookmarksUtil.getBookmarksWithAllOfTheseTagsOnly((List<Bookmark>)(Object)bookmarksPan.getItemsList(), selectedTags.getItemNames()));

                //update tag selection panels
                Set<String> b = BookmarksUtil.getTags((List<Bookmark>)(Object)bookmarksPan.getItemsList());

                for (String st: selectedTags.getItemNames())
                {//making sure that the selected tags don't appear in the tag selection window.
                    b.remove(st);
                }

                tagsSelectionPan.setItemsList(convertToSelectableTags(BookmarksUtil.getSortedList(b)));
                addBookmarkTypesToPanel();
            }
        }
        else if (o instanceof SelectedTag)
        {
            selectedTags.getItemsList().remove(o);
            selectedTags.refresh();
            System.out.println("selected tag clicked");

            bookmarksPan.setItemsList((List<ListableItem>)(Object)BookmarksUtil.getBookmarksWithAllOfTheseTagsOnly((List<Bookmark>)(Object)bookmarks, selectedTags.getItemNames()));

            //update tag selection panels
            Set<String> b = BookmarksUtil.getTags((List<Bookmark>)(Object)bookmarksPan.getItemsList());

            for (String st: selectedTags.getItemNames())
            {//making sure that the selected tags don't appear in the tag selection window.
                b.remove(st);
            }

            tagsSelectionPan.setItemsList(convertToSelectableTags(BookmarksUtil.getSortedList(b)));
            addBookmarkTypesToPanel();
        }
        else if (o instanceof Bookmark)
        {
            System.out.println("bookmark clicked");
        }
        else if (o instanceof BookmarkType)
        {
            System.out.println("bookmark type clicked");
            List<Bookmark> b = (List<Bookmark>)(Object)bookmarksPan.getItemsList();
            Set<BasicResource> br = new HashSet<>();

            for (Bookmark bk: b)
            {//get resources and add to set
                br.add(bk.getResource());
            }

            b = BookmarksUtil.getBookmarksByType(b,new ArrayList<>(br));

            for (Bookmark bk: b)
            {
                System.out.println(bk.getName());
            }
        }
        else
        {
            System.out.println("Unspecified object action "+o.getClass().getName());
        }
    }
}

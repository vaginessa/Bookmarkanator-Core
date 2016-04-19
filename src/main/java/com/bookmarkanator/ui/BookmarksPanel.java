package com.bookmarkanator.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import com.bookmarkanator.bookmarks.*;

public class BookmarksPanel extends JPanel {
    private JScrollPane scroll;
    private List<Bookmark> bookmarkList;
    private List<Bookmark> currentBookmarkList;
    private JPanel pan;
    private JComboBox search;
    private Set<String> bookmarkNames;
    private Map<String, List<Bookmark>> bookmarksSearchMap;

    public BookmarksPanel() {
        super();
        this.setBackground(Color.yellow);
        this.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();

        bookmarkNames = new HashSet<>();
        bookmarksSearchMap = new HashMap<>();

//        this.setBackground(Color.red);
        setBorder(BorderFactory.createLineBorder(Color.red));

        search  = new JComboBox(new String[]{""});
        search.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                System.out.println("clicked ");
                getSelectedBookmarks();
            }
        });
        final String[] st = new String[1];//final container for inputted text

        final JTextField tfListText = (JTextField) search.getEditor().getEditorComponent();
        tfListText.addCaretListener(new CaretListener() {
            private String lastText;

            @Override
            public void caretUpdate(CaretEvent e) {
                String text = tfListText.getText();
                if (!text.equals(lastText)) {
                    lastText = text;
                    st[0] = lastText;
                }
            }
        });

        search.setEditable(true);
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSelectedBookmarks();

            }
        });
        search.getEditor().getEditorComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {

                //TODO Get rid of sound when backspace reaches the left side of the text area.

                if (e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_DOWN && e.getKeyCode() != KeyEvent.VK_ENTER && e.getKeyCode() != KeyEvent.VK_RIGHT && e.getKeyCode() != KeyEvent.VK_LEFT)
                {
                    List<String> res = BookmarksUtil.getSuggestedTags(bookmarkNames, st[0], 10);
                    res.add(0, st[0]);
                    search.setModel(new DefaultComboBoxModel(res.toArray()));
                    search.setPopupVisible(true);
                    System.out.println("Key released");
                    if (st[0].isEmpty())
                    {
                        currentBookmarkList = getBookmarkList();
                        refresh();
                    }
                }
                System.out.println("Element size "+getCurrentBookmarkList().isEmpty());
                if (search.getModel().getSize()==1)
                {
                    search.setPopupVisible(false);
                }
            }
        });

        scroll = new JScrollPane();

        pan = new JPanel();
        pan.setBorder(BorderFactory.createLineBorder(Color.blue));
        pan.setLayout(new ModifiedFlowLayout(1,10,10));
        pan.setPreferredSize(new Dimension(150, 1000));

        scroll.getViewport().add(pan);
        con.fill = GridBagConstraints.BOTH;
        con.weighty = .01;
        con.weightx = 1;
        con.gridx = 0;
        con.gridy = 0;

        this.add(search, con);
        con.weighty = 1;
        con.gridy = 2;
        this.add(scroll, con);

    }

    private List<Bookmark> getSelectedBookmarks()
    {
        String selected = search.getSelectedItem().toString();
        System.out.println("selected method "+ selected);
        search.setPopupVisible(false);
        search.getEditor().setItem(null);
        List<Bookmark> l = bookmarksSearchMap.get(selected);
        if (l==null)
        {
            l = new ArrayList<>();
        }
        currentBookmarkList = l;
        refresh();
        return l;
    }

    public void refresh()
    {
        pan.removeAll();
        bookmarkNames.clear();


        for (Bookmark b: getCurrentBookmarkList())
        {
            BookmarkPanel bp = new BookmarkPanel(b);
            bp.setAlignmentX(Component.CENTER_ALIGNMENT);
            pan.add(bp);
            bookmarkNames.add(b.getName());
        }

        bookmarksSearchMap = BookmarksUtil.getBookmarksText(getBookmarkList());
        this.scroll.updateUI();
    }

    public List<Bookmark> getBookmarkList() {
        return bookmarkList;
    }

    public List<Bookmark> getCurrentBookmarkList()
    {
        return currentBookmarkList;
    }

    public void setBookmarkList(List<Bookmark> bookmarkList) {
        this.bookmarkList = bookmarkList;
        currentBookmarkList = bookmarkList;
        refresh();
    }
}

package com.bookmarkanator.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.interfaces.*;

public class ListableItemsPanel extends JPanel
{
    private JScrollPane scroll;
    private List<ListableItem> itemsList;
    private List<ListableItem> currentlyShowingItemsList;
    private JPanel pan;
    private JComboBox search;
    private Set<String> itemNames;
    private Map<String, List<ListableItem>> itemsSearchMap;

    public ListableItemsPanel()
    {
        super();
        this.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();

        itemNames = new HashSet<>();
        itemsSearchMap = new HashMap<>();

        search = new JComboBox();
        search.addItemListener(new ItemListener()
        {
            @Override
            public void itemStateChanged(ItemEvent e)
            {
                System.out.println("clicked ");
                getSelectedItems();
            }
        });
        final String[] st = new String[1];//final container for inputted text

        //The following 13 lines gotten from stackoverflow.com
        final JTextField tfListText = (JTextField) search.getEditor().getEditorComponent();
        tfListText.addCaretListener(new CaretListener()
        {
            private String lastText;

            @Override
            public void caretUpdate(CaretEvent e)
            {
                String text = tfListText.getText();
                if (!text.equals(lastText))
                {
                    lastText = text;
                    st[0] = lastText;
                }
            }
        });

        search.setEditable(true);
        search.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                getSelectedItems();
            }
        });
        search.getEditor().getEditorComponent().addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
            }

            @Override
            public void keyPressed(KeyEvent e)
            {
            }

            @Override
            public void keyReleased(KeyEvent e)
            {

                //TODO Get rid of sound when backspace reaches the left side of the text area.

                if (e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_DOWN && e.getKeyCode() != KeyEvent.VK_ENTER &&
                    e.getKeyCode() != KeyEvent.VK_RIGHT && e.getKeyCode() != KeyEvent.VK_LEFT)
                {
                    List<String> res = BookmarksUtil.getSuggestedTags(itemNames, st[0], 10);
                    res.add(0, st[0]);
                    search.setModel(new DefaultComboBoxModel(res.toArray()));
                    search.setPopupVisible(true);
                    System.out.println("Key released");
                    if (st[0].isEmpty())
                    {
                        currentlyShowingItemsList = getItemsList();
                        refresh();
                    }
                }
                System.out.println("Element size " + getCurrentlyShowingItemsList().isEmpty());
                if (search.getModel().getSize() == 1)
                {
                    search.setPopupVisible(false);
                }
            }
        });

        scroll = new JScrollPane();

        pan = new JPanel();
        pan.setBorder(BorderFactory.createLineBorder(Color.black));
        pan.setLayout(new ModifiedFlowLayout(1, 10, 10));
        //setting any dimension causes the layout manager to layout the components immediatly, rather than after the size changes.
        pan.setPreferredSize(new Dimension(150, 100));

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

    private List<ListableItem> getSelectedItems()
    {
        String selected = search.getSelectedItem().toString();
        System.out.println("selected method " + selected);
        search.setPopupVisible(false);
        search.getEditor().setItem(selected);
        List<ListableItem> l = itemsSearchMap.get(selected);
        if (l == null)
        {
            l = new ArrayList<>();
        }
        currentlyShowingItemsList = l;
        refresh();
        return l;
    }

    public void refresh()
    {
        pan.removeAll();
        itemNames.clear();

        for (ListableItem b : getCurrentlyShowingItemsList())
        {
            ListableItemPanel bp = new ListableItemPanel(b);
            bp.setAlignmentX(Component.CENTER_ALIGNMENT);
            pan.add(bp);
            itemNames.add(b.getName());
        }

        itemsSearchMap = BookmarksUtil.getListableItemsTextStrings(getItemsList());
        this.scroll.updateUI();
    }

    public List<ListableItem> getItemsList()
    {
        return itemsList;
    }

    public List<ListableItem> getCurrentlyShowingItemsList()
    {
        return currentlyShowingItemsList;
    }

    public void setItemsList(List<ListableItem> itemsList)
    {
        this.itemsList = itemsList;
        currentlyShowingItemsList = itemsList;
        refresh();
    }
}

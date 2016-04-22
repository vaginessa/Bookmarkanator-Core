package com.bookmarkanator.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;

import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.interfaces.*;

public class ListableItemsPanel extends JPanel {
    //TODO Add a way to show search panel or not.
    private JScrollPane scroll;
    private List<ListableItem> itemsList;
    private List<ListableItem> currentlyShowingItemsList;
    private JPanel pan;
    private JComboBox search;
    private Set<String> itemNames;
    private Map<String, List<ListableItem>> itemsSearchMap;

    public ListableItemsPanel() {
        super();
        this.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();

        currentlyShowingItemsList = new ArrayList<>();
        itemsList = new ArrayList<>();
        itemNames = new HashSet<>();
        itemsSearchMap = new HashMap<>();

        search = new JComboBox();
        search.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                System.out.println("State change "+e.paramString());
                System.out.println("clicked ");
                getSelectedItems();
            }
        });
        final String[] st = new String[1];//final container for inputted text

        //The following lines were inspired by stackoverflow.com
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
        //end stackoverflow.com

        search.setEditable(true);
        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Action!");
                getSelectedItems();
            }
        });
        search.getEditor().getEditorComponent().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {

                //TODO Get rid of sound when backspace reaches the left side of the text area.
                //TODO find out why key events are triggering action performed and item state changed events.
                if (e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_DOWN && e.getKeyCode() != KeyEvent.VK_ENTER &&
                        e.getKeyCode() != KeyEvent.VK_RIGHT && e.getKeyCode() != KeyEvent.VK_LEFT) {
                    List<String> res = BookmarksUtil.getSuggestedTags(itemNames, st[0], 10);
                    res.add(0, st[0]);
                    search.setModel(new DefaultComboBoxModel(res.toArray()));
                    search.setPopupVisible(true);
                    System.out.println("Key released");
                    if (st[0].isEmpty()) {
                        currentlyShowingItemsList = getItemsList();
                        refresh();
                    }
                }
                System.out.println("Element size " + getCurrentlyShowingItemsList().isEmpty());
                if (search.getModel().getSize() == 1) {
                    search.setPopupVisible(false);
                }
            }
        });

        scroll = new JScrollPane();
//        scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
//            @Override
//            public void adjustmentValueChanged(AdjustmentEvent e) {
//                System.out.println("Adjusting "+e.paramString()+" "+e.getAdjustmentType());
//            }
//        });

//        scroll.getViewport().addChangeListener(new ChangeListener() {
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                //TODO when a component is added (or set visible) the container is relayed out. When it is relayed out
//                //it calls this listener and causes a feedback loop. Find a way to stop the feedback loop.
//
//                //disabling until I can figure out the feedback issue
//                if (!((JTextField) search.getEditor().getEditorComponent()).getText().isEmpty())
//                {//always show jcombobox if something is selected
//                    search.setVisible(true);
//                } else {//determine if showing search jcombobox.
//
//                    if (scroll.getVerticalScrollBar().isVisible()) {
//                        search.setVisible(true);
//                        searchBoxJustAdded = true;
//                    } else {
//                        search.setVisible(false);
//                        searchBoxJustAdded = false;
//                    }
//                    scroll.updateUI();
//                }
//            }
//        });

        pan = new JPanel();
        pan.setBorder(BorderFactory.createLineBorder(Color.black));
        pan.setLayout(new ModifiedFlowLayout(1, 10, 10));
        //setting any dimension causes the layout manager to layout the components immediately, rather than after the size changes.


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

    private List<ListableItem> getSelectedItems() {
        String selected = search.getSelectedItem().toString();
        System.out.println("selected method " + selected);
        search.setPopupVisible(false);
        search.getEditor().setItem(selected);
        List<ListableItem> l = itemsSearchMap.get(selected);
        if (l == null) {
            l = new ArrayList<>();
        }
        currentlyShowingItemsList = l;
        refresh();
        return l;
    }

    public void refresh() {
        pan.removeAll();
        itemNames.clear();

        for (ListableItem b : getCurrentlyShowingItemsList()) {
            ListableItemPanel bp = new ListableItemPanel(b);
            bp.setAlignmentX(Component.CENTER_ALIGNMENT);
            pan.add(bp);
            itemNames.add(b.getName());
        }

        itemsSearchMap = BookmarksUtil.getListableItemsTextStrings(getItemsList());
        this.scroll.updateUI();

    }

    public List<ListableItem> getItemsList() {
        return itemsList;
    }

    public List<ListableItem> getCurrentlyShowingItemsList() {
        return currentlyShowingItemsList;
    }

    public void setItemsList(List<ListableItem> itemsList) {
        this.itemsList = itemsList;
        currentlyShowingItemsList = itemsList;
        refresh();
    }

    public Set<String> getItemNames() {
        return itemNames;
    }

    public void addItem(ListableItem listableItem)
    {
        List l = getItemsList();
        l.add(listableItem);
        setItemsList(l);

    }
}

package com.bookmarkanator.ui.panel.listpanel;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.interfaces.*;
import com.bookmarkanator.ui.layout.*;
import com.bookmarkanator.ui.panel.itempanel.*;

/**
 * This class represents a scrollable, searchable list of JPanels, that have text and a type.
 */
public class StringsPanel<E> extends JPanel {

    //TODO Found a bug in the labels search function. There were two labels: Java, and Java 8. When I searched j, or java, it only came up with Java
    //as the result, if I searched for 8 it would come up with Java 8. A minor bug but could cause issues later. -Update; this bug is caused by the case
    //of the text. It missed Java 8 because the j was capitalized, but the lowercase j list of results only contains Java not Java 8. Add in a feature of
    //the search suggestions results that if the results are to short ignore case.

    //TODO Arrange the results put in the scroll pane alphabetically.

    // ============================================================
    // Fields
    // ============================================================

    private JScrollPane scroll;
    private JPanel pan;
    private JComboBox search;

    private LinkedHashMap<String, E> labels;
    private LinkedHashMap<String, E> visibleLabels;
    private Map<String, Set<SubStringResults>> searchMap;//map used in getting tag suggestions.
    private boolean dirty;//reload search map if dirty

    private Observer observer;//will be added to each panel that is added to this container.
    private String type;
    private StringPanel displayPanel;

    // ============================================================
    // Constructors
    // ============================================================

    public StringsPanel(Observer observer, String type, StringPanel panelToUse) {
        super();
        this.observer = observer;
        this.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();
        visibleLabels = new LinkedHashMap<>();
        labels = new LinkedHashMap<>();

        search = new JComboBox();
        searchMap = new HashMap<>();
        dirty = false;

        this.type = type;
        displayPanel = panelToUse;
        search.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                System.out.println("clicked ");
                getSelectedItems();
//                observer.update(null, search);
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
                    System.out.println("Carrot update");
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
//                observer.update(null, search);
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
                //TODO find out why key events are triggering action performed and item state changed events on Linux Mint.

                //TODO Scenario: Add a couple of tags to the tag selection panel, then do a search for one (hit enter), then remove
                //that tag. The tags that were hidden by the search are still hidden. Pressing backspace will reveal them again. This is a
                //larger bug.
                if (e.getKeyCode() != KeyEvent.VK_UP && e.getKeyCode() != KeyEvent.VK_DOWN && e.getKeyCode() != KeyEvent.VK_ENTER &&
                        e.getKeyCode() != KeyEvent.VK_RIGHT && e.getKeyCode() != KeyEvent.VK_LEFT) {

                    checkRefillSearchMap();

                    List<String> res = BookmarksUtil.getSuggestedTags(searchMap, st[0], 10);

                    res.add(0, st[0]);
                    search.setModel(new DefaultComboBoxModel(res.toArray()));
                    search.setPopupVisible(true);
                    if (st[0].isEmpty()) {

                        visibleLabels.clear();

                        for (String s: getLabels().keySet())
                        {
                            visibleLabels.put(s, getLabels().get(s));
                        }
                        refresh();
                    }
                }

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
//        pan.setPreferredSize(new Dimension(150,800));

        //TODO find a way to set the preferred size to address the following issue:
        //setting any dimension causes the layout manager to layout the components immediately, rather than after the size changes.
        //when it adds a component it has to resize the container, when adding many components this looks bad.


        scroll.getViewport().add(pan);
        con.fill = GridBagConstraints.BOTH;
        con.weighty = .01;
        con.weightx = 1;
        con.gridx = 0;
        con.gridy = 0;

        super.add(search, con);
        con.weighty = 1;
        con.gridy = 2;
        super.add(scroll, con);
    }

    public StringsPanel()
    {
    }

    // ============================================================
    // Public Methods
    // ============================================================

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public JComboBox getSearch()
    {
        return search;
    }

    /**
     * Refresh is called to replace the panels that are displaying the text strings. This is called automatically by various methods internally.
     * The user should call this after adding all necessary strings to display so as to add components only once.
     */
    public void refresh()
    {
        pan.removeAll();

        for (String b : getVisibleLabels().keySet()) {
            E i = getVisibleLabels().get(b);
            if (i!=null)
            {
                StringPanel sp = displayPanel.getNew(i, getObserver(), getType()); pan.add(sp);
            }
        }
        //TODO maintain an internal list of string to jpanel mappings, and only add new ones that are not already in the list? If so how to handle
        //duplicate elements?
        this.scroll.updateUI();

        //Updating the search map using only the showing items.
        searchMap = BookmarksUtil.makeTagsList(getVisibleLabels().keySet());
    }

    public LinkedHashMap<String, E> getLabels() {
        return labels;
    }

    public LinkedHashMap<String, E> getVisibleLabels() {
        return visibleLabels;
    }

    public void setLabels(List<E> labels) {
        this.labels.clear();
        this.visibleLabels.clear();

        for (E e: labels)
        {
            this.labels.put(e.toString(), e);
            this.visibleLabels.put(e.toString(), e);
        }

        refresh();
    }

    public Observer getObserver() {
        return observer;
    }

    @Override
    public void remove(Component comp)
    {
        removeFromMe(comp);
        pan.remove(comp);
    }

    @Override
    public void remove(int index)
    {
        Component comp = this.getComponent(index);
        removeFromMe(comp);
        pan.remove(index);
    }

    @Override
    public void removeAll()
    {
        labels.clear();
        dirty = true;
        super.removeAll();
    }

    @Override
    public Component add(Component comp)
    {//adding to panel inside scroll pane instead of to this panel.
        return pan.add(addToMe(comp));
    }

    @Override
    public Component add(String name, Component comp)
    {
        return pan.add(name, addToMe(comp));
    }

    @Override
    public Component add(Component comp, int index)
    {
        return pan.add(addToMe(comp), index);
    }

    @Override
    public void add(Component comp, Object constraints)
    {
        pan.add(addToMe(comp), constraints);
    }

    @Override
    public void add(Component comp, Object constraints, int index)
    {
        pan.add(addToMe(comp), constraints, index);
    }


    // ============================================================
    // Private Methods
    // ============================================================
    private Component addToMe(Component component)
    {
        if (component instanceof StringPanelInterface)
        {//add the text of this component being added
            StringPanel sp = (StringPanel)component;
            E element = (E)sp.getItem();
            labels.put(element.toString(),element);
            visibleLabels.put(element.toString(),element);
            dirty = true;
            return displayPanel.getNew(sp.getText(), getObserver(), getType());
        }
        return component;//pass component through if not the right class type.
    }

    private void removeFromMe(Component component)
    {
        if (component instanceof StringPanelInterface)
        {//remove the label of the supplied jpanel.
            StringPanelInterface sp = (StringPanelInterface)component;
            labels.remove((E)sp.getItem().toString());
            visibleLabels.remove((E)sp.getItem().toString());
            dirty = true;
        }
    }

    private void checkRefillSearchMap()
    {
        if (dirty)
        {
            searchMap = BookmarksUtil.makeTagsList(visibleLabels.keySet());
            dirty = false;
            System.out.println("Redoing list!!!!!! ");
        }
    }

    private List<E> getSelectedItems() {

        //get suggested tags for the currently selected search item.
        String selected = search.getSelectedItem().toString();
        System.out.println("selected method " + selected);
        search.setPopupVisible(false);
        search.getEditor().setItem(selected);
        List<String> l = BookmarksUtil.getSuggestedTags(searchMap,selected, 10);

        //add only the tags that match those search items to the visiblelabels map.
        visibleLabels.clear();

        for (String s: l)
        {//adding all visible items that match the search results
            E tmpE = labels.get(s);

            if (tmpE!=null)
            {
                visibleLabels.put(tmpE.toString(), tmpE);
            }
        }

        //change the displayed panels to reflect the visible components.
        refresh();

        //Make list to return.
        List<E> li = new ArrayList<>();

        for (String e:visibleLabels.keySet())
        {
            li.add(visibleLabels.get(e));
        }
        return li;
    }

}

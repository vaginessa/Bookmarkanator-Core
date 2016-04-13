package com.bookmarkanator.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BookmarksPanel extends JPanel {
    private GridBagConstraints con;
    private JScrollPane scroll;

    public BookmarksPanel(Dimension dimension) {
        super();
//        this.setLayout(new GridBagLayout());
//        con = new GridBagConstraints();
//        con.fill = GridBagConstraints.BOTH;
        scroll = new JScrollPane();
//        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(dimension);
        JPanel pan = new JPanel();
//        GridLayout gl = new GridLayout();
//        gl.setHgap(20);
//        gl.setVgap(10);
//        gl.setColumns(4);
//        gl.setRows(5);
//
//        pan.setLayout(gl);
pan.setBackground(Color.red);
//        this.add(pan, BorderLayout.CENTER);
        pan.setPreferredSize(dimension);

        for (int c=0;c<10;c++)
        {
            JPanel tmpPan = new JPanel();
            JLabel label = new JLabel("");
            tmpPan.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    System.out.println("Mouse clicked!");
                    if (label.getText().equals("clicked"))
                    {
                        label.setText("Un clicked");
                    }
                    else
                    {
                        label.setText("clicked");
                    }

                }
            });

            tmpPan.setBackground(Color.getHSBColor(c*10, c*20, c*30));
            tmpPan.setPreferredSize(new Dimension(80, 30));
            tmpPan.add(label);
            pan.add(tmpPan);
        }

        for (int c=0;c<10;c++)
        {
            JPanel tmpPan = new JPanel();
            JButton button = new JButton("          ");
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    System.out.println("Mouse clicked!");
                    if (button.getText().equals("clicked"))
                    {
                        button.setText("Un clicked");
                    }
                    else
                    {
                        button.setText("clicked");
                    }

                }
            });

            tmpPan.setBackground(Color.getHSBColor(c*10, c*20, c*30));
            tmpPan.setPreferredSize(new Dimension(80, 30));
            tmpPan.add(button);
            pan.add(tmpPan);
        }

scroll.add(pan);
        scroll.getViewport().add(pan);
//        scroll.add(new JLabel("hello"));
//        pan.setBackground(Color.red);
//        pan.add(new JLabel("bookmarks!"));
//        pan.setPreferredSize(dimension);
//        scroll.add(pan);
//        scroll.setBackground(Color.blue);
//        this.add(scroll,BorderLayout.CENTER);
//        this.setPreferredSize(dimension);
        this.add(scroll);
        this.setBackground(Color.yellow);
    }
}

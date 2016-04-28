package com.bookmarkanator.interfaces;


import javax.swing.*;
import java.util.Observer;

public interface StringItemPanel
{
    JPanel getDisplayPanel(String string, Observer observer);
}

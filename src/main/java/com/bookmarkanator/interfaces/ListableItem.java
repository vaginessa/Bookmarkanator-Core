package com.bookmarkanator.interfaces;

import java.util.*;
import javax.swing.*;

public interface ListableItem
{
    void setLastAccessedDate(Date lastAccessedDate);
    void execute()
        throws Exception;
    String getText();
    String getName();
    String getTypeString();
    Icon getIcon();
}

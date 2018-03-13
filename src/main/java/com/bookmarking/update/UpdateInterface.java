package com.bookmarking.update;

import java.util.*;

public interface UpdateInterface
{
    List<UpdateActionInterface> checkForUpdates();
    List<UpdateActionInterface> doUpdates(List<UpdateActionInterface> updateActionInterface);
}

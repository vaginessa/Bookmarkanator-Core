package com.bookmarking.ui;

import java.util.*;
import com.bookmarking.update.config.*;

public interface UpdateUIInterface
{
    // Gives the ability for the UI to modify the updates list prior to updating.
    Set<UpdateConfigEntry> approveUpdates(Set<UpdateConfigEntry> updates);

    void setUpdateProgress(String updateItemKey, Double progress);

    void setUpdateStatus(String updateItemKey, String status);
}

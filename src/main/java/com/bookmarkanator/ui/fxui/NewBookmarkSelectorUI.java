package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.ui.fxui.bookmarks.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.scene.control.*;

public class NewBookmarkSelectorUI implements NewBookmarkSelectionInterface
{
    Set<AbstractUIBookmark> types;
    AbstractUIBookmark selected;

    @Override
    public void setTypes(Set<AbstractUIBookmark> types)
    {
        this.types = types;
    }

    @Override
    public Set<AbstractUIBookmark> getTypes()
    {
        return this.types;
    }

    @Override
    public AbstractUIBookmark getSelectedBookmarkType()
    {
        return selected;
    }

    @Override
    public void show()
    {

        List<String> dialogData = new ArrayList<>();
        for (AbstractUIBookmark bkUI: types)
        {
            if (bkUI.getBookmark()==null)
            {
                MLog.warn("Null bookmark encountered");
                continue;
            }
            dialogData.add(bkUI.getBookmark().getTypeName());
        }


        Dialog dialog = new ChoiceDialog(dialogData.get(0), dialogData);
        dialog.setTitle("This is a title");
        dialog.setHeaderText("Select your choice");

        Optional<String> result = dialog.showAndWait();
        String selected = "cancelled.";

        if (result.isPresent()) {
            selected = result.get();
        }
        System.out.println(result);
    }

    @Override
    public void hide()
    {
            //Do nothing here because we are using a modal dialog that closes when we are done with it.
    }
}

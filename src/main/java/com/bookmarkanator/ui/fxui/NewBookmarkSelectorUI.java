package com.bookmarkanator.ui.fxui;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.ui.fxui.bookmarks.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.scene.control.*;

public class NewBookmarkSelectorUI implements NewBookmarkSelectionInterface
{
    Set<AbstractUIBookmark> types;
    Map<String, AbstractUIBookmark> typesMap;//<bookmark type string, the ui bookmark it will use>
    AbstractUIBookmark selected;
    UIControllerInterface controller;
    private boolean editMode;

    public NewBookmarkSelectorUI(UIControllerInterface controller)
    {
        this.controller = controller;
        typesMap = new HashMap<>();
    }

    @Override
    public void setTypes(Set<AbstractUIBookmark> types)
    {
        this.types = types;

        for (AbstractUIBookmark abs : types)
        {
            typesMap.put(abs.getBookmark().getTypeName(), abs);
        }
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
        throws Exception
    {

        List<String> dialogData = new ArrayList<>();
        for (AbstractUIBookmark bkUI : types)
        {
            if (bkUI.getBookmark() == null)
            {
                MLog.warn("Null bookmark encountered");
                continue;
            }
            dialogData.add(bkUI.getBookmark().getTypeName());
        }

        Dialog dialog = new ChoiceDialog(dialogData.get(0), dialogData);
        dialog.setTitle("New Bookmark Window");
        dialog.setHeaderText("Please select a bookmark type to create");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent())
        {
            String item = result.get();
            MLog.fine("Selected " + item);

            AbstractUIBookmark bk = typesMap.get(item);
            if (bk != null)
            {
                selected = bk;
                AbstractBookmark abBK = bk.newBookmarkView();
                if (abBK != null)
                {
                    Bootstrap.context().addBookmark(abBK);
                    controller.updateUI();
                }
            }
            else
            {
                MLog.warn("Value selected has no map entry. Selected item: " + item);
            }
        }
//        System.out.println(result);
    }

    @Override
    public void hide()
    {
        //Do nothing here because we are using a modal dialog that closes when we are done with it.
    }

    @Override
    public boolean getEditMode()
    {
        return editMode;
    }

    @Override
    public void setEditMode(boolean editMode)
    {
        this.editMode = editMode;
    }

    @Override
    public UIControllerInterface getController()
    {
        return this.controller;
    }

    @Override
    public void setController(UIControllerInterface controller)
    {
        this.controller = controller;
    }
}

package com.bookmarkanator.ui.fxui.bookmarks;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.ui.interfaces.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;

public abstract class AbstractUIBookmark
{
    private AbstractBookmark abstractBookmark;
    protected static Map<UUID, Stage> openStagesMap;//<Bookmark id, Stage that is showing it>
    protected UIControllerInterface controller;

    public AbstractUIBookmark()
    {
        if (openStagesMap == null)
        {
            openStagesMap = new HashMap<>();
        }
    }

    public void setBookmark(AbstractBookmark abstractBookmark)
    {
        this.abstractBookmark = abstractBookmark;
    }

    public AbstractBookmark getBookmark()
    {
        return this.abstractBookmark;
    }

    // ============================================================
    //
    // Abstract Methods
    // ============================================================

    public abstract Image getTypeIcon();

    public abstract void show()
        throws Exception;

    public abstract AbstractBookmark edit()
        throws Exception;

    public abstract void delete()
        throws Exception;

    public abstract AbstractBookmark newBookmarkView()
        throws Exception;

    protected void addDeleteButton(Dialog dialog, ButtonType buttonType)
    {
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, buttonType, ButtonType.CANCEL);
        final Button tmp = (Button) dialog.getDialogPane().lookupButton(buttonType);
        tmp.setStyle("-fx-background-color:red");
        tmp.addEventFilter(ActionEvent.ACTION, event ->
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("This bookmark will be deleted.");
            alert.setContentText("Continue?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() != ButtonType.OK)
            {
                event.consume();
            }
        });
    }

    public UIControllerInterface getController()
    {
        return controller;
    }

    public void setController(UIControllerInterface controller)
    {
        this.controller = controller;
    }

    /**
     * Using class name is the name that this bookmark requires
     *
     * @return The name of the bookmark that this UI element needs to use.
     */
    public abstract String getRequiredBookmarkClassName();
}

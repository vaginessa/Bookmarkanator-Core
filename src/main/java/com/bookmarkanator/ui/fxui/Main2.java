package com.bookmarkanator.ui.fxui;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class Main2 extends Application
{
    @Override
    public void start(Stage stage)
        throws Exception
    {
        HBox root = (HBox) FXMLLoader.load(this.getClass().getResource("/com.bookmarkanator.ui.fxui/testUI.fxml"));

        Scene scene = new Scene(root, 300, 275);

        stage.setTitle("FXML Welcome");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        Application.launch(args);
    }
}

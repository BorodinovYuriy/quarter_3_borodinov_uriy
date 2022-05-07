package com.example.csbd;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ModalWindowDeleteUser {

    private static boolean decision = false;

    public static boolean isDecision() {
        return decision;
    }

    public static void newWindowDeleteUser (String title) {
        Stage window = new Stage();
        //закрывает доступ к другим окнам пока это открыто
        window.initModality(Modality.APPLICATION_MODAL);

        Pane pane = new Pane();

        Button deleteBtn = new Button("Delete");
        deleteBtn.setMinSize(150,10);

        Button undoBtn = new Button("Undo");
        undoBtn.setMinSize(150,10);

        deleteBtn.setOnAction(event -> {
            decision = true;
            window.close();
        });

        undoBtn.setOnAction(event -> {
            decision = false;
            window.close();
        });

        HBox box = new HBox(deleteBtn, undoBtn);
        box.setSpacing(50);

        //добавление списка нод
        pane.getChildren().add(box);

        Scene scene = new Scene(pane,350,25);

        window.setScene(scene);
        window.setTitle(title);
        window.setResizable(false);
        //так же отображает с блокировкой
        window.showAndWait();




    }


}

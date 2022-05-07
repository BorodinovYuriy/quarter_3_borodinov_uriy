package com.example.csbd;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ModalWindowChangeLogPassNick {

    private static String modifiedUser;

    public static String getModifiedUser() {
        return modifiedUser;
    }

    public static void newWindowDeleteUser (String title) {
        Stage window = new Stage();
        //закрывает доступ к другим окнам пока это открыто
        window.initModality(Modality.APPLICATION_MODAL);

        Pane pane = new Pane();

        Label modifiedUserData = new Label("Меняем данные пользователя!");

        TextField modifiedLogin = new TextField();
        modifiedLogin.setMaxWidth(200);
        modifiedLogin.setPromptText("new Login");
        modifiedLogin.setFocusTraversable(false);


        TextField modifiedPassword = new TextField();
        modifiedPassword.setMaxWidth(200);
        modifiedPassword.setPromptText("new Password");
        modifiedPassword.setFocusTraversable(false);

        TextField modifiedNickname = new TextField();
        modifiedNickname.setMaxWidth(200);
        modifiedNickname.setPromptText("new Nickname");
        modifiedNickname.setFocusTraversable(false);

        Button confirmBtn = new Button("Confirm");
        confirmBtn.setMinSize(150,10);
        confirmBtn.setFocusTraversable(false);

        Button undoBtn = new Button("Undo");
        undoBtn.setMinSize(150,10);

        confirmBtn.setOnAction(event -> {

            if(modifiedLogin.getText().isBlank()
                    | modifiedPassword.getText().isBlank()
                    | modifiedNickname.getText().isBlank()){
                System.out.println("Поля не заполнены ->ModalWindowChangeLogPassNick");
            }else{
                modifiedUser =("/modifiedUser "
                        +modifiedLogin.getText().trim()+" "
                        +modifiedPassword.getText().trim()+" "
                        +modifiedNickname.getText().trim());
                /*System.out.println("Modified user: "+modifiedUser);*/
                window.close();
            }
        });

        undoBtn.setOnAction(event -> {
            window.close();
        });

        HBox buttonBox = new HBox(confirmBtn, undoBtn);

        HBox.setMargin(confirmBtn, new Insets(10.0));
        HBox.setMargin(undoBtn, new Insets(10.0));

        VBox dataBox = new VBox(modifiedUserData,modifiedLogin,modifiedPassword,modifiedNickname,buttonBox);
        //добавление списка нод

        pane.getChildren().add(dataBox);

        Scene scene = new Scene(pane,342,140);

        window.setScene(scene);
        window.setTitle(title);
        window.setResizable(false);
        //так же отображает с блокировкой
        window.showAndWait();




    }


}


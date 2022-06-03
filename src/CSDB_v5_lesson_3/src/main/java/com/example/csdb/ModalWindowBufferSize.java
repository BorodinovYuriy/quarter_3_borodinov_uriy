package com.example.csdb;

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
public class ModalWindowBufferSize {

    private static int bSize;

    public static void bufferChanger(String title) {
        Stage window = new Stage();
        //закрывает доступ к другим окнам пока это открыто
        window.initModality(Modality.APPLICATION_MODAL);

        Pane pane = new Pane();

        Label label = new Label("Введите буфер истории: 10-100 (строк).");

        TextField tfBuffIn = new TextField();
        tfBuffIn.setMaxWidth(200);
        tfBuffIn.setPromptText("от 10 до 100");
        tfBuffIn.setFocusTraversable(false);

        Button confirmBtn = new Button("Confirm");
        confirmBtn.setMinSize(150,10);
        confirmBtn.setFocusTraversable(false);

        Button undoBtn = new Button("Undo");
        undoBtn.setMinSize(150,10);

        confirmBtn.setOnAction(event -> {
            //задаём значение кнопки подтверждения
            int num = 0;
            try {
                num = Integer.parseInt(tfBuffIn.getText().trim());
            }catch (NumberFormatException e){
                System.out.println("некорректный ввод");
            }
            if(num >= 10 && num <= 100){
                bSize = num;
                System.out.println("значение для буфера MWBS - "+bSize);

                window.close();
            }else{
                System.out.println("некорректный ввод размера массива истории");
                tfBuffIn.clear();
                undoBtn.requestFocus();
            }
        });

        undoBtn.setOnAction(event -> {
            window.close();
        });

        HBox buttonBox = new HBox(confirmBtn, undoBtn);

        HBox.setMargin(confirmBtn, new Insets(10.0));
        HBox.setMargin(undoBtn, new Insets(10.0));

        Label warning = new Label("       При изменении размера - история буфера удалится!!!");

        VBox dataBox = new VBox(tfBuffIn,buttonBox,warning);
        //добавление списка нод

        pane.getChildren().add(dataBox);

        Scene scene = new Scene(pane,342,140);

        window.setScene(scene);
        window.setTitle(title);
        window.setResizable(false);
        //так же отображает с блокировкой
        window.showAndWait();

    }

    public static String getBufferSize(){
        return String.valueOf(bSize);
    }


}
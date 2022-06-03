package com.example.csbd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ClientApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("client-view.fxml"));
        //добавление аналога SetDefaultCloseOperation
        stage.setOnCloseRequest(e -> ClientController.onStageClose());

        Scene scene = new Scene(fxmlLoader.load(), 400, 400);
        stage.setTitle("CSBD");
        stage.setScene(scene);
        stage.show();

    }


    public static void main(String[] args) {
        launch();
    }
}
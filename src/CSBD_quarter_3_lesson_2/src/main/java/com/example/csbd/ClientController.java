package com.example.csbd;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientController {

    @FXML
    VBox allApp;
    @FXML
    TextArea textArea;
    @FXML
    TextField textField;
    @FXML
    Button button;
    @FXML
    VBox topVBox;
    @FXML
    HBox botHBox;
    @FXML
    TextField logTF;
    @FXML
    PasswordField passPF;
    @FXML
    Button authB;
    @FXML
    ListView listView;
    @FXML
            Button addNewUser;
    @FXML
            VBox addBox;
    @FXML
            TextField newLog;
    @FXML
            TextField newPass;
    @FXML
            TextField newNick;
    @FXML
            Button addButton;
    @FXML
            Button undoButton;
    @FXML
    MenuBar menu;
    @FXML
    MenuItem closeConnection;
    @FXML
    MenuItem deleteUser;
    @FXML
    MenuItem modifiedUser;


    Socket socket;
    DataInputStream in;
    static DataOutputStream out;

    String IP_ADDRESS = "localhost";
    int PORT = 8189;

    @FXML
    //отображение панельки для добавления нового пользователя
    public void addUser(){
        addBox.setManaged(true);
        addBox.setVisible(true);
        topVBox.setManaged(false);
        topVBox.setVisible(false);
        newLog.requestFocus();
    }
    //возврат к панельке ввода логина/пароля
    public void setDeactive(){
        addBox.setManaged(false);
        addBox.setVisible(false);
        topVBox.setManaged(true);
        topVBox.setVisible(true);
        logTF.clear();
        passPF.clear();
        newLog.clear();
        newPass.clear();
        newNick.clear();
        logTF.requestFocus();
    }
    public void addUserFinish(){
        if(newLog.getText().isEmpty() || newPass.getText().isEmpty() || newNick.getText().isEmpty()){
            textArea.appendText("Заполните пустые поля\n");
        }else {
            if(socket == null || socket.isClosed()){
                connect();
            }
            try {
                out.writeUTF("/new "+ newLog.getText() +" "+ newPass.getText() +" "+ newNick.getText());

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Не удалось отослать запрос на добавление серверу -> ClientController");
            }



            addBox.setManaged(false);
            addBox.setVisible(false);
            topVBox.setManaged(true);
            topVBox.setVisible(true);
            try {
                out.writeUTF("/end");
            } catch (IOException e) {
                System.out.println("не получилось отослать /end (addUserFinish)");
            }
        }
    }
    public void keyListener(KeyEvent keyEvent) {
        if (keyEvent.getCode().getCode() == 10) {
            sendMessage();
        }
    }
    public void setActive(boolean isAuthorized){
        if(!isAuthorized){
            //добавляет или убирает панель вообще в сочитании с setVisible
            topVBox.setVisible(true);
            topVBox.setManaged(true);
            botHBox.setVisible(false);
            botHBox.setManaged(false);
        }else{
            topVBox.setVisible(false);
            topVBox.setManaged(false);
            botHBox.setVisible(true);
            botHBox.setManaged(true);
        }
    }
    public void sendMessage() {
        try {
            //перехват служебных сообщений до отправки на сервер
            if(textField.getText().startsWith("/deleteThisUser")){
                textField.clear();
                deleteThisUser();
            }else if(textField.getText().startsWith("/modifiedUser")) {
                textField.clear();
                modifiedUser();
            }else {
                //отправка сообщения на сервер
                out.writeUTF(textField.getText());
                textField.clear();
                textField.requestFocus();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Send message error!->sendMessage()-ClientController)");
        }

    }
    public void auth(){
        //проверка пустого поля ввода
        if(logTF.getText().isBlank() || passPF.getText().isBlank()){
            textArea.appendText("Input Login/Password\n");
            return;
        }
        //попытка соединения
        if (socket == null || socket.isClosed()){
            connect();
        }
        try {
            //отправка логина/пароля на аутентификацию серверу
            out.writeUTF("/auth " + logTF.getText() + " " + passPF.getText());
            logTF.clear();
            passPF.clear();
        } catch (IOException | RuntimeException e) {
            System.out.println("RuntimeExc/IOExc -> auth() -ClientController");
            System.out.println("Сервер offline");
        }
    }
    public void disconnect(){
        try {
            out.writeUTF("/end");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException ->disconnect() -ClientController");
        }
    }
    public static void onStageClose() {
        //Действия при закрытии окна
        System.out.println("Ура! расходимся! =)");
        try {
            out.writeUTF("/end");
//            Platform.exit();
//            System.exit(0);
        }catch (IOException e){
            System.out.println("IO Exception onStageClose() on exit app!");
        }catch (NullPointerException e){
            System.out.println("Невозможно отослать серверу сообщение '/end'");
        }
    }
    public boolean isDeleteUser(){
        ModalWindowDeleteUser.newWindowDeleteUser("Вы собираетесь удалить пользователя!");
        System.out.println("Решение по удалению "+ModalWindowDeleteUser.isDecision());
        return ModalWindowDeleteUser.isDecision();
    }
    public void deleteThisUser(){
        boolean tf = isDeleteUser();
        //чистим textField, а то если там написать "/deleteThisUser"
        //и удалиться через меню -> сыпется клиент.
        textField.clear();
        if(tf){
            try {
                out.writeUTF("/deleteThisUser");

                menu.setDisable(true);
                setActive(false);
                textArea.appendText("Disconnect"+"\n");
                System.out.println("Disconnect"+"\n");
                try {
                    socket.close();
                    in.close();
                    out.close();
                }catch (IOException e){
                    System.out.println("IOException -> deleteThisUser() -ClientController" );
                }


            } catch (IOException e) {
                System.out.println("IOException -> deleteThisUser() -ClientHandler");
            }
        }
    }
    public void clearTextArea(){
        textArea.clear();
    }
    public void modifiedUser(){
        ModalWindowChangeLogPassNick.newWindowDeleteUser("Модификация данных пользователя");
        try {
            out.writeUTF(ModalWindowChangeLogPassNick.getModifiedUser());
        } catch (IOException e) {
            System.out.println("IOException ->modifiedUser() -ClientController");
        } catch (NullPointerException e){
            System.out.println("break!-> modifiedUser() -ClientController");
        }

    }
    public void saveToFile(ActionEvent event){
        //сохраняет в корень проекта в кодировке windows -1251,
        //сам проект UTF-8 -> иначе в txt будут кроказябры
        //Хорошо бы узнать как с кодировкой воевать...
        File myFile = new File("chatHistory.txt");
        try{
            PrintWriter writer =
                    new PrintWriter(new FileWriter(myFile,true));
            String tmp = textArea.getText();
            writer.println(tmp + "__________________");
            writer.flush();
            writer.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public void connect() {
        try {
            socket = new Socket(IP_ADDRESS, PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(new Thread(() -> {
                try {
                    //авторизация клиента!!!
                    while (true) {
                        to: try {
                            String str = in.readUTF();
                            //регистрация нового пользователя не пройдена
                            if(str.startsWith("/regoff")){
                                textArea.appendText("Логин/Никнейм занят!\n");
                                break to;
                            }
                            //регистрация нового пользователя пройдена
                            if(str.startsWith("/regok")){
                                textArea.appendText("Регистрация прошла успешно!\n");
                                setActive(false);
                                break to;
                            }
                            //авторизация пройдена
                            if (str.startsWith("/authok")) {

                                System.out.println("!/authok! " + str);//!!!
                                setActive(true);
                                menu.setDisable(false);
                                textArea.appendText("Online\n");
                                break;
                                //авторизация не пройдена
                            }else{
                                textArea.appendText("Wrong log/pass!" + "\n");
                                System.out.println("Некорректный ввод пароля");
                            }
                        }catch (SocketException e){
                            System.out.println("SocketException connect() -> run() -ClientController");
                        }
                    }
                    //Служебная часть
                    while (true) {
                        try{
                            String str = in.readUTF();

                            if(str.startsWith("/")){
                                if(str.startsWith("/show")){
                                    String[] nicknames = str.split(" ");

                                    Platform.runLater (() -> {
                                        listView.getItems().clear();
                                        for (int i = 1; i < nicknames.length; i++) {
                                            listView.getItems().add(nicknames[i]);
                                        }
                                    });
                                }
                                //обработка /end или печать в textArea
                                if(str.equals("/end")){
                                    //закрываем доступ к элементам
                                    menu.setDisable(true);
                                    setActive(false);
                                    textArea.appendText("Disconnect"+"\n");
                                    System.out.println("Disconnect"+"\n");
                                    break;
                                }
                            } else {
                                //приём и печать str в textArea
                                textArea.appendText(str + "\n");
                            }

                        } catch (SocketException e){
                            //Действия при потере клиентом сервера
                            System.out.println("Сервер не отвечает (/-> run() вывод текста ClientController)");
                            textArea.appendText("Сервер не отвечает, попробуйте перезайти"+"\n");
                            //закрываем доступ к элементам
                            menu.setDisable(true);
                            setActive(false);
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("IOException -> connect() -ClientController");
                } finally {
                    try {
                        socket.close();
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        System.out.println("IOException -> connect() <finally> - ClientController");
                    }
                }
            })).start();
        }catch (IOException | RuntimeException e) {
            //при попытке соединится с offline сервером
            System.out.println("<finally> server is offline -> connect()- ClientController");
            textArea.appendText("Сервер не найден(offline)"+"\n");
        }
    }



}
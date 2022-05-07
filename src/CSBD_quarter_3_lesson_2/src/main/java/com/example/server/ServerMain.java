package com.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class ServerMain {

    private Vector<ClientHandler> clientHandlers;

    public boolean isNickFree(String nick){
        if(clientHandlers.isEmpty())return true;
        for (ClientHandler client:
                clientHandlers) {
            if(client.getNickname().equals(nick)){
                return false;
            }
        }
        return true;
    }
    public void sendOnlineUsers(){
//         * Воспользовались стримом:
//         * Взяли наш вектор, получили поток/стрим элементов
//         * и по одому методом МАП обьекта клиентхендлера получили ник
//         * и сложили в список стрингов....
        StringBuilder sb = new StringBuilder();
        List<String> list = clientHandlers.stream().map(ClientHandler::getNickname).toList();//переделываем

        //Отправляем никнеймы клиентам
        for (String s:
                list) {
            sb.append(s);
            sb.append(" ");
        }

        sendToAll("/show " + sb.toString().trim());

    }
    public void subscribe(ClientHandler client) {
        clientHandlers.add(client);
    }
    public void unsubscribe(ClientHandler client) {
        sendToAll("unsubscribe " + client.getNickname() + " - is out!\n");
        clientHandlers.remove(client);
        sendOnlineUsers();
    }
    public void sendToAll(String msg){
        for (ClientHandler client:
                clientHandlers) {
            client.sendMsg(msg);
        }
    }
    public void sendToOnly(String str, String nick) {
        for (ClientHandler client :
                clientHandlers)
            if (nick.equals(client.getNickname())) {
                client.sendMsg(str);
            }
    }
    public void start() {
        ServerSocket server;
        Socket socket;

        clientHandlers = new Vector<>();

        try {
            //Соединение с БД
            AuthServer.connect();
            //Старт сервера
            server = new ServerSocket(8189); //localhost:8189 -- 0 - 65000
            System.out.println("Сервер запущен");

            //цикл без break!!!
            while (true){
                socket = server.accept();
                System.out.println("Попытка подключения клиента");
                new ClientHandler(socket, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sendToAll("/end");
        }
        AuthServer.disconnect();
    }

















}
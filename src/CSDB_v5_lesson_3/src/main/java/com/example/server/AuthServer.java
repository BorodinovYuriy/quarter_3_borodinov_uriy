package com.example.server;

import java.sql.*;
/**
 * Класс AuthServer переписан, теперь отправляет "безопасные" запросы к базе данных
 * */
public class AuthServer {
    //классы-драйверы для sql что бы могли
    // посылать запросы к базе в формате sql...
    //static - должны быть доступны во всём проекте
    private static Connection connection;
    private static Statement statement;

    public static void connect() {
        //находим класс драйвера
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:users.db");
            statement = connection.createStatement();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("SQL exception");
        }
    }
    public static void disconnect(){
        //закрыли всё
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("disconnect AuthServer exception");
        }
    }
    public static String getNickByLogPassSQL(String log, String pass){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement( "select nickname from users where login = ? and password = ?");
            preparedStatement.setString(1,log );
            preparedStatement.setString(2,pass );
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getString(1);
            }
        }catch (SQLException e) {
            System.out.println("getNickByLogPassSQL проблема");
            e.printStackTrace();
        }
        return null;

    }
    public static boolean setLoginPasswordNickname(String log, String pass, String nick){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("insert into users (login, password, nickname) values (?,?,?)");
            preparedStatement.setString(1,log  );
            preparedStatement.setString(2,pass );
            preparedStatement.setString(3,nick );
            preparedStatement.execute();
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL setLoginPasswordNickname() проблема");
            return false;
        }
    }
    public static void deleteUserFromBD(String nick){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE nickname = ?");
            preparedStatement.setString(1,nick  );
            preparedStatement.execute();
        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL deleteUserFromBD() проблема");
        }
    }
    public static void modifiedLogPassNick(String oldNickname, String modLog, String modPass, String modNick){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET login = ?, password = ?, nickname = ? WHERE nickname = ?");
            preparedStatement.setString(1,modLog  );
            preparedStatement.setString(2,modPass );
            preparedStatement.setString(3,modNick );
            preparedStatement.setString(4,oldNickname );
            preparedStatement.execute();
            System.out.println("SQL запрос изменения данных - выполнен");
        }catch (SQLException e) {
            System.out.println("SQL modifiedLogPassNick() -> запрос не выполнен!");
        }
    }
    public static boolean isUserFree(String log, String nick){
        boolean answer = false;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE login = ? OR nickname = ?");
            preparedStatement.setString(1,log  );
            preparedStatement.setString(2,nick  );
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                answer = true;
            }else return false;
            }catch (SQLException e) {
            System.out.println("isUserFree проблема");
            e.printStackTrace();
        }
        return answer;
    }

}

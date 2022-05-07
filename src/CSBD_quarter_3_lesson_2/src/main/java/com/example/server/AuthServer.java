package com.example.server;

import java.sql.*;

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
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("disconnect exception");
        }
    }
    public static String getNickByLogPassSQL(String log, String pass){
        String sql = String.format( "select nickname from users where login = '%s' and password = '%s'", log, pass);
        //просим statement выполнить запрос
        try {
            //resultSet - таблица с возвращаемым от sql результатом
            ResultSet resultSet = statement.executeQuery(sql);
            if(resultSet.next()){
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }
    public static boolean setLoginPasswordNickname(String log, String pass, String nick){
        String sql = String.format("insert into users (login, password, nickname) values ('%s', '%s', '%s')",log, pass, nick);
        try {
            statement.execute(sql);//выполни наш запрос execute(запрос без "возврата")
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL setLoginPasswordNickname() не фурычит");
            return false;
        }
    }
    public static void deleteUserFromBD(String nick){
        String sql = String.format("DELETE FROM users WHERE nickname = '%s'", nick);
        try {
            statement.execute(sql);//выполни наш запрос execute(запрос без "возврата")
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL deleteUserFromBD() не фурычит");
        }
    }

    public static void modifiedLogPassNick(String oldNickname, String modLog, String modPass, String modNick){
        /*System.out.println("--"+oldNickname+"--"+modLog+"--"+modPass+"--"+modNick+"--");*/
        String sql = String.format("UPDATE users SET login = '%s', password = '%s', nickname = '%s' WHERE nickname = '%s'", modLog, modPass, modNick, oldNickname);
        try {

            statement.execute(sql);//выполни наш запрос execute(запрос без "возврата")
            System.out.println("SQL запрос изменения данных - выполнен");
        } catch (SQLException e) {
            System.out.println("SQL modifiedLogPassNick() -> запрос не выполнен!");
        }
    }

    public static boolean isUserFree(String log, String nick){
        String sql = String.format( "SELECT * FROM users WHERE login = '%s' OR nickname = '%s'", log, nick);
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            if(!resultSet.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}

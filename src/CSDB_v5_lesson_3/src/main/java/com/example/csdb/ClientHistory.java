package com.example.csdb;
/**
 * В классе истории создаётся история с изменяемым буфером (запоминает от 10 до 100 строк).
 * При смене размера буфера (количество строк хранящихся в памяти) - создаётся новый массив, данные старого — удаляются.
 * Можно историю сохранять отдельно методом saveToFile(), в таком случае имя файла сохранения будет одно для всех клиентов.
 * Метод хранения истории в буфере работает следующим образом:
 * отправляет серверу сообщение с запросом, где получает связь с текущим пользователем и обратную сервисную команду:
 * пришлось реализовать такой вариант, что бы не было проблем с доступом к экземпляру класса ClientHistory, а так же
 * к его методам изменения размера буфера.
 * Можно команду изменения размера буфера прописать и в чате в формате (/bufferLength размер) - имеется проверка на корректность.
 * Сохраняется динамическая история на диск в виде класса с сериализацией, задаётся уникальное имя.
 *При удалении пользователя файл динамической истории так же удаляется...
 * */
import java.io.*;

public class ClientHistory implements Serializable {
    private String historyNick;

    private  String[] historyArr = new String[10];
    private  int bufferSize = historyArr.length;

    public  String[] getHistoryArr() {
        return historyArr;
    }
    public  void historyRecordToArr(String line){
        if(line != null){
            for (int i = historyArr.length - 1; i > 0; i--) {
                historyArr[i] = historyArr[i-1];
            }
            historyArr[0] = line;
        }

    }

    public void setHistoryNick(String historyNick) {
        this.historyNick = historyNick;
    }
    public String getHistoryNick() {
        return historyNick;
    }

    public void setBufferSize(int buffSize) {
        historyArr = new String[buffSize];
        System.out.println("Размер массива historyArr "+historyArr.length);
    }
}

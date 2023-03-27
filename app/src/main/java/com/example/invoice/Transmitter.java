package com.example.invoice;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Transmitter {


    public static final String ID_ADD_USER = "1";
    public static final String ID_REMOVE_USER = "2";
    public static final String ID_ADD_ORDER = "3";
    public static final String ID_REMOVE_ORDER = "4";
    public static final String ID_ADD_PURCHASE = "5";
    public static final String ID_REMOVE_PURCHASE = "6";
    public static final String ID_GET_USER_NAME = "7";
    public static final String ID_GET_USER_ID = "8";
    public static final String ID_GET_USER_LIST = "9";
    public static final String ID_GET_ORDERS_FOR_MONTH_BY_ID = "10";
    public static final String ID_GET_PURCHASE_FOR_MONTH_BY_ID = "11";
    public static final String ID_GET_MONTH = "12";
    public static final String ID_GET_ORDERS_FOR_MONTH = "13";
    public static final String ID_GET_PURCHASE_FOR_MONTH = "14";
    public static final String ID_GET_TOTAL = "15";
    public static final String ID_PRINT = "16";
    public static final String SEPARATOR = ":";


    public static String IP = "0.0.0.0";
    public static int PORT = 8080;


    public static String sendRequest(String request) {

        class SocketThread extends Thread {

            final String request;
            String result = "";

            public SocketThread(String request) {
                this.request = request;
            }

            @Override
            public void run() {
                try {
                    Socket socket = new Socket(IP, PORT);
                    InputStream inputStream = socket.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    OutputStream outputStream = socket.getOutputStream();
                    PrintWriter printWriter = new PrintWriter(outputStream, true);
                    printWriter.println(request);
                    result = bufferedReader.readLine();
                    result = result == null || result.equals("") ? "False" : result;
                    bufferedReader.close();
                    printWriter.close();
                    socket.close();
                } catch (IOException e) {
                    result = "False";
                    MainActivity.main.runOnUiThread(() -> Toast.makeText(MainActivity.main, "Server nicht erreichbar!", Toast.LENGTH_SHORT).show());
                }
            }
        }

        SocketThread socketThread = new SocketThread(request);
        socketThread.start();
        try {
            socketThread.join();
            return socketThread.result;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "False";
        }
    }

    public static String[] getUser() {
        String users = sendRequest(ID_GET_USER_LIST);
        return users.split(SEPARATOR);
    }

    public static String addUser(String name) {
        return sendRequest(ID_ADD_USER + SEPARATOR + name);
    }

    public static String getIdByName(String name) {
        return sendRequest(ID_GET_USER_ID + SEPARATOR + name);
    }

    public static String getNameById(int id) {
        return sendRequest(ID_GET_USER_NAME + SEPARATOR + id);
    }

    public static String removeUser(String name) {
        return sendRequest(ID_REMOVE_USER + SEPARATOR + name);
    }

    public static String addOrder(int identifier, int count, String date) {
        String result = "";
        for (int i = 0; i < count; i++) {
            result = sendRequest(ID_ADD_ORDER + SEPARATOR + identifier + SEPARATOR + date);
        }
        return result;
    }

    public static String addPurchase(int identifier, float bill, String date) {
        return sendRequest(ID_ADD_PURCHASE + SEPARATOR + identifier + SEPARATOR + bill + SEPARATOR + date);
    }

    public static int getOrderCountById(int identifier, String month) {
        String result = sendRequest(ID_GET_ORDERS_FOR_MONTH_BY_ID + SEPARATOR + identifier + SEPARATOR + month);
        return result.equals("False")? 0: Integer.parseInt(result);
    }

    public static float getPurchaseSumById(int identifier, String month) {
        String result = sendRequest(ID_GET_PURCHASE_FOR_MONTH_BY_ID + SEPARATOR + identifier + SEPARATOR + month);
        return result.equals("False")? 0: Float.parseFloat(result);
    }

    public static ArrayList<String> getAll(){
        String result = sendRequest(ID_GET_MONTH);
        if (result.equals("False"))
            return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(result.split(SEPARATOR)));
    }

    public static int getOrderCountForMonth(String month) {
        String result = sendRequest(ID_GET_ORDERS_FOR_MONTH + SEPARATOR + month);
        return result.equals("False")? 0: Integer.parseInt(result);
    }

    public static float getPurchaseSumForMonth(String month) {
        String result = sendRequest(ID_GET_PURCHASE_FOR_MONTH + SEPARATOR + month);
        return result.equals("False")? 0: Float.parseFloat(result);
    }

    public static String[] getUserTotal(int identifier, String month){
        String[] result = sendRequest(ID_GET_TOTAL + SEPARATOR + identifier + SEPARATOR + month).split(SEPARATOR);
        System.out.println(result);
        return result;
    }

    public static String save(){
        String result = sendRequest(ID_PRINT);
        if (result.equals("True")){
            return "Erfolgreich gespeichert!";
        }else{
            return "Es konnte nicht gespeichert werden!";
        }
    }

}

package com.example.invoice;

public class TransmitterConnection {
    public String IP = "0.0.0.0";
    public int PORT = 8080;

    public TransmitterConnection(String ip, int port){
        IP = ip;
        PORT = port;
    }

    public TransmitterConnection(String input){

        String[] inPort = input.split(" ");
        IP = inPort.length > 0 ? inPort[0] : "0.0.0.0";
        PORT = inPort.length > 1 ? Integer.parseInt(inPort[1]) : 8080;
    }
}

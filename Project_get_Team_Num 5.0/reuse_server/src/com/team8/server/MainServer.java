package com.team8.server;


import java.io.IOException;

public class MainServer {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Server mainServer=new Server();
        try {
            mainServer.StartServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

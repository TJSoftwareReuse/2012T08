package com.team8.client;


import com.team8.server.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;



public class MainClient {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader br = null;
        PrintWriter pw=null;
        try {
            //客户端socket指定服务器的地址和端口号
            socket = new Socket("127.0.0.1", 8080);
            System.out.println("Socket=" + socket);
            //同服务器原理一样
            br = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), Server.encode));

            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream(),Server.encode)));
            Scanner scanner = new Scanner(System.in);
            String m="";

            while(!m.equals("END"))
            {
                System.out.println(m);
                m=scanner.nextLine();
                pw.println(m);
                pw.flush();
                if(m.equals("END"))
                    break;
                System.out.println(br.readLine());
                System.out.println(br.readLine());
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("close......");
                br.close();
                pw.close();
                socket.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

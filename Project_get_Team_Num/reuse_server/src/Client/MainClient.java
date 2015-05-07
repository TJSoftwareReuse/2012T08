package Client;

import java.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import Server.*;

public class MainClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Socket socket = null;  
	      BufferedReader br = null;  
	      PrintWriter pw=null; 
	      try {  
	          //�ͻ���socketָ���������ĵ�ַ�Ͷ˿ں�  
	          socket = new Socket("127.0.0.1", Server.PORT);  
	          System.out.println("Socket=" + socket);  
	          //ͬ������ԭ��һ��  
	          br = new BufferedReader(new InputStreamReader(  
	                  socket.getInputStream()));  
	          pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(  
	                  socket.getOutputStream())));  
	          Scanner scanner = new Scanner(System.in);  
	          String m;
	          do
	          {
	        	  m=scanner.nextLine();
	        	  pw.println(m);
	        	  pw.flush();
	        	  System.out.println(br.readLine());
	        	  System.out.println(br.readLine());
	          }while(m!="END");
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

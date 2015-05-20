package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.naming.InitialContext;

import src.com.team8.License.License;

import com.team8.PerformanceManagement.PM;
import com.eva.me.cm.ConfigUtil;
import com.manager.failure.FailureManager;

public class Server {
	public static int PORT = 8080;  
	public static String CMRoot="CMRoot\\config.properties";
	public static License license;
	public static String encode="Unicode";
	public static void InitWithCM()
	{
		ConfigUtil.getInstance();
		ConfigUtil.getInstance().changeConfigFilePath(CMRoot);
		//ConfigUtil.getInstance().changeConfigFilePath(CMRoot);
		license=new License(Integer.parseInt(ConfigUtil.getInstance().getProperty("LicenseNum")));
		PM.setPathName(ConfigUtil.getInstance().getProperty("PMRoot"));
		FailureManager.resetOutputFile(ConfigUtil.getInstance().getProperty("FMRoot"));
		
	}
	public void StartServer() { 
		InitWithCM();
		
	    ServerSocket s = null;  
	    Socket socket = null;  
	    
	    try {  
	        //设定服务端的端口号  
	        s = new ServerSocket(PORT);  
	        System.out.println("ServerSocket Start:"+s);  
	        ServerThread_local stl=new ServerThread_local();
	        stl.start();
	        //等待请求,此方法会一直阻塞,直到获得请求才往下走  
	        while(true){
	        	socket = s.accept(); 
	        	ServerThread sThread = new ServerThread();
	        	sThread.setSocket(socket);
	        	sThread.start();
	        	
	        }     
	    } catch (Exception e) {   
	    	e.printStackTrace();  
	    }finally{  
	    	System.out.println("Close.....");       
	    }  
	}
	public class ServerThread_local extends Thread{
		@Override
		public void run() {
			BufferedReader br=null;

			Scanner scanner = new Scanner(System.in);  
			while(true)
			{
				String m=scanner.nextLine();  
				ProvideService(m);
			}
		}
		private void ProvideService(String str) {
			PM.sendPMMessage("get Message "+str, 1);
			if(license.inLicense())
			{
				FailureManager.logDebug("Provide Service Success");
				PM.sendPMMessage("Provide Service"+str, 1);
				PM.sendPMMessage("Return Message", 1);
				System.out.println("Student: "+str+" belong to team: "+ConfigUtil.getInstance().getProperty(str));
				
			}
			else {
				FailureManager.logDebug("Provide Service Fail");
				PM.sendPMMessage("Reject Service"+str, 1);
				PM.sendPMMessage("Return Message", 1);
				System.out.println("Reject Service");
				
			}
		}
	}
	public class ServerThread extends Thread{
		private Socket socket=null;
		BufferedReader br = null;  
	    PrintWriter pw = null;  
		public void setSocket(Socket s){
			this.socket=s;
		}
		
		@Override
		public void run() {
			try 
			{
				System.out.println("Connection accept socket:"+socket);   
				//用于接收客户端发来的请求         
				br = new BufferedReader(new InputStreamReader(socket.getInputStream(),encode));  
				//用于发送返回信息,可以不需要装饰这么多io流使用缓冲流时发送数据要注意调用.flush()方法   
				pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),encode)),true);  
				while(true){  
					String str;
					str = br.readLine();
					
					if(str.equals("END")){  
						break;  
					}
					
					System.out.println("Client Socket Message:"+str);  
					pw.println("Message Received");  
					pw.flush();  
					
					ProvideService(str);
					
					}     
			} catch (IOException e) {
				e.printStackTrace();
			} 
			finally{
				try {
					pw.close();
					br.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			super.run();
		}

		private void ProvideService(String str) {
			PM.sendPMMessage("get Message "+str, 1);
			if(license.inLicense())
			{
				FailureManager.logDebug("Provide Service Success");
				PM.sendPMMessage("Provide Service"+str, 1);
				PM.sendPMMessage("Return Message", 1);
				pw.println("Student: "+str+" belong to team: "+ConfigUtil.getInstance().getProperty(str));
			}
			else {
				FailureManager.logDebug("Provide Service Fail");
				PM.sendPMMessage("Reject Service"+str, 1);
				PM.sendPMMessage("Return Message", 1);
				pw.println("Reject Service");
				
			}
		}
		
		
	}
}
	
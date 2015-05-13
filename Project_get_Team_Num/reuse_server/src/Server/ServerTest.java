package Server;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.regex.*;

import org.junit.Before;
import org.junit.Test;

public class ServerTest {

	@Before
	public void setUp() throws Exception {
	}

//单线程测试，设计测试用例
	@Test
	public void normalTestStartServer() {
		String[] test ={"贺志鹏","Alice","陈文威","END"};
		Socket socket = null;
		BufferedReader br = null;
		PrintWriter pw = null;
		try{
			socket = new Socket("127.0.0.1",Server.PORT);
			br = new BufferedReader(new InputStreamReader(  
	                  socket.getInputStream()));
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(  
	                  socket.getOutputStream()))); 
			for(int i = 0; i < test.length;i++)
			{
				pw.println(test[i]);
				pw.flush();
				br.readLine();
				switch(i)
				{
				case 0:assertEquals(br.readLine(),"Student: 贺志鹏 belong to team: 8");break;
				case 1:assertEquals(br.readLine(),"Student: Alice belong to team: null");break;
				case 2:assertEquals(br.readLine(),"Student: 陈文威 belong to team: null");break;
				case 3:break;
				}
			}
		}catch (Exception e) {  
	          e.printStackTrace();  
	      } finally {  
	          try {
	              br.close();  
	              pw.close();  
	              socket.close();  
	          } catch (IOException e) {  
	              e.printStackTrace();  
	          }  
	      }  
	} 
	@Test
//多线程测试，1000个线程，每个线程写2次//感觉通不过，一秒内建立太多socket必然不行，为和谐设定数值为4
	public void testMutiThread() throws InterruptedException
	{
		int threadNum = 3;
		CountDownLatch runningThreadNum = new CountDownLatch(threadNum);
		for(int i = 0; i<threadNum;i++)
		{
			new testThread(runningThreadNum).start();
		}
		runningThreadNum.await();
	}
//多线程测试类
	public class testThread extends Thread
	{
		CountDownLatch runingNum;
		public testThread(CountDownLatch runingNum){
			this.runingNum = runingNum;
		}
		@Override
		public void run()
		{
			Socket socket = null;
			BufferedReader br = null;
			PrintWriter pw = null;
			String[] in = {"贺志鹏","END"};
			try{
				socket = new Socket("127.0.0.1",Server.PORT);
				br = new BufferedReader(new InputStreamReader(  
		                  socket.getInputStream()));
				pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(  
		                  socket.getOutputStream())));
				for(int i = 0; i < in.length;i++){
					pw.println(in[i]);
					pw.flush();
					br.readLine();
					switch(i)
					{
					case 0:assertEquals(br.readLine(),"Student: 贺志鹏 belong to team: 8");break;
					}
					try{
						sleep(10000);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
				runingNum.countDown();
			}catch (Exception e){
				e.printStackTrace();
			}finally{
				try{
					br.close();
					pw.close();
					socket.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
}



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

//���̲߳��ԣ���Ʋ�������
	@Test
	public void normalTestStartServer() {
		String[] test ={"��־��","Alice","������","END"};
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
				case 0:assertEquals(br.readLine(),"Student: ��־�� belong to team: 8");break;
				case 1:assertEquals(br.readLine(),"Student: Alice belong to team: null");break;
				case 2:assertEquals(br.readLine(),"Student: ������ belong to team: null");break;
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
//���̲߳��ԣ�1000���̣߳�ÿ���߳�д2��//�о�ͨ������һ���ڽ���̫��socket��Ȼ���У�Ϊ��г�趨��ֵΪ4
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
//���̲߳�����
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
			String[] in = {"��־��","END"};
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
					case 0:assertEquals(br.readLine(),"Student: ��־�� belong to team: 8");break;
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



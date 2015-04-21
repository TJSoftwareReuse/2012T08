import java.util.Random;

import com.team8.PerformanceManagement.*;


public class MainTest {
	public static void main(String[] args) {
		
		System.out.println("start");
		test am=new test(200,20, "aaaa", 4);
		test bm=new test( 400,10, "bbbb", 8);
		test cm=new test( 2000,2, "ccc", 40);
		test dm=new test(100, 40,"ddd", 2);
		Thread m=new Thread(){
			public void run() {
				System.out.println("shutdown");
			};
		};
		
		
		am.start();
		bm.start();
		cm.start();
		dm.start();
	}
	//�����߳̽��в���
	public static class test extends Thread
	{
		int time;
		String message;
		int num;
		int times;
		//ʱ����   ���ʹ�������  ��Ϣ  ��Ϣ����
		public test(int time,int times,String message,int num) {
			this.time=time;
			this.times=times;
			this.message=message;
			this.num=num;
			// TODO Auto-generated constructor stub
		}
		@Override
		public void run()
		{
			Random r=new Random(System.currentTimeMillis());
			while(times>=0)
			{
				times--;
				try {
					sleep(time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(times+":"+message);
				PM.sendPMMessage(message, num);
			}
			
		}
	}
}

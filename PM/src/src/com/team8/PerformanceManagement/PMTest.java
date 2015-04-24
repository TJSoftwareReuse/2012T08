package src.com.team8.PerformanceManagement;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;




public class PMTest extends TestCase{

	
	@Before
	public void setUp() throws Exception {
	}
	//测试Reset()函数
	@Test
	public void testReset()
	{
		PM.Reset(0);
		PM.sendPMMessage("a", 22);
		String FileName=PM.getFileName(0);
		File file=new File(FileName);
		PM.Reset(0);
		assertFalse(file.exists());
		assertTrue(PM.getMap().isEmpty());
	}
	// 测试正常情况,0分钟延迟
	@Test
	public void testGetFileNameNormal() {
		SimpleDateFormat target = new SimpleDateFormat("yyyy-MM-dd-HH-mm");// 设置日期格式
		assertEquals("LOG\\"+target.format(new Date())+".log", PM.getFileName(0));
	}
	// 测试时间超过一个小时的情况
	@Test
	public void testGetFileNameMore() {
		PM.Reset(0);
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		Date date = new Date();
		Calendar dar = Calendar.getInstance();
		dar.setTime(date);
		dar.add(Calendar.HOUR, 2);
		String FileName = "LOG\\"+dft.format(dar.getTime())+".log";
		String result = PM.getFileName(120);
		assertEquals(FileName,result);
	}

	@Test
	public void testSendPMMessage() {
		PM.Reset(0);
		PM.sendPMMessage("Alice", 12);
		Map<String, Integer> tempMap=PM.getMap();
		assertEquals(true,tempMap.containsKey("Alice"));
		assertEquals(new Integer(12),tempMap.get("Alice"));
		PM.Reset(0);
	}
	@Test
	public void testSendMorePMMessage() {
		PM.Reset(0);
		PM.sendPMMessage("Alice", 12);
		PM.sendPMMessage("Bob", 10);
		PM.sendPMMessage("Alice", 10);
		Map<String, Integer> tempMap=PM.getMap();
		assertEquals(true,tempMap.containsKey("Alice"));
		assertEquals(new Integer(22),tempMap.get("Alice"));
		assertEquals(true,tempMap.containsKey("Bob"));
		assertEquals(new Integer(10),tempMap.get("Bob"));
		PM.Reset(0);
	}
	@Test
	public void testSendErrorPMMessage() {
		PM.Reset(0);
		PM.sendPMMessage("error", -1);
		String FileName=PM.getFileName(0);
		File file=new File(FileName);
		assertFalse(file.exists());
		
		PM.sendPMMessage(null, -1);
		assertFalse(file.exists());
	}
	@Test
	//通过读取log文件测试输出效果
	public void testOutputNormal() {
		PM.Reset(0);
		PM.sendPMMessage("Loki", 18);
		PM.sendPMMessage("MMM", 17);
		PM.sendPMMessage("Loki", 1000);
		File target = new File(PM.getFileName(0));
		assertTrue(target.exists());
		
		try {
			BufferedReader bf=new BufferedReader(new FileReader(target));
			String FirstLine=bf.readLine();
			String SecondLine=bf.readLine();
			
			assertEquals(FirstLine, "Loki:1018");
			assertEquals(SecondLine, "MMM:17");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}
	//多线程测试 压力测试1000个线程 每个线程写5次
	@Test
	public void testMutiThread() throws InterruptedException
	{
		PM.Reset(0);
		int threadNum=1000;
		CountDownLatch runningThreadNum = new CountDownLatch(threadNum);
		for(int i=0;i<threadNum;i++)
		{
			new testThread(runningThreadNum,300).start();
		}
		runningThreadNum.await();
		
		File target = new File(PM.getFileName(0));
		assertTrue(target.exists());
		try {
			FileReader fr=new FileReader(target);
			BufferedReader bf=new BufferedReader(fr);
			String FirstLine=bf.readLine();
			assertEquals(FirstLine, "TestThread:"+threadNum*5);
			bf.close();
			fr.close();
			PM.Reset(0);
			assertFalse(target.exists());
			assertTrue(PM.getMap().isEmpty());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			PM.Reset(0);
		}
		
	}
	//多线程测试 压力测试1000个线程 每个线程写5次
	@Test
	public void testMutiThreadOverMinute() throws InterruptedException
	{
		PM.Reset(0);
		int threadNum=100;
		CountDownLatch runningThreadNum = new CountDownLatch(threadNum);
		for(int i=0;i<threadNum;i++)
		{
			new testThread(runningThreadNum,12000).start();
		}
		runningThreadNum.await();
		File target1 = new File(PM.getFileName(-1));
		File target2 = new File(PM.getFileName(0));
		assertTrue(target1.exists());
		assertTrue(target2.exists());
		try {
			FileReader fr=new FileReader(target1);
			BufferedReader bf=new BufferedReader(fr);
			String FirstLine=bf.readLine();
			bf.close();
			fr.close();
			fr=new FileReader(target2);
			bf=new BufferedReader(fr);
			String SecondLine=bf.readLine();
			int m1=Integer.parseInt((FirstLine.split(":"))[1]);
			int m2=Integer.parseInt((SecondLine.split(":"))[1]);
			assertEquals(m1+m2, threadNum*5);
			bf.close();
			fr.close();
			
		} catch (IOException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			PM.Reset(0);
			PM.Reset(-1);
		}
	}
	//多线程测试类
	public class testThread extends Thread
	{
		CountDownLatch runingNum;
		int sleeptime=300;
		public testThread(CountDownLatch runingNum,int sleep) {
			// TODO Auto-generated constructor stub
			this.runingNum=runingNum;
			sleeptime=sleep;
		}
		@Override
		public void run()
		{
			int times=5;
			while(times>0)
			{
				times--;
				try {
					sleep(sleeptime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				PM.sendPMMessage("TestThread", 1);
			}
			runingNum.countDown();
		}
	}
	public static void main(String[] args) {
        junit.textui.TestRunner.run(
            PMTest.class);
	}
}

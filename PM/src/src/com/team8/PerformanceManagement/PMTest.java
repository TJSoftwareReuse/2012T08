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

import org.junit.Before;
import org.junit.Test;




public class PMTest {

	//多线程测试类
		public class testThread extends Thread
		{
			@Override
			public void run()
			{
				int times=5;
				while(times>0)
				{
					times--;
					try {
						sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					PM.sendPMMessage("TestThread", 1);
				}
				
			}
		}
	@Before
	public void setUp() throws Exception {
	}
	@Test
	public void testReset()
	{
		PM.Reset();
		PM.sendPMMessage("a", 22);
		String FileName=PM.getFileName(0);
		File file=new File(FileName);
		PM.Reset();
		assertFalse(file.exists());
	}
	
	@Test
	// 测试正常情况,0分钟延迟
	public void testGetFileNameNormal() {
		SimpleDateFormat target = new SimpleDateFormat("yyyy-MM-dd-HH-mm");// 设置日期格式
		assertEquals(target.format(new Date())+".log", PM.getFileName(0));
	}

	@Test
	// 测试时间超过一个小时的情况
	public void testGetFileNameMore() {
		PM.Reset();
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		Date date = new Date();
		Calendar dar = Calendar.getInstance();
		dar.setTime(date);
		dar.add(Calendar.HOUR, 2);
		String FileName = dft.format(dar.getTime())+".log";
		String result = PM.getFileName(120);
		assertEquals(FileName,result);
	}

	@Test
	public void testSendPMMessage() {
		PM.Reset();
		String FileName=PM.getFileName(0);
		PM.sendPMMessage("Alice", 12);
		Map<String, Integer> tempMap=PM.getMap();
		assertEquals(true,tempMap.containsKey("Alice"));
		assertEquals(new Integer(12),tempMap.get("Alice"));
		
		PM.sendPMMessage("Alice", 10);
		PM.sendPMMessage("Bob", 10);
		tempMap=PM.getMap();
		if (PM.getFileName(0).equals(FileName)) {
			assertEquals(true,tempMap.containsKey("Alice"));
			assertEquals(new Integer(22),tempMap.get("Alice"));
		}
		else{
			assertEquals(true,tempMap.containsKey("Alice"));
			assertEquals(new Integer(10),tempMap.get("Alice"));
		}
	}

	@Test
	//通过检查文件是否存在来测试void方法
	//检测输出是否正确
	public void testOutputNormal() {
		PM.Reset();
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
	//多线程测试
	@Test
	public void testMutiThread()
	{
		PM.Reset();
		Thread m1=new testThread();
		Thread m2=new testThread();
		Thread m3=new testThread();
		Thread m4=new testThread();
		m1.run();
		m2.run();
		m3.run();
		m4.run();
		File target = new File(PM.getFileName(0));
		assertTrue(target.exists());
		
		try {
			FileReader fr=new FileReader(target);
			BufferedReader bf=new BufferedReader(fr);
			String FirstLine=bf.readLine();
			assertEquals(FirstLine, "TestThread:20");
			bf.close();
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

	}

}

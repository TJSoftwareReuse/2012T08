package src.com.team8.PerformanceManagement;

import static org.junit.Assert.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;


public class PMTest {

	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	// �����������,0�����ӳ�
	public void testGetFileNameNormal() {
		SimpleDateFormat target = new SimpleDateFormat("yyyy-MM-dd-HH-mm");// �������ڸ�ʽ
		assertEquals(target.format(new Date())+".log", PM.getFileName(0));
	}

	@Test
	// ����ʱ�䳬��һ��Сʱ�����
	public void testGetFileNameMore() {

		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		Date date = new Date();
		Calendar dar = Calendar.getInstance();
		dar.setTime(date);
		dar.add(Calendar.MINUTE, 20);
		String FileName = dft.format(dar.getTime())+".log";
		String result = PM.getFileName(20);
		assertEquals(FileName,result);
	}

	@Test
	public void testSendPMMessage() {
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
	//ͨ������ļ��Ƿ����������void����
	public void testOutputNormal() {
		int tmp = 0;
		PM.sendPMMessage("Loki", 18);
		File target = new File(PM.getFileName(0));
		if(target.exists())
		{
			tmp = 1;
		}
		assertEquals(tmp,1);
	}

}

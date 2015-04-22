package com.team8.PerformanceManagement;

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

	PM p = new PM();
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
		dar.add(java.util.Calendar.HOUR_OF_DAY, 2);
		dft.format(dar.getTime());

		String result = PM.getFileName(120);
		assertEquals(dft + ".log", result);
	}

	@Test
	public void testSendPMMessage() {
		p.sendPMMessage("Alice", 12);
	//	Map<String, Integer> tmp = new HashMap<String,Integer>();
	//	tmp.put("Alice", 12);
		assertEquals(12,p.forTest());
	}

	@Test
	//ͨ������ļ��Ƿ����������void����
	public void testOutputNormal() {
		int tmp = 0;
		p.sendPMMessage("Loki", 18);
		File target = new File(p.getFileName(0));
		if(target.exists())
		{
			tmp = 1;
		}
		assertEquals(tmp,1);
	}

}

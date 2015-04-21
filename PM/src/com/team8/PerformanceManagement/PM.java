package com.team8.PerformanceManagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;


public class PM {
	
	private static class SingletonHolder     
	{     
			public static int Interval=60;
	        public final static Map<String, Integer> instance = new HashMap<String,Integer>();
	        public static Timer time=new Timer();
	        public static String lastFileName=getFileName(-1);
	}
	public static String getFileName(int moreMinute) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE,moreMinute);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		return dateFormat.format(calendar.getTime())+".log";
	}
	//发送消息方法  value 可以为负数
	public synchronized static void sendPMMessage(String name,int value)
	{
		System.out.println("send");
		if(value<0)return;
		String FileName=getFileName(0);
		File file=new File(FileName);
		if(FileName.equals(SingletonHolder.lastFileName))
		{
			if(SingletonHolder.instance.containsKey(name))
				SingletonHolder.instance.put(name, value+SingletonHolder.instance.get(name));
			else {
				SingletonHolder.instance.put(name, value);
			}
		}
		else {
			SingletonHolder.lastFileName=FileName;
			SingletonHolder.instance.clear();
			System.out.println("clear");
			SingletonHolder.instance.put(name, value);
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("output:"+SingletonHolder.instance.size());
		output(file);
	}
	//输出到项目文件下以 年-月-日-时-分  命名  如果可输出内容为空，则取消输出
	public static synchronized void output(File file) {
		System.out.println("output");
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
			for (String key: (SingletonHolder.instance).keySet()) {
				pw.println(key+":"+SingletonHolder.instance.get(key).toString());
			}  
			pw.close();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}



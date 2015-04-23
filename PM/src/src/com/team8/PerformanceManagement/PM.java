package src.com.team8.PerformanceManagement;

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

import org.omg.CORBA.PRIVATE_MEMBER;

public class PM {
	//每次执行发送信息函数的时候修改一次log文件 对于时间开销比较大，但保证了每条消息都能保存到文件中
	private PM()
	{
		
	}

	private static class SingletonHolder     
	{     
			public final static Map<String, Integer> instance = new HashMap<String,Integer>();
	        public static String lastFileName=getFileName(-1);
	}
	public static String getFileName(int moreMinute) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE,moreMinute);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		return dateFormat.format(calendar.getTime())+".log";
	}
	//发送消息方法  value 可以为负数或0  负数不被添加到系统
	public synchronized static void sendPMMessage(String name,int value)
	{
		//System.out.println("send");
		
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
		//	System.out.println("clear");
			SingletonHolder.instance.put(name, value);
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//System.out.println("output:"+SingletonHolder.instance.size());
		output(file);
	}
	//将保存的信息全部 输出到项目文件下     
	private static synchronized void output(File file) {
	//	System.out.println("output");
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
	public static Map<String, Integer> getMap()
	{
		return SingletonHolder.instance;	
	}
}



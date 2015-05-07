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


/**
 * @author      Group_8_Tongji_University <https://github.com/TJSoftwareReuse/2012T08>
 * @since       2015-04-24
 */
public class PM {
	/**
	 * 这是一个单例，不提供单独的构造函数
	 * It's a Singleton and won't provide Constructor
	 * Please use PM directly
	 */
	private PM()
	{
		
	}
	private static class SingletonHolder     
	{     
			public final static Map<String, Integer> instance = new HashMap<String,Integer>();
	        public static String lastFileName=getFileName(-1);
	        public static boolean isReset=false;
	}
	/**
	 * 根骨时间获得要写入的文件名
	 * <p>
	 * 2014年2月29日23分   获取文件格式 : LOG\2014-02-29-23.log
	 * 精确到分钟
	 * <p>
	 *
	 * @param   moreMinute   获得moreMinute时间后要输出的文件名
	 * @return  filename  moreMinute时间后要输出的文件名
	 */
	public static String getFileName(int moreMinute) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE,moreMinute);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		return "LOG\\"+dateFormat.format(calendar.getTime())+".log";
	}
	/**
	 * 发送PM信息
	 * <p>
	 * 发送PM信息给单例，每次发送即时写入文件
	 * <p>
	 * 每次写入是当前分钟收到的所有数据
	 * <p>
	 * 间隔一分钟发送的信息将会调用 getFileName(0)  获取新的文件
	 * <p>
	 * 提供线程锁   支持多线程操作  输出效率降低
	 * <p>
	 * 使用 {{@link #getFileName(0)} 获得将要输出的文件名
	 * <p>
	 * 使用 {{@link #output(File)}}  输出该分钟内收到的全部信息
	 * <p>
	 * 因为每次写入是进行写覆盖，所以最坏情况下时间复杂度是O(n^2)
	 * <p>
	 * 每次调用该函数，即时输出到文件，确保程序尽可能输出PM信息
	 * <p>
	 * @param  name   PM信息的字符描述
	 * @param  value    PM信息的输出次数     value>0
	 * @exception  IOException  
	 */
	public synchronized static void sendPMMessage(String name,int value)
	{
		if(value<=0)return;
		String FileName=getFileName(0);
		File file=new File(FileName);
		//System.out.println("send");
		try {
			if(!file.getParentFile().exists())
			{	
				file.getParentFile().mkdirs();
			}
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
				SingletonHolder.instance.put(name, value);
				file.createNewFile();
				System.out.println(FileName+" be created");
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		output(file);
	}
	/**
	 * 私有函数：输出该分钟内收到的信息
	 * <p>
	 * 将该分钟内收到的信息输出到file文件下
	 * 这是内部私有函数 仅攻类内部输出使用
	 * <p>
	 * @param file  输出到该文件对象 请确保文件已经生成
	 * @exception IOException
	 */
	private static synchronized void output(File file) {
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(out));
			for (String key: (SingletonHolder.instance).keySet()) {
				pw.println(key+":"+SingletonHolder.instance.get(key).toString());
			}  
			pw.flush();
			pw.close();
			out.close();
			SingletonHolder.isReset=false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	/**
	 * 返回该分钟内收到的信息
	 * <p>
	 * 将类内部保存的信息通过Map方式返回
	 * 该方法是为了方便测试人员进行测试
	 * <p>
	 * @exception IOException
	 * @return  Map  PM信息和输出次数的对应表
	 */
	public static Map<String, Integer> getMap()
	{
		return SingletonHolder.instance;
	}
	/**
	 * 返回该分钟内收到的信息
	 * <p>
	 * 重置该类
	 * 清空该分钟的输出记录
	 * 该方法是为了方便测试人员进行测试
	 * <p>
	 */
	public static void Reset()
	{
		if(SingletonHolder.isReset==false)
		{
			SingletonHolder.isReset=true;
			System.gc();//为了保证文件能够正常删除，需要这个函数
			SingletonHolder.instance.clear();
			File lastFile=new File(getFileName(0));
			if(lastFile.exists())
				lastFile.delete();
			SingletonHolder.lastFileName=getFileName(-1);
		}
	}
}



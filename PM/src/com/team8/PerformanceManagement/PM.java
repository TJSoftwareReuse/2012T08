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
	 * ����һ�����������ṩ�����Ĺ��캯��
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
	 * ����ʱ����Ҫд����ļ���
	 * <p>
	 * 2014��2��29��23��   ��ȡ�ļ���ʽ : LOG\2014-02-29-23.log
	 * ��ȷ������
	 * <p>
	 *
	 * @param   moreMinute   ���moreMinuteʱ���Ҫ������ļ���
	 * @return  filename  moreMinuteʱ���Ҫ������ļ���
	 */
	public static String getFileName(int moreMinute) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE,moreMinute);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		return "LOG\\"+dateFormat.format(calendar.getTime())+".log";
	}
	/**
	 * ����PM��Ϣ
	 * <p>
	 * ����PM��Ϣ��������ÿ�η��ͼ�ʱд���ļ�
	 * <p>
	 * ÿ��д���ǵ�ǰ�����յ�����������
	 * <p>
	 * ���һ���ӷ��͵���Ϣ������� getFileName(0)  ��ȡ�µ��ļ�
	 * <p>
	 * �ṩ�߳���   ֧�ֶ��̲߳���  ���Ч�ʽ���
	 * <p>
	 * ʹ�� {{@link #getFileName(0)} ��ý�Ҫ������ļ���
	 * <p>
	 * ʹ�� {{@link #output(File)}}  ����÷������յ���ȫ����Ϣ
	 * <p>
	 * ��Ϊÿ��д���ǽ���д���ǣ�����������ʱ�临�Ӷ���O(n^2)
	 * <p>
	 * ÿ�ε��øú�������ʱ������ļ���ȷ�����򾡿������PM��Ϣ
	 * <p>
	 * @param  name   PM��Ϣ���ַ�����
	 * @param  value    PM��Ϣ���������     value>0
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
	 * ˽�к���������÷������յ�����Ϣ
	 * <p>
	 * ���÷������յ�����Ϣ�����file�ļ���
	 * �����ڲ�˽�к��� �������ڲ����ʹ��
	 * <p>
	 * @param file  ��������ļ����� ��ȷ���ļ��Ѿ�����
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
	 * ���ظ÷������յ�����Ϣ
	 * <p>
	 * �����ڲ��������Ϣͨ��Map��ʽ����
	 * �÷�����Ϊ�˷��������Ա���в���
	 * <p>
	 * @exception IOException
	 * @return  Map  PM��Ϣ����������Ķ�Ӧ��
	 */
	public static Map<String, Integer> getMap()
	{
		return SingletonHolder.instance;
	}
	/**
	 * ���ظ÷������յ�����Ϣ
	 * <p>
	 * ���ø���
	 * ��ո÷��ӵ������¼
	 * �÷�����Ϊ�˷��������Ա���в���
	 * <p>
	 */
	public static void Reset()
	{
		if(SingletonHolder.isReset==false)
		{
			SingletonHolder.isReset=true;
			System.gc();//Ϊ�˱�֤�ļ��ܹ�����ɾ������Ҫ�������
			SingletonHolder.instance.clear();
			File lastFile=new File(getFileName(0));
			if(lastFile.exists())
				lastFile.delete();
			SingletonHolder.lastFileName=getFileName(-1);
		}
	}
}



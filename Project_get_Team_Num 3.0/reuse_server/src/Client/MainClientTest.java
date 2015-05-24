//Author：杨丰
//为了和贺志鹏的ServerTest区分开了，名字改为了MainClientTest，同样也是对Server端进行测试

package Client;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.invoke.VolatileCallSite;
import java.net.Socket;
import java.util.PrimitiveIterator.OfDouble;

import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

import org.apache.bcel.verifier.exc.StaticCodeConstraintException;
import org.junit.Before;
import org.junit.Test;

import com.eva.me.cm.ConfigUtil;
import com.manager.failure.FailureManager;
import com.team8.PerformanceManagement.PM;
import com.team8.License.License;

import Main.Main;
import Server.*;

public class MainClientTest {

	// 全局变量
	public static String CMRoot = "CMRoot\\config.properties";
	public static License license;
	public volatile static int myTimer = 1000;
	
	//所有线程公用一个License的个数
	public volatile static int license_num = InitWithCM();	 

	//初始化license，为了得到license的个数
	public static int InitWithCM() {
		ConfigUtil.getInstance();
		ConfigUtil.getInstance().changeConfigFilePath(CMRoot);

		license = new License(Integer.parseInt(ConfigUtil.getInstance()
				.getProperty("LicenseNum")));
		PM.setPathName(ConfigUtil.getInstance().getProperty("PMRoot"));
		FailureManager.resetOutputFile(ConfigUtil.getInstance().getProperty(
				"FMRoot"));
		return license.getLicense();
	}
	@Before
	public void setUp() throws Exception {

	}

	// 进行多线程测试
	@Test
	public void MultiRequestsTest() {

		// 构造一个Runner
		TestRunnable runner = new TestRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void runTest() throws Throwable {
				// 测试内容
				Socket socket = null;
				BufferedReader br = null;
				PrintWriter pw = null;
				
				//每个线程单独进行下列测试
				String[] test_str = {"4",		//测试输入为正常组号；
									"",			//测试输入为空；
									"555",     //测试输入为不存在的组号；
									"aaabb",   //测试输入为字母；
									"END"};     //测试结束指令
				
				try {
					socket = new Socket("127.0.0.1", Server.PORT);
					br = new BufferedReader(new InputStreamReader(
							socket.getInputStream(),Server.encode));
					pw = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(socket.getOutputStream(),Server.encode)));
					
					//多线程运行时让每个线程之间间隔0.5s，避免出现license数目错误问题
					myTimer += 500;
					try {
						Thread.sleep(myTimer);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for (int i = 0; i < test_str.length; ++i) {
						pw.println(test_str[i]);	//会自动给字符串加上换行符号
						pw.flush();
						br.readLine();	//这一行代码将“Message Received”跳过
						switch (i) {
						case 0:
							if( license_num > 0)
							{
								assertEquals("Team: 4 has student: 梁竞文:彭秋辰:胡文超:杨爽",
									br.readLine());
								license_num -= 1;
							}
							else 
							{
								assertEquals("Reject Service",
										br.readLine());
								
							}	
							break;
						case 1:
							if(license_num > 0)
							{
								assertEquals("Student:  belong to team: null", 
									br.readLine());
								license_num -= 1;
							}
							else 
							{
								assertEquals("Reject Service",
										br.readLine());
							}	
							break;
						case 2:
							if(license_num > 0)
							{
									assertEquals("Team: 555 has student: null", 
									br.readLine());
									license_num -= 1;
							} 
							else 
							{
								assertEquals("Reject Service",
										br.readLine());
							}	
							break;
						case 3:
							if(license_num > 0)
							{
								assertEquals("Student: aaabb belong to team: null", 
									br.readLine());
								license_num -= 1;
							}
							else 
							{
								assertEquals("Reject Service",
										br.readLine());
							}	
							break;
							
						}
					}
				} catch (Exception e) {
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
		};
		
		
		int runnerCount = 10;
		// Runner数组，相当于并发多少个Client。
		TestRunnable[] trs = new TestRunnable[runnerCount];

		for (int i = 0; i < runnerCount; i++) {
			trs[i] = runner;
		}
		// 用于执行多线程测试用例的Runner，将前面定义的单个Runner组成的数组传入
		MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
		try {
			// 开发并发执行数组里定义的内容
			mttr.runTestRunnables();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}

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
	
	//所有线程公用一个License的个数
	public volatile int license_num = InitWithCM();	 
	


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
			@Override
			public void runTest() throws Throwable {
				// 测试内容
				Socket socket = null;
				BufferedReader br = null;
				PrintWriter pw = null;
				
				//每个线程单独进行下列测试
				String[] test_str = {"123",		//测试输入为数字时；
									"",			//测试输入为空时；
									"杨丰",        //测试输入为存在的同学；
									"周杰伦",    //测试输入为不存在的同学；
									"END"};      //测试结束指令
				
				try {
					socket = new Socket("127.0.0.1", Server.PORT);
					br = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));
					pw = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(socket.getOutputStream())));
					for (int i = 0; i < test_str.length; ++i) {
						pw.println(test_str[i]);
						pw.flush();
						br.readLine();	//这一行代码将“Message Received”跳过
						switch (i) {
						case 0:
							if( license_num > 0)
							{
								assertEquals("Student: 123 belong to team: null",
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
									assertEquals("Student: 杨丰 belong to team: 8", 
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
								assertEquals("Student: 周杰伦 belong to team: null", 
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
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
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
		
		
		int runnerCount = 20;
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

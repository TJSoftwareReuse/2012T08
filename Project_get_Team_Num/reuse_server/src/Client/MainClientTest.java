//Author�����
//Ϊ�˺ͺ�־����ServerTest���ֿ��ˣ����ָ�Ϊ��MainClientTest��ͬ��Ҳ�Ƕ�Server�˽��в���

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

	// ȫ�ֱ���
	public static String CMRoot = "CMRoot\\config.properties";
	public static License license;
	
	//�����̹߳���һ��License�ĸ���
	public volatile int license_num = InitWithCM();	 
	


	//��ʼ��license��Ϊ�˵õ�license�ĸ���
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

	// ���ж��̲߳���
	@Test
	public void MultiRequestsTest() {

		// ����һ��Runner
		TestRunnable runner = new TestRunnable() {
			@Override
			public void runTest() throws Throwable {
				// ��������
				Socket socket = null;
				BufferedReader br = null;
				PrintWriter pw = null;
				
				//ÿ���̵߳����������в���
				String[] test_str = {"123",		//��������Ϊ����ʱ��
									"",			//��������Ϊ��ʱ��
									"���",        //��������Ϊ���ڵ�ͬѧ��
									"�ܽ���",    //��������Ϊ�����ڵ�ͬѧ��
									"END"};      //���Խ���ָ��
				
				try {
					socket = new Socket("127.0.0.1", Server.PORT);
					br = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));
					pw = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(socket.getOutputStream())));
					for (int i = 0; i < test_str.length; ++i) {
						pw.println(test_str[i]);
						pw.flush();
						br.readLine();	//��һ�д��뽫��Message Received������
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
									assertEquals("Student: ��� belong to team: 8", 
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
								assertEquals("Student: �ܽ��� belong to team: null", 
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
		// Runner���飬�൱�ڲ������ٸ�Client��
		TestRunnable[] trs = new TestRunnable[runnerCount];

		for (int i = 0; i < runnerCount; i++) {
			trs[i] = runner;
		}
		// ����ִ�ж��̲߳���������Runner����ǰ�涨��ĵ���Runner��ɵ����鴫��
		MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
		try {
			// ��������ִ�������ﶨ�������
			mttr.runTestRunnables();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}

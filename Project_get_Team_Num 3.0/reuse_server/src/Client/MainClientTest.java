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
	public volatile static int myTimer = 1000;
	
	//�����̹߳���һ��License�ĸ���
	public volatile static int license_num = InitWithCM();	 

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
			@SuppressWarnings("deprecation")
			@Override
			public void runTest() throws Throwable {
				// ��������
				Socket socket = null;
				BufferedReader br = null;
				PrintWriter pw = null;
				
				//ÿ���̵߳����������в���
				String[] test_str = {"4",		//��������Ϊ������ţ�
									"",			//��������Ϊ�գ�
									"555",     //��������Ϊ�����ڵ���ţ�
									"aaabb",   //��������Ϊ��ĸ��
									"END"};     //���Խ���ָ��
				
				try {
					socket = new Socket("127.0.0.1", Server.PORT);
					br = new BufferedReader(new InputStreamReader(
							socket.getInputStream(),Server.encode));
					pw = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(socket.getOutputStream(),Server.encode)));
					
					//���߳�����ʱ��ÿ���߳�֮����0.5s���������license��Ŀ��������
					myTimer += 500;
					try {
						Thread.sleep(myTimer);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for (int i = 0; i < test_str.length; ++i) {
						pw.println(test_str[i]);	//���Զ����ַ������ϻ��з���
						pw.flush();
						br.readLine();	//��һ�д��뽫��Message Received������
						switch (i) {
						case 0:
							if( license_num > 0)
							{
								assertEquals("Team: 4 has student: ������:���ﳽ:���ĳ�:��ˬ",
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



package src.com.team8.License;
/**
 * License.java
 * @author  team8
 **/
public class License {
	private int license = 0;
	private int count = 0; 
	/**
	 * ���캯��
	 * 
	 * <p>
	 * ���캯������ʼ��License����
	 * 
	 * @param License ��ʼ��License����
	 *  
	 **/
	public License(int License) {
		// TODO Auto-generated constructor stub
		this.license=License;
	}
	/**
	 * �ж��Ƿ���License
	 * <p>
	 * �ж��Ƿ���License
	 * ���û��License ����False ����License����һ����
	 * <p>
	 * ֧�ֶ��߳�
	 * @return boolean  license�Ƿ���ʣ�� True��ʾ������
	 *  
	 **/
	public synchronized boolean inLicense(){
		count ++;
	if((license != 0) && (count < license)){
		return true;
	}
	else
		return false;
	}
	/**
	 * ���License����
	 * <p>
	 * 
	 * @return int  License������(��ʼֵ)
	 *  
	 **/
	public int getLicense(){
		return license;
	}
	/**
	 * ���License����
	 * <p>
	 * ��֧�ֶ��߳�
	 * 
	 * @return int  License������(��ʼֵ)
	 *  
	 **/
}

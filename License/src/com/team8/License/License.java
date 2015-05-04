

package src.com.team8.License;
/**
 * License.java
 * @author  team8
 **/
public class License {
	private int license = 0;
	private int count = 0; 
	/**
	 * 构造函数
	 * 
	 * <p>
	 * 构造函数，初始化License数量
	 * 
	 * @param License 初始化License数量
	 *  
	 **/
	public License(int License) {
		// TODO Auto-generated constructor stub
		this.license=License;
	}
	/**
	 * 判断是否还有License
	 * <p>
	 * 判断是否还有License
	 * 如果没有License 返回False 否则License减少一个。
	 * <p>
	 * 支持多线程
	 * @return boolean  license是否还有剩余 True表示获得许可
	 *  
	 **/
	public synchronized boolean inLicense(){
		
	if((license != 0) && (count < license)){
		count ++;
		return true;
	}
	else
		return false;
	}
	/**
	 * 获得License总数
	 * <p>
	 * 
	 * @return int  License总数量(初始值)
	 *  
	 **/
	public int getLicense(){
		return license;
	}
	public int getRemain()
	{
		return license-count;
	}
}

package Main;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;

import com.eva.me.cm.ConfigUtil;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConfigUtil cf=ConfigUtil.getInstance();
		cf.setProperty("???", "a");
		ConfigUtil.getInstance().changeConfigFilePath("CMRoot\\config.properties");
		System.out.println(ConfigUtil.getInstance().getProperty("PMRoot"));
		System.out.println(ConfigUtil.getInstance().getProperty("∂°”ÓÛœ"));

		
}
	 public static String stringToAscii(String value)
	 {
	  StringBuffer sbu = new StringBuffer();
	  char[] chars = value.toCharArray();
	  for (int i = 0; i < chars.length; i++) {
	   if(i != chars.length - 1)
	   {
	    sbu.append((int)chars[i]).append(",");
	   }
	   else {
	    sbu.append((int)chars[i]);
	   }
	  }
	  return sbu.toString();
	 }
	
}
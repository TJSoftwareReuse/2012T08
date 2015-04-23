
/*
 * License.java
 * @author Fiona
 */
package com.team8;

public class License {
	private int license = 0;
	private int count = 0; 
	//do this function when check
	public boolean inLicense(){
		count ++;
		if((license != 0) && (count < license)){
			return true;
		}
		else
			return false;
	}
	//set your license
	public void setlicense(int license){
		this.license = license;
	}
	public int getlicense(){
		return license;
	}
}

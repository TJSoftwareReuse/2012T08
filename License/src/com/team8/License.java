
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
		if((license != 0) && (count < license)){
			return true;
		}
		else
			return false;
	}
	//do this function when count
	public void click(){
		count ++;
	}
	//set your license
	public void setlicense(int license){
		this.license = license;
	}
	public int getlicense(){
		return license;
	}
}

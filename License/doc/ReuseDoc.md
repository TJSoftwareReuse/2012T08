#这是License模块的复用文档，该文档将会大致介绍这个项目

##项目说明：

1. 使用该项目需要初始化一个License对象，传入一个int值表示最大的使用次数，使用次数可以为负数，最大值为Max_int
2. 每次调用inLicense()函数，来判定是否可以剩余License，
    * 如果可以，返回true，并减少一个License
    * 如果不可以，返回false
3. 支持多线程访问
4. 不支持初始化License后额外增加License
5. 可以调用 getLicense()方法获得剩余的License数量，该方法返回一个int值表示剩余的License数量
6. 程序关闭后，License对象不会被自动保存

##使用说明：

1. 下载Jar包
2. 将jar包添加到项目中
3. 开始使用

##使用示例：

``` java

import java.io.Console;
import src.com.team8.License.License;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		License license = new License(2);
		System.out.println("licenseTotalNum: "+license.getLicense());
		System.out.println("licenseStatu: "+license.inLicense());
		System.out.println("licenseStatu: "+license.inLicense());
		System.out.println("licenseStatu: "+license.inLicense());
		System.out.println("licenseStatu: "+license.inLicense());
		System.out.println("licenseTotalNum: "+license.getLicense());
	}

}

```
 
###输出结果：

```

licenseTotalNum: 2
licenseStatu: true
licenseStatu: true
licenseStatu: false
licenseStatu: false
licenseTotalNum: 2

```



#这是PM模块的复用文档，该文档将会大致介绍这个项目

##使用说明：

1. 该模块提供几个静态方法,不提供单独的构造函数,在一个项目中无法声明多个对象,无需声明额外对象
2. 发送PM消息请调用  PM.sendPMMessage(String name,int value) 方法,不支持其他格式其他类型的PM消息
    * value>0且value<MAX_int
3. 发送消息的时候会自动在项目文件夹下新建一个LOG的文件夹，暂不提供变更文件位置功能
4. 每接受一条消息,都会将该分钟内收到的**所有消息**输出到 "项目文件夹\LOG" 下,并以yyyy-MM-dd-HH-mm.log命名该文件
5. 若输出目标已存在，将会覆盖目标文件中的内容
    * 如2015年4月28日19时33分发送的消息,将会保存在  "项目路径\LOG\2015-04-24-19-33.log"文件中
    * 输出格式  每行一个不同类型的消息    name:value
    * 如果该文件已存在，将会覆盖该文件中的内容
6. 如果该分钟内没有收到任何消息,将不会建立该分钟所对应的log文件
7. 支持多线程
8. 该类中还提供其他几个方法,他们是为了提高测试人员进行测试使用的。不建议使用该模块的时候进行调用。
9. 该类中的其他方法将在java doc中进行描述,详情请查看java doc开发文档


##使用示例：

``` java

import com.team8.PerformanceManagement.PM;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PM.sendPMMessage("First Message", 10);
		PM.sendPMMessage("First Message", 3);
		PM.sendPMMessage("Second Message", 5);
		PM.sendPMMessage("Third Message", 8);
		PM.sendPMMessage("Second Message", 5);
		PM.sendPMMessage("First Message", 5);
		PM.sendPMMessage("Second Message", 5);
		
	}

}

```

###输出结果：

```

First Message:18
Third Message:8
Second Message:15

```


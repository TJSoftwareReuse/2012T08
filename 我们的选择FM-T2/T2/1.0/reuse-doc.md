# Failure Management 复用文档

### 总体描述
1. 构件名： FailureManager
2. 功能：该构件可接收应用程序的告警信息并输出告警信息到一个单独的告警文件
3. 其他
	* 使用构件前应导入： ```import com.manager.failure.FailureManager;```
	* 构件支持5种警告类型，分别为
		1. INFO （information）
		2. DEBUG （debug）
		3. WARN （warning）
		3. ERROR （error）
		4. FATAL （fatal）
	* 所有警告信息输出到一个文件中，格式为：```时间戳 警告类型 警告信息``` 具体可见示例
	* 用户可以使用构件提供的API在运行中更换输出文件。
	* 该构件线程安全		
 
### 函数说明

函数名称  | 函数功能
------------- | -------------
logInfo(String)  | 接收并输出普通信息至文件，参数为信息内容
logDebug(String)  | 接收并输出调试信息至文件，参数为信息内容
logWarn(String)  | 接收并输出警告信息至文件，参数为信息内容
logError(String)  | 接收并输出错误信息至文件，参数为信息内容
logFatal(String)  | 接收并输出严重错误信息至文件，参数为信息内容
resetOutputFile(String) | 更改输出文件，参数为目标文件名




### 使用示例

##### 示例代码
```
import com.manager.failure.FailureManager;
    
public class Main {
    static public void main(String args[]){
        
        // use default log file
        FailureManager.logDebug("here is a debug");
        FailureManager.logError("here is an error");
        FailureManager.logInfo("here is an info");
        FailureManager.logFatal("here is a fatal");
        FailureManager.logWarn("here is a warning");
        
        // change log file
        FailureManager.resetOutputFile("output.out");
        FailureManager.logDebug("here is a debug changed");
        FailureManager.logError("here is an error changed");
        FailureManager.logInfo("here is an info changed");
        FailureManager.logFatal("here is a fatal changed");
        FailureManager.logWarn("here is a warning changed");

    }
}
```

##### 示例输出 out.log
```
2015-04-30 16:02:36 DEBUG here is a debug
2015-04-30 16:02:36 ERROR here is an error
2015-04-30 16:02:36 INFO here is an info
2015-04-30 16:02:36 FATAL here is a fatal
2015-04-30 16:02:36 WARN here is a warning

```

##### 示例输出 output.log
```
2015-04-30 16:02:36 DEBUG here is a debug changed
2015-04-30 16:02:36 ERROR here is an error changed
2015-04-30 16:02:36 INFO here is an info changed
2015-04-30 16:02:36 FATAL here is a fatal changed
2015-04-30 16:02:36 WARN here is a warning changed

```
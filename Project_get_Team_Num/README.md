#我们的找队员编号的项目

1. 允许多个用户同时访问server 没开过线程对同步进行测试，所以并不清楚会不会存在多线程的问题这需要测试人员进行测试
2. CM的文件路径设置在 ".\CMRoot\config.properties"
3. PM的文件路径设置在 ".\PMRoot\"
4. FM的文件名设置为".\FMRoot"
5. LicenseNum设定为5，并且启动server的时候，不对CM数据进行更改
6. 多个用户共享同一个License
7. 如果学生名不存在，返回的队伍号码为null
8. 学生名->team号 保存在CM文件中，因为CM文件暂不支持utf8, 所以保存的数据是通过转换的ascii，可以在config.properties查看
9. 端口号为8080，ip为本机ip ：127.0.0.1
10. 在客户端输入 "END" 结束客户端，只能通过手动结束服务端

#以下是简单测试界面：

* 开启三个客户端，把我的朋友们作为测试用例，对服务器同时进行访问，得到的不同的结果
* 图片保存在picture文件夹下

server端的输出

<img alt="server端的输出" src="https://github.com/TJSoftwareReuse/2012T08/blob/master/Project_get_Team_Num/reuse_server/Picture_For_Project/MainServer.png" >

Client1端输入与输出(蓝色表示输入)

<img alt="Client1" src="https://github.com/TJSoftwareReuse/2012T08/blob/master/Project_get_Team_Num/reuse_server/Picture_For_Project/MainClient1.png">

Client2端输入与输出(蓝色表示输入)

<img alt="Client1" src="https://github.com/TJSoftwareReuse/2012T08/blob/master/Project_get_Team_Num/reuse_server/Picture_For_Project/MainClient2.png" >

Client3端输入与输出(蓝色表示输入)

<img alt="Client1" src="https://github.com/TJSoftwareReuse/2012T08/blob/master/Project_get_Team_Num/reuse_server/Picture_For_Project/MainClient3.png" >


##额外的设想

可以添加的功能

	1. 管理员登录功能
	2. 管理员查看License
	3. 关闭server后保存剩余License数量作为新的配置参数
	4. 管理员修改License功能

以上功能只是瞎想想，我不做。

##需要添加的包

	* T1的CM
	* T2的FM
	* T8的PM
	* T8的License


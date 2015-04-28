#这是License模块的复用文档，该文档将会大致介绍这个项目

##使用说明：

1. 使用该项目需要初始化一个License对象，传入一个int值表示最大的使用次数，使用次数可以为负数，最大值为Max_int
2. 每次调用inLicense()函数，来判定是否可以剩余License，
    * 如果可以，返回true，并减少一个License
    * 如果不可以，返回false
3. 支持多线程访问
4. 不支持初始化License后额外增加License
5. 可以调用 getLicense()方法获得剩余的License数量，该方法返回一个int值表示剩余的License数量
6. 程序关闭后，License对象不会被自动保存

# springboot

#### 介绍
springboot学习

#### 软件架构
Springboot+Redis+Mybatis


#### 软件功能
1、每天凌晨将日志操作表前一天的日志信息生成excel保存并提供下载。
2、将下载后的以日期yyyy-MM-dd作为key，总数量和下载地址作为value保存在redis。
3、后台通过redis可以查询每日的日志操作信息是否已存在。
4、可以通过redis将已存在的key，下载所需的excel文件。


#### 使用说明
1. 源码建议使用的IDE开发环境是idea(64)。
2. 在idea中使用"File -> Open...",然后选择下载源码的路径来导入项目。
3. 导入项目源码后首先使用"File -> Project Structure..."来确认Java SDK版本，本项目为1.8。
4. 然后使用"File -> Settings... -> Build, Execution, Deployment -> Build Tools -> Maven"来确认Maven版本及配置，本次为Maven 3。


#### 发布运行
1. 本项目的打包运行方式是以jar包(而非war)方式来创建的。导出的jar包较大(43M)，没有放在项目源码中。
2. 如何导出jar包：在idea中打开"Maven Projects"视图(打开按钮一般默认在右侧的第四个)，依次展开"本项目名 -> Lifecycle",双击"package"开始打包。
3. 打包过程中会从Maven仓库中下载依赖包。如果下载失败，请参考网页(https://blog.csdn.net/yyjava/article/details/81504541)修改配置较为快速稳定的国内maven仓库地址。
4. 修改项目源码根目录中的application.properties配置文件，配置正确的Redis、数据库和Excel导出文件路径。然后上传到jar包同一目录下。
5. 用"java -jar ××××-0.0.1-SNAPSHOT.jar"命令运行jar包程序(内嵌Tomcat容器)。


#### 码云特技

1. 使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2. 码云官方博客 [blog.gitee.com](https://blog.gitee.com)
3. 你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解码云上的优秀开源项目
4. [GVP](https://gitee.com/gvp) 全称是码云最有价值开源项目，是码云综合评定出的优秀开源项目
5. 码云官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6. 码云封面人物是一档用来展示码云会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
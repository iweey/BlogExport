# 公众号跟博客导出工具。



## 导出公众号文章

1.安装wkhtmltopdf，把程序路径加入到环境变量PATH中

2.安装nodejs和npm

3.安装Anyproxy

npm install -g anyproxy 

4.启动Anyproxy

anyproxy --intercept --silent true  --rule ~/项目名/src/main/resources/weixinSpider.js

5.修改系统代理    默认端口是8001。下面是Mac的设置页面，Windows类似。

![mac代理](https://github.com/laijiawei/BlogExport/blob/main/docs/image/system_proxy.png)

6.运行项目的Application类

7.打开PC端微信，找到要导出的公众号页面，点击下面按钮打开历史文章目录

![打开历史文章目录](https://github.com/laijiawei/BlogExport/blob/main/docs/image/weixin_history.png)

8.看后台日志，等到爬虫任务完成，完成之后会生成历史文章合并的pdf，默认放到项目路径下的export目录



## 导出新浪博客

1.安装wkhtmltopdf，把程序路径加入到环境变量PATH中

2.修改StartSinaBlog类的配置,把下面一串数字填写到userId中

![用户id](https://github.com/laijiawei/BlogExport/blob/main/docs/image/sinablog_userid.png)

3.启动StartSinaBlog类

4.看后台日志，等到爬虫任务完成，完成之后会生成历史文章合并的pdf，默认放到项目路径下的export目录

## 未来的开发计划

1.生成word格式

2.导出微信文章的评论

3.开发https的代理解析工具，替换Anyproxy

4.增加CSDN 博客园  雪球的文章导出



## 我的联系方式

 qq  876733756

微信 lai876733756

邮箱 laijiawei123@gmail.com

# 公众号跟博客导出工具。



## 导出公众号文章

1.安装wkhtmltopdf，把程序路径加入到环境变量path中

2.安装nodejs和npm

3.安装Anyproxy

npm install -g anyproxy 

4.启动Anyproxy

anyproxy --intercept --silent true  --rule ~/项目名/src/main/resources/weixinSpider.js

5.修改系统代理    默认端口是8001

![mac代理](https://raw.github.com/laijiawei/repositpry/master/BlogExport/doc/image/system_proxy.png)

6.运行项目的Application类

7.打开PC端微信，点击历史文章按钮

![历史文章按钮](https://raw.github.com/laijiawei/repositpry/master/BlogExport/doc/image/weixin_history.png)

![历史文章目录](https://raw.github.com/laijiawei/repositpry/master/BlogExport/doc/image/weixin_article_dic.png)

8.看后台日志，等到任务完成，生成的pdf默认放到项目路径下的export目录



## 导出新浪博客

1.安装wkhtmltopdf，加入到环境变量

2.修改StartSinaBlog类的博客的userId,启动StartSinaBlog类

3.等到任务完成，生成的pdf默认放到项目路径下的export目录





## 未来的开发计划

1.生成word格式

2.导出微信文章的评论

3.开发https的代理解析工具，替换Anyproxy






# SpringBooTTemplate


# 使用方法

1.进入toolgpt开启python解释器

```bash
uvicorn server_fastAPI:app --reload --host 0.0.0.0
```

2.进入Java主项目

1.修改resultExPythonImpl.java中的apiUrl为你开启后端解释器机器的IP地址

```java
String apiUrl = "http://10.211.55.2:8000/execute";
```

2.在GptServiceImpl.java中填写你的OpenAI keys以及代理服务器

```java
String token = "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";
String proxyHost = "127.0.0.1";
int proxyPort = 8118;
```

3.启动Java项目，访问http://localhost:8080/chat.html



# 后续

流式传输实现那其实有更好的库，之后会引入；本项目只是一个实验性demo，最终目的其实是开发一套更为全面的CodeInterpreter功能，让GPT作为大脑使用众多的AI工具，之后还会围绕着多用户带来的并发访问问题进行修改，还有代码规范啊比如模块化一下keys和promte的管理，这部分其实蛮难的，又想让人看得懂，又不希望添加过多的文件，目前一些实现说实话封装的奶奶都不认识了，就API的参数都要封装成枚举类就...

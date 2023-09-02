# GPTInterpreter

# 展示
提醒：由于老家发水灾断网，所以开的手机热点，访问openai的接口偶尔会卡住，特别是第二张图，这并不是代码解释器的瓶颈，而是因为网络环境太差回调函数一直在等【end】标志。
结论：没有代码解释器，谁来了都不好使！！大语言模型不适合直接进行计算任务（百度还是挺厉害的）。

## GPTInterpreter

![MY_排序](https://raw.githubusercontent.com/ShuaiZhang1998/figure/main/figure/202308301738579.gif)



![my_计算](https://raw.githubusercontent.com/ShuaiZhang1998/figure/main/figure/202308301754926.gif)



## 豆包（字节）



![豆包计算](https://raw.githubusercontent.com/ShuaiZhang1998/figure/main/figure/202308301755395.gif)



## 文言一心



![文言一心计算](https://raw.githubusercontent.com/ShuaiZhang1998/figure/main/figure/202308301756050.gif)



## GPT3.5



![GPT3.5计算](https://raw.githubusercontent.com/ShuaiZhang1998/figure/main/figure/202308301757481.gif)



## GPT4

![GPT4计算](https://raw.githubusercontent.com/ShuaiZhang1998/figure/main/figure/202308301800655.gif)



## GPT4包含代码解释器

![GPT4代码解释器](https://raw.githubusercontent.com/ShuaiZhang1998/figure/main/figure/202308301759375.gif)

# 使用方法

1.进入toolgpt开启python解释器

```bash
uvicorn server_fastAPI:app --reload --host 0.0.0.0
```

2.进入Java主项目

1.修改application.yml为开启python解释器的服务地址和端口

```java
String apiUrl = "http://10.211.55.2:8000/execute";
```

2.在application.yml中填写你的OpenAI keys以及代理服务器

```java
String token = "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";
String proxyHost = "127.0.0.1";
int proxyPort = 8118;
```

3.启动Java项目，访问http://localhost:8080/chat.html



# 后续

流式传输实现那其实有更好的库，之后会引入；本项目只是一个实验性demo，最终目的其实是开发一套更为全面的CodeInterpreter功能，让GPT作为大脑使用众多的AI工具，之后还会围绕着多用户带来的并发访问问题进行修改，还有代码规范啊比如模块化一下keys和promte的管理，这部分其实蛮难的，又想让人看得懂，又不希望添加过多的文件，目前一些实现说实话封装的奶奶都不认识了，就API的参数都要封装成枚举类就...



# 日志

2023/08/23

优化python代码解释器部分[PythonExecutionServiceImpl]

- 构建了一个新的bean用于注入RestTemplate，并将默认content-type设置为json格式
- 把服务地址抽出来放到了配置文件里
- 命名修改，注释修改

优化GPT调用部分[GptServiceImpl]

- 为RestTemplate添加了代理支持，使用注解开启(详细看配置文件中的RestTemPlateConfig部分)
- 建造者模式重构GPT的阻塞式调用


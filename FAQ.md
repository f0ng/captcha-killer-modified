# 1.用法

#### [releases](https://github.com/f0ng/captcha-killer-modified/releases/)下载最新插件与验证码识别端(`captcha-killer-modified.jar`、`codereg.py`)
#### 使用Burp加载`captcha-killer-modified.jar`
#### 再使用`python3 codereg.py`开启验证码识别模块，前提安装[ddddocr](https://github.com/sml2h3/ddddocr)
#### <a id="Template">模板</a>
```
POST /reg HTTP/1.1
Host: 127.0.0.1:8888
User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:97.0) Gecko/20100101 Firefox/97.0
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8
Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2
Accept-Encoding: gzip, deflate
Connection: keep-alive
Upgrade-Insecure-Requests: 1
Content-Type: application/x-www-form-urlencoded
Content-Length: 8332

<@BASE64><@IMG_RAW></@IMG_RAW></@BASE64>
```
#### 接口地址设置为`http://127.0.0.1:8888`，即可开始识别
#### 其余用法移步[captcha-killer用法](https://gv7.me/articles/2019/burp-captcha-killer-usage/)

# 2.加载插件错误

#### 0x01 检查加载的是否是jar文件
#### 0x02 与启动burp的jdk版本有关，可以适当选择与`jdk10`相近的版本启动

# 3.验证码识别错误

#### 1.查看模板是否正确,<a href="#Template">模板</a>
#### 2.检查`python3 codereg.py`是否正常启动，可以本地尝试使用ddddocr进行识别验证码验证
#### 3.重新下载本插件与验证码服识别端进行加载

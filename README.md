# captcha-killer-modified 适配新版Burpsuite

## 原项目地址：  https://github.com/c0ny1/captcha-killer

# [用法与常见报错](https://github.com/f0ng/captcha-killer-modified/blob/main/FAQ.md)

## 免责声明

该工具仅用于安全自查检测

由于传播、利用此工具所提供的信息而造成的任何直接或者间接的后果及损失，均由使用者本人负责，作者不为此承担任何责任。

本人拥有对此工具的修改和解释权。未经网络安全部门及相关部门允许，不得善自使用本工具进行任何攻击活动，不得以任何方式将其用于商业目的。

### 文章案例
>https://github.com/c0ny1/captcha-killer  [插件源项目]
>
>https://gv7.me/articles/2019/burp-captcha-killer-usage/ [插件用法]
>
>https://github.com/sml2h3/ddddocr [验证码识别项目]
>
>https://github.com/PoJun-Lab/blaster [验证码登录爆破]
>
>https://www.cnblogs.com/4geek/p/17145385.html#!comments [captcha-killer-modified详细用法及部分问题解决方案(如验证码识别位数问题)]

交流群

<img width="389" alt="image" src="https://user-images.githubusercontent.com/48286013/204838629-e289f0fe-3bd8-4393-82ef-a2a19d7b7f4c.png">


二维码失效请加微信`f-f0ng`、备注captchakillermodified交流

关注主页公众号（only security），回复`captchakillermodified`获取下载地址】





### 提issue之前请说明如下字段：
1. burp版本
2. 启动burp的jdk版本
3. burp的Extender中Options配置的jdk版本

# 捐赠 （如果项目有帮助到您，可以选择捐赠一些费用用于captcha-killer-modified的后续版本维护，本项目长期维护）

<img width="251" alt="image" src="https://github.com/f0ng/autoDecoder/assets/48286013/5151b992-b98a-4cef-a6c7-e83e068eb363">

<img width="251" alt="image" src="https://github.com/f0ng/autoDecoder/assets/48286013/e9318b91-2521-4c14-93d8-9737fd7a4729">


# 赞助合作商

|                                                            | 赞助合作商 | 推荐理由                                                                                             |
|------------------------------------------------------------|------------|--------------------------------------------------------------------------------------------------|
| ![YesCaptcha](https://cdn.wenanzhe.com/img/yescaptcha.png) | [YesCaptcha](https://yescaptcha.com/i/bmHz3C) | 谷歌reCaptcha验证码 / hCaptcha验证码 / funCaptcha验证码商业级识别接口 [点我](https://yescaptcha.com/i/bmHz3C) 直达VIP4 |

## 插件优化的地方
1. 修改了原项目中`sun.misc.BASE64Encoder`报错的问题

2. 优化了验证码`data:image`识别问题

3. 添加了ddddocr验证码识别库

4. 增加自定义关键词获取验证码

<img width="1439" alt="image" src="https://user-images.githubusercontent.com/48286013/159115886-7e482f8e-d36a-416a-8b67-c535e741d114.png">



 <img src="https://user-images.githubusercontent.com/48286013/159009462-b028fb9f-05de-4c82-ae72-f576fa0adf8c.png" width="160" height="800" />
 
  <img src="https://user-images.githubusercontent.com/48286013/159009480-b4dcb61e-7798-49cc-a118-dfd1e02ae592.png" width="115" height="650" />
    
识别成功率在85%左右。

具体修改请查看微信公众号文章
https://mp.weixin.qq.com/s/_P6OlL1xQaYSY1bvZJL4Uw


## 更新日志

【2022-3-21】 增加可识别情况，~~当出现关键字为B/base64时，进行验证码识别~~

【2022-3-24】 增加自定义关键字，删减锁定按钮

<img width="630" alt="image" src="https://user-images.githubusercontent.com/48286013/159827943-7ee4480c-b090-42e6-a5c7-485fc6fb500e.png">

<img width="614" alt="image" src="https://user-images.githubusercontent.com/48286013/159828004-821758a5-3626-4446-b951-0527377c8c14.png">

【2022-3-30】适配`data:image\/png`与base64中出现`\r\n`情况

<img width="627" alt="image" src="https://user-images.githubusercontent.com/48286013/160766851-c5b4a872-9be6-4afb-a547-1af843a1e101.png">

【2022-4-12】提升准确性，修改识别验证码端代码，主要修改如下：

1. 增加basic认证，方便部署在公网，使用`tmux`在后台运行即可

2. 对验证码识别部分进行修改，针对识别出来多位，可以进行自行删改，举例，如验证码是四位，但是ddddocr识别出来了五位，那么可以截取`text=ocr.classification(img_bytes)[0:4]`前四位；
   
   如ddddocr对特定类验证码的识别中字母`O`与数字`0`识别混淆，可以进行替换`text=ocr.classification(img_bytes).replace("0","O")`

【2022-7-2】

1. 优化验证码对于base64的识别#10 ，原因在于base64编码中存在`\n`，`0.16`版本增加对`\n`的处理，感谢@DreamAndSun 师傅反馈

【2022-11-30】 0.17

1. 添加响应提取，针对获取验证码请求中有类似token字段，在登录包的同时需要token校验的情况，在需要token校验的字段使用`@captcha-killer-modified@`
<img width="650" alt="image" src="https://user-images.githubusercontent.com/48286013/204822669-7ea6022e-8028-4526-a653-03488a196d48.png">

2. 增加对验证码进行二次处理的案例(验证码为gif图，且验证码具体是在gif图的第二帧，无法直接识别)，见[用法与常见报错](https://github.com/f0ng/captcha-killer-modified/blob/main/FAQ.md)

【2022-12-9】 0.18
1. 添加`@captcha@`参数替代验证码，方便在repeater参数内进行测试

<img width="804" alt="image" src="https://user-images.githubusercontent.com/48286013/206609271-5cc8bdcf-2141-4616-9a60-7ab9493f18c2.png">

【2022-12-14】 0.19

增加URL解码、过滤图片编码中的`.`

【2022-12-23】 0.20

修复了url识别问题、爆破顺序错乱问题、响应包直接为base64编码导致爆破失败问题

【2023-2-1】 0.21

- 增加默认验证码模板`ddddocr`，适配`codereg.py`

<img width="675" alt="image" src="https://user-images.githubusercontent.com/48286013/215937694-d494a9b9-0b31-4c5c-adf8-0cb24e60f43c.png">

- 增加识别结果关键字显示，方便查看关键字是否与验证码对应
<img width="493" alt="image" src="https://user-images.githubusercontent.com/48286013/215937812-f43d592e-170b-4fb0-86e4-2f34fc16bb0e.png">

【2023-2-10】 0.21-beta    
- 优化验证码编码中的`\n`处理
- 优化`@captcha@`的判断方式

【2023-3-14】 0.22 重要问题修复
- 修复了装载插件会影响proxy选项卡的问题

【2023-3-28】 0.23 
- 增加[验证码返回包中明文返回验证码爆破案例](https://github.com/f0ng/captcha-killer-modified/blob/main/FAQ.md#13-%E9%AA%8C%E8%AF%81%E7%A0%81%E5%93%8D%E5%BA%94%E5%8C%85%E6%9C%89%E6%98%8E%E6%96%87%E9%AA%8C%E8%AF%81%E7%A0%81%E5%A6%82%E4%BD%95%E9%85%8D%E5%90%88%E5%B7%A5%E5%85%B7%E4%BD%BF%E7%94%A8)
- base64编码中应对`fromUrlSafe`函数(`-`转义为`+`,`_`转义为`/`)

【2023-5-22】 0.24
- 修复验证码在intruder中无法显示的bug
- 再次修复了装载插件会影响proxy选项卡的问题

【2023-7-2】 0.24.1
- 修复加载插件影响intruder速度的问题(临时增加了一个按钮控制是否开启该插件)

<img width="642" alt="image" src="https://github.com/f0ng/captcha-killer-modified/assets/48286013/8c8132ac-dd38-494f-aa47-80b6db4a7c93">

【2023-9-15】 0.24.2
- 优化@captcha-killer-modified@关键字

【2023-12-5】 0.24.3
- 修复新版burp获取不到验证码问题

【2024-1-4】 0.24.4
- 服务端识别代码增加算术接口，可以进行算术验证码的识别

【2024-4-2】 0.24.5
1. 针对复杂算数验证码，进行训练获得模型，若有训练验证码的需求，可以联系作者代为训练，需捐赠，捐赠具体费用可以联系作者。这里取若依的验证码进行演示，测试了109个验证码，识别错误1个，准确率98%+
<img width="159" alt="image" src="https://github.com/f0ng/captcha-killer-modified/assets/48286013/077968b6-97ba-4366-baf9-ceba078020e7">

2. 添加两个接口，添加reg2【识别无混淆的四则运算，项目默认模板】、reg3模板【识别混淆变形的若依四则运算验证码，默认模板不支持，需额外捐赠，捐赠具体费用可以联系作者】


![f](https://starchart.cc/f0ng/captcha-killer-modified.svg)

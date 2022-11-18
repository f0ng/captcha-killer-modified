# captcha-killer-modified 适配新版Burpsuite

## 原项目地址：

## https://github.com/c0ny1/captcha-killer

[用法与常见报错](https://github.com/f0ng/captcha-killer-modified/blob/main/FAQ.md)

关注主页公众号（only security），回复`captchakillermodified`获取下载地址】

### 提issue之前请说明如下字段：
1. burp版本
2. 启动burp的jdk版本
3. burp的Extender中Options配置的jdk版本

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


>https://github.com/c0ny1/captcha-killer  [插件源项目]
>
>https://gv7.me/articles/2019/burp-captcha-killer-usage/ [插件用法]
>
>https://github.com/sml2h3/ddddocr [验证码识别项目]
>
>https://github.com/PoJun-Lab/blaster [验证码登录爆破]
>


![f](https://starchart.cc/f0ng/captcha-killer-modified.svg)

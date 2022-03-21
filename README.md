# captcha-killer-modified 适配新版Burpsuite

## 原项目地址：

## https://github.com/c0ny1/captcha-killer


1.修改了原项目中`sun.misc.BASE64Encoder`报错的问题

2.优化了验证码`data:image`识别问题

3.添加了ddddocr验证码识别库

<img width="1439" alt="image" src="https://user-images.githubusercontent.com/48286013/159115886-7e482f8e-d36a-416a-8b67-c535e741d114.png">



 <img src="https://user-images.githubusercontent.com/48286013/159009462-b028fb9f-05de-4c82-ae72-f576fa0adf8c.png" width="160" height="800" />
 
  <img src="https://user-images.githubusercontent.com/48286013/159009480-b4dcb61e-7798-49cc-a118-dfd1e02ae592.png" width="115" height="650" />
    
识别成功率在85%左右。

具体修改请查看微信公众号文章
https://mp.weixin.qq.com/s/_P6OlL1xQaYSY1bvZJL4Uw


## 更新日志

【2022-3-21】 增加可识别情况，当出现关键字为B/base64时，进行验证码识别

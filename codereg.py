# -*- coding:utf-8 -*-
# author:f0ngf0ng
# @Date: 2022/3/11 下午1:44
import argparse
import ddddocr                       # 导入 ddddocr
from aiohttp import web
import base64
print("欢迎使用captcha-killer-modified服务端脚本，项目地址:https://github.com/f0ng/captcha-killer-modified\n谷歌reCaptcha验证码 / hCaptcha验证码 / funCaptcha验证码商业级识别接口：https://yescaptcha.com/i/bmHz3C\n\n")
parser = argparse.ArgumentParser()

parser.add_argument("-p", help="http port",default="8888")
args = parser.parse_args()
ocr = ddddocr.DdddOcr()
port = args.p

auth_base64 = "f0ngauth" # 可自定义auth认证
# 识别常规验证码
async def handle_cb2(request):
    if request.headers.get('Authorization') != 'Basic ' + auth_base64:
        return web.Response(text='Forbidden', status='403')
    # print(await request.text())
    img_base64 = await request.text()
    img_bytes = base64.b64decode(img_base64)
    # return web.Response(text=ocr.classification(img_bytes)[0:4]) 验证码取前四位
    # return web.Response(text=ocr.classification(img_bytes)[0:4].replace("0","o")) 验证码取前四位、验证码中的0替换为o
    res = ocr.classification(img_bytes)
    print(res)

    return web.Response(text=ocr.classification(img_bytes)[0:10])

# 识别算术验证码
async def handle_cb(request):
    zhi = ""
    if request.headers.get('Authorization') != 'Basic ' + auth_base64:
        return web.Response(text='Forbidden', status='403')
    # print(await request.text())
    img_base64 = await request.text()
    img_bytes = base64.b64decode(img_base64)
    res = ocr.classification(img_bytes).replace("=","").replace("?","")
    print(res)
    if'+'in res:
        zhi =  int(res.split('+')[0])+int(res.split('+')[1][:-1])
        print(zhi)
        return web.Response(text=str(zhi))
    elif'-'in res:
        zhi =  int(res.split('-')[0])-int(res.split('-')[1][:-1])
        print(zhi)
        return web.Response(text=str(zhi))
    elif'*'in res:
        zhi =  int(res.split('*')[0])*int(res.split('*')[1][:-1])
        print(zhi)
        return web.Response(text=str(zhi))
    elif 'x' in res:
        zhi = int(res.split('x')[0])*int(res.split('x')[1][:-1])
        print(zhi)
        return web.Response(text=str(zhi))
    elif '/'in res:
        zhi =  int(res.split('/')[0])/int(res.split('/')[1][:-1])
        return web.Response(text=str(int(zhi)))
    else:
        return web.Response(text=res)



app = web.Application()
app.add_routes([
    web.post('/reg2', handle_cb), # 识别算数验证码
    web.post('/reg', handle_cb2), # 识别常规验证码
])

if __name__ == '__main__':

    web.run_app(app, port=port)
    

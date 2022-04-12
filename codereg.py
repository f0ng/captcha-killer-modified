# -*- coding:utf-8 -*-
# author:f0ngf0ng
# @Date: 2022/3/11 下午1:44
import argparse
import ddddocr                       # 导入 ddddocr
from aiohttp import web
import base64

parser = argparse.ArgumentParser()

parser.add_argument("-p", help="http port",default="8888")
args = parser.parse_args()
ocr = ddddocr.DdddOcr()
port = args.p

auth_base64 = "f0ngauth" # 可自定义auth认证

async def handle_cb(request):
    if request.headers.get('Authorization') != 'Basic ' + auth_base64:
        return web.Response(text='Forbidden', status='403')
    print(await request.text())
    img_base64 = await request.text()
    img_bytes = base64.b64decode(img_base64)
    # return web.Response(text=ocr.classification(img_bytes)[0:4]) 验证码取前四位
    # return web.Response(text=ocr.classification(img_bytes)[0:4].replace("0","o")) 验证码取前四位、验证码中的0替换为o
    return web.Response(text=ocr.classification(img_bytes)[0:4])

app = web.Application()
app.add_routes([
    web.post('/reg', handle_cb),
])

if __name__ == '__main__':
    web.run_app(app, port=port)

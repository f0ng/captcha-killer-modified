# -*- coding: utf-8 -*-
# @Time    : 2024/4/19 19:25
# @Software: f0ng
import argparse
import ddddocr  # 导入 ddddocr
from aiohttp import web
import base64

print(
    "欢迎使用captcha-killer-modified服务端脚本，项目地址:https://github.com/f0ng/captcha-killer-modified\n谷歌reCaptcha验证码 / hCaptcha验证码 / funCaptcha验证码商业级识别接口：https://yescaptcha.com/i/bmHz3C\n\n")
parser = argparse.ArgumentParser()

parser.add_argument("-p", help="http port", default="8888")
args = parser.parse_args()
ocr = ddddocr.DdddOcr()
port = args.p

auth_base64 = "f0ngauth"  # 可自定义auth认证


# 识别纯整数0-9
async def handle_cb00(request):
    if request.headers.get('Authorization') != 'Basic ' + auth_base64:
        return web.Response(text='Forbidden', status='403')
    ocr.set_ranges(0)
    img_base64 = await request.text()
    img_bytes = base64.b64decode(img_base64)
    # return web.Response(text=ocr.classification(img_bytes)[0:4]) 验证码取前四位
    # return web.Response(text=ocr.classification(img_bytes)[0:4].replace("0","o")) 验证码取前四位、验证码中的0替换为o
    res = ocr.classification(img_bytes,probability=True)
    s = ""
    for i in res['probability']:
        s += res['charsets'][i.index(max(i))]
    print(s)
    return web.Response(text=s)

# 识别纯小写英文a-z
async def handle_cb01(request):
    if request.headers.get('Authorization') != 'Basic ' + auth_base64:
        return web.Response(text='Forbidden', status='403')
    ocr.set_ranges(1)
    img_base64 = await request.text()
    img_bytes = base64.b64decode(img_base64)
    # return web.Response(text=ocr.classification(img_bytes)[0:4]) 验证码取前四位
    # return web.Response(text=ocr.classification(img_bytes)[0:4].replace("0","o")) 验证码取前四位、验证码中的0替换为o
    res = ocr.classification(img_bytes,probability=True)
    s = ""
    for i in res['probability']:
        s += res['charsets'][i.index(max(i))]
    print(s)
    return web.Response(text=s)

# 识别纯大写英文A-Z
async def handle_cb02(request):
    if request.headers.get('Authorization') != 'Basic ' + auth_base64:
        return web.Response(text='Forbidden', status='403')
    ocr.set_ranges(2)
    img_base64 = await request.text()
    img_bytes = base64.b64decode(img_base64)
    # return web.Response(text=ocr.classification(img_bytes)[0:4]) 验证码取前四位
    # return web.Response(text=ocr.classification(img_bytes)[0:4].replace("0","o")) 验证码取前四位、验证码中的0替换为o
    res = ocr.classification(img_bytes,probability=True)
    s = ""
    for i in res['probability']:
        s += res['charsets'][i.index(max(i))]
    print(s)
    return web.Response(text=s)

# 识别小写英文a-z + 大写英文A-Z
async def handle_cb03(request):
    if request.headers.get('Authorization') != 'Basic ' + auth_base64:
        return web.Response(text='Forbidden', status='403')
    ocr.set_ranges(3)
    img_base64 = await request.text()
    img_bytes = base64.b64decode(img_base64)
    # return web.Response(text=ocr.classification(img_bytes)[0:4]) 验证码取前四位
    # return web.Response(text=ocr.classification(img_bytes)[0:4].replace("0","o")) 验证码取前四位、验证码中的0替换为o
    res = ocr.classification(img_bytes,probability=True)
    s = ""
    for i in res['probability']:
        s += res['charsets'][i.index(max(i))]
    print(s)
    return web.Response(text=s)

# 识别小写英文a-z + 整数0-9
async def handle_cb04(request):
    if request.headers.get('Authorization') != 'Basic ' + auth_base64:
        return web.Response(text='Forbidden', status='403')
    ocr.set_ranges(4)
    img_base64 = await request.text()
    img_bytes = base64.b64decode(img_base64)
    # return web.Response(text=ocr.classification(img_bytes)[0:4]) 验证码取前四位
    # return web.Response(text=ocr.classification(img_bytes)[0:4].replace("0","o")) 验证码取前四位、验证码中的0替换为o
    res = ocr.classification(img_bytes,probability=True)
    s = ""
    for i in res['probability']:
        s += res['charsets'][i.index(max(i))]
    print(s)
    return web.Response(text=s)

# 识别大写英文A-Z + 整数0-9
async def handle_cb05(request):
    if request.headers.get('Authorization') != 'Basic ' + auth_base64:
        return web.Response(text='Forbidden', status='403')
    ocr.set_ranges(5)
    img_base64 = await request.text()
    img_bytes = base64.b64decode(img_base64)
    # return web.Response(text=ocr.classification(img_bytes)[0:4]) 验证码取前四位
    # return web.Response(text=ocr.classification(img_bytes)[0:4].replace("0","o")) 验证码取前四位、验证码中的0替换为o
    res = ocr.classification(img_bytes,probability=True)
    s = ""
    for i in res['probability']:
        s += res['charsets'][i.index(max(i))]
    print(s)
    return web.Response(text=s)


# 识别小写英文a-z + 大写英文A-Z + 整数0-9
async def handle_cb06(request):
    if request.headers.get('Authorization') != 'Basic ' + auth_base64:
        return web.Response(text='Forbidden', status='403')
    ocr.set_ranges(6)
    img_base64 = await request.text()
    img_bytes = base64.b64decode(img_base64)
    # return web.Response(text=ocr.classification(img_bytes)[0:4]) 验证码取前四位
    # return web.Response(text=ocr.classification(img_bytes)[0:4].replace("0","o")) 验证码取前四位、验证码中的0替换为o
    res = ocr.classification(img_bytes,probability=True)
    s = ""
    for i in res['probability']:
        s += res['charsets'][i.index(max(i))]
    print(s)
    return web.Response(text=s)

# 识别自定义字符，默认为识别算术
async def handle_cb000(request):
    if request.headers.get('Authorization') != 'Basic ' + auth_base64:
        return web.Response(text='Forbidden', status='403')
    ocr.set_ranges(request.headers.get('ranges'))
    print(request.headers.get('ranges'))
    img_base64 = await request.text()
    img_bytes = base64.b64decode(img_base64)
    # return web.Response(text=ocr.classification(img_bytes)[0:4]) 验证码取前四位
    # return web.Response(text=ocr.classification(img_bytes)[0:4].replace("0","o")) 验证码取前四位、验证码中的0替换为o
    res = ocr.classification(img_bytes,probability=True)
    s = ""
    for i in res['probability']:
        s += res['charsets'][i.index(max(i))]
    print(s)
    if '+' in s:
        zhi = int(s.split('+')[0]) + int(s.split('+')[1][:-1])
        print(zhi)
        return web.Response(text=str(zhi))
    elif '-' in s:
        zhi = int(s.split('-')[0]) - int(s.split('-')[1][:-1])
        print(zhi)
        return web.Response(text=str(zhi))
    elif '*' in s:
        zhi = int(s.split('*')[0]) * int(s.split('*')[1][:-1])
        print(zhi)
        return web.Response(text=str(zhi))
    elif 'x' in s:
        zhi = int(s.split('x')[0]) * int(s.split('x')[1][:-1])
        print(zhi)
        return web.Response(text=str(zhi))
    elif '/' in s:
        zhi = int(s.split('/')[0]) / int(s.split('/')[1][:-1])
        return web.Response(text=str(zhi))
    else:
        return web.Response(text=s)



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
    res = ocr.classification(img_bytes).replace("=", "").replace("?", "")
    print(res)
    if '+' in res:
        zhi = int(res.split('+')[0]) + int(res.split('+')[1][:-1])
        print(zhi)
        return web.Response(text=str(zhi))
    elif '-' in res:
        zhi = int(res.split('-')[0]) - int(res.split('-')[1][:-1])
        print(zhi)
        return web.Response(text=str(zhi))
    elif '*' in res:
        zhi = int(res.split('*')[0]) * int(res.split('*')[1][:-1])
        print(zhi)
        return web.Response(text=str(zhi))
    elif 'x' in res:
        zhi = int(res.split('x')[0]) * int(res.split('x')[1][:-1])
        print(zhi)
        return web.Response(text=str(zhi))
    elif '/' in res:
        zhi = int(res.split('/')[0]) / int(res.split('/')[1][:-1])
        return web.Response(text=str(zhi))
    else:
        return web.Response(text=res)


app = web.Application()
app.add_routes([
    web.post('/reg2', handle_cb),  # 识别算数验证码
    web.post('/reg', handle_cb2),  # 识别常规验证码

    web.post('/reg00', handle_cb00),  # 识别纯整数0-9
    web.post('/reg01', handle_cb01),  # 识别纯小写英文a-z
    web.post('/reg02', handle_cb02),  # 识别纯大写英文A-Z
    web.post('/reg03', handle_cb03),  # 识别小写英文a-z + 大写英文A-Z
    web.post('/reg04', handle_cb04),  # 识别小写英文a-z + 整数0-9
    web.post('/reg05', handle_cb05),  # 识别大写英文A-Z + 整数0-9
    web.post('/reg06', handle_cb06),  # 识别小写英文a-z + 大写英文A-Z + 整数0-9
    web.post('/reg000', handle_cb000),  # 识别自定义
])

if __name__ == '__main__':
    web.run_app(app, port=int(port))


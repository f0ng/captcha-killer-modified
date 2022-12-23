/**
 * Copyright (c) 2019 c0ny1 (https://github.com/c0ny1/captcha-killer)
 * License: MIT
 */
package utils;

import burp.BurpExtender;
import burp.IHttpRequestResponse;
import burp.IRequestInfo;
import entity.HttpService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {
    private String url;
    private String protocol;
    private String method;
    private String httpversion;
    private String host;
    private int port;
    private String path;
    private HttpService service;
    private Map<String,String> headers = new HashMap<String, String>();
    private String data;
    private String raw;
    private byte[] byteImg;

    public HttpClient(String url,String raw,byte[] byteImg) throws IOException {
        this.url = url;
        this.raw = processLine(raw);
        this.byteImg = byteImg;
        //解析标签
        parseLabel();
        //解析Request各个属性
        parserRequest();
        //更新Content-Length
        updateContentLength();
    }

    /**
     * 将请求包头中\n替换为\r\n,因为\n可能会导致某些服务器报错，无法正确识别请求包。
     * @param reqraw
     * @return
     */
    public String processLine(String reqraw){
        String method = null;
        String header = null;
        String body = null;
        String request = null;

        if(reqraw.startsWith("GET")){
            method = "GET";
        }else if(reqraw.startsWith("POST")){
            method = "POST";
        }else{
            method = "unkown";
            return reqraw;
        }

        if(method.equals("GET")) {
            header = reqraw;

            //将所有\n替换为\r\n,注意\r\n和\n混合情况下的替换。
            header.replace("\r\n","\n");
            header.replace("\n","\r\n");

            request = header;
        }

        if(method.equals("POST")){
            int n = reqraw.indexOf("\n\n") != -1 ? reqraw.indexOf("\n\n") : reqraw.indexOf("\r\n\r\n");
            header = reqraw.substring(0, n).trim();
            body = reqraw.substring(n + 1, reqraw.length()).trim();
            if(header.indexOf("\n")>=0 && header.indexOf("\r\n") <0){
                header = header.replace("\n","\r\n");
            }
            request = header + "\r\n\r\n" + body;
        }
        return request;
    }


    public String getHttpService(){
        return service.toString();
    }

    public String getRaw(){
        return this.raw;
    }


    /**
     * 解析标签，可以参考下
     */
    public void parseLabel() throws IOException {
        LableParser parser = new LableParser(byteImg);
//        System.out.println("######################");
//        System.out.println(raw);
        raw = parser.parseAllLable(raw);
//        System.out.println("**********************");
//        System.out.println(raw);
    }

    private void parserRequest(){
//        if(Util.isURL(this.url)){
            service = new HttpService(this.url);

            try {
                IRequestInfo requestInfo = BurpExtender.helpers.analyzeRequest(service, this.raw.getBytes());
                requestInfo.getBodyOffset();

                this.method = requestInfo.getMethod();

                for (String header : requestInfo.getHeaders()) {
                    if (header.indexOf(this.method) >= 0 && header.indexOf("HTTP/") >= 0) {
                        this.path = header.split(" ")[1];
                        this.httpversion = header.split(" ")[2];
                        continue;
                    }

                    if (header.indexOf(":") > 0) {
                        String key = header.substring(0, header.indexOf(":"));
                        String value = Util.trimStart(header.substring(header.indexOf(":")+1, header.length()));
                        this.headers.put(key, value);
                    }
                }

                if (this.method.equals("POST")) {
//                    System.out.println("oldd" + data);
//                    System.out.println(this.raw);
                    this.data = this.raw.substring(requestInfo.getBodyOffset(), this.raw.length());
//                    System.out.println("neww" + data);
                }
            }catch (Exception e){
//                BurpExtender.stderr.println(e.getMessage());
            }
//        }
    }

    private void parserRequestOld(){
        if(Util.isURL(this.url)){
            service = new HttpService(this.url);
            String[] rawsArray = this.raw.split(System.lineSeparator());
            try {
                for (int i = 0; i < rawsArray.length; i++) {
                    if (i == 0) {
                        this.method = rawsArray[0].split(" ")[0];
                        this.path = rawsArray[0].split(" ")[1];
                        this.httpversion = rawsArray[0].split(" ")[2];
                    } else if (this.method.equals("POST") && i == rawsArray.length - 1) {
                        this.data = rawsArray[i].trim();
                    } else {
                        if (rawsArray[i].indexOf(": ") > 0) {
                            String key = rawsArray[i].split(": ")[0];
                            String value = rawsArray[i].split(": ")[1];
                            this.headers.put(key.trim(), value.trim());
                        }
                    }
                }
            }catch (Exception e){
                BurpExtender.stdout.println(e.getMessage());
            }
        }
    }





    /**
     * 更新请求包的Content-Length头
     * 注意：不更新该头部，可能会导致服务端无法获取完整的请求信息。
     * @return
     */
    public void updateContentLength(){
        /**
         * 在处理GET数据包时,要注意包结果严格来讲最后要有两个\r\n。有的web服务器对数据包要求比较严格，可能会导致请求识别。
         * 该问题曾出现在请求某网站的验证码时，返回了403状态。
         */
        System.out.println(method);
//        System.out.println(data);
        if(method.equals("POST")) {
            int length = data.length();
            headers.put("Content-Length", String.valueOf(length));
            String reqLine = String.format("%s %s %s",method,path,httpversion);
            reqLine += "\r\n";
            for(Map.Entry<String,String> header:headers.entrySet()){
                String line = String.format("%s: %s",header.getKey(),header.getValue());
                reqLine += line;
                reqLine += "\r\n";
            }

            reqLine += "\r\n";
            reqLine += data;
            this.raw = reqLine;
        }
    }


    public byte[] doReust(){
        byte[] req = raw.getBytes();
        try {
//            System.out.println(service);
            IHttpRequestResponse reqrsp = BurpExtender.callbacks.makeHttpRequest(service, req);
            byte[] response = reqrsp.getResponse();
            return response;
        }catch (Exception e){
            e.printStackTrace();
            BurpExtender.stderr.println(e);
        }
        return null;

    }
}

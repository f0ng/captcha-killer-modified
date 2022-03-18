/**
 * Copyright (c) 2019 c0ny1 (https://github.com/c0ny1/captcha-killer)
 * License: MIT
 */
package entity;

import burp.BurpExtender;
import burp.IHttpService;

import java.net.MalformedURLException;
import java.net.URL;

public class HttpService implements IHttpService {
    private String protocol;
    private String host;
    private int port;

    public HttpService(String url){
        if(url.startsWith("https://")){
            protocol = "https";
        }else{
            protocol = "http";
        }

        try {
            URL u = new URL(url);
            this.protocol = u.getProtocol();
            this.host = u.getHost();
            if(u.getPort() == -1){
                if(protocol.equals("https")){
                    this.port = 443;
                }else{
                    this.port = 80;
                }
            }else{
                this.port = u.getPort();
            }

        } catch (MalformedURLException e) {
            BurpExtender.stderr.println("[-] " + e.getMessage());
        }
    }

    public HttpService(String protocol, String host, int port){
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return String.format("%s://%s:%d",this.protocol,this.host,this.port);
    }
}

/**
 * Copyright (c) 2019 c0ny1 (https://github.com/c0ny1/captcha-killer)
 * License: MIT
 */
package entity;

import java.util.ArrayList;
import java.util.List;

public class TmplEntity {
    public static List<TmplEntity> tpls = new ArrayList<TmplEntity>();
    private String name;
    private HttpService service;
    private String reqpacke;
    private Rule rule;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HttpService getService() {
        return service;
    }

    public void setService(HttpService service) {
        this.service = service;
    }

    public String getReqpacke() {
        return reqpacke;
    }

    public void setReqpacke(String reqpacke) {
        this.reqpacke = reqpacke;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}

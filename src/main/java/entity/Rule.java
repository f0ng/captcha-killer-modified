/**
 * Copyright (c) 2019 c0ny1 (https://github.com/c0ny1/captcha-killer)
 * License: MIT
 */
package entity;

import com.alibaba.fastjson.JSONObject;

public class Rule {
    public final static int RULE_TYPE_RESPONSE_DATA = 0;
    public final static int RULE_TYPE_REGULAR = 1;
    public final static int RULE_TYPE_POSISTION = 2;
    public final static int RULE_TYPE_START_END_STRING = 3;
    public final static int RULE_TYPE_JSON_MATCH = 4;
    public final static int RULE_TYPE_XML_MATCH = 5;
    private int type;
    private String rule;
    private int nStart;
    private int nEnd;
    private String strStart;
    private String strEnd;

    public Rule(int type,String rule){
        this.type = type;
        if(type == RULE_TYPE_RESPONSE_DATA){
            this.rule = "response_data";
        }else if(type == RULE_TYPE_REGULAR){
            this.rule = rule;
        }else if(type == RULE_TYPE_POSISTION){
            int[] position = parsePositionRule(rule);
            this.nStart = position[0];
            this.nEnd = position[1];
        }else if(type == RULE_TYPE_START_END_STRING){
            String[] str = parseStartEndString(rule);
            this.strStart = str[0];
            this.strEnd = str[1];
        }else if(type == RULE_TYPE_JSON_MATCH){
            this.rule = rule;
        }else if(type == RULE_TYPE_XML_MATCH){
            this.rule = rule;
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRule() {
        if(type == RULE_TYPE_RESPONSE_DATA){
            return "request_data";
        }else if(type == RULE_TYPE_REGULAR){
            return rule;
        }else if(type == RULE_TYPE_POSISTION){
            return String.format("{\"start\":%d,\"end\":%d}",nStart,nEnd);
        }else if(type == RULE_TYPE_START_END_STRING){
            return String.format("{\"start\":%s,\"end\":%s}",strStart,strEnd);
        }else if(type == RULE_TYPE_JSON_MATCH){
            return rule;
        }else if(type == RULE_TYPE_XML_MATCH){
            return rule;
        }else{
            return rule;
        }

    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public int getnStart() {
        return nStart;
    }

    public void setnStart(int nStart) {
        this.nStart = nStart;
    }

    public int getnEnd() {
        return nEnd;
    }

    public void setnEnd(int nEnd) {
        this.nEnd = nEnd;
    }

    public String getStrStart() {
        return strStart;
    }

    public void setStrStart(String strStart) {
        this.strStart = strStart;
    }

    public String getStrEnd() {
        return strEnd;
    }

    public void setStrEnd(String strEnd) {
        this.strEnd = strEnd;
    }

    public static int[] parsePositionRule(String rule){
        JSONObject json = (JSONObject) JSONObject.parse(rule);
        int start = Integer.valueOf(json.get("start").toString());
        int end = Integer.valueOf(json.get("end").toString());
        return new int[]{start,end};
    }

    public static String[] parseStartEndString(String rule){
        JSONObject json = (JSONObject) JSONObject.parse(rule);
        String start = json.get("start").toString();
        String end = json.get("end").toString();
        return new String[]{start,end};
    }
}

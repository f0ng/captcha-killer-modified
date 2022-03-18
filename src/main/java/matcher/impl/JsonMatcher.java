package matcher.impl;

/**
 * Autor: c0ny1
 * Date: 2020-05-21
 * Description: API Json结果匹配模块
 */

import com.alibaba.fastjson.JSONObject;
import entity.MatchResult;
import matcher.IMathcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static utils.Util.getRspBody;

public class JsonMatcher implements IMathcher {
    private List<String> result = new ArrayList<String>();
    private List<String> keywords = new ArrayList<String>();

    protected void searchJson(String strJson,String fields){
        String[] fs = fields.split("\\.");
        String current_field = fs[0];
        String new_field = null;
        if(fs.length != 1) {
            new_field = fields.replace(current_field+".", "");
        }else{
            new_field = current_field;
        }

        if(strJson.indexOf('{') == 0){
            JSONObject jsonObj = JSONObject.parseObject(strJson);
            for (Map.Entry<String, Object> entry : jsonObj.entrySet()) {
                String value = jsonObj.getString(entry.getKey());
                if(entry.getKey().equals(current_field)){
                    if(fs.length == 1){ // 判断是否是末尾属性
                        result.add(value);
                    }
                    searchJson(value,new_field);
                } else {
                    //若未发现头部属性，继续全关键字搜索
                    searchJson(value,fields);
                }
            }
        } else if(strJson.indexOf('[') == 0){
            List<Object> list = JSONObject.parseArray(strJson, Object.class);
            for(Object obj:list){
                if(obj.getClass().getName().equals("com.alibaba.fastjson.JSONObject")){
                    JSONObject jsonObj = (JSONObject)obj;
                    for (Map.Entry<String, Object> entry : jsonObj.entrySet()) {
                        String value = jsonObj.getString(entry.getKey());
                        if(entry.getKey().equals(current_field)){
                            if(fs.length == 1){
                                result.add(value);
                            }
                            searchJson(value,new_field);
                        } else {
                            searchJson(value,fields);
                        }
                    }
                }
            }
        }
    }

    /**
     * 遗留问题：当json存在多个属性有相同值的情况，生成的匹配规则不一定准确。临时解决方案是人工微调自动生成的规则。
     * @param strJson
     * @param val
     * @param keyword
     */
    protected void makeKeyword(String strJson,String val,String keyword){
        if(keyword == null){
            keyword = "";
        }
        if(strJson.indexOf('{') == 0){
            JSONObject jsonObj = JSONObject.parseObject(strJson);
            for (Map.Entry<String, Object> entry : jsonObj.entrySet()) {
                String value = jsonObj.getString(entry.getKey());
                String kw = keyword;
                kw += keyword.equals("") ? entry.getKey() : "." + entry.getKey();
                if(value.equals(val)){
                    keywords.add(kw);
                } else {
                    makeKeyword(value,val,kw);
                }
            }
        } else if(strJson.indexOf('[') == 0){
            List<Object> list = JSONObject.parseArray(strJson, Object.class);
            for(Object obj:list){
                if(obj.getClass().getName().equals("com.alibaba.fastjson.JSONObject")){
                    JSONObject jsonObj = (JSONObject)obj;
                    for (Map.Entry<String, Object> entry : jsonObj.entrySet()) {
                        String value = jsonObj.getString(entry.getKey());
                        String kw = keyword;
                        kw += keyword.equals("") ? entry.getKey() : "." + entry.getKey();
                        if(value.equals(val)){
                            keywords.add(kw);
                        } else {
                            makeKeyword(value,val,kw);
                        }
                    }
                }
            }
        }
    }

    @Override
    public MatchResult match(String str, String keyword) {
        MatchResult matchResult = new MatchResult();
        String rsqData =  new String(getRspBody(str.getBytes()));
        this.searchJson(rsqData,keyword);
        if(result.size() != 0){
            String res = result.get(0);
            int start = str.indexOf(res); //存在相同值的化，可能不准确，待优化
            int end = start + res.length();
            matchResult.setResult(res);
            matchResult.setStart(start);
            matchResult.setEnd(end);
            return matchResult;
        }
        return null;
    }

    @Override
    public String buildKeyword(String str, String value) {
        this.makeKeyword(str,value,null);
        if(keywords.size() != 0){
            return keywords.get(0);
        }
        return null;
    }
}

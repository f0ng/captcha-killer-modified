package matcher.impl;

import entity.MatchResult;
import matcher.IMathcher;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static utils.Util.getRspBody;

public class XmlMatcher implements IMathcher {
    private List<String> result = new ArrayList<String>();
    private List<String> keywords = new ArrayList<String>();

    @Override
    public MatchResult match(String strResponse, String keyword) {
        MatchResult matchResult = new MatchResult();
        String rsqData =  new String(getRspBody(strResponse.getBytes()));
        try {
            final SAXReader sax = new SAXReader();
            final Document document = sax.read(new ByteArrayInputStream(rsqData.getBytes()));
            final Element root = document.getRootElement();// 获取根节点
            this.searchXml(root,keyword);
            if(this.result.size() != 0){
                String res = result.get(0);
                int start = rsqData.indexOf(res); //存在相同值的化，可能不准确，待优化
                int end = start + res.length();
                matchResult.setResult(res);
                matchResult.setStart(start);
                matchResult.setEnd(end);
            }else{
                matchResult.setResult("Alert: No match to");
            }
        }catch (Exception e){
            matchResult.setResult(String.format("Error: %s",e.getMessage()));
        }
        return matchResult;
    }

    @Override
    public String buildKeyword(String strResponse, String value) {
        String keyword = null;
        String rsqData =  new String(getRspBody(strResponse.getBytes()));
        try {
            final SAXReader sax = new SAXReader();
            final Document document = sax.read(new ByteArrayInputStream(rsqData.getBytes()));
            final Element root = document.getRootElement();// 获取根节点
            this.buildKeyword(root,value,null);
            if(this.keywords.size() != 0){
                keyword = keywords.get(0);
            }else{
                keyword = "build keyword fail!";
            }
        }catch (Exception e){
            keyword = String.format("build keyword error: %s",e.getMessage());
        }
        return keyword;
    }


    protected void searchXml(final Element node,String keyword) {
        String[] fs = keyword.split("\\.");
        String current_keyword = fs[0];
        String attr_name = null;
        String new_keyword = null;

        if(current_keyword.contains("#")){
            attr_name = current_keyword.split("#")[1];
            current_keyword = current_keyword.split("#")[0];
        }

        if(fs.length != 1) {
            new_keyword = keyword.replace(current_keyword + ".", "");
        }else{
            new_keyword = current_keyword;
        }

        // 判断是匹配到keyword的头部关键字，否则继续使用原keyword继续递归搜索，直到匹配头部关键字
        if(node.getName().equals(current_keyword)){
            if(attr_name != null){
                final List<Attribute> listAttr = node.attributes();
                for (final Attribute attr : listAttr) {
                    if(attr.getName().equals(attr_name)){
                        result.add(attr.getValue());
                    }
                }
                // 属性不存在子树，所以它肯定是keyword末端，无需在继续递归了。
                return;
            }

            // 如果是keyword末尾关键字，则添加到结果，否则继续使用新的关键字递归搜索
            if(fs.length == 1) {
                result.add(node.getTextTrim());
            }else{
                final List<Element> listElement = node.elements();
                for (final Element e : listElement) {
                    searchXml(e,new_keyword);
                }
            }
        }else{
            final List<Element> listElement = node.elements();
            for (final Element e : listElement) {
                searchXml(e,keyword);
            }
        }
    }

    protected void buildKeyword(Element node, String val, String keyword){
        if(keyword == null){
            keyword = "";
        }
        String kw = keyword;
        kw += keyword.equals("") ? node.getName() : "." + node.getName();

        //当前节点内容是否是验证码值
        if(node.getTextTrim().equals(val)){
            keywords.add(kw);
        }

        //当前节点所有属性值中是否有验证码值
        final List<Attribute> listAttr = node.attributes();
        for (final Attribute attr : listAttr) {
            if(attr.getValue().equals(val)){
                String attrkw = kw;
                attrkw += "#" + attr.getName();
                keywords.add(attrkw);
            }
        }

        final List<Element> listElement = node.elements();
        for(final Element e:listElement){
            buildKeyword(e,val,kw);
        }
    }
}

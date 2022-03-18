package matcher.impl;

import entity.MatchResult;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;

public class XmlMatcherTest {

    @org.junit.Test
    public void match() {
        String strXml = "<ResultMessage><success>true</success><code>0</code><message>success</message><data code=\"9e9e\"><result>9e9e</result><id>18f064e2d3f947dc91e94c98c4461217</id></data></ResultMessage>";
        String keyword = "ResultMessage.data#code";
        XmlMatcher xmlMatcher = new XmlMatcher();
        xmlMatcher.searchXml(getRoot(strXml),keyword);
        //System.out.println(String.format("result: %s start:%d end:%d",matchResult.getResult(),matchResult.getStart(),matchResult.getEnd()));
    }

    @org.junit.Test
    public void buildKeyword() {
        String strXml = "<ResultMessage><success>true</success><code>0</code><message>success</message><data code=\"9e9e\"><result>9e9e</result><id>18f064e2d3f947dc91e94c98c4461217</id></data></ResultMessage>";

        XmlMatcher xmlMatcher = new XmlMatcher();
        xmlMatcher.buildKeyword(getRoot(strXml),"9e9e",null);
        //System.out.println(String.format("keyword: %s",keyword));
    }

    public static Element getRoot(String strXml){
        SAXReader sax = new SAXReader();
        Element root = null;
        try {
            Document document = sax.read(new ByteArrayInputStream(strXml.getBytes()));
            root = document.getRootElement();// 获取根节点
        }catch (Exception e){
            e.printStackTrace();
        }
        return root;
    }
}
/**
 * Copyright (c) 2019 c0ny1 (https://github.com/c0ny1/captcha-killer)
 * License: MIT
 * autor: c0ny1
 * date: 2019-10-20 13:45
 * description: 该类用于解析接口请求包中的标签
 */
package utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.DatatypeConverter;
import static utils.Util.*;

public class LableParser {
    private byte[] byteImage;


    public LableParser(byte[] byteImage){
        this.byteImage = byteImage;
    }


    public String parseAllLable(String str){
        String reqTpl = str;



        while (reqTpl.indexOf("<@")>=0){
            reqTpl = parseOneLable(reqTpl);
        }
//        System.out.println("1111111111111111111");
//        System.out.println(reqTpl);

        if(reqTpl.indexOf("[base64]")>=0){
            String b64encode = reqTpl.substring(reqTpl.indexOf("[base64]")+8,reqTpl.lastIndexOf("[base64]"));
            String b64decode = new String(base64Decode(b64encode));
            reqTpl = reqTpl.replace(String.format("[base64]%s[base64]",b64encode),b64decode);
        }
//        System.out.println("222222222222222222");
//        System.out.println(reqTpl);
        return reqTpl;
    }


    /**
     * 通过标签位置递归解析标签
     * @param str
     * @return
     */
    private String parseOneLable(String str){
        String parseRes = "";
        String lable = str.substring(str.indexOf("<@")+2,str.indexOf(">"));
        if(!lable.equals(null)&&!lable.trim().equals("")) {
            String prefix = String.format("<@%s>", lable);
            String suffix = String.format("</@%s>", lable);
            String allContent = str.substring(str.indexOf(prefix), str.indexOf(suffix) + suffix.length());
            String subContent = str.substring(str.indexOf(prefix) + prefix.length(), str.indexOf(suffix));

            if (subContent.indexOf("<@") >= 0 && subContent.indexOf(">") >= 0) {
//                System.out.println("5555555555555555");
//                System.out.println(str);
                str = str.replace(subContent, parseOneLable(subContent));
//                System.out.println("6666666666666666");
//                System.out.println(str);
                allContent = str.substring(str.indexOf(prefix), str.indexOf(suffix) + suffix.length());
                subContent = str.substring(str.indexOf(prefix) + prefix.length(), str.indexOf(suffix));

                parseRes = str.replace(allContent, encdoeLable(lable, subContent));
            } else {
                parseRes = str.replace(allContent, encdoeLable(lable, subContent));
            }
        }
        return parseRes;
    }


    /**
     * 通过正则表达式解析标签
     * 注意：由于内容中若出现\r\n就会导致无法匹配，故暂时弃用!
     * @param str
     * @return
     */
    private String parseOneLableByRegular(String str){
        String parseRes = "";
        String regular1 = "<@(.*?)>";
        String lable = matchByRegular(str,regular1);
//        System.out.println("123");
        if(!lable.equals(null) && !lable.trim().equals("")) {
            String regular2 = String.format("<@%s>(.*?)</@%s>", lable, lable);
            String all_content = matchByRegular(str,regular2,0);
            String content = matchByRegular(str,regular2,1);
            String sublable = matchByRegular(content,regular1);
            if(!sublable.equals(null) && !sublable.trim().equals("")){
                str = str.replace(content,parseOneLable(content));
                content = matchByRegular(str,regular2,1);
                all_content = matchByRegular(str,regular2,0);
                parseRes = str.replace(all_content,encdoeLable(lable,content));
            }else{
                parseRes = str.replace(all_content,encdoeLable(lable,content));
            }
        }
        return parseRes;
    }


    private String encdoeLable(String type,String str){ // 处理请求编码
        String encodeStr = "";

        switch (type){
            case "BASE64":
                if(str.startsWith("[base64]")){
                    str = str.replace("[base64]","");
                    encodeStr = base64Encode(base64Decode(str));
                }else {
                    encodeStr = base64Encode(str);
                }
                break;
            case "URLENCODE":
                if(str.startsWith("[base64]")) {
                    str = str.replace("[base64]","");
                    byte[] byteRes = base64Decode(str);
                    encodeStr = URLEncode(new String(byteRes));
                }else {
                    encodeStr = URLEncode(str);
                }
                break;
            case "IMG_RAW":
//                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaa");
                String strr = new String(byteImage);
                if ( (strr.contains("data:image") && !strr.startsWith("data:image")) || (strr.contains("data%3Aimage") && !strr.startsWith("data%3Aimage")) ){
                    strr = new String(byteImage);
                    String pattern = "(data:image.*?)[\"|&]|(data%2Aimage.*?)[\"|&]";
                    Pattern r = Pattern.compile(pattern);
                    Matcher m = r.matcher(strr);
                    if (m.find( )) {
                        strr = m.group(0).replace("\"","").replace("&","") ;
                    }
//                    byteImage = strr.getBytes();
                    byteImage = DatatypeConverter.parseBase64Binary(strr.substring(strr.indexOf(",") + 1));
                }
                 //注意：byte[]转string后，string再转byte[]无法还原的，故该地方采用base64编码存储byte[]，等到使用时在解码为byte[]。
                String base64Img = base64Encode(byteImage);
                encodeStr = "[base64]" + base64Img + "[base64]";
//                System.out.println(encodeStr);
                break;
            default:
                encodeStr = str;
                break;
        }
        return encodeStr;
    }

    public static void main(String[] args) {
//        String str = "a<@URLENCODE><@BASE64>1</@BASE64></@URLENCODE>b<@BASE64>sd<@IMG_RAW></@IMG_RAW>sds\n\rsdsdd</@BASE64>";
//        str = "<@URLENCODE><@1URLENCODE>+11111</@1URLENCODE></@URLENCODE>";
//        LableParser lableParser = new LableParser("123".getBytes());
//        String res = lableParser.parseAllLable(str);
//        System.out.println(res);

        String reqTpl = "a[base64]eHh4[base64]b";
        String base64encode = reqTpl.substring(reqTpl.indexOf("[base64]")+8,reqTpl.lastIndexOf("[base64]"));
        String base64decode = new String(base64Decode(base64encode));
        String str = reqTpl.replace(String.format("[base64]%s[base64]",base64encode),base64decode);
//        System.out.println(str);
    }
}

/**
 * Copyright (c) 2019 c0ny1 (https://github.com/c0ny1/captcha-killer)
 * License: MIT
 */
package utils;

import burp.BurpExtender;
import burp.IResponseInfo;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static burp.BurpExtender.stdout;

// import sun.misc.BASE64Decoder;
// import sun.misc.BASE64Encoder;
//import java.util.Base64.Decoder;
//import java.util.Base64.Encoder;

public class Util {
    /**
     * 插件Banner信息
     * @return
     */
    public static String getBanner(String extName,String version){
        String bannerInfo =
                          "[*] \n"
                        + "[*] ###################################################\n"
                        + "[*]   " + extName + " v" + version +"\n"
                        + "[*]   author: c0ny1\n"
                        + "[*]   email:  root@gv7.me\n"
                        + "[*]   github: http://github.com/c0ny1/captcha-killer\n"
                        + "[*] ###################################################\n"
                        + "[*]  modifier: f0ng\n"
                        + "[*]  github: http://github.com/f0ng/captcha-killer-modified\n"
                        + "[!]  Install " + extName + " successful!\n"
                        + "[*]  Please enjoy it ^_^\n"
                        + "[*]  use `@captcha@` replace captcha\n"
                        + "[*]  use `@captcha-killer-modified@` replace captcha's token\n"
                        + "[*] ";
        return bannerInfo;
    }

    public static boolean isImage(String str_img,String words) throws IOException {
//        System.out.println(str_img);
        String pattern = "(" + words + ".*?)[,&}/+=\\w]+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str_img);
        if (m.find( )) {
            str_img = m.group(0).replace("\"","").replace("&","").replace("Base64:","").replace("base64:","") ;
        }
        if (!str_img.contains("data:image")){
            str_img = "data:image/jpeg;base64," + str_img;
        }

        byte[] img = DatatypeConverter.parseBase64Binary(str_img.substring(str_img.indexOf(",") + 1));
        boolean isImg = false;
        InputStream buffin = new ByteArrayInputStream(img);
        BufferedImage image = ImageIO.read(buffin);
        if(image == null){
            isImg = false;
        }else {
            isImg = true;
        }
//        System.out.println(isImg);
        return isImg;
    }

    public static boolean isImage(String str_img) throws IOException {
//        System.out.println(str_img);
        String pattern = "(data:image.*?)[\"|&]|(data%2Aimage.*?)[\"|&]";
        str_img = str_img.replace("\\r\\n","").replace("\\","");
//        System.out.println(str_img);
        str_img = URLDecoder.decode(str_img,"utf-8");
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str_img);
        if (m.find( )) {
            str_img = m.group(0).replace("\"","").replace("&","").replace("Base64:","").replace("base64:","").replace(".","") ;
        }
        if (!str_img.contains("data:image")){
            str_img = "data:image/jpeg;base64," + str_img;
        }
//        stdout.println(str_img);
        str_img = str_img.replace(" ","+");
        byte[] img = DatatypeConverter.parseBase64Binary(str_img.substring(str_img.indexOf(",") + 1));
        boolean isImg = false;
        InputStream buffin = new ByteArrayInputStream(img);
        BufferedImage image = ImageIO.read(buffin);
//        System.out.println(image);
        if(image == null){
            isImg = false;
        }else {
            isImg = true;
        }
        System.out.println(image);
        System.out.println(isImg);
        return isImg;
    }

    public static byte[] dataimgToimg(String str_img) throws IOException {
        String pattern = "(data:image.*?)[\"|&]|(data%2Aimage.*?)[\"|&]";
        str_img = str_img.replace("\\r\\n","").replace("\\","");
        str_img = str_img.replace("\\n","");
        str_img = URLDecoder.decode(str_img,"utf-8");
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str_img);
        if (m.find( )) {
            str_img = m.group(0).replace("\"","").replace("&","").replace("Base64:","").replace("base64:","").replace(".","") ;
        }
        if (!str_img.contains("data:image")){
            str_img = "data:image/jpeg;base64," + str_img;
        }
        str_img = str_img.replace(" ","+");
        byte[] img = DatatypeConverter.parseBase64Binary(str_img.substring(str_img.indexOf(",") + 1));
//        InputStream buffin = new ByteArrayInputStream(img);
        return img;
    }

    public static String extractToken(String str_img,String token) throws IOException {
        String pattern =   token ;
        str_img = str_img.replace("\\r\\n","").replace("\\","");
        str_img = str_img.replace("\\n","");
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str_img);

        if (m.find()) {
            str_img = m.group(1).replace("\"", "").replace("&", "").replace("Base64:", "").replace("base64:", "");
        }
        if( str_img.length() > 150){
            str_img = "提取关键字过长，请确认提取是否正确！";
        }

        return str_img;
    }



    public static byte[] dataimgToimg(String str_img,String words) throws IOException {
        String pattern = "(" + words + ".*?)[,&}/+=\\w]+";
        str_img = str_img.replace("\\r\\n","").replace("\\","");
        str_img = str_img.replace("\\n","");

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str_img);
        str_img = URLDecoder.decode(str_img,"utf-8");
        if (m.find( )) {
            stdout.println(m.group(0));
            str_img = m.group(0).replace("\"","").replace(words,"").replace(":","").replace("&","").replace(",","") ;
        }
        if (!str_img.contains("data:image")){
            str_img = "data:image/jpeg;base64," + str_img;
        }
        str_img = str_img.replace(" ","+");
//            stdout.println(str_img);
        //str_img = str_img;
//        System.out.println(str_img);
//        System.out.println(str_img.indexOf(","));
        byte[] img = DatatypeConverter.parseBase64Binary(str_img.substring(str_img.indexOf(",") + 1));
        //InputStream buffin = new ByteArrayInputStream(img);
        return img;
    }

    public static boolean isImage(byte[] img){
        // Reference: https://www.cnblogs.com/shihaiming/p/10404700.html
        boolean isImg = false;
        InputStream buffin = new ByteArrayInputStream(img);
        try {
            //两种判断方式只能选中一种
            //第一种方式
//            ImageInputStream iis = ImageIO.createImageInputStream(buffin);
//            Iterator iter = ImageIO.getImageReaders(iis);
//            if (!iter.hasNext()) {
//                isImg = false;
//            }else {
//                isImg = true;
//            }
            //第二方式
            BufferedImage image = ImageIO.read(buffin);
            if(image == null){
                isImg = false;
            }else {
                isImg = true;
            }
        } catch (IOException e) {
            BurpExtender.stderr.println(e.getMessage());
            isImg = false;
        }
        return isImg;
    }

    public static ImageIcon byte2img(byte[] img) {
        InputStream buffin = new ByteArrayInputStream(img);
        Image image = null;
        ImageIcon icon = null;
        try {
            image = ImageIO.read(buffin);
            icon = new ImageIcon(image);
        } catch (IOException e) {
            BurpExtender.stderr.println(e.getMessage());
            icon = null;
        }
        return icon;
    }

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i=begin; i<begin+count; i++) bs[i-begin] = src[i];
        return bs;
    }

    public static String matchByStartEndPosition(String str,String rule){
        int nStart = 0;
        int nEnd = 0;
        if(nStart > 0 && nStart < nEnd){
            return "Rules of the error: start should >0 and <end";
        }

        if(nEnd <= str.length()){
            return String.format("Rules of the error: end should < response.length(%s)",str.length());
        }
        return str.substring(nStart,nEnd);
    }

    public static String URLEncode(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            stdout.println(e.getMessage());
        }
        return result;
    }

    public static String URLDecode(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            stdout.println(e.getMessage());
        }
        return result;
    }

    public static String base64Encode(byte[] byteArray){
        //https://www.cnblogs.com/alter888/p/9140732.html
//        final Base64.Encoder encoder = new Base64.Encoder;
        byte[] res = Base64.getEncoder().encode(byteArray);
        String res2 = new String(res);
        res2 = res2.replace(System.lineSeparator(),"");
        return res2;
    }

    public static String base64Encode(String str){
//        final Base64.Encoder encoder = new Base64.Encoder;

        byte[] b = new byte[]{};
        try {
            b = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] res = Base64.getEncoder().encode(b);
        String res2 = new String(res);
        //去除base64结果中的换行符，java base64编码默认会76个字母换行一次
        res2 = res2.replace(System.lineSeparator(),"");
        return res2;
    }

    public static byte[] base64Decode(String str){
//        final Base64.Decoder decoder = new Base64.Decoder;
        byte[] byteRes = new byte[]{};
        byteRes = Base64.getDecoder().decode(str);
        return byteRes;
    }

    public static boolean isURL(String url){
        if (url ==  null ){
            return   false ;
        }
        String regEx =  "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-"
                +  "Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
                +  "2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
                +  "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|"
                +  "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-"
                +  "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
                +  "-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/"
                +  "[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*$" ;
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(url);
        return  matcher.matches();
    }

    /**
     * 转义正则特殊字符 $()*+.[]?\^{},|
     * Reference:https://www.cnblogs.com/lovehansong/p/7874337.html
     * @param keyword
     * @return
     */
    public static String escapeExprSpecialWord(String keyword) {
        if (!keyword.equals(null) && !keyword.trim().equals("")) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|",":"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
            keyword = keyword.replace("\r","\\r");
            keyword = keyword.replace("\n","\\n");
        }
        return keyword;
    }

    /**
     * 转移json特殊字符 $()*+.[]?{}/^-|"
     * Renference: https://www.cnblogs.com/javalanger/p/10913838.html
     * @param keyword
     * @return
     */
    public static String escapeJsonString(String keyword){
        if (!keyword.equals(null) && !keyword.trim().equals("")) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|",":","-"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
            keyword = keyword.replace("\r","\\r");
            keyword = keyword.replace("\n","\\n");
        }
        return keyword;
    }


    public static byte[] getRspBody(byte[] response){
        IResponseInfo responseInfo = BurpExtender.helpers.analyzeResponse(response);
        int bodyOffset = responseInfo.getBodyOffset();
        int body_length = response.length - bodyOffset;
        return subBytes(response,bodyOffset,body_length);
    }

    /**
     * 统计字符串str中有多少个字符串keyword
     * @param str
     * @param keyword
     * @return 返回存在的个数
     */
    public static int getStringCount(String str,String keyword){
        int len = keyword.length();
        int count = 0;
        int pos = str.indexOf(keyword);
        if(pos != -1){
            count += 1;
            count += getStringCount(str.substring(pos + len,str.length()),keyword);
        }else{
            return count;
        }

        return count;
    }

    /**
     * 统计字符串中有多少个换行符号
     * @param str
     * @return
     */
    public static int getRNCount(String str){
        int count = 0;
        int rCount = Util.getStringCount(str,"\r");
        int nCount = Util.getStringCount(str,"\n");
        if(rCount > 0){
            count = rCount;
        }
        if(nCount > 0){
            count = nCount;
        }
        return count;
    }


    public static byte[][] requestImage(String url,String raw) throws IOException {
//        if(Util.isURL(url)) {
            HttpClient http = new HttpClient(url, raw, null);
            byte[] rsp = http.doReust();

//            BurpExtender.stdout.println(new String(rsp).replace("\r\n","\r\n\\r\\n"));
            BurpExtender.gui.getTaResponse().setText(new String(rsp).replace("\r\n","\\r\\n\r\n"));
            int BodyOffset = BurpExtender.helpers.analyzeResponse(rsp).getBodyOffset();
            int body_length = rsp.length - BodyOffset;
            byte[] byteImg = Util.subBytes(rsp, BodyOffset, body_length);
            return new byte[][]{byteImg, rsp};
//        }else{
//            BurpExtender.stderr.println("[-] captcha URL format invalid");
//            return null;
//        }
    }

    public static String matchByRegular(String str,String reg){
        String res = "";
        int start = 0;
        int end = 0;
        Pattern r = Pattern.compile(reg);
        Matcher m = r.matcher(str);
        if (m.find()) {
            res = m.group(1);//0会获取多余的内容
            start = m.start();
            int n = str.substring(start,str.length()).indexOf(res);
            start += n;
            end = start + res.length();
        }
        return res;
    }

    public static String matchByRegular(String str,String reg,int n){
        String res = "";
        Pattern r = Pattern.compile(reg,Pattern.MULTILINE);
        Matcher m = r.matcher(str);
        if (m.find()) {
            res = m.group(n);//0会获取多余的内容
        }
        return res;
    }

    /**
     * 将字符串开头空格去掉
     * @param str
     * @return
     */
    public static String trimStart(String str) {
        if (str == "" || str == null) {
            return str;
        }

        final char[] value = str.toCharArray();
        int start = 0, last = 0 + str.length();
        int end = last;
        while ((start <= end) && (value[start] <= ' ')) {
            start++;
        }
        if (start == 0 && end == last) {
            return str;
        }
        if (start >= end) {
            return "";
        }
        return str.substring(start, end);
    }
}

package utils;

import java.io.IOException;

/**
 * @author f0ng
 * @date 2022/3/30
 */
public class test2 {
    public static void main(String[] args) throws IOException {
//        String url = "https://0.zone:443";
//
//        String raw = "GET /api/captcha/?t=1671786233651 HTTP/1.1\n" +
//                "Host: 0.zone\n" +
//                "Cookie: csrftoken=FHW6BhBwlh8eFO0iqwm8VDuWKXCZ7bP8XPsu3cR0wbe4FuDhlIm6vW5h4NOHHpAq; sessionid=8ylbc9riyci8nkor68fmiqbu7q0g4tjs\n" +
//                "User-Agent: Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9B176 Safari/7534.48.3\n" +
//                "Accept: application/json\n" +
//                "Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2\n" +
//                "Accept-Encoding: gzip, deflate\n" +
//                "Cache-Control: no-cache\n" +
//                "X-Csrftoken: FHW6BhBwlh8eFO0iqwm8VDuWKXCZ7bP8XPsu3cR0wbe4FuDhlIm6vW5h4NOHHpAq\n" +
//                "Sec-Fetch-Dest: empty\n" +
//                "Sec-Fetch-Mode: cors\n" +
//                "Sec-Fetch-Site: same-origin\n" +
//                "Te: trailers\n" +
//                "\n";
//        HttpClient http = new HttpClient(url, raw, null);
//        byte[] rsp = http.doReust();
//
//        System.out.println(rsp);

        String a = "Businesstypecode: ";
        System.out.println(a.indexOf(":"));
        System.out.println(Util.trimStart(a.substring(a.indexOf(":")+1)));



    }

}

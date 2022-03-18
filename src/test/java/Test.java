import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        String  str = "title=\"韩国和规范\">yfffgfg<a herf=\"gfytffjhhjg\" title=\"预付费更改\">iuiuyuytf<a herf=\"fhgytfddtr\" title=\"了解客户感觉\">uigfg";
        Pattern p = Pattern.compile("title=\"(.+?)\"");
        Matcher m = p.matcher(str);
        //System.out.println(m.start());
        while(m.find()) {
            String res = m.group(1);
            int start = m.start();
            int n = str.substring(start,str.length()).indexOf(res);

            System.out.println(res);
            //System.out.println(m.lookingAt());
            System.out.println(start);
            System.out.println(start + n);
        }

        System.out.println("ⓔⓧⓐⓜⓟⓛⓔ.ⓒⓞⓜ");
    }
}

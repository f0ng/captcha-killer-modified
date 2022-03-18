import utils.Util;

public class Test1 {
    public static void main(String[] args) {
        String str = "a\n\rb\n\rc\n\r";
        System.out.println(str.indexOf("b"));
        str = "sdfsfsteerwfwfdsvsdfggefdf";
        System.out.println(str.indexOf("sssssssssss"));

        String header = "bbb: sdsdsd:sdsdsdsd";
        System.out.println(header.substring(0, header.indexOf(":")));
        System.out.println(Util.trimStart(header.substring(header.indexOf(":")+1, header.length())));
    }
}

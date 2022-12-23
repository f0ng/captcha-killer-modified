package utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;

/**
 * @author f0ng
 * @date 2022/3/30
 */
public class test extends JFrame{
    public void GUI() {
        String a = "{\"code\":0,\"message\":\"\",\"data\":{\"id\":\"cd2a68265e7ebc6aa7b18032623d9b99\",\"img\":\"data:image\\/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAAAoBAMAAAD6VkJwAAAAG1BMVEXz+\\/6TWyPn5+LPv6vDq5C3\\r\\nl3Wfbz7b08erg1ldU+\\/iAAABQUlEQVQ4je2UsU7DMBCGr3ajMNYkjT0mS9WRAA+QVKJzKxgYG4Si\\r\\njoRIiLEGqfDYXHxJqYRFnIUB9V98cvz5fP\\/ZATjpH4pdXwxF1iJ6GkZ4IaSbNt\\/NrRMy2oF3TmGl\\r\\nipULkmQwlhSWsSddzzYxgx8CUzTzGvclouPwAEAT3GuIjg9IbtYmbz0ED6E7GKSmR3lfkmRHI5ui\\r\\naVkT7Q\\/ffDu8j9sAax+ZskqmlZnkhbWqcdBF2nSp2SR51A9NkL7nGwtSrVhNtwxLoCz5FnhjN3rO\\r\\nAwtSlELQVmg21VJJstHH9Tbk8+7qmSIvrEtTwpkk7\\/wp2nhpYb61Fh9m5Ohd2tRdLF6E+PXesTYd\\r\\n6Iz6O8OHMbcd7admakkLawksckJgqagffHLcWCdxNGI7DGFRPHd9Q53ySAz9m\\/j3i4HESX+qL6nX\\r\\nKovUqUE8AAAAAElFTkSuQmCC\\r\\n\"}}";
        String pattern = "(data:image.*?)[\"|&]|(data%2Aimage.*?)[\"|&]";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(a);
        if (m.find()) {
            a = m.group(0).replace("\"", "").replace("&", "").replace("Base64:", "").replace("base64:", "");
        }
        System.out.println(a);
        byte[] img = DatatypeConverter.parseBase64Binary(a.substring(a.indexOf(",") + 1));
        InputStream buffin = new ByteArrayInputStream(img);
        System.out.println(buffin);
        ImageIcon icon = Util.byte2img(img);
        //System.out.println(icon.getImage());

        setTitle("图像测试");
        JPanel panel = new JPanel();
        JLabel label = new JLabel();
        //ImageIcon img = new ImageIcon("images/logo.jpg");// 创建图片对象
        label.setIcon(icon);
        panel.add(label);
        add(panel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);// JFrame最大化
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 让JFrame的关闭按钮起作用
        setVisible(true);// 显示JFrame

    }

    public static void main(String[] args) {

        test d = new test();
        d.GUI();


    }
}

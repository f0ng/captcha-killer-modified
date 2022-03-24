/**
 * Copyright (c) 2019 c0ny1 (https://github.com/c0ny1/captcha-killer)
 * License: MIT
 */
package ui.model;

import burp.BurpExtender;
import entity.CaptchaEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.xml.bind.DatatypeConverter;
import ui.GUI;
import static utils.Util.byte2img;
import static utils.Util.dataimgToimg;

public class TableModel extends AbstractTableModel {
    private List<String> title = new ArrayList<String>();
    private JTable table;
    public TableModel(JTable table){
        this.table = table;
        title.add(0,"验证码");
        title.add(1,"识别结果");
    }

    public String getColumnName(int column) {
        // TODO Auto-generated method stub
        return title.get(column);
    }

    @Override
    public int getRowCount() {
        return GUI.captcha.size();
    }

    @Override
    public int getColumnCount() {
        return title.size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        if(columnIndex == 0){
            return Icon.class;
        }
        return String.class;
    }

    @Override
    public Object getValueAt(int row, int column) {
        CaptchaEntity captcha = GUI.captcha.get(row);
        //String words = tfWords
        try {
            switch (column) {
                case 0:
                    String strr = new String(captcha.getImage());
                    String words = BurpExtender.gui.getTfwords().getText();

                    if (!words.equals("") && strr.contains(words)){
                        byte[] byteImage = dataimgToimg(new String(captcha.getImage()) ,words);
                        ImageIcon icon = byte2img(byteImage);
                        table.setRowHeight(row, icon.getIconHeight() + 5);//让行高自动适应图片高
                        return icon;
                    }else {
                        if ((strr.contains("data:image") && !strr.startsWith("data:image")) || (strr.contains("data%3Aimage") && !strr.startsWith("data%3Aimage"))) {
                            String pattern = "(data:image.*?)[\"|&]|(data%2Aimage.*?)[\"|&]";
                            Pattern r = Pattern.compile(pattern);
                            Matcher m = r.matcher(strr);
                            if (m.find()) {
                                strr = m.group(0).replace("\"", "").replace("&", "").replace("Base64:", "").replace("base64:", "");
                            }
                            if (!strr.contains("data:image")) {
                                strr = "data:image/jpeg;base64," + strr;
                            }
                            byte[] byteImage = DatatypeConverter.parseBase64Binary(strr.substring(strr.indexOf(",") + 1));
                            ImageIcon icon = byte2img(byteImage);
                            table.setRowHeight(row, icon.getIconHeight() + 5);//让行高自动适应图片高
                            return icon;
                        } else {
                            ImageIcon icon = byte2img(captcha.getImage());
                            table.setRowHeight(row, icon.getIconHeight() + 5);//让行高自动适应图片高
                            return icon;
                        }
                    }

                case 1:
                    return captcha.getResult();
                default:
                    return "";
            }
        }catch (Exception e ){

        }
        return "";
    }
}

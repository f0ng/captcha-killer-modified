/**
 * Copyright (c) 2019 c0ny1 (https://github.com/c0ny1/captcha-killer)
 * License: MIT
 */
package ui;

import burp.BurpExtender;
import com.alibaba.fastjson.JSON;
import entity.HttpService;
import entity.Rule;
import entity.TmplEntity;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * autor: c0ny1
 * date: 2019-10-19
 * description: 自定对话框类，用于删除和更新模版
 */
public class CustomDlg extends JDialog {
    public static final int TYPE_DEL_TPL = 0;
    public static final int TYPE_UPDATE_TPL = 1;
    int type;
    JPanel mainPanel;
    JLabel lbTitle;
    JComboBox cbmtpl;
    JButton btnOk;
    JButton btnCanel;
    CustomDlg dlg;


    CustomDlg(final int type){
        this.type = type;
        dlg = this;
        initGUI();
        InitEvent();
    }


    private void initGUI(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(2,2,2,2));
        lbTitle = new JLabel("模版");
        cbmtpl = new JComboBox();
        for(TmplEntity tpl:TmplEntity.tpls){
            cbmtpl.addItem(tpl.getName());
        }
        if(type == CustomDlg.TYPE_DEL_TPL){
            this.setTitle("删除模版");
            cbmtpl.addItem("所有模版");
            btnOk = new JButton("删除");
        }else{
            this.setTitle("更新模版");
            btnOk = new JButton("更新");
        }
        btnCanel = new JButton("取消");
        GBC gbc_lbtitle = new GBC(0,0,1,1).setFill(GBC.HORIZONTAL).setInsets(10,10,0,0);
        GBC gbc_cbmtpl = new GBC(1,0,99,1).setFill(GBC.HORIZONTAL).setInsets(10,10,0,10).setWeight(100,1);;
        GBC gbc_btnok = new GBC(1,1,1,1).setFill(GBC.HORIZONTAL).setInsets(10,10,0,0);;
        GBC gbc_btncanel = new GBC(2,1,1,1).setFill(GBC.HORIZONTAL).setInsets(10,10,0,10);;
        mainPanel.add(lbTitle,gbc_lbtitle);
        mainPanel.add(cbmtpl,gbc_cbmtpl);
        mainPanel.add(btnOk,gbc_btnok);
        mainPanel.add(btnCanel,gbc_btncanel);

        this.setModal(true);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout(0,0));
        this.add(mainPanel);
        this.pack();
        Dimension screensize=Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(screensize.width/2-this.getWidth()/2,screensize.height/2-this.getHeight()/2,this.getWidth(),this.getHeight());
    }


    private void InitEvent(){
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int n = cbmtpl.getSelectedIndex();
                String msg = null;
                if(type == CustomDlg.TYPE_DEL_TPL) {
                    if(n == -1){
                        return;
                    }else if(n == TmplEntity.tpls.size()){
                        TmplEntity.tpls.clear();
                        msg = "清空所有自定义模版成功!";
                    }else {
                        TmplEntity entity = TmplEntity.tpls.get(n);
                        msg = String.format("删除模版[%s]成功!",entity.getName());
                        TmplEntity.tpls.remove(n);
                    }
                    String tpldb = JSON.toJSONString(TmplEntity.tpls);
                    BurpExtender.callbacks.saveExtensionSetting("tpldb", tpldb);
                    JOptionPane.showMessageDialog(CustomDlg.this,msg, "captcha-killer提示", JOptionPane.INFORMATION_MESSAGE);
                    dlg.setVisible(false);
                }else if(type == CustomDlg.TYPE_UPDATE_TPL){
                    TmplEntity entity = TmplEntity.tpls.get(n);
                    TmplEntity tpl = new TmplEntity();
                    tpl.setName(entity.getName());
                    tpl.setReqpacke(BurpExtender.gui.getTaInterfaceTmplReq().getText());
                    tpl.setService(new HttpService(BurpExtender.gui.getInterfaceURL().getText()));
                    tpl.setRule(new Rule(BurpExtender.gui.getCbmRuleType().getSelectedIndex(),BurpExtender.gui.getRegular().getText()));
                    synchronized (TmplEntity.tpls){
                        TmplEntity.tpls.set(n,tpl);
                    }
                    String tpldb = JSON.toJSONString(TmplEntity.tpls);
                    BurpExtender.callbacks.saveExtensionSetting("tpldb",tpldb);
                    JOptionPane.showMessageDialog(CustomDlg.this, String.format("更新内容到模版[%s]成功!",entity.getName()), "captcha-killer提示", JOptionPane.INFORMATION_MESSAGE);
                    dlg.setVisible(false);
                }
            }
        });

        btnCanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dlg.setVisible(false);
            }
        });
    }
}

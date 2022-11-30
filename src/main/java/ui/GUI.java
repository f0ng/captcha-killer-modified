/**
 * Copyright (c) 2019 c0ny1 (https://github.com/c0ny1/captcha-killer)
 * License: MIT
 */
package ui;

import burp.BurpExtender;
import com.alibaba.fastjson.JSON;
import entity.*;
import matcher.impl.JsonMatcher;
import matcher.impl.XmlMatcher;
import ui.model.TableModel;
import utils.HttpClient;
import utils.RuleMannager;
import utils.Util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static utils.Util.dataimgToimg;
import static utils.Util.extractToken;

public class GUI {
    private JPanel MainPanel;
    public JSplitPane spImg;
    private JSplitPane spInterface;
    private JSplitPane spOption;
    private JSplitPane spAll;

    //获取验证码面板
    private JLabel lbURL;
    private JTextField tfURL;
    private JButton btnGetCaptcha;
    private JTextArea taRequest;
    private JLabel lbCaptcha;

    private JLabel lbWords; // 关键字label
    public JTextField tfWords; // 关键字textfield

    private JLabel lbToken; // token关键字
    public JTextField tfToken; // token关键字

    private JLabel lbTokenex; // token关键字显示
    public JLabel tfTokenex; // token关键字显示

    public String tokenwords ; //token 关键字提取的结果

    private JLabel lbImage;
    private JToggleButton tlbLock;
    private JTextArea taResponse;

    //接口配置编码
    private JPanel plInterfaceReq;
    private JPopupMenu pmInterfaceMenu;
    private JTabbedPane tpInterfaceReq;
    private JTextArea taInterfaceTmplReq;
    private JTextArea taInterfaceRawReq;
    private JLabel lbInterfaceURL;
    private JTextField tfInterfaceURL;
    private JButton btnIdentify;
    private JTabbedPane tpInterfaceRsq;
    private JPanel plInterfaceRsq;
    //private JTextArea taInterfaceRsq;
    private JTextPane InterfaceRsq;
    private JLabel lbRuleType = new JLabel("匹配方式:");
    private JComboBox cbmRuleType;
    private JLabel lbRegular = new JLabel("匹配规则:");
    private JTextField tfRegular;
    private JButton btnSaveTmpl;
    JMenuItem miMarkIdentifyResult = new JMenuItem("标记为识别结果");
    JPopupMenu pppInterfaceRsq = new JPopupMenu();

    //识别结果面板
    private JPanel plResult;
    private JTable table;
    private JScrollPane spTable;
    private TableModel model;
    private JPopupMenu pppMenu = new JPopupMenu();
    private JMenuItem miClear = new JMenuItem("清空");
    private JMenuItem miShowIntruderResult = new JMenuItem("关闭Intruder识别结果显示");

    //一些公共变量
    private byte[] byteImg;
    public static final List<CaptchaEntity> captcha = new ArrayList<CaptchaEntity>();

    public JTextField getTfwords(){
        return tfWords;
    }


    public JTextField getTfURL(){
        return tfURL;
    }

    public JTextArea getTaRequest(){
        return taRequest;
    }

    public JTextArea getTaResponse(){
        return taResponse;
    }

    public JButton getBtnGetCaptcha(){
        return this.btnGetCaptcha;
    }

    public JTable getTable(){
        return table;
    }

    public TableModel getModel(){
        return this.model;
    }

    public String getCaptchaURL(){
        return this.tfURL.getText();
    }

    public String getCaptchaReqRaw(){
        return this.taRequest.getText();
    }

    public JTextField getInterfaceURL(){
        return this.tfInterfaceURL;
    }

    public JTextArea getTaInterfaceTmplReq(){
        return this.taInterfaceTmplReq;
    }

    public JTextArea getInterfaceReqRaw(){
        return this.taInterfaceRawReq;
    }

    public JTextField getRegular(){
        return this.tfRegular;
    }

    public JComboBox getCbmRuleType(){
        return this.cbmRuleType;
    }

    public GUI(){
        initGUI();
        initEvent();
    }

    public void initGUI(){
        String tokenwords = "";
        MainPanel = new JPanel();
        MainPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
        MainPanel.setLayout(new BorderLayout(0, 0));

        //图片获取面板
        lbURL = new JLabel("验证码URL:");
        tfURL = new JTextField(20);
        btnGetCaptcha = new JButton("获取");

        taRequest = new JTextArea();
        taRequest.setLineWrap(true);
        taRequest.setWrapStyleWord(true);//断行不断字
        JScrollPane spRequest = new JScrollPane(taRequest);

        JPanel imgLeftPanel = new JPanel();
        imgLeftPanel.setLayout(new GridBagLayout());
        GBC gbc_lburl = new GBC(0,0,1,1).setFill(GBC.HORIZONTAL).setInsets(3,3,0,0);
        GBC gbc_tfurl = new GBC(1,0,1,1).setFill(GBC.BOTH).setWeight(100,1).setInsets(3,3,0,0);
        GBC gbc_btngetcaptcha = new GBC(2,0,11,1).setInsets(3,3,0,3);
        GBC gbc_tarequst = new GBC(0,1,100,100).setFill(GBC.BOTH).setWeight(100,100).setInsets(3,3,3,3);
        imgLeftPanel.add(lbURL,gbc_lburl);
        imgLeftPanel.add(tfURL,gbc_tfurl);
        imgLeftPanel.add(btnGetCaptcha,gbc_btngetcaptcha);
        imgLeftPanel.add(spRequest,gbc_tarequst);

        JPanel imgRigthPanel = new JPanel();
//        GridBagLayout gbc = new GridBagLayout();
//        gbc.anchor = GridBagConstraints.EAST;

        imgRigthPanel.setLayout(new GridBagLayout());

        lbImage = new JLabel("");
        lbCaptcha = new JLabel("验证码:");

        lbWords = new JLabel("关键字:");
        tfWords = new JTextField(10);

        lbToken = new JLabel("响应提取(regex):");
        tfToken = new JTextField(10);
        GUI.this.tokenwords = tokenwords;

        lbTokenex = new JLabel("提取的关键字:");
        tfTokenex = new JLabel("");

//        tlbLock = new JToggleButton("锁定");
//        tlbLock.setToolTipText("当配置好所有选项后，请锁定防止配置被改动！");

        taResponse = new JTextArea();
        taResponse.setLineWrap(true);
        taResponse.setWrapStyleWord(true);//断行不断字
        taResponse.setEditable(true);
        JScrollPane spResponse = new JScrollPane(taResponse);

        GBC gbc_lbcaptcha = new GBC(2,0,1,1).setFill(GBC.BOTH).setInsets(3,3,0,0);
        GBC gbc_lbimage = new GBC(3,0,1,1).setFill(GBC.BOTH).setWeight(1,1).setInsets(3,3,0,0);
        GBC gbc_lbwords = new GBC(0,0,1,1).setFill(GBC.BOTH).setInsets(3,3,0,1);
        GBC gbc_tfwords = new GBC(1,0,1,1).setFill(GBC.HORIZONTAL).setWeight(20,1).setInsets(3,3,0,0);

        GBC gbc_lbtoken = new GBC(0,1,1,1).setFill(GBC.NONE).setInsets(3,3,0,1);
        GBC gbc_tftoken = new GBC(1,1,1,1).setFill(GBC.BOTH).setWeight(20,1).setInsets(3,3,0,0);
        GBC gbc_lbtokenex = new GBC(2,1,1,1).setFill(GBC.BOTH).setInsets(3,3,0,1);
        GBC gbc_tftokenex = new GBC(3,1,1,1).setFill(GBC.NONE).setWeight(20,1).setInsets(3,3,0,0);

        //GBC gbc_tlblock = new GBC(4,0,1,1).setFill(GBC.BOTH).setInsets(3,3,0,3);
        GBC gbc_taresponse = new GBC(0,2,100,100).setFill(GBC.BOTH).setWeight(100,100).setInsets(3,3,3,3);


        imgRigthPanel.add(lbWords,gbc_lbwords);
        imgRigthPanel.add(tfWords,gbc_tfwords);

        imgRigthPanel.add(lbCaptcha,gbc_lbcaptcha);
        imgRigthPanel.add(lbImage,gbc_lbimage);

        imgRigthPanel.add(lbToken,gbc_lbtoken);
        imgRigthPanel.add(tfToken,gbc_tftoken);

        imgRigthPanel.add(lbTokenex,gbc_lbtokenex);
        imgRigthPanel.add(tfTokenex,gbc_tftokenex);



        //imgRigthPanel.add(tlbLock,gbc_tlblock);
        imgRigthPanel.add(spResponse,gbc_taresponse);

        spImg = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        spImg.setResizeWeight(0);
        spImg.setLeftComponent(imgLeftPanel);
        spImg.setRightComponent(imgRigthPanel);

        // 识别接口配置面板
        spInterface = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        spInterface.setResizeWeight(0.5);
        plInterfaceReq = new JPanel();
        plInterfaceReq.setLayout(new GridBagLayout());

        lbInterfaceURL = new JLabel("接口URL:");
        tfInterfaceURL = new JTextField(30);
        btnIdentify = new JButton("识别");

        tpInterfaceReq = new JTabbedPane();
        taInterfaceTmplReq = new JTextArea();
        taInterfaceTmplReq.setLineWrap(true);
        taInterfaceTmplReq.setWrapStyleWord(true);
        JScrollPane spInterfaceReq = new JScrollPane(taInterfaceTmplReq);
        taInterfaceRawReq = new JTextArea();
        taInterfaceRawReq.setLineWrap(true);
        taInterfaceRawReq.setWrapStyleWord(true);
        taInterfaceRawReq.setEditable(false);
        JScrollPane spInterfaceRawReq = new JScrollPane(taInterfaceRawReq);
        tpInterfaceReq.addTab("Requst template",spInterfaceReq);
        tpInterfaceReq.addTab("Requst raw",spInterfaceRawReq);

        GBC gbc_lbinterfaceurl = new GBC(1,0,1,1).setFill(GBC.HORIZONTAL).setInsets(3,3,0,0);
        GBC gbc_tfinterfaceurl = new GBC(2,0,1,1).setFill(GBC.HORIZONTAL).setInsets(3,3,0,0).setWeight(100,1);
        GBC gbc_btnidentify = new GBC(3,0,1,1).setFill(GBC.HORIZONTAL).setInsets(3,3,0,3);
        GBC gbc_tpinterfacereq = new GBC(0,1,100,100).setFill(GBC.BOTH).setWeight(100,100).setInsets(3,3,3,3);
        plInterfaceReq.add(lbInterfaceURL,gbc_lbinterfaceurl);
        plInterfaceReq.add(tfInterfaceURL,gbc_tfinterfaceurl);
        plInterfaceReq.add(btnIdentify,gbc_btnidentify);
        plInterfaceReq.add(tpInterfaceReq,gbc_tpinterfacereq);

        plInterfaceRsq = new JPanel();
        plInterfaceRsq.setLayout(new GridBagLayout());
        String[] str = new String[]{"Response data","Regular expression","Define the start and end positions","Defines the start and end strings","json field match","xml element match"};
        cbmRuleType = new JComboBox(str);

        tfRegular = new JTextField(30);
        tfRegular.setText("response_data");
        tfRegular.setEnabled(false);
        btnSaveTmpl = new JButton("匹配");
        btnSaveTmpl.setToolTipText("用于测试编写的规则是否正确");
        tpInterfaceRsq = new JTabbedPane();
        InterfaceRsq = new JTextPane();
        InterfaceRsq.setEditable(true);

        pppInterfaceRsq.add(miMarkIdentifyResult);

        //JScrollPane spInterfaceRsq = new JScrollPane(taInterfaceRsq);
        JScrollPane spInterfaceRsq = new JScrollPane(InterfaceRsq);
        tpInterfaceRsq.addTab("Response raw",spInterfaceRsq);
        GBC gbc_lbruletype = new GBC(0,0,1,1).setFill(GBC.HORIZONTAL).setInsets(3,3,0,0);
        GBC gbc_cbmruletype = new GBC(1,0,1,1).setFill(GBC.HORIZONTAL).setInsets(3,3,0,0);
        GBC gbc_lbregular = new GBC(2,0,1,1).setFill(GBC.HORIZONTAL).setInsets(3,3,0,0);
        GBC gbc_tfregular = new GBC(3,0,1,1).setFill(GBC.HORIZONTAL).setInsets(3,3,0,0).setWeight(100,1);
        GBC gbc_btnsavetmpl = new GBC(4,0,1,1).setFill(GBC.HORIZONTAL).setInsets(3,3,0,3);
        GBC gbc_tpinterfacersq = new GBC(0,1,100,100).setFill(GBC.BOTH).setWeight(100,100).setInsets(3,3,3,3);
        plInterfaceRsq.add(lbRuleType,gbc_lbruletype);
        plInterfaceRsq.add(cbmRuleType,gbc_cbmruletype);
        plInterfaceRsq.add(lbRegular,gbc_lbregular);
        plInterfaceRsq.add(tfRegular,gbc_tfregular);
        plInterfaceRsq.add(btnSaveTmpl,gbc_btnsavetmpl);
        plInterfaceRsq.add(tpInterfaceRsq,gbc_tpinterfacersq);
        spInterface.setLeftComponent(plInterfaceReq);
        spInterface.setRightComponent(plInterfaceRsq);

        //识别结果面板
        plResult = new JPanel();
        pppMenu.add(miClear);
        pppMenu.add(miShowIntruderResult);
        table = new JTable();
        model = new TableModel(table);
        table.setModel(model);
        table.setAutoCreateRowSorter(true);
        spTable = new JScrollPane(table);
        plResult.setLayout(new GridBagLayout());
        GBC gbc_taresult = new GBC(0,0,100,100).setFill(GBC.BOTH).setWeight(100,100).setInsets(3,3,3,3);
        plResult.add(spTable,gbc_taresult);

        //面板合并
        spOption = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        spOption.setResizeWeight(0.5);
        spOption.setTopComponent(spImg);
        spOption.setBottomComponent(spInterface);
        spAll = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        spAll.setResizeWeight(0.6);
        spAll.setLeftComponent(spOption);
        spAll.setRightComponent(plResult);

        MainPanel.add(spAll);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BurpExtender.callbacks.customizeUiComponent(spAll);
                BurpExtender.callbacks.customizeUiComponent(MainPanel);
            }
        });
    }

    public void initEvent(){
        //获取验证码
        btnGetCaptcha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(tfURL.getText().equals(null) || tfURL.getText().trim().equals("")){
                    JOptionPane.showMessageDialog(null,"请设置验证码URL","captcha-killer提示",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(taRequest.getText().equals(null) || taRequest.getText().trim().equals("")){
                    JOptionPane.showMessageDialog(null,"请设置获取验证码的请求数据包","captcha-killer提示",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(!Util.isURL(tfURL.getText())){
                    JOptionPane.showMessageDialog(null,"验证码URL不合法！","captcha-killer提示",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                GetCaptchaThread thread = new GetCaptchaThread(tfURL.getText(),taRequest.getText());
                thread.start();
            }
        });

        //锁定
//        tlbLock.addChangeListener(new ChangeListener(){
//            public void stateChanged(ChangeEvent e) {
//                boolean isSelected = tlbLock.isSelected();
//                if(isSelected){
//                    tlbLock.setText("解锁");
//                    tfURL.setEnabled(false);
//                    taRequest.setEnabled(false);
//                    btnGetCaptcha.setEnabled(false);
//                    taResponse.setEnabled(false);
//                    tfInterfaceURL.setEnabled(false);
//                    btnIdentify.setEnabled(false);
//                    taInterfaceTmplReq.setEnabled(false);
//                    cbmRuleType.setEnabled(false);
//                    taInterfaceRawReq.setEnabled(false);
//                    tfRegular.setEnabled(false);
//                    btnSaveTmpl.setEnabled(false);
//                    InterfaceRsq.setEnabled(false);
//                }else{
//                    tlbLock.setText("锁定");
//                    tfURL.setEnabled(true);
//                    taRequest.setEnabled(true);
//                    btnGetCaptcha.setEnabled(true);
//                    taResponse.setEnabled(true);
//                    tfInterfaceURL.setEnabled(true);
//                    btnIdentify.setEnabled(true);
//                    taInterfaceTmplReq.setEnabled(true);
//                    cbmRuleType.setEnabled(true);
//                    taInterfaceRawReq.setEnabled(true);
//                    tfRegular.setEnabled(true);
//                    btnSaveTmpl.setEnabled(true);
//                    InterfaceRsq.setEnabled(true);
//                }
////                tlbLock.setSelected(isSelected);
//            }
//        });

        //识别
        btnIdentify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println(byteImg);

                if(byteImg == null){
                    JOptionPane.showMessageDialog(null,"请先获取要识别的图片","captcha-killer提示",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(!Util.isImage(byteImg)){
                    JOptionPane.showMessageDialog(null,"要识别的不是图片，请重新获取！","captcha-killer提示",JOptionPane.WARNING_MESSAGE);
                    System.out.println("被退出");
                    return;
                }

                if(tfInterfaceURL.getText().trim() == null || tfInterfaceURL.getText().trim().equals("")){
                    JOptionPane.showMessageDialog(null,"请设置好接口URL","captcha-killer提示",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(taInterfaceTmplReq.getText().trim() == null|| taInterfaceTmplReq.getText().trim().equals("")){
                    JOptionPane.showMessageDialog(null,"请设置调用接请求数据包","captcha-killer提示",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(tfRegular.getText().trim() == null|| tfRegular.getText().trim().equals("")){
                    JOptionPane.showMessageDialog(null,"请设置好匹配结果的正则","captcha-killer提示",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                IdentifyCaptchaThread thread = new IdentifyCaptchaThread(tfInterfaceURL.getText(),taInterfaceTmplReq.getText(),byteImg);
                thread.start();
            }
        });
        // 接口数据包面板右键
        taInterfaceTmplReq.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                    synchronized (TmplEntity.tpls){
                        String str = BurpExtender.callbacks.loadExtensionSetting("tpldb");
                        if(str != "" && str != null) {
                            try {
                                TmplEntity.tpls = JSON.parseArray(str, TmplEntity.class);
                            }catch (Exception ex){
                                TmplEntity.tpls = new ArrayList<TmplEntity>();
                            }
                        }
                    }
                    pmInterfaceMenu = new JPopupMenu();
                    JMenu menuTmplManager = new JMenu("模版库");
                    JMenuItem miGeneralTmpl = new JMenuItem("通用模版");
                    JMenuItem miTesseract = new JMenuItem("tesseract-ocr-web");
                    JMenuItem miBaiduOCR = new JMenuItem("Baidu OCR");
                    JMenuItem miCNNCaptcha = new JMenuItem("cnn_captcha");
                    JMenuItem miSaveTpl = new JMenuItem("保存为模版");
                    JMenuItem miUpdateTpl = new JMenuItem("更新模版");
                    JMenuItem miDelTpl = new JMenuItem("删除模版");
                    JMenuItem miImageRaw = new JMenuItem("验证码图片二进制内容标签");
                    miImageRaw.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int n = taInterfaceTmplReq.getSelectionStart();
                            taInterfaceTmplReq.insert("<@IMG_RAW></@IMG_RAW>",n);
                        }
                    });
                    JMenuItem miBase64Encode = new JMenuItem("Base64编码标签");
                    miBase64Encode.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int start = taInterfaceTmplReq.getSelectionStart();
                            int end = taInterfaceTmplReq.getSelectionEnd();
                            String newStr = String.format("<@BASE64>%s</@BASE64>",taInterfaceTmplReq.getSelectedText());
                            StringBuffer sbRaw = new StringBuffer(taInterfaceTmplReq.getText());
                            sbRaw.replace(start,end,newStr);
                            taInterfaceTmplReq.setText(sbRaw.toString());
                        }
                    });
                    JMenuItem miURLEncode = new JMenuItem("URL编码标签");
                    miURLEncode.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int start = taInterfaceTmplReq.getSelectionStart();
                            int end = taInterfaceTmplReq.getSelectionEnd();
                            String newStr = String.format("<@URLENCODE>%s</@URLENCODE>",taInterfaceTmplReq.getSelectedText());
                            StringBuffer sbRaw = new StringBuffer(taInterfaceTmplReq.getText());
                            sbRaw.replace(start,end,newStr);
                            taInterfaceTmplReq.setText(sbRaw.toString());
                        }
                    });

                    menuTmplManager.add(miGeneralTmpl);
                    menuTmplManager.add(miTesseract);
                    menuTmplManager.add(miBaiduOCR);
                    menuTmplManager.add(miCNNCaptcha);
                    pmInterfaceMenu.add(menuTmplManager);
                    pmInterfaceMenu.add(miSaveTpl);
                    pmInterfaceMenu.add(miUpdateTpl);
                    pmInterfaceMenu.add(miDelTpl);
                    pmInterfaceMenu.addSeparator();
                    pmInterfaceMenu.add(miImageRaw);
                    pmInterfaceMenu.add(miBase64Encode);
                    pmInterfaceMenu.add(miURLEncode);

                    miGeneralTmpl.addActionListener(new MenuActionManger());
                    miTesseract.addActionListener(new MenuActionManger());
                    miBaiduOCR.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            tfInterfaceURL.setText("https://aip.baidubce.com:443");
                            taInterfaceTmplReq.setText("POST /rest/2.0/ocr/v1/accurate?access_token=[TOKEN] HTTP/1.1\r\n" +
                                    "Host: aip.baidubce.com\r\n" +
                                    "Connection: close\r\n" +
                                    "Cache-Control: max-age=0\r\n" +
                                    "Upgrade-Insecure-Requests: 1\r\n" +
                                    "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36\r\n" +
                                    "Sec-Fetch-Mode: navigate\r\n" +
                                    "Sec-Fetch-User: ?1\r\n" +
                                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\r\n" +
                                    "Sec-Fetch-Site: none\r\n" +
                                    "Accept-Encoding: gzip, deflate\r\n" +
                                    "Accept-Language: zh-CN,zh;q=0.9\r\n" +
                                    "Content-Type: application/x-www-form-urlencoded\r\n" +
                                    "Content-Length: 55\r\n" +
                                    "\r\n" +
                                    "image=<@URLENCODE><@BASE64><@IMG_RAW></@IMG_RAW></@BASE64></@URLENCODE>");
                            cbmRuleType.setSelectedIndex(Rule.RULE_TYPE_REGULAR);
                            tfRegular.setText("\"words\"\\: \"(.*?)\"\\}");
                        }
                    });
                    miCNNCaptcha.addActionListener(new MenuActionManger());
                    miSaveTpl.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    if(tfInterfaceURL.getText().trim() == null || tfInterfaceURL.getText().trim().equals("")){
                                        JOptionPane.showMessageDialog(null,"请设置好接口URL","captcha-killer提示",JOptionPane.WARNING_MESSAGE);
                                        return;
                                    }

                                    if(taInterfaceTmplReq.getText().trim() == null|| taInterfaceTmplReq.getText().trim().equals("")){
                                        JOptionPane.showMessageDialog(null,"请设置调用接请求数据包","captcha-killer提示",JOptionPane.WARNING_MESSAGE);
                                        return;
                                    }

                                    if(tfRegular.getText().trim() == null|| tfRegular.getText().trim().equals("")){
                                        JOptionPane.showMessageDialog(null,"请设置好匹配结果的正则","captcha-killer提示",JOptionPane.WARNING_MESSAGE);
                                        return;
                                    }

                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            String tplName = JOptionPane.showInputDialog(null,"请输入模版名字","captcha-killer提示",JOptionPane.INFORMATION_MESSAGE);
                                            TmplEntity tpl = new TmplEntity();
                                            tpl.setName(tplName);
                                            tpl.setReqpacke(taInterfaceTmplReq.getText());
                                            tpl.setService(new HttpService(tfInterfaceURL.getText()));
                                            tpl.setRule(new Rule(cbmRuleType.getSelectedIndex(),tfRegular.getText()));
                                            synchronized (TmplEntity.tpls){
                                                TmplEntity.tpls.add(tpl);
                                            }
                                            String tpldb = JSON.toJSONString(TmplEntity.tpls);
                                            BurpExtender.callbacks.saveExtensionSetting("tpldb",tpldb);
                                        }
                                    });

                                }
                            });
                        }
                    });
                    //miUpdateTpl.addActionListener(new MenuActionManger());
                    miUpdateTpl.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(tfInterfaceURL.getText().trim() == null || tfInterfaceURL.getText().trim().equals("")){
                                JOptionPane.showMessageDialog(null,"请设置好接口URL","captcha-killer提示",JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            if(taInterfaceTmplReq.getText().trim() == null|| taInterfaceTmplReq.getText().trim().equals("")){
                                JOptionPane.showMessageDialog(null,"请设置调用接请求数据包","captcha-killer提示",JOptionPane.WARNING_MESSAGE);
                                return;
                            }

                            if(tfRegular.getText().trim() == null|| tfRegular.getText().trim().equals("")){
                                JOptionPane.showMessageDialog(null,"请设置好匹配结果的正则","captcha-killer提示",JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    CustomDlg dlg = new CustomDlg(CustomDlg.TYPE_UPDATE_TPL);
                                    dlg.setVisible(true);
                                }
                            });
                        }
                    });
                    //miDelTpl.addActionListener(new MenuActionManger());
                    miDelTpl.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    CustomDlg dlg = new CustomDlg(CustomDlg.TYPE_DEL_TPL);
                                    dlg.setVisible(true);
                                }
                            });
                        }
                    });
                    miImageRaw.addActionListener(new MenuActionManger());
                    miBase64Encode.addActionListener(new MenuActionManger());
                    miURLEncode.addActionListener(new MenuActionManger());

                    if(TmplEntity.tpls.size()>0){
                        menuTmplManager.addSeparator();
                        for(TmplEntity tpl:TmplEntity.tpls){
                            JMenuItem item = new JMenuItem(tpl.getName());
                            item.addActionListener(new MenuItemAction(tpl));
                            menuTmplManager.add(item);
                        }
                    }
                    pmInterfaceMenu.show(taInterfaceTmplReq, e.getX(), e.getY());
                }
            }
        });

        // 规则类型
        cbmRuleType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch (e.getStateChange()){
                    case ItemEvent.SELECTED:
                        if(e.getItem().equals("Response data")){
                            tfRegular.setText("response_data");
                            tfRegular.setEnabled(false);
                        }else{
                            tfRegular.setEnabled(true);
                        }
                        break;
                }
            }
        });
        //匹配
        btnSaveTmpl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Style keywordStyle = ((StyledDocument) InterfaceRsq.getDocument()).addStyle("Keyword_Style", null);
                StyleConstants.setBackground(keywordStyle, Color.YELLOW);
                Style normalStyle = ((StyledDocument) InterfaceRsq.getDocument()).addStyle("Keyword_Style", null);
                StyleConstants.setForeground(normalStyle, Color.BLACK);
                ((StyledDocument) InterfaceRsq.getDocument()).setCharacterAttributes(0,InterfaceRsq.getText().length(),normalStyle,true);

                int type = cbmRuleType.getSelectedIndex();
                String rule = tfRegular.getText();
                Rule ruleEntity = new Rule(type,rule);
                MatchResult result = RuleMannager.match(InterfaceRsq.getText(),ruleEntity);

                //JOptionPane.showMessageDialog(null,Util.getStringCount(InterfaceRsq.getText(),System.lineSeparator())-1,"", JOptionPane.WARNING_MESSAGE);
                int offest = result.getStart();
                int length = result.getEnd() - result.getStart();
                int count = 0;
                //如果规则类型不是Posisition类，需要进行地址转换
                if(type != Rule.RULE_TYPE_POSISTION){
                    //IndexOf获取到的字符串的位置和JTextPanel面板中的位置不一致，这是由于换行符号造成的。故需要前者减去换行符的个数就等于后者。
                    String rspRaw = InterfaceRsq.getText();
                    rspRaw = rspRaw.substring(0,rspRaw.indexOf(result.getResult()));
                    //为了兼容win和*nix，故要分别统计\r和\n
                    int rCount = Util.getStringCount(rspRaw,"\r");
                    int nCount = Util.getStringCount(rspRaw,"\n");
                    if(rCount > 0){
                        count = rCount;
                    }
                    if(nCount > 0){
                        count = nCount;
                    }
                    offest -= count;
                }

                ((StyledDocument) InterfaceRsq.getDocument()).setCharacterAttributes(offest,length,keywordStyle,true);
            }
        });
        // 标记为结果
        miMarkIdentifyResult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int start = InterfaceRsq.getSelectionStart();
                int end = InterfaceRsq.getSelectionEnd();
                int rnCount = 0;
                String raw = InterfaceRsq.getText();
                String rspData = new String(Util.getRspBody(raw.getBytes()));
                String rule = null;
                switch (cbmRuleType.getSelectedIndex()){
                    case Rule.RULE_TYPE_REGULAR:
                        //JTextPanel.getSelectionStart()和getSelectionEnd()与String.indexOf获取到的位置有区别，需要进行转换
                        rnCount = Util.getRNCount(raw.substring(0,start));
                        rule = RuleMannager.generateRegular(raw,start+rnCount,end+rnCount);
                        tfRegular.setText(rule);
                        break;
                    case Rule.RULE_TYPE_POSISTION:
                        rule = RuleMannager.generatePositionRule(start,end);
                        tfRegular.setText(rule);
                        break;
                    case Rule.RULE_TYPE_START_END_STRING:
                        //JTextPanel.getSelectionStart()和getSelectionEnd()与String.indexOf获取到的位置有区别，需要进行转换
                        rnCount = Util.getRNCount(raw.substring(0,start));
                        rule = RuleMannager.generateStartEndRule(raw,start+rnCount,end+rnCount);
                        tfRegular.setText(rule);
                        break;
                    case Rule.RULE_TYPE_JSON_MATCH:
                        JsonMatcher jsonMatcher = new JsonMatcher();
                        rule = jsonMatcher.buildKeyword(rspData,InterfaceRsq.getSelectedText());
                        tfRegular.setText(rule);
                    case Rule.RULE_TYPE_XML_MATCH:
                        XmlMatcher xmlMatcher = new XmlMatcher();
                        rule = xmlMatcher.buildKeyword(raw,InterfaceRsq.getSelectedText());
                        tfRegular.setText(rule);
                    default:
                        break;
                }

            }
        });

        //接口返回数据面板右键显示菜单
        InterfaceRsq.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //当选择匹配模式为Response data时，不显示邮件菜单
                if(cbmRuleType.getSelectedIndex() == 0){
                    return;
                }
                if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                    pppInterfaceRsq.show(InterfaceRsq, e.getX(), e.getY());
                }
            }
        });

        //结果列表选中事件
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int row = table.getSelectedRow();
                if(row != -1) {
                    taInterfaceRawReq.setText(new String(captcha.get(row).getReqRaw()));
                    InterfaceRsq.setText(new String(captcha.get(row).getRsqRaw()));
                }
            }
        });

        //结果列表右键事件，显示菜单
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                    pppMenu.show(table, e.getX(), e.getY());
                }
            }
        });

        //清空结果
        miClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (captcha){
                    captcha.clear();
                    model.fireTableDataChanged();
                }
            }
        });

        miShowIntruderResult.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(BurpExtender.isShowIntruderResult) {
                    BurpExtender.isShowIntruderResult = false;
                    miShowIntruderResult.setText("显示Intruder识别结果");
                }else{
                    BurpExtender.isShowIntruderResult = true;
                    miShowIntruderResult.setText("隐藏Intruder识别结果");
                }
            }
        });
    }


    public class GetCaptchaThread extends Thread {
        private String url;
        private String raw;

        public GetCaptchaThread(String url,String raw) {
            this.url = url;
            this.raw = raw;
        }

        public void run() {
            btnGetCaptcha.setEnabled(false);
            //清洗验证码URL
            HttpService service = new HttpService(url);
            tfURL.setText(service.toString());

            try {
                byte[][] bytesResResp = Util.requestImage(url,raw);

                byte[] byteRes = bytesResResp[0]; // 只有响应包
                byte[] byteResp = bytesResResp[1]; // 响应头+响应包
                String words = tfWords.getText().trim();
                String token = tfToken.getText().trim();

                if (!token.trim().equals("")){
                    tokenwords = extractToken(new String(byteResp) ,token);
                    tfTokenex.setText(tokenwords);
                }
                BurpExtender.stdout.println(tokenwords);

                if(!words.trim().equals("")){
                    byteImg = dataimgToimg(new String(byteRes) ,words);
                }else {
//                    System.out.println("6666");
                    //System.out.println(new String(byteRes));

                    if (Util.isImage(byteRes)) {
//                        System.out.println("55555");
                        byteImg = byteRes;
                    } else if (Util.isImage(new String(byteRes))) {
//                        System.out.println("7777");
                        byteImg = dataimgToimg(new String(byteRes));
                    } else {
//                        System.out.println("8888");
                        lbImage.setIcon(null);
                        System.out.println("this isn't image");
                        lbImage.setText("获取到的不是图片文件或者未设置关键词！");
                        lbImage.setForeground(Color.RED);
                        return;
                    }
                }

                ImageIcon icon = Util.byte2img(byteImg);
                lbImage.setIcon(icon);
                lbImage.setText("");
            } catch (Exception e) {
                BurpExtender.stderr.println(e.getMessage());
            }finally {
                BurpExtender.gui.getBtnGetCaptcha().setEnabled(true);
            }
        }
    }


    public static CaptchaEntity identifyCaptcha(String url,String raw,byte[] byteImg,int type,String pattern) throws IOException {
        CaptchaEntity cap = new CaptchaEntity();
        cap.setImage(byteImg);
        HttpClient http = new HttpClient(url, raw, byteImg);
        cap.setReqRaw(http.getRaw().getBytes());
        byte[] rsp = http.doReust();
        cap.setRsqRaw(rsp);;
        String rspRaw = new String(rsp);

        Rule rule = new Rule(type,pattern);
        MatchResult result = RuleMannager.match(rspRaw, rule);
        cap.setResult(result.getResult());
        //排查请求速度过快可能会导致
        BurpExtender.stdout.println("---------------------------------------------");
        BurpExtender.stdout.println(rspRaw);
        BurpExtender.stdout.println("[+] res = " + result.getResult());
        return cap;
    }

    public class IdentifyCaptchaThread extends Thread{
        private String url;
        private String raw;
        private byte[] img;
        private String pattern;

        public IdentifyCaptchaThread(String url,String raw,byte[] img){
            this.url = url;
            this.raw = raw;
            this.img = img;
            this.pattern = tfRegular.getText();
        }

        @Override
        public void run() {

            int type = cbmRuleType.getSelectedIndex();
            Rule myRule = new Rule(type,tfRegular.getText());

            InterfaceRsq.setText("");
            //btnIdentify.setEnabled(false);
            //清洗接口URL
            HttpService service = new HttpService(url);
            tfInterfaceURL.setText(service.toString());

            HttpClient http = null;
            try {
                http = new HttpClient(url,raw,byteImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
            taInterfaceRawReq.setText(http.getRaw());
            byte[] rsp = http.doReust();
            String rspRaw = new String(rsp);
            InterfaceRsq.setText(rspRaw);
            btnIdentify.setEnabled(true);

            MatchResult result = RuleMannager.match(rspRaw,myRule);
            CaptchaEntity cap = new CaptchaEntity();
            cap.setImage(img);
            cap.setReqRaw(http.getRaw().getBytes());
            cap.setRsqRaw(rsp);
            cap.setResult(result.getResult());

            synchronized (captcha){
                int row = captcha.size();
                captcha.add(cap);
                model.fireTableRowsInserted(row,row);
            }
        }
    }

    public class MenuActionManger implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    public class MenuItemAction implements ActionListener {
        private TmplEntity tpl;

        public MenuItemAction(TmplEntity tpl){
            this.tpl = tpl;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            tfInterfaceURL.setText(tpl.getService().toString());
            taInterfaceTmplReq.setText(tpl.getReqpacke());
            cbmRuleType.setSelectedIndex(tpl.getRule().getType());
            tfRegular.setText(tpl.getRule().getRule());
        }
    }

    public Component getComponet(){
        return MainPanel;
    }
}


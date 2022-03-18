/**
 * Copyright (c) 2019 c0ny1 (https://github.com/c0ny1/captcha-killer)
 * License: MIT
 */
package ui;

import burp.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单类，负责显示菜单，处理菜单事件
 */
public class Menu implements IContextMenuFactory {
    public List<JMenuItem> createMenuItems(final IContextMenuInvocation invocation) {
        List<JMenuItem> menus = new ArrayList();
        JMenu menu = new JMenu("captcha-killer");
        JMenuItem miSend2Cap = new JMenuItem("Send to captcha panel");
        JMenuItem miSend2Interface = new JMenuItem("Send to interface panel");
        menu.add(miSend2Cap);
        menu.add(miSend2Interface);

        final IHttpRequestResponse iReqResp = invocation.getSelectedMessages()[0];
        IRequestInfo reqInfo = BurpExtender.helpers.analyzeRequest(iReqResp.getRequest());

        miSend2Cap.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent arg0) {
                try {
                    IHttpService httpservice = iReqResp.getHttpService();
                    String url = String.format("%s://%s:%d",httpservice.getProtocol(),httpservice.getHost(),httpservice.getPort());
                    BurpExtender.gui.getTfURL().setText(url);
                    BurpExtender.gui.getTaRequest().setText(new String(iReqResp.getRequest()));
                }catch (Exception e){
                    BurpExtender.stderr.println("[-] " + e.getMessage());
                }
            }
        });

        miSend2Interface.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    IHttpService httpservice = iReqResp.getHttpService();
                    String url = String.format("%s://%s:%d",httpservice.getProtocol(),httpservice.getHost(),httpservice.getPort());
                    BurpExtender.gui.getInterfaceURL().setText(url);
                    BurpExtender.gui.getTaInterfaceTmplReq().setText(new String(iReqResp.getRequest()));
                }catch (Exception ex){
                    BurpExtender.stderr.println("[-] " + ex.getMessage());
                }
            }
        });
        menus.add(menu);
        return menus;
    }
}

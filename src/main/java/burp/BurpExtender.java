/**
 * MIT License
 *
 * Copyright (c) 2019 c0ny1
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package burp;

import ui.GUI;
import ui.Menu;
import utils.Util;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class BurpExtender implements IBurpExtender,ITab,IIntruderPayloadGeneratorFactory, IIntruderPayloadGenerator,IHttpListener{
    public static IBurpExtenderCallbacks callbacks;
    public static IExtensionHelpers helpers;
    private String extensionName = "captcha-killer-modified";
    private String version ="0.20";
    public static boolean isShowIntruderResult = true; // 识别结果是否显示Intruder模块结果
    public static PrintWriter stdout;
    public static PrintWriter stderr;
    public static GUI gui;
    public Boolean Isreplace = false;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks calllbacks) {
        this.callbacks = calllbacks;
        this.helpers = calllbacks.getHelpers();
        this.stdout = new PrintWriter(calllbacks.getStdout(),true);
        this.stderr = new PrintWriter(calllbacks.getStderr(),true);
        gui = new GUI();
        callbacks.setExtensionName(String.format("%s %s",extensionName,version));
        calllbacks.registerContextMenuFactory(new Menu());
        calllbacks.registerIntruderPayloadGeneratorFactory(this);
        callbacks.registerHttpListener(BurpExtender.this); // 注册 HttpListener 接口

        stdout = new PrintWriter(callbacks.getStdout(),true);
        stderr = new PrintWriter(callbacks.getStderr(),true);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BurpExtender.this.callbacks.addSuiteTab(BurpExtender.this);
            }
        });
        stdout.println(Util.getBanner(extensionName,version));


    }

    @Override
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo)
    {

        if (toolFlag == IBurpExtenderCallbacks.TOOL_PROXY || toolFlag == IBurpExtenderCallbacks.TOOL_INTRUDER || toolFlag == IBurpExtenderCallbacks.TOOL_REPEATER){
            if (messageIsRequest) {

                byte[] request = messageInfo.getRequest();
                IRequestInfo iRequestInfo = helpers.analyzeRequest(messageInfo);
                // 获取请求中的所有参数
                List<String> headersList = iRequestInfo.getHeaders();
                int bodyOffset = iRequestInfo.getBodyOffset();
                byte[] body = Arrays.copyOfRange(request, bodyOffset, request.length);
                int i = 0;
//                BurpExtender.stdout.println("81");
                for (String singleheader : headersList) {
                    headersList.set(i ,singleheader.replace("@captcha-killer-modified@",gui.tokenwords));
                    i++;
//                    BurpExtender.stdout.println(headersList.get(i-1));
                }

                String cap = "";
                if ((headersList.contains("@captcha@") || new String(body).contains("@captcha@"))  ) {
                    try {
                        Isreplace = true;
                        cap = GUI.identifyCaptchas(gui.getInterfaceURL().getText(), gui.getTaInterfaceTmplReq().getText(), gui.byteImg, gui.getCbmRuleType().getSelectedIndex(), gui.getRegular().getText());
                        BurpExtender.stdout.println(cap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                byte[] httpmsgresp = helpers.buildHttpMessage(headersList, (new String(body).replace("@captcha-killer-modified@",gui.tokenwords).replace("@captcha@",cap).getBytes()));
                messageInfo.setRequest(httpmsgresp);

                GUI.GetCaptchaThread thread = new GUI.GetCaptchaThread(gui.tfURL.getText(), gui.taRequest.getText());
                thread.start();
                Isreplace = false;

            }
            else{

                if (Isreplace ) {
                    GUI.GetCaptchaThread thread = new GUI.GetCaptchaThread(gui.tfURL.getText(), gui.taRequest.getText());
                    thread.start();
                    Isreplace = false;
                }
            }
        }


    }

    @Override
    public String getTabCaption() {
        return extensionName;
    }

    @Override
    public Component getUiComponent() {
        return gui.getComponet();
    }

    @Override
    public boolean hasMorePayloads() {
        return true;
    }

    @Override
    public byte[] getNextPayload(byte[] bytes) {
//        if(!Util.isURL(gui.getInterfaceURL().getText())){
//            return "Interface URL format invalid".getBytes();
//        }
//
//        CaptchaEntity cap = new CaptchaEntity();
//        int count = 0;
//        try {
//            byte[] byteImg = Util.requestImage(gui.getCaptchaURL(),gui.getCaptchaReqRaw());
//            //遗留问题：burp自带的发包，无法指定超时。如果访问速度过快，这里可能为空。
//            while (count < 3){
//                cap = GUI.identifyCaptcha(gui.getInterfaceURL().getText(),gui.getTaInterfaceTmplReq().getText(),byteImg,gui.getCbmRuleType().getSelectedIndex(),gui.getRegular().getText());
//                if(cap.getResult() == null || cap.getResult().trim().equals("")){
//                    Thread.sleep(1000);
//                    count += 1;
//                }else{
//                    break;
//                }
//            }
//
//            synchronized (gui.captcha){
//                int row = gui.captcha.size();
//                gui.captcha.add(cap);
//                gui.getModel().fireTableRowsInserted(row,row);
//            }
//        } catch (Exception e) {
//            cap.setResult(e.getMessage());
//        }
//
//        return cap.getResult().getBytes();
        GeneratePayloadSwingWorker gpsw = new GeneratePayloadSwingWorker();
        gpsw.execute();
        try {
            Object result = gpsw.get();
//            BurpExtender.stdout.println(new String((byte[])result));
            return (byte[])result;
        }catch (Exception e){
            e.printStackTrace();
            return String.format("Erro: %s",e.getMessage()).getBytes();
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public String getGeneratorName() {
        return this.extensionName;
    }

    @Override
    public IIntruderPayloadGenerator createNewInstance(IIntruderAttack iIntruderAttack) {
        return this;
    }
}

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

/* loaded from: captcha-killer-modified-0.21-jdk8.jar:burp/BurpExtender.class */
public class BurpExtender implements IBurpExtender, ITab, IIntruderPayloadGeneratorFactory, IIntruderPayloadGenerator, IHttpListener ,IIntruderPayloadProcessor{
    public static IBurpExtenderCallbacks callbacks;
    public static IExtensionHelpers helpers;
    public static boolean isShowIntruderResult = true;
    public static PrintWriter stdout;
    public static PrintWriter stderr;
    public static GUI gui;
    private String extensionName = "captcha-killer-modified";
    private String version = "0.24.1";
    public String processname = "captcha-killer-modified";
    public static Boolean Isreplace = false;

    @Override // burp.IBurpExtender
    public void registerExtenderCallbacks(IBurpExtenderCallbacks calllbacks) {
        callbacks = calllbacks;
        helpers = calllbacks.getHelpers();


        stdout = new PrintWriter(calllbacks.getStdout(), true);
        stderr = new PrintWriter(calllbacks.getStderr(), true);
        gui = new GUI();
        callbacks.setExtensionName(String.format("%s %s", this.extensionName, this.version));
        calllbacks.registerContextMenuFactory(new Menu());
        calllbacks.registerIntruderPayloadGeneratorFactory(this);
        callbacks.registerIntruderPayloadProcessor(this);
        callbacks.registerHttpListener(this);
        stdout = new PrintWriter(callbacks.getStdout(), true);
        stderr = new PrintWriter(callbacks.getStderr(), true);
        SwingUtilities.invokeLater(new Runnable() { // from class: burp.BurpExtender.1
            @Override // java.lang.Runnable
            public void run() {
                BurpExtender burpExtender = BurpExtender.this;
                BurpExtender.callbacks.addSuiteTab(BurpExtender.this);
            }
        });
        stdout.println(Util.getBanner(this.extensionName, this.version));
    }

    @Override // burp.IHttpListener
    public void processHttpMessage(int toolFlag, boolean messageIsRequest, IHttpRequestResponse messageInfo) {
        if (!(toolFlag == 4 || toolFlag == 32 || toolFlag == 64)) {
            return;
        }else if ( toolFlag == IBurpExtenderCallbacks.TOOL_INTRUDER || toolFlag == IBurpExtenderCallbacks.TOOL_REPEATER) {
            if (messageIsRequest) {
                byte[] request = messageInfo.getRequest();
                IRequestInfo iRequestInfo = helpers.analyzeRequest(messageInfo);
                List<String> headersList = iRequestInfo.getHeaders();
                int bodyOffset = iRequestInfo.getBodyOffset();
                byte[] body = Arrays.copyOfRange(request, bodyOffset, request.length);

//                String cap = "";
//                stdout.println(headersList.contains("@captcha-killer-modified@"));
//                stdout.println(headersList);
//                stdout.println(new String(body));
                if ( (headersList.get(0).contains("@captcha@") || new String(body).contains("@captcha@") || (  (toolFlag == IBurpExtenderCallbacks.TOOL_REPEATER ) && headersList.get(0).contains("@captcha-killer-modified@")  ) || ((toolFlag == IBurpExtenderCallbacks.TOOL_REPEATER ) && new String(body).contains("@captcha-killer-modified@") ) || ( toolFlag == IBurpExtenderCallbacks.TOOL_INTRUDER )) && gui.getUsebutton() ) {

                    BurpExtender.stdout.println(getGeneratorName());
                        try {
                            if ( (toolFlag == IBurpExtenderCallbacks.TOOL_INTRUDER && headersList.contains("@captcha@"))
                                    ||
                                    (toolFlag == IBurpExtenderCallbacks.TOOL_INTRUDER && headersList.contains("@captcha-killer-modified@"))
                                    ||
                                    headersList.contains("@captcha@")
                                    ||
                                    new String(body).contains("@captcha@")
                                    ||
                                    (  (toolFlag == IBurpExtenderCallbacks.TOOL_REPEATER ) && headersList.contains("@captcha-killer-modified@")  )
                                    ||
                                    ((toolFlag == IBurpExtenderCallbacks.TOOL_REPEATER ) && new String(body).contains("@captcha-killer-modified@") && new String(body).contains("@captcha@"))  ) {
//                        BurpExtender.stdout.println(Arrays.toString(gui.byteImg));
                                BurpExtender.gui.cap = GUI.identifyCaptchas(gui.getInterfaceURL().getText(), gui.getTaInterfaceTmplReq().getText(), BurpExtender.gui.byteImg, gui.getCbmRuleType().getSelectedIndex(), gui.getRegular().getText());

                                BurpExtender.gui.tfcapex.setText(gui.cap);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        int i = 0;
                        for (String singleheader : headersList) {
                            headersList.set(i, singleheader.replace("@captcha-killer-modified@", gui.tokenwords));
                            i++;
                        }

                        byte[] httpmsgresp = helpers.buildHttpMessage(headersList, new String(body).replace("@captcha-killer-modified@", gui.tokenwords).replace("@captcha@", BurpExtender.gui.cap).getBytes());
                        messageInfo.setRequest(httpmsgresp);

                    Isreplace = true;
                }


            } else if (!messageIsRequest && Isreplace) {

                GUI.GetCaptchaThread thread = new GUI.GetCaptchaThread(gui.tfURL.getText(), gui.taRequest.getText());
                thread.start();
                try {
                    Thread.sleep(1250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                stdout.println( "response:" + Arrays.toString(BurpExtender.gui.byteImg).length());
//                stdout.println(Isreplace);
//                stdout.println("+++++++++++++++++++++++");
                Isreplace = false;
            }
        }
    }

    @Override // burp.ITab
    public String getTabCaption() {
        return this.extensionName;
    }

    @Override // burp.ITab
    public Component getUiComponent() {
        return gui.getComponet();
    }

    @Override // burp.IIntruderPayloadGenerator
    public boolean hasMorePayloads() {
        return true;
    }

    @Override // burp.IIntruderPayloadGenerator
    public byte[] getNextPayload(byte[] bytes) {

        GeneratePayloadSwingWorker gpsw = new GeneratePayloadSwingWorker();
        gpsw.execute();
        try {
            Object result = gpsw.get();
            return (byte[]) result;
        } catch (Exception e) {
            e.printStackTrace();
            return String.format("Erro: %s", e.getMessage()).getBytes();
        }
    }

    @Override // burp.IIntruderPayloadGenerator
    public void reset() {
    }

    @Override // burp.IIntruderPayloadGeneratorFactory
    public String getGeneratorName() {
        return this.processname;
    }

    @Override // burp.IIntruderPayloadGeneratorFactory
    public IIntruderPayloadGenerator createNewInstance(IIntruderAttack iIntruderAttack) {
        return this;
    }

    @Override
    public String getProcessorName() {
        return "captcha";
    }

    @Override
    public byte[] processPayload(byte[] currentPayload, byte[] originalPayload, byte[] baseValue) {
        return new byte[0];
    }
}
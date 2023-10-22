package burp;

import ui.GUI;
import utils.Util;

import javax.swing.*;

import static burp.BurpExtender.gui;

public class GeneratePayloadSwingWorker extends SwingWorker {
    @Override
    protected Object doInBackground() throws Exception {
//        BurpExtender.stdout.println("*****");
        if(!Util.isURL(gui.getInterfaceURL().getText())){
            return "Interface URL format invalid".getBytes();
        }

//        CaptchaEntity cap = new CaptchaEntity();
//        String cap = null;
        int count = 0;
        try {
//            byte[] byteImg = Util.requestImage(BurpExtender.gui.getCaptchaURL(),BurpExtender.gui.getCaptchaReqRaw())[0];
//            byte[] byteResp = Util.requestImage(BurpExtender.gui.getCaptchaURL(),BurpExtender.gui.getCaptchaReqRaw())[1];

//            byte[][] bytesResResp = Util.requestImage(gui.getCaptchaURL(), gui.getCaptchaReqRaw());

//            byte[] byteImg = bytesResResp[0]; // 只有响应包

//            BurpExtender.stdout.println(new String(gui.byteImg));
//            byte[] byteResp = bytesResResp[1]; // 响应头+响应包
//            byteImg = new String(byteImg).replace("data:image/png;base64,","").getBytes();
//            String token = gui.tfToken.getText().trim();
//            if (!token.trim().equals("")) {
//                gui.tokenwords = extractToken(new String(byteResp) ,token);
//            }
            BurpExtender.gui.cap = GUI.identifyCaptchas( BurpExtender.gui.getInterfaceURL().getText(),BurpExtender.gui.getTaInterfaceTmplReq().getText(),BurpExtender.gui.byteImg,BurpExtender.gui.getCbmRuleType().getSelectedIndex(),BurpExtender.gui.getRegular().getText() );

            //遗留问题：burp自带的发包，无法指定超时。如果访问速度过快，这里可能为空。
            while (count < 3 ) {
//                BurpExtender.stdout.println("error");
//                BurpExtender.stdout.println(gui.cap);
//                BurpExtender.gui.cap = GUI.identifyCaptchas( BurpExtender.gui.getInterfaceURL().getText(),BurpExtender.gui.getTaInterfaceTmplReq().getText(),BurpExtender.gui.byteImg,BurpExtender.gui.getCbmRuleType().getSelectedIndex(),BurpExtender.gui.getRegular().getText() ) ;
//                gui.cap = GUI.identifyCaptchas(gui.getInterfaceURL().getText(), gui.getTaInterfaceTmplReq().getText(), gui.byteImg, gui.getCbmRuleType().getSelectedIndex(), gui.getRegular().getText());
//                if(cap.getResult() == null || cap.getResult().trim().equals("")){
//                    Thread.sleep(1000);
//                    count += 1;
//                }else{
//                    break;
//                }
                if(gui.cap == null || gui.cap.trim().equals("")){
                    Thread.sleep(1500);
                    count += 1;
                }else{
                    break;
                }
            }


//            if(BurpExtender.isShowIntruderResult) {
//                synchronized (BurpExtender.gui.captcha) {
//                    int row = BurpExtender.gui.captcha.size();
//                    BurpExtender.gui.captcha.add(cap);
//                    BurpExtender.gui.getModel().fireTableRowsInserted(row, row);
//                }
//            }
        } catch (Exception e) {
//            cap(e.getMessage());
        }
        return gui.cap.getBytes();
    }
}

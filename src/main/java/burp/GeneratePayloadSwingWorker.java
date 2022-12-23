package burp;

import entity.CaptchaEntity;
import ui.GUI;
import utils.Util;

import javax.swing.*;

import static utils.Util.extractToken;

public class GeneratePayloadSwingWorker extends SwingWorker {
    @Override
    protected Object doInBackground() throws Exception {
        if(!Util.isURL(BurpExtender.gui.getInterfaceURL().getText())){
            return "Interface URL format invalid".getBytes();
        }

        CaptchaEntity cap = new CaptchaEntity();
        int count = 0;
        try {
//            byte[] byteImg = Util.requestImage(BurpExtender.gui.getCaptchaURL(),BurpExtender.gui.getCaptchaReqRaw())[0];
//            byte[] byteResp = Util.requestImage(BurpExtender.gui.getCaptchaURL(),BurpExtender.gui.getCaptchaReqRaw())[1];

            byte[][] bytesResResp = Util.requestImage(BurpExtender.gui.getCaptchaURL(),BurpExtender.gui.getCaptchaReqRaw());
            byte[] byteImg = bytesResResp[0]; // 只有响应包
            BurpExtender.stdout.println("*****");
//            BurpExtender.stdout.println(new String(byteImg));
            byte[] byteResp = bytesResResp[1]; // 响应头+响应包
//            byteImg = new String(byteImg).replace("data:image/png;base64,","").getBytes();
            String token = BurpExtender.gui.tfToken.getText().trim();
            if (!token.trim().equals("")){
                BurpExtender.gui.tokenwords = extractToken(new String(byteResp) ,token);
            }

            //遗留问题：burp自带的发包，无法指定超时。如果访问速度过快，这里可能为空。
            while (count < 3){
                cap = GUI.identifyCaptcha(BurpExtender.gui.getInterfaceURL().getText(),BurpExtender.gui.getTaInterfaceTmplReq().getText(),byteImg,BurpExtender.gui.getCbmRuleType().getSelectedIndex(),BurpExtender.gui.getRegular().getText());
                if(cap.getResult() == null || cap.getResult().trim().equals("")){
                    Thread.sleep(1000);
                    count += 1;
                }else{
                    break;
                }
            }


            if(BurpExtender.isShowIntruderResult) {
                synchronized (BurpExtender.gui.captcha) {
                    int row = BurpExtender.gui.captcha.size();
                    BurpExtender.gui.captcha.add(cap);
                    BurpExtender.gui.getModel().fireTableRowsInserted(row, row);
                }
            }
        } catch (Exception e) {
            cap.setResult(e.getMessage());
        }
        return cap.getResult().getBytes();
    }
}

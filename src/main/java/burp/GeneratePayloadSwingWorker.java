package burp;

import entity.CaptchaEntity;
import ui.GUI;
import utils.Util;

import javax.swing.*;

public class GeneratePayloadSwingWorker extends SwingWorker {
    @Override
    protected Object doInBackground() throws Exception {
        if(!Util.isURL(BurpExtender.gui.getInterfaceURL().getText())){
            return "Interface URL format invalid".getBytes();
        }

        CaptchaEntity cap = new CaptchaEntity();
        int count = 0;
        try {
            byte[] byteImg = Util.requestImage(BurpExtender.gui.getCaptchaURL(),BurpExtender.gui.getCaptchaReqRaw());
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

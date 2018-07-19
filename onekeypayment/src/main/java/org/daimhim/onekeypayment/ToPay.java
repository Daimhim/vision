package org.daimhim.onekeypayment;

import android.app.Activity;

import org.daimhim.onekeypayment.model.PaymentRequest;


/**
 * 项目名称：org.daimhim.onekeypayment
 * 项目版本：vision
 * 创建时间：2018.05.30 15:41
 * 修改人：Daimhim
 * 修改时间：2018.05.30 15:41
 * 类描述：
 * 修改备注：
 *
 * @author：Daimhim
 */
public class ToPay {
    private ToPay(){

    }
    private static class SingletonHolder{
        private final static ToPay instance=new ToPay();
    }
    public static ToPay getInstance(){
        return SingletonHolder.instance;
    }
    /**
     * 支付
     * @param pActivity 调用界面 支付完成后会 = null
     * @param pPaymentRequest 支付参数
     * @param pPaymentCallback 支付返回
     */
    public void toPayment(Activity pActivity,PaymentRequest pPaymentRequest, PaymentCallback pPaymentCallback) {
        if (null != pPaymentRequest && null != pPaymentCallback) {
            new Paymenting(pActivity, pPaymentCallback).execute(pPaymentRequest);
        }
    }
}

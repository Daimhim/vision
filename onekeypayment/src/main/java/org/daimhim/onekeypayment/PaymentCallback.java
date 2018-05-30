package org.daimhim.onekeypayment;

import org.daimhim.onekeypayment.model.PaymentReponse;

/**
 * 项目名称：org.daimhim.onekeypayment
 * 项目版本：vision
 * 创建时间：2018.05.30 18:58
 * 修改人：Daimhim
 * 修改时间：2018.05.30 18:58
 * 类描述：
 * 修改备注：
 *
 * @author：Daimhim
 */
public interface PaymentCallback {
    /**
     *  支付成功
     * @param result 返回Bean
     */
    void onPaymentSuccess(PaymentReponse result);

    /**
     *  支付失败
     * @param status 失败状态
     */
    void onPaymentFailure(String status);
}

package org.daimhim.onekeypayment.model;

import android.text.TextUtils;

/**
 * 项目名称：org.daimhim.onekeypayment.model
 * 项目版本：vision
 * 创建时间：2018.05.30 15:53
 * 修改人：Daimhim
 * 修改时间：2018.05.30 15:53
 * 类描述：
 * 修改备注：
 *
 * @author：Daimhim
 */
public class PaymentReponse {
    /**
     * 错误信息
     */
    private String errorMessage;
    private String payStatus;
    private String orderSn;
    private String orderId;
    private String account;
    private String result;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String pErrorMessage) {
        errorMessage = pErrorMessage;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String pPayStatus) {
        payStatus = pPayStatus;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String pOrderSn) {
        orderSn = pOrderSn;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String pOrderId) {
        orderId = pOrderId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String pAccount) {
        account = pAccount;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String pResult) {
        result = pResult;
    }

    @Override
    public String toString() {
        return "PaymentReponse{" +
                "errorMessage='" + errorMessage + '\'' +
                ", payStatus='" + payStatus + '\'' +
                ", orderSn='" + orderSn + '\'' +
                ", orderId='" + orderId + '\'' +
                ", account='" + account + '\'' +
                ", result='" + result + '\'' +
                '}';
    }

    public PaymentReponse analysisAL(String rawResult){
        if (TextUtils.isEmpty(rawResult)){

            return this;
        }

        String[] resultParams = rawResult.split(";");
        for (String resultParam : resultParams) {
            if (resultParam.startsWith("resultStatus")) {
                payStatus = gatValue(resultParam, "resultStatus");
            }
            if (resultParam.startsWith("result")) {
                result = gatValue(resultParam, "result");
            }
            if (resultParam.startsWith("memo")) {
                errorMessage = gatValue(resultParam, "memo");
            }
        }
        return this;
    }

    private String gatValue(String content, String key) {
        String prefix = key + "={";
        return content.substring(content.indexOf(prefix) + prefix.length(),
                content.lastIndexOf("}"));
    }
}

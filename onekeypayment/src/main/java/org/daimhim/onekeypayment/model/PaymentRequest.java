package org.daimhim.onekeypayment.model;

/**
 * 项目名称：org.daimhim.onekeypayment.model
 * 项目版本：vision
 * 创建时间：2018.05.30 15:50
 * 修改人：Daimhim
 * 修改时间：2018.05.30 15:50
 * 类描述：
 * 修改备注：
 *
 * @author：Daimhim
 */
public class PaymentRequest {
    /**
     * 订单编号
     */
    private String orderSn;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 账户
     */
    private String account;
    /**
     * 需付款金额
     */
    private String dealPrice;
    /**
     * 0 微信
     * 1 支付宝
     */
    private int payType;
    /**
     * sign
     */
    private String signinfo;

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "orderSn='" + orderSn + '\'' +
                ", orderId='" + orderId + '\'' +
                ", account='" + account + '\'' +
                ", dealPrice='" + dealPrice + '\'' +
                ", payType=" + payType +
                ", signinfo='" + signinfo + '\'' +
                '}';
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

    public String getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(String pDealPrice) {
        dealPrice = pDealPrice;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int pPayType) {
        payType = pPayType;
    }

    public String getSigninfo() {
        return signinfo;
    }

    public void setSigninfo(String pSigninfo) {
        signinfo = pSigninfo;
    }
}

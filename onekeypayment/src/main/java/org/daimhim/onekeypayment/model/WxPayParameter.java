package org.daimhim.onekeypayment.model;

/**
 * 项目名称：org.daimhim.onekeypayment.model
 * 项目版本：vision
 * 创建时间：2018.05.31 10:00
 * 修改人：Daimhim
 * 修改时间：2018.05.31 10:00
 * 类描述：
 * 修改备注：
 *
 * @author：Daimhim
 */
public class WxPayParameter extends PayParameter{
    /**
     * 时间戳
     */
    private String timeStamp;
    /**
     * package
     */
    private String packageValue;
    /**
     * 加密签名
     */
    private String paySign;
    /**
     * 支付类型
     */
    private String signType;
    private String nonceStr;
    /**
     * appid
     */
    private String appId;
    private String partnerId;

    private String prepayId;
    /**
     * API密钥，在商户平台设置
     */
    private String apikey;

    @Override
    public String toString() {
        return "WxPayParameter{" +
                "timeStamp='" + timeStamp + '\'' +
                ", packageValue='" + packageValue + '\'' +
                ", paySign='" + paySign + '\'' +
                ", signType='" + signType + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", appId='" + appId + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", prepayId='" + prepayId + '\'' +
                ", apikey='" + apikey + '\'' +
                '}';
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String pTimeStamp) {
        timeStamp = pTimeStamp;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String pPackageValue) {
        packageValue = pPackageValue;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String pPaySign) {
        paySign = pPaySign;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String pSignType) {
        signType = pSignType;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String pNonceStr) {
        nonceStr = pNonceStr;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String pAppId) {
        appId = pAppId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String pPartnerId) {
        partnerId = pPartnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String pPrepayId) {
        prepayId = pPrepayId;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String pApikey) {
        apikey = pApikey;
    }

}

package org.daimhim.onekeypayment.model;

/**
 * 项目名称：org.daimhim.onekeypayment.model
 * 项目版本：vision
 * 创建时间：2018.05.31 10:05
 * 修改人：Daimhim
 * 修改时间：2018.05.31 10:05
 * 类描述：
 * 修改备注：
 *
 * @author：Daimhim
 */
public class AlPayParameter extends PayParameter {
    private String app_id;
    private String biz_content;
    private String charset;
    private String method;
    private String sign_type;
    private String timestamp;
    private String version;
    private String sign;
    private String format;
    /**
     * 支付宝支付回调
     */
    private String notify_url;
    /**
     * 签名后的结果 自定义字段
     */
    private String signInfo;
    /**
     * 私钥   需要拼接时传入
     */
    private String private_key;

    @Override
    public String toString() {
        return "AlPayParameter{" +
                "app_id='" + app_id + '\'' +
                ", biz_content='" + biz_content + '\'' +
                ", charset='" + charset + '\'' +
                ", method='" + method + '\'' +
                ", sign_type='" + sign_type + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", version='" + version + '\'' +
                ", sign='" + sign + '\'' +
                ", format='" + format + '\'' +
                ", notify_url='" + notify_url + '\'' +
                ", signInfo='" + signInfo + '\'' +
                ", private_key='" + private_key + '\'' +
                '}';
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String pPrivate_key) {
        private_key = pPrivate_key;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String pNotify_url) {
        notify_url = pNotify_url;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String pFormat) {
        format = pFormat;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String pApp_id) {
        app_id = pApp_id;
    }

    public String getBiz_content() {
        return biz_content;
    }

    public void setBiz_content(String pBiz_content) {
        biz_content = pBiz_content;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String pCharset) {
        charset = pCharset;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String pMethod) {
        method = pMethod;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String pSign_type) {
        sign_type = pSign_type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String pTimestamp) {
        timestamp = pTimestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String pVersion) {
        version = pVersion;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String pSign) {
        sign = pSign;
    }

    public String getSignInfo() {
        return signInfo;
    }

    public void setSignInfo(String pSignInfo) {
        signInfo = pSignInfo;
    }

}

package org.daimhim.onekeypayment;

/**
 * 项目名称：org.daimhim.onekeypayment
 * 项目版本：vision
 * 创建时间：2018.05.30 17:40
 * 修改人：Daimhim
 * 修改时间：2018.05.30 17:40
 * 类描述：
 * 修改备注：
 *
 * @author：Daimhim
 */
class PaymentConst {
    /***************支付宝相关***************/
    /**
     * 商户PID
     */
    static String PARTNER;
    /**
     * 商户收款账号
     */
    static String SELLER;


    /****************** 微信支付相关 *********************/
    /**
     *  appid
     */
    static String WX_APP_ID;
    /**
     * 商户号
     */
    static String WX_MCH_ID;
    /**
     * API密钥，在商户平台设置
     */
    static String WX_API_KEY;
    /**
     * 支付宝支付
     */
    public static final int AL_PAY = 1;
    /**
     * 微信支付
     */
    public static final int WX_PAY = 2;
    /**
     * 银联支付
     */
    public static final int YL_PAY = 3;
    /**
     * 支付状态
     */
    public static final String PAY_SUCCESS = "9000";
    public static final String PAY_CANCEL = "6001";
    public static final String PAY_FAILURE = "4000";
    public static final String PAY_ING = "8000";
    public static final String PAY_NO_EXECUTABLE_TARGET = "110";
    public static final String PAY_WX_NOT_INSTALLED = "-1";
    public static final String PAY_AL_NOT_INSTALLED = "-2";
    public static final String PAY_UNKNOWN_MISTAKE = "-100";
    // 9000	订单支付成功
    // 8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
    // 4000	订单支付失败
    // 5000	重复请求
    // 6001	用户中途取消
    // 6002	网络连接出错
    // 6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
    // 其它	其它支付错误
}

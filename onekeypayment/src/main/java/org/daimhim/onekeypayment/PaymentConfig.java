package org.daimhim.onekeypayment;
/*
请在AndroidManifest注册以下三个Activity
        <!--  支付宝支付界面回调 -->
		<activity
			android:name="com.alipay.sdk.app.H5PayActivity"
			android:exported="false"
			android:screenOrientation="portrait"
			android:windowSoftInputMode="adjustPan|stateHidden" />
		<!--  微信支付界面回调 -->
		<activity
			android:name="org.daimhim.onekeypayment.WXPayEntryActivity"
			android:exported="true"
			android:launchMode="singleTop"
			android:screenOrientation="portrait"
			android:windowSoftInputMode="adjustPan|stateHidden">
		</activity>
		<activity-alias
			android:name=".wxapi.WXPayEntryActivity"
			android:exported="true"
			android:targetActivity="org.daimhim.onekeypayment.WXPayEntryActivity" />
 */

/**
 * 项目名称：org.daimhim.onekeypayment
 * 项目版本：vision
 * 创建时间：2018.05.30 16:58
 * 修改人：Daimhim
 * 修改时间：2018.05.30 16:58
 * 类描述：
 * 修改备注：
 *
 * @author：Daimhim
 */
public class PaymentConfig {
    /**
     * 初始化
     *
     * @param wxappid  appid
     * @param wxmchid  商户号
     * @param wxapikey API密钥，在商户平台设置
     * @param partner  商户PID
     * @param seller   商户收款账号
     */
    public static void init(String wxappid, String wxmchid, String wxapikey, String partner, String seller) {
        PaymentConst.WX_APP_ID = wxappid;
        PaymentConst.WX_MCH_ID = wxmchid;
        PaymentConst.WX_API_KEY = wxapikey;
        PaymentConst.PARTNER = partner;
        PaymentConst.SELLER = seller;
    }
}

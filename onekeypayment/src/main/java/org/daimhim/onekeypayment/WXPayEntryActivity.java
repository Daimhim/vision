package org.daimhim.onekeypayment;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 项目名称：org.daimhim.onekeypayment.wxapi
 * 项目版本：vision
 * 创建时间：2018.05.30 17:05
 * 修改人：Daimhim
 * 修改时间：2018.05.30 17:05
 * 类描述：微信支付回掉
 * 修改备注：
 *
 * @author：Daimhim
 */
public class WXPayEntryActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WXAPIFactory.createWXAPI(this, PaymentConst.WX_APP_ID).handleIntent(getIntent(), Paymenting.sIWXAPIEventHandler);
        finish();
    }
}

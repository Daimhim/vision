package org.daimhim.onekeypayment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.daimhim.onekeypayment.model.PaymentReponse;
import org.daimhim.onekeypayment.model.PaymentRequest;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static org.daimhim.onekeypayment.PaymentConst.AL_PAY;
import static org.daimhim.onekeypayment.PaymentConst.PAY_CANCEL;
import static org.daimhim.onekeypayment.PaymentConst.PAY_FAILURE;
import static org.daimhim.onekeypayment.PaymentConst.PAY_SUCCESS;
import static org.daimhim.onekeypayment.PaymentConst.PAY_UNKNOWN_MISTAKE;
import static org.daimhim.onekeypayment.PaymentConst.PAY_WX_NOT_INSTALLED;
import static org.daimhim.onekeypayment.PaymentConst.WX_PAY;

/**
 * 项目名称：org.daimhim.onekeypayment
 * 项目版本：vision
 * 创建时间：2018.05.30 18:46
 * 修改人：Daimhim
 * 修改时间：2018.05.30 18:46
 * 类描述：
 * 修改备注：
 *
 * @author：Daimhim Params是指调用execute()方法时传入的参数类型和doInBackgound()的参数类型
 * Progress是指更新进度时传递的参数类型，即publishProgress()和onProgressUpdate()的参数类型
 * Result是指doInBackground()的返回值类型
 */
class Paymenting extends AsyncTask<PaymentRequest, Integer, PaymentReponse> {
    /**
     * 微信回调接口
     */
    public static PayIWXAPIEventHandler sIWXAPIEventHandler;

    @SuppressLint("StaticFieldLeak")
    private Activity mActivity;
    private PaymentCallback mPaymentCallback;

    public Paymenting(Activity pActivity, PaymentCallback pPaymentCallback) {
        mActivity = pActivity;
        mPaymentCallback = pPaymentCallback;
    }

    @Override
    protected PaymentReponse doInBackground(PaymentRequest... pPaymentRequests) {
        PaymentReponse lReponse = null;
        try {
            lReponse = new PaymentReponse();
            PaymentRequest lPaymentRequest = pPaymentRequests[0];
            switch (lPaymentRequest.getPayType()) {
                case AL_PAY:
                    //支付宝
                    String lPay = new PayTask(mActivity).pay(lPaymentRequest.getSigninfo(), true);
                    if (TextUtils.isEmpty(lPay)) {
                        lReponse.setErrorMessage(mActivity.getString(R.string.unknown_mistake));
                        lReponse.setPayStatus(PAY_UNKNOWN_MISTAKE);
                    } else {
                        lReponse.analysisAL(lPay);
                    }
                    break;
                case WX_PAY:
                    //微信
                    IWXAPI lWXAPI = WXAPIFactory.createWXAPI(mActivity, PaymentConst.WX_APP_ID, true);
                    if (lWXAPI.isWXAppInstalled()) {
                        Map<String, String> stringMap = decodeXml(lPaymentRequest.getSigninfo());
                        PayReq req = new PayReq();
                        req.appId = PaymentConst.WX_APP_ID;
                        req.partnerId = PaymentConst.WX_MCH_ID;
                        //预支付交易会话ID
                        req.prepayId = stringMap.get("prepay_id");
                        req.packageValue = "Sign=WXPay";
                        req.nonceStr = stringMap.get("nonce_str");
                        req.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
                        MD5 md5 = new MD5();
                        md5.put("appid", req.appId);
                        md5.put("noncestr", req.nonceStr);
                        md5.put("package", req.packageValue);
                        md5.put("partnerid", req.partnerId);
                        md5.put("prepayid", req.prepayId);
                        md5.put("timestamp", req.timeStamp);
                        req.sign = md5.getMessageDigest(md5.toString().getBytes()).toUpperCase(Locale.CHINA);
                        lWXAPI.sendReq(req);
                        sIWXAPIEventHandler = new PayIWXAPIEventHandler();
                        wait();
                        BaseResp lBaseResp = sIWXAPIEventHandler.getBaseResp();
                        if (null != lBaseResp) {
                            lReponse.setResult(String.format("%s", lBaseResp.errCode));
                        }
                        if (null != lBaseResp && lBaseResp.errCode == 0) {
                            lReponse.setErrorMessage("支付成功");
                            lReponse.setPayStatus(PAY_SUCCESS);
                        } else if (null != lBaseResp && lBaseResp.errCode == -2) {
                            lReponse.setErrorMessage("支付取消");
                            lReponse.setPayStatus(PAY_CANCEL);
                        } else {
                            lReponse.setErrorMessage("支付失败");
                            lReponse.setPayStatus(PAY_FAILURE);
                        }
                    } else {
                        lReponse.setErrorMessage(mActivity.getString(R.string.wx_not_installed));
                        lReponse.setPayStatus(PAY_WX_NOT_INSTALLED);
                    }
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            recycling();
        }
        return lReponse;
    }

    /**
     * 在执行前
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * 在执行后
     *
     * @param pPaymentReponse
     */
    @Override
    protected void onPostExecute(PaymentReponse pPaymentReponse) {
        super.onPostExecute(pPaymentReponse);
        if (null != pPaymentReponse) {
            mPaymentCallback.onPaymentSuccess(pPaymentReponse);
        } else {
            mPaymentCallback.onPaymentFailure(PaymentConst.PAY_FAILURE);
        }
    }

    private void recycling() {
        mActivity = null;
        sIWXAPIEventHandler = null;
    }

    /**
     * 在进度更新
     *
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    class PayIWXAPIEventHandler implements IWXAPIEventHandler {

        private BaseResp mBaseResp;

        @Override
        public void onReq(BaseReq pBaseReq) {

        }

        @Override
        public void onResp(BaseResp pBaseResp) {
            mBaseResp = pBaseResp;
            notify();
        }

        public BaseResp getBaseResp() {
            return mBaseResp;
        }
    }

    private Map<String, String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<>(10);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if (!"xml".equals(nodeName)) {
                            // 实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion-e--->", e.toString());
        }
        return null;
    }

    public class MD5 {

        public MD5() {
            param = new LinkedHashMap<>();
        }

        public final String getMessageDigest(byte[] buffer) {
            char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            try {
                MessageDigest mdTemp = MessageDigest.getInstance("MD5");
                mdTemp.update(buffer);
                byte[] md = mdTemp.digest();
                int j = md.length;
                char str[] = new char[j * 2];
                int k = 0;
                for (int i = 0; i < j; i++) {
                    byte byte0 = md[i];
                    str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                    str[k++] = hexDigits[byte0 & 0xf];
                }
                return new String(str);
            } catch (Exception e) {
                return null;
            }
        }

        private Map<String, String> param;

        public void put(String key, String value) {
            param.put(key, value);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (param == null) {
                return builder.toString();
            }
            for (Map.Entry<String, String> entry : param.entrySet()) {
                builder.append(entry.getKey());
                builder.append("=");
                builder.append(entry.getValue());
                builder.append("&");
            }
            builder.append("key=");
            builder.append(PaymentConst.WX_API_KEY);
            return builder.toString();
        }
    }
}

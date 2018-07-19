package org.daimhim.onekeypayment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.daimhim.onekeypayment.model.AlPayParameter;
import org.daimhim.onekeypayment.model.PaymentReponse;
import org.daimhim.onekeypayment.model.PaymentRequest;
import org.daimhim.onekeypayment.model.WxPayParameter;

import java.lang.reflect.Field;
import java.security.MessageDigest;
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

    private PaymentingFragment mFragment;

    String TAG = getClass().getSimpleName();
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
        Log.e(TAG, "doInBackground");
        PaymentReponse lReponse = null;
        try {
            lReponse = new PaymentReponse();
            PaymentRequest lPaymentRequest = pPaymentRequests[0];
            if (BuildConfig.DEBUG) {
                Log.d(TAG, lPaymentRequest.toString());
            }
            switch (lPaymentRequest.getPayType()) {
                case AL_PAY:
                    //支付宝
                    AlPayParameter lPayParameter1 = (AlPayParameter) lPaymentRequest.getPayParameter();
                    String lPay = null;
                    if (TextUtils.isEmpty(lPayParameter1.getSignInfo())) {
                        lPay = new PayTask(mActivity).pay(alPayParameter(lPayParameter1), true);
                    } else {
                        lPay = new PayTask(mActivity).pay(lPayParameter1.getSignInfo(), true);
                    }
                    if (TextUtils.isEmpty(lPay)) {
                        lReponse.setErrorMessage(mActivity.getString(R.string.unknown_mistake));
                        lReponse.setPayStatus(PAY_UNKNOWN_MISTAKE);
                    } else {
                        lReponse.analysisAL(lPay);
                    }
                    break;
                case WX_PAY:
                    //微信
                    WxPayParameter lPayParameter = (WxPayParameter) lPaymentRequest.getPayParameter();
                    IWXAPI lWXAPI = WXAPIFactory.createWXAPI(mActivity, lPayParameter.getAppId(), true);
                    lWXAPI.registerApp(lPayParameter.getAppId());
                    if (lWXAPI.isWXAppInstalled()) {
                        PayReq req = new PayReq();
                        PaymentConst.WX_APP_ID = lPayParameter.getAppId();
                        req.appId = lPayParameter.getAppId();
                        req.partnerId = lPayParameter.getPartnerId();
                        req.prepayId = lPayParameter.getPrepayId();
                        req.packageValue = lPayParameter.getPackageValue();
                        req.nonceStr = lPayParameter.getNonceStr();
                        req.timeStamp = TextUtils.isEmpty(lPayParameter.getTimeStamp()) ?
                                String.valueOf(System.currentTimeMillis() / 1000) : lPayParameter.getTimeStamp();
                        //如果后台不返回则自行加密
                        if (TextUtils.isEmpty(lPayParameter.getPaySign())) {
                            MD5 md5 = new MD5();
                            md5.put("appid", req.appId);
                            md5.put("partnerid", req.partnerId);
                            md5.put("prepayid", req.prepayId);
                            md5.put("package", req.packageValue);
                            md5.put("noncestr", req.nonceStr);
                            md5.put("timestamp", req.timeStamp);
                            md5.put("key", lPayParameter.getApikey());
                            req.sign = md5.getMessageDigest(md5.toString().getBytes()).toUpperCase(Locale.CHINA);
                        } else {
                            req.sign = lPayParameter.getPaySign();
                        }
                        lWXAPI.sendReq(req);
                        sIWXAPIEventHandler = new PayIWXAPIEventHandler();
                        while (!sIWXAPIEventHandler.isRun) {
                            try {
                                Thread.sleep(600);
                            } catch (InterruptedException pE) {
                                pE.printStackTrace();
                            }
                        }
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
        mFragment = new PaymentingFragment();
        mActivity.getFragmentManager()
                .beginTransaction()
                .add(mFragment, TAG)
                .commit();
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
        mActivity.getFragmentManager().beginTransaction().remove(mFragment).commit();
        mActivity = null;
        mFragment = null;
        sIWXAPIEventHandler = null;
        PaymentConst.WX_APP_ID = null;
        cancel(true);
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
        boolean isRun = false;
        private BaseResp mBaseResp;

        @Override
        public void onReq(BaseReq pBaseReq) {

        }

        @Override
        public void onResp(BaseResp pBaseResp) {
            mBaseResp = pBaseResp;
            isRun = true;
        }

        public BaseResp getBaseResp() {
            return mBaseResp;
        }
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
            return builder.toString();
        }
    }

    /**
     * 用于编成支付宝 需要的参数
     *
     * @param pClass 外面传入的参数
     * @return 返回值
     * @throws IllegalAccessException 反射失败
     */
    private String alPayParameter(AlPayParameter pClass) {

        Field[] lDeclaredFields = pClass.getClass().getDeclaredFields();
        StringBuilder lStringBuilder = new StringBuilder();
        try {
            for (Field field :
                    lDeclaredFields) {
                field.setAccessible(true);
                if (!"signInfo".equals(field.getName())) {
                    lStringBuilder.append(field.getName())
                            .append("=")
                            .append((String) field.get(pClass))
                            .append("&");
                }
            }
        } catch (IllegalAccessException pE) {
            pE.printStackTrace();
        }
        //删除最后一个
        lStringBuilder.deleteCharAt(lStringBuilder.length() - 1);
        return lStringBuilder.toString();
    }
}

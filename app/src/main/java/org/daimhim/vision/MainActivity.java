package org.daimhim.vision;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import org.daimhim.onekeypayment.PaymentCallback;
import org.daimhim.onekeypayment.PaymentConst;
import org.daimhim.onekeypayment.ToPay;
import org.daimhim.onekeypayment.model.PaymentReponse;
import org.daimhim.onekeypayment.model.PaymentRequest;
import org.daimhim.onekeypayment.model.WxPayParameter;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, PaymentCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_pay).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        ToPay lToPay = new ToPay();
        WxPayParameter lPayParameter = new WxPayParameter();
        String json = "{\"appid\":\"wxb4ba3c02aa476ea1\",\"partnerid\":\"1900006771\",\"package\":\"Sign=WXPay\",\"noncestr\":\"42a782eab96d255f4a39a4201fa1c83d\",\"timestamp\":1527750648,\"prepayid\":\"wx3115104846006489528e9bf70766136291\",\"sign\":\"86F0C0A3EBE4488AD9A6F5476A97E45D\"}";
        lPayParameter.setAppId("wxb4ba3c02aa476ea1");
        lPayParameter.setPartnerId("1900006771");
        lPayParameter.setPackageValue("Sign=WXPay");
        lPayParameter.setNonceStr("42a782eab96d255f4a39a4201fa1c83d");
        lPayParameter.setTimeStamp("1527750648");
        lPayParameter.setPrepayId("wx3115104846006489528e9bf70766136291");
        lPayParameter.setPaySign("86F0C0A3EBE4488AD9A6F5476A97E45D");
        PaymentRequest lPaymentRequest = new PaymentRequest(lPayParameter);
        lPaymentRequest.setPayType(PaymentConst.WX_PAY);
        lToPay.toPayment(this, lPaymentRequest,this);
        Toast.makeText(this,"开始支付。。。",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentSuccess(PaymentReponse result) {
        Log.i("MainActivity:","onPaymentSuccess:"+result.toString());
    }

    @Override
    public void onPaymentFailure(String status) {
        Log.i("MainActivity","onPaymentFailure:"+status);
    }

}

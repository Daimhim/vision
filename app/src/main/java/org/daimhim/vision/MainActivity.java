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

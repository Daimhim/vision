package org.daimhim.vision;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alipay.sdk.app.H5PayActivity;
import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.util.H5PayResultModel;

import org.daimhim.weakhandler.WeakHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new WeakHandler();
        new H5PayCallback() {
            @Override
            public void onPayResult(H5PayResultModel pH5PayResultModel) {

            }
        };
        startActivity(new Intent(this, H5PayActivity.class));
    }
}

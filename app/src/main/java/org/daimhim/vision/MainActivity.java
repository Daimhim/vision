package org.daimhim.vision;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_error)
    TextView mTvError;
    @BindView(R.id.tv_test_path)
    TextView mTvTestPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }



    @OnClick({R.id.tv_error, R.id.tv_test_path})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_error:
                throw new NullPointerException("test");
            case R.id.tv_test_path:
                break;
                default:
                    break;
        }
    }
}

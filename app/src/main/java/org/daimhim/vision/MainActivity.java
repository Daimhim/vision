package org.daimhim.vision;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.daimhim.weakhandler.WeakHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new WeakHandler();
    }
}

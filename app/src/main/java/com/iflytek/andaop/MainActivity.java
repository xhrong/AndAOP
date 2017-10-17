package com.iflytek.andaop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import org.android10.gintonic.annotation.DebugTrace;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Demo().doN();
                Log.e("dddd","ddddd");
            }
        });
        test(10);
    }

    @DebugTrace
    private void test(int i){
        Log.e("dddd","ddddd"+i);
    }
}

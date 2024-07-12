package com.lf.step;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.lf.steputil.LFStepUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        TextView tvCount = findViewById(R.id.tv_count);

        boolean isSupport = LFStepUtil.isSupportStepsProvider();
        Log.d("111---", "isSupport---" + isSupport);


        findViewById(R.id.tv_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = LFStepUtil.getAllSteps(MainActivity.this, null);

                tvCount.setText(String.valueOf(count));
            }
        });

    }
}
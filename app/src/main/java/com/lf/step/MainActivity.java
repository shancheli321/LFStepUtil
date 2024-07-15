package com.lf.step;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.lf.steputil.LFXMStepUtil;
import com.lf.steputil.MyStepActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        TextView tvCount = findViewById(R.id.tv_count);

        boolean isSupport = LFXMStepUtil.isSupportStepsProvider();
        Log.d("111---", "isSupport---" + isSupport);


        findViewById(R.id.tv_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = LFXMStepUtil.getAllSteps(MainActivity.this, null);

                tvCount.setText(String.valueOf(count));
            }
        });

        findViewById(R.id.tv_hw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyStepActivity.class);
                startActivity(intent);
            }
        });

    }
}
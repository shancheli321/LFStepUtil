package com.lf.step;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.lf.steputil.LFHWStepUtil;
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

        findViewById(R.id.tv_web).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyWebActivity.class);
                startActivity(intent);
            }
        });

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
//                Intent intent = new Intent(MainActivity.this, MyStepActivity.class);
//                startActivity(intent);

                LFHWStepUtil.requestAuth(MainActivity.this);
            }
        });

        getoperation();
    }


    private void getoperation() {
        Log.d("TAG", "[权限]" + "ACTIVITY_RECOGNITION 未获得");
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            // 检查权限状态
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACTIVITY_RECOGNITION)) {
                //  用户彻底拒绝授予权限，一般会提示用户进入设置权限界面
                Log.d("TAG", "[权限]" + "ACTIVITY_RECOGNITION 以拒绝，需要进入设置权限界面打开");
            } else {
                //  用户未彻底拒绝授予权限
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION}, 1);
                Log.d("TAG", "[权限]" + "ACTIVITY_RECOGNITION 未彻底拒绝拒绝，请求用户同意");
            }
//                return;
        }else{

            Log.d("TAG", "[权限]" + "ACTIVITY_RECOGNITION ready");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 申请成功
                    Log.d("TAG", "[权限]" + "ACTIVITY_RECOGNITION 申请成功");
                } else {
                    // 申请失败
                    Log.d("TAG", "[权限]" + "ACTIVITY_RECOGNITION 申请失败");
                }
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LFHWStepUtil.onActivityResult(requestCode, data);
    }
}
package com.lf.steputil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.hihealth.ConsentsController;
import com.huawei.hms.hihealth.DataController;
import com.huawei.hms.hihealth.HiHealthOptions;
import com.huawei.hms.hihealth.HiHealthStatusCodes;
import com.huawei.hms.hihealth.HuaweiHiHealth;
import com.huawei.hms.hihealth.data.DataType;
import com.huawei.hms.hihealth.data.Field;
import com.huawei.hms.hihealth.data.SamplePoint;
import com.huawei.hms.hihealth.data.SampleSet;
import com.huawei.hms.hihealth.data.Scopes;
import com.huawei.hms.hihealth.result.HealthKitAuthResult;
import com.huawei.hms.support.api.entity.auth.Scope;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.hihealth.SettingController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class MyStepActivity extends AppCompatActivity {

    // SettingController对象
    private SettingController mSettingController;

    // 通过startActivityForResult方式拉起授权流程界面的请求码，具体数值开发者可以自行定义
    private static final int REQUEST_AUTH = 1002;
    private static final String TAG = "HealthKitAuthActivity";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_step);
        initUI();
        initService();
        // 步骤2授权流程，每次启动时调用一下
        requestAuth();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HiHealthOptions hiHealthOptions = HiHealthOptions.builder()
                .addDataType(DataType.DT_CONTINUOUS_STEPS_DELTA, HiHealthOptions.ACCESS_READ)
                .addDataType(DataType.DT_CONTINUOUS_STEPS_DELTA, HiHealthOptions.ACCESS_WRITE)
                .build();

        AuthHuaweiId signInHuaweiId = HuaweiIdAuthManager.getExtendedAuthResult(hiHealthOptions);
        DataController dataController = HuaweiHiHealth.getDataController(getApplicationContext(), signInHuaweiId);
        // 1. 使用指定的数据类型（步数增量： DT_CONTINUOUS_STEPS_DELTA），调用readTodaySummation接口查询该数据类型的当日统计值
        Task<SampleSet> todaySummationTask = dataController.readTodaySummation(DataType.DT_CONTINUOUS_STEPS_DELTA);
        // 2. 调用readTodaySummation接口查询当日的统计值是异步操作，需要设置成功或失败Listener，返回数据查询成功或失败
        // 注意：该接口查询的是某种数据类型的当日统计值，查询时间范围为当日00:00:00的时间戳至接口调用时当前系统时间戳
        // 该接口将开始时间或者结束时间落在查询时间范围内的所有数据点的数据值相加，并返回相加后的统计值
        todaySummationTask.addOnSuccessListener(new OnSuccessListener<SampleSet>() {
            @Override
            public void onSuccess(SampleSet sampleSet) {
                Log.d(TAG, "Success read today summation from HMS core");
                if (sampleSet != null) {
                    showSampleSet(sampleSet);
                }
            }
        });
        todaySummationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                String errorCode = e.getMessage();
                Log.d(TAG, errorCode + ": " + errorCode);

//                int code = Integer.valueOf(errorCode);
                String errorMsg = HiHealthStatusCodes.getStatusCodeMessage(50011);
                Log.d(TAG, errorCode + ": " + errorMsg);
            }
        });
    }

    /**
     * 将SampleSet对象中的SamplePoint打印输出
     *
     * @param sampleSet 样本数据集合
     */
    private void showSampleSet(SampleSet sampleSet) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (SamplePoint samplePoint : sampleSet.getSamplePoints()) {
            Log.d(TAG, "Sample point type: " + samplePoint.getDataType().getName());
            Log.d(TAG, "Start: " + dateFormat.format(new Date(samplePoint.getStartTime(TimeUnit.MILLISECONDS))));
            Log.d(TAG, "End: " + dateFormat.format(new Date(samplePoint.getEndTime(TimeUnit.MILLISECONDS))));
            for (Field field : samplePoint.getDataType().getFields()) {
                Log.d(TAG, "Field: " + field.getName() + " Value: " + samplePoint.getFieldValue(field));
                if (field.getName().equals("steps")){
                    mTextView.setText("今日的步数为:" + samplePoint.getFieldValue(field));
                }
            }
        }
    }

    private void initUI() {
        mTextView = findViewById(R.id.text);
    }

    /**
     * 初始化SettingController
     */
    private void initService() {
        mSettingController = HuaweiHiHealth.getSettingController(this);
    }

    /**
     * 声明需要申请的Scope，并获取Intent启动授权流程，此方法必须在Activity中使用
     */
    private void requestAuth() {
        // 添加需要申请的权限，这里只是举例说明，开发者需要根据实际情况添加所需的权限
        String[] scopes = new String[] {
                // 查看和存储HUAWEI Health Service Kit中的步数
                Scopes.HEALTHKIT_STEP_READ, Scopes.HEALTHKIT_STEP_WRITE
//                // 查看和存储HUAWEI Health Service Kit中的身高体重
//                Scopes.HEALTHKIT_HEIGHTWEIGHT_READ, Scopes.HEALTHKIT_HEIGHTWEIGHT_WRITE,
//                // 查看和存储HUAWEI Health Service Kit中的心率数据
//                Scopes.HEALTHKIT_HEARTRATE_READ, Scopes.HEALTHKIT_HEARTRATE_WRITE
        };

        // 获取授权流程Intent，true表示开启运动健康App授权流程，false表示不开启
        Intent intent = mSettingController.requestAuthorizationIntent(scopes, true);

        // 打开授权流程页面
        Log.i(TAG, "start authorization activity");
        startActivityForResult(intent, REQUEST_AUTH);
    }


    public void cancelAuthorization() {
        // 1. 获取ConsentsController实例对象
        // 请注意此处的this为Activity对象
        ConsentsController mConsentsController = HuaweiHiHealth.getConsentsController(this);
        // 2. 是否删除用户数据,true为删除用户数据，false为不删除用户数据
        boolean clearUserData = true;
        // 3. 取消应用全部授权，是否删除用户数据
        Task<Void> task = mConsentsController.cancelAuthorization(clearUserData);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "cancelAuthorization success");
                if(clearUserData){
                    Log.i(TAG, "clearUserData success");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "cancelAuthorization exception");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 只处理授权流程的响应
        if (requestCode == REQUEST_AUTH) {
            // 从intent中获取授权响应结果
            HealthKitAuthResult result = mSettingController.parseHealthKitAuthResultFromIntent(data);
            if (result == null) {
                Log.d(TAG, "authorization fail");
                return;
            }

            if (result.isSuccess()) {
                Log.d(TAG, "authorization success");
                // authorizedScopes为用户勾选授权给您应用的权限信息
                // 请注意如果此处的权限信息和步骤二中登录时设置的权限信息存在差异，即用户在授权的时候没有勾选全部的权限信息
                // 开发者可以重新调用步骤二中的登录授权接口，拉起华为帐号的登录授权
                // 为了提升用户体验，开发者需注意拉起华为帐号的登录授权的频率，避免频繁拉起授权界面
                if (result.getAuthAccount() != null && result.getAuthAccount().getAuthorizedScopes() != null) {
                    Set<Scope> authorizedScopes = result.getAuthAccount().getAuthorizedScopes();
                    Log.d(TAG, "authorization scope size " + authorizedScopes.size());
                }
            } else {
                Log.d(TAG, "authorization fail, errorCode:" + result.getErrorCode());
            }
        }
    }

}
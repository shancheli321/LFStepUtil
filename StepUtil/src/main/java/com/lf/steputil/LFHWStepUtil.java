package com.lf.steputil;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.hihealth.DataController;
import com.huawei.hms.hihealth.HiHealthOptions;
import com.huawei.hms.hihealth.HuaweiHiHealth;
import com.huawei.hms.hihealth.SettingController;
import com.huawei.hms.hihealth.data.DataType;
import com.huawei.hms.hihealth.data.SampleSet;
import com.huawei.hms.hihealth.data.Scopes;
import com.huawei.hms.hihealth.result.HealthKitAuthResult;
import com.huawei.hms.support.api.entity.auth.Scope;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;

import java.util.Set;

/**
 */
public class LFHWStepUtil {

    /*
        手机Android Version7.0~13（API level 24~33），或手机系统为HarmonyOS。
        手机装有HMS Core（APK）4.0.2.300及以上版本；如果需要读取运动健康App的数据，HMS Core（APK）须为5.0.4.300及以上版本。
        手机装有华为运动健康App版本号11.0.0.512及以上。

        111506425
        d696020aa2514e55d2a38df7b80653239e7331ada9015f441b3cbb0caebc71ad
     */

    // 通过startActivityForResult方式拉起授权流程界面的请求码，具体数值开发者可以自行定义
    private static final int REQUEST_AUTH = 1002;
    private static final String TAG = "HealthKitAuthActivity";

    // SettingController对象
    private static SettingController mSettingController;


    /**
     * 声明需要申请的Scope，并获取Intent启动授权流程，此方法必须在Activity中使用
     */
    public static void requestAuth(Activity context) {

        mSettingController = HuaweiHiHealth.getSettingController(context);

        // 添加需要申请的权限，这里只是举例说明，开发者需要根据实际情况添加所需的权限
        String[] scopes = new String[] {
                // 查看和存储HUAWEI Health Service Kit中的步数
                Scopes.HEALTHKIT_STEP_READ
//                , Scopes.HEALTHKIT_STEP_WRITE,
//                // 查看和存储HUAWEI Health Service Kit中的身高体重
//                Scopes.HEALTHKIT_HEIGHTWEIGHT_READ, Scopes.HEALTHKIT_HEIGHTWEIGHT_WRITE,
//                // 查看和存储HUAWEI Health Service Kit中的心率数据
//                Scopes.HEALTHKIT_HEARTRATE_READ, Scopes.HEALTHKIT_HEARTRATE_WRITE
        };

        // 获取授权流程Intent，true表示开启运动健康App授权流程，false表示不开启
        Intent intent = mSettingController.requestAuthorizationIntent(scopes, true);

        // 打开授权流程页面
        Log.i(TAG, "start authorization activity");
        context.startActivityForResult(intent, REQUEST_AUTH);
    }

    public static int getAllStep(Context context) {

        HiHealthOptions hiHealthOptions = HiHealthOptions.builder().addDataType(DataType.DT_CONTINUOUS_STEPS_DELTA, HiHealthOptions.ACCESS_READ).build();

        AuthHuaweiId authHuaweiId = HuaweiIdAuthManager.getExtendedAuthResult(hiHealthOptions);
        DataController dataController = HuaweiHiHealth.getDataController(context, authHuaweiId);

        // 1. 使用指定的数据类型（步数增量：DT_CONTINUOUS_STEPS_DELTA），调用readTodaySummation接口查询该数据类型的当日统计值
        Task<SampleSet> todaySummationTask = dataController.readTodaySummation(DataType.DT_CONTINUOUS_STEPS_DELTA);

        // 2. 调用readTodaySummation接口查询当日的统计值是异步操作，需要设置成功或失败Listener，返回数据查询成功或失败
        // 注意：该接口查询的是某种数据类型的当日统计值，查询时间范围为当日00:00:00的时间戳至接口调用时当前系统时间戳
        // 该接口将开始时间或者结束时间落在查询时间范围内的所有数据点的数据值相加，并返回相加后的统计值
        todaySummationTask.addOnSuccessListener(new OnSuccessListener<SampleSet>() {
            @Override
            public void onSuccess(SampleSet sampleSet) {

                if (sampleSet != null) {

                }
            }
        });
        todaySummationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.i("DataController","Failed to read today summation from HMS core" + e.getMessage());
            }
        });
        return 0;
    }


    public static void onActivityResult(int requestCode, @Nullable Intent data) {

        // 只处理授权流程的响应
        if (requestCode == REQUEST_AUTH) {
            // 从intent中获取授权响应结果
            HealthKitAuthResult result = mSettingController.parseHealthKitAuthResultFromIntent(data);
            if (result == null) {
                Log.w(TAG, "authorization fail");
                return;
            }

            if (result.isSuccess()) {
                Log.i(TAG, "authorization success");
                // authorizedScopes为用户勾选授权给您应用的权限信息
                // 请注意如果此处的权限信息和步骤二中登录时设置的权限信息存在差异，即用户在授权的时候没有勾选全部的权限信息
                // 开发者可以重新调用步骤二中的登录授权接口，拉起华为帐号的登录授权
                // 为了提升用户体验，开发者需注意拉起华为帐号的登录授权的频率，避免频繁拉起授权界面
                if (result.getAuthAccount() != null && result.getAuthAccount().getAuthorizedScopes() != null) {
                    Set<Scope> authorizedScopes = result.getAuthAccount().getAuthorizedScopes();
                    Log.i(TAG, "authorization scope size " + authorizedScopes.size());
                }
            } else {
                Log.w(TAG, "authorization fail, errorCode:" + result.getErrorCode());
            }
        }
    }
}

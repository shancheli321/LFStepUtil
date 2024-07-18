package com.lf.steputil;

import android.content.Context;
import android.util.Log;

import com.vivo.healthservice.kit.CallResult;
import com.vivo.healthservice.kit.HealthKitConstants;
import com.vivo.healthservice.kit.HealthKitManager;
import com.vivo.healthservice.kit.OnCallback;
import com.vivo.healthservice.kit.bean.data.DataType;
import com.vivo.healthservice.kit.bean.data.Record;
import com.vivo.healthservice.kit.bean.data.Value;
import com.vivo.healthservice.kit.bean.dbOperation.Query;
import com.vivo.healthservice.kit.bean.dbOperation.QuerySortOrder;
import com.vivo.healthservice.kit.controller.PermissionController;
import com.vivo.healthservice.kit.bean.Permission;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


/**
 * @date: 2024/7/16
 */
public class LFVOStepUtil {

    /*
        <uses-permission android:name="com.vivo.healthservice.permission.DATA_ACCESS" />

        -dontwarn com.vivo.healthservice.kit.bean.**
        -keep class com.vivo.healthservice.kit.** { *;}
        -keep class com.vivo.healthservice.kit.cloud.AuthResult { *;}
        -keep class com.vivo.healthservice.kit.CallResult { *;}


     */

    private static final String TAG = "LFVOStepUtil--step--";

    private static String mCollectorName = "HOUR"; // 采集器名称

    /**
     * 查询数据权限
     */
    public static void checkPermission(Context context) {

        HealthKitManager manager = HealthKitManager.getInstance(context);

        manager.getPermissionController().getDataAuth(new OnCallback<List<Permission>>() {
            @Override
            public void onCallResult(CallResult<List<Permission>> callResult) {
                if (callResult.isSuccess()) {
                    List<Permission> permissionInfoList = callResult.getData();
                    //获取当前应用所拥有的权限


                } else {
                    if (callResult.getCode() == HealthKitConstants.ServerCode.ERROR_PERMISSION_ILLEGAL) {
                        //数据类型不存在，没有开发者权限
                        getPermisssion(context);

                    } else if (callResult.getCode() == HealthKitConstants.ServerCode.ERROR_PERMISSION_NOT_MATCH) {
                        //所申请权限与开发者权限不匹配
                    }
                }
            }
        });
    }

    public static void getPermisssion(Context context) {
        List<Permission> permissions = new ArrayList<>();

        // 步数
        Permission stepPermission = new Permission();
        stepPermission.setDataTypeName(DataType.STEPS_DELTA.getName());
        stepPermission.setUserPermission(HealthKitConstants.UserPermission.READ_WRITE);
        permissions.add(stepPermission);

        HealthKitManager manager = HealthKitManager.getInstance(context);

        manager.getPermissionController().requestDataAuth(permissions, new OnCallback<List<Permission>>() {
            @Override
            public void onCallResult(CallResult<List<Permission>> callResult) {
                if (callResult.isSuccess()) {
                    //请求授权成功，vivo health kit会弹框让用户授权

                } else {
                    if (callResult.getCode() == HealthKitConstants.ServerCode.ERROR_PERMISSION_ILLEGAL) {
                        //数据类型不存在，没有开发者权限

                    } else if (callResult.getCode() == HealthKitConstants.ServerCode.ERROR_PERMISSION_NOT_MATCH) {
                        //所申请权限与开发者权限不匹配

                    }
                }
            }
        });
    }


    public static void getStepCount(Context context) {
        long startStepTime = getStartTime();
        long endStepTime = System.currentTimeMillis();

        // 还可以基于设备查询
        Query query_step = new Query.Builder(DataType.STEPS_DELTA)
                .setStartTime(startStepTime)
                .setEndTime(endStepTime)
                .setCollectorName(mCollectorName)
                .build();

        // 添加查询排序信息
        QuerySortOrder sortOrder_step = new QuerySortOrder();
        sortOrder_step.orderType = QuerySortOrder.START_TIME;
        sortOrder_step.ascending = true;//按照开始时间降序排序
        query_step.setSortOrder(sortOrder_step);

        HealthKitManager mManager = HealthKitManager.getInstance(context);

        mManager.getRecordController().query(query_step, new OnCallback<List<Record>>() {
            @Override
            public void onCallResult(CallResult<List<Record>> callResult) {
                Log.d(TAG, "get stepRecord callResult:" + callResult);
                if (callResult.isSuccess()) {
                    Log.d(TAG, "get step recordList:" + callResult.getData());

                    // 获取前getStepCount条数据
                    List<Record> records = callResult.getData();
                    if (records.isEmpty()) {
                        Log.d(TAG, "步数为空--");
                    } else {
                        for (Record r : records) {
                            Map<String, Value> values = r.getValues();
                            for (String key : values.keySet()) {

                            }
                        }
                    }
                } else {
                    Log.d(TAG, "获取步数失败--");
                }
            }
        });
    }


    private static long getStartTime() {
        // 获取一个Calendar实例，并默认使用系统时区和语言环境
        Calendar calendar = Calendar.getInstance();

        // 将时间设置为当前时间
        // 注意：这一步实际上在大多数情况下是多余的，因为getInstance()已经初始化为当前时间了
        // 但为了清晰起见，我还是保留了它
        calendar.setTimeInMillis(System.currentTimeMillis());

        // 设置小时为0
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        // 设置分钟为0
        calendar.set(Calendar.MINUTE, 1);

        // 设置秒为0
        calendar.set(Calendar.SECOND, 0);

        // 设置毫秒为0（可选）
        calendar.set(Calendar.MILLISECOND, 0);

        // 返回午夜12点（即当天的0点）的时间戳（毫秒）
        return calendar.getTimeInMillis();
    }


    private static long getEndTime() {
        // 获取一个Calendar实例，并默认使用系统时区和语言环境
        Calendar calendar = Calendar.getInstance();

        // 将时间设置为当前时间
        // 注意：这一步实际上在大多数情况下是多余的，因为getInstance()已经初始化为当前时间了
        // 但为了清晰起见，我还是保留了它
        calendar.setTimeInMillis(System.currentTimeMillis());

        // 设置小时为0
        calendar.set(Calendar.HOUR_OF_DAY, 23);

        // 设置分钟为0
        calendar.set(Calendar.MINUTE, 59);

        // 设置秒为0
        calendar.set(Calendar.SECOND, 59);

        // 设置毫秒为0（可选）
        calendar.set(Calendar.MILLISECOND, 0);

        // 返回午夜12点（即当天的0点）的时间戳（毫秒）
        return calendar.getTimeInMillis();
    }

}

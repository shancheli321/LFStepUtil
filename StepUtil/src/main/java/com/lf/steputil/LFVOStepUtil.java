package com.lf.steputil;

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


    /**
     * 查询数据权限
     */
    public static void checkPermission() {
        HealthKitManager manager = HealthKitManager.getInstance(getApplicationContext());
        PermissionController permissionCtrl = manager.getPermissionController();

        HealthKitManager manager = HealthKitManager.getInstance(getApplicationContext());
        manager.getPermissionController().getDataAuth(new OnCallback<List<Permission>>() {
            @Override
            public void onCallResult(CallResult<List<Permission>> callResult) {
                if (callResult.isSuccess()) {
                    List<Permission> permissionInfoList = callResult.getData();
                    //获取当前应用所拥有的权限
                } else {
                    if (callResult.getCode() == HealthKitConstants.ServerCode.ERROR_PERMISSION_ILLEGAL) {
                        //数据类型不存在，没有开发者权限
                    } else if (callResult.getCode() == HealthKitConstants.ServerCode.ERROR_PERMISSION_NOT_MATCH) {
                        //所申请权限与开发者权限不匹配
                    }
                }
            });
        }
    }

    public static void getPermisssion() {
        List<Permission> permissions = new ArrayList<>();
        Permission itemPermission = new Permission();
        itemPermission.setDataTypeName(DataType.STEPS_DELTA.getName());
        itemPermission.setUserPermission(HealthKitConstants.UserPermission.READ_WRITE);
        permissions.add(itemPermission);

        HealthKitManager manager = HealthKitManager.getInstance(getApplicationContext());
        manager.getPermissionController().requestDataAuth(permissions, new OnCallback<List<Permission>>() {
            @Override
            public void onCallResult(CallResult<List<Permission>> callResult) {
                if (callResult.isSuccess()) {
                    //请求授权成功，vivo health kit会弹框让用户授权
                } else if (callResult.getCode() == HealthKitConstants.ServerCode.REQUEST_AUTH_SUCCESS) {
                    //用户授权成功
                } else {
                    if (callResult.getCode() == HealthKitConstants.ServerCode.ERROR_PERMISSION_ILLEGAL) {
                        //数据类型不存在，没有开发者权限
                        VLog.d(TAG, "requestDataAuth fail,Some data types do not exist:" + callResult.getData());
                    } else if (callResult.getCode() == HealthKitConstants.ServerCode.ERROR_PERMISSION_NOT_MATCH) {
                        //所申请权限与开发者权限不匹配
                    }
                }
            });
    }


}

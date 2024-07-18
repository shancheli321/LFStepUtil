package com.lf.steputil;

import android.content.Context;

import com.hihonor.mcs.fitness.health.HealthKit;
import com.hihonor.mcs.fitness.health.constants.DataType;
import com.hihonor.mcs.fitness.health.data.HonorSignInAccount;
import com.hihonor.mcs.fitness.health.data.SampleData;
import com.hihonor.mcs.fitness.health.datastore.QueryRequest;
import com.hihonor.mcs.fitness.health.datastore.QueryResponse;
import com.hihonor.mcs.fitness.health.datastruct.StepField;
import com.hihonor.mcs.fitness.health.exception.HealthKitException;
import com.hihonor.mcs.fitness.health.task.OnFailureListener;
import com.hihonor.mcs.fitness.health.task.OnSuccessListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @date: 2024/7/16
 */
public class LFRYStepUtil {


    public static int getAllStep(Context context) {

        // 构建步数数据对象，设置查询的开始时间，结束时间，查询结果的排列方式，查询结果的条目限制和分页起始目录。
        long startTime = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1);
        long endTime = System.currentTimeMillis();
        QueryRequest queryRequest = new QueryRequest(DataType.SAMPLE_STEPS, startTime, endTime);
        // 查询结果升序排列。
        queryRequest.setOrderBy(QueryRequest.ORDER_ASCENDING);
        queryRequest.setPageSize(20);
        queryRequest.setPageToken(0 + "");

        // appId：开发者服务平台创建的应用 id
        // openId：从用户登录荣耀账号授权后返回的 SignInAccountInfo 对象中获取；
        // accessToken：通过 SignInAccountInfo 对象中获取的 Authorization Code 获取，参考“获取用户授权”章节的授权码模式登陆

        String appId = "104416179";
        String openId = "";
        String accessToken = "";
        HonorSignInAccount honorSignInAccount = new HonorSignInAccount(appId, openId, accessToken);

        // 调用querySampleData接口查询Health Kit中的步数数据。
        HealthKit.getDataStoreClient(context).querySampleData(honorSignInAccount, queryRequest)
                .addOnSuccessListener(new OnSuccessListener<QueryResponse<SampleData>>() {
                    @Override
                    public void onSuccess(QueryResponse<SampleData> sampleDataQueryResponse) {
                        // 查询结果为开始时间到结束时间内所有的步数采样数据，数据结构参考数据类型对应章节。
                        List<SampleData> sampleDataList = sampleDataQueryResponse.getDataList();
                        if (sampleDataList != null && !sampleDataList.isEmpty()) {
                            for (SampleData sampleData : sampleDataList) {
                                long sTime = sampleData.getStartTime();
                                long eTime = sampleData.getEndTime();
                                int step = sampleData.getInteger(StepField.FIELD_STEP_NAME);
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // 查询失败相应的处理逻辑。
                        int errorCode = ((HealthKitException) e).getErrorCode();
                        String errorMsg = e.getMessage();
                    }
                });

        return 0;
    }
}

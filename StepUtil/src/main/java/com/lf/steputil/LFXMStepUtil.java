package com.lf.steputil;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.lf.steputil.mi.XMStepConstants;
import com.lf.steputil.mi.XMStepEntity;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

/**
 * @date: 2024/7/12
 */
public class LFXMStepUtil {

    /*
        计步器Sensor 5分钟上报一次数据，后台Service将计步数据记录插入本地数据库，若没有数据上报, 则不插入；
        app第一次请求数据会立即响应, 后台Service会立即返回给app最新的记录数据；
        app在1分钟内多次请求数据, 则只有第一次得到的数据是最新的, 后面的请求结果和第一次相同；
        一条记录只有一种计步模式.。例如, 用户在10分钟内有600步数据, 400步走路, 200步跑步, 则这10分钟会分拆成两条记录, 400步走路和200步跑步；
        只传给应用层3种计步模式: 0: 不支持(在不支持计步的手机上不会得到数据), 2: 走路, 3: 跑步。
     */


    /**
     * 在功能开始之前判断是否支持stepsProvider功能
     * @return
     */
    public static boolean isSupportStepsProvider() {
        boolean isSupport= getBoolean("support_steps_provider",false);

        return isSupport;
    }

    /**
     *
     * selection	false	String	从可选列中选取自定义条件	获取满足条件的记录行, 若为null则获取所有行
     * selectionArgs	false	String[]	一般为可选列的值	Selection中带?的格式化参数
     * @param args
     * @return
     */
    public static int getAllSteps(Context context, String[] args) {
        ContentResolver contentResolver = context.getContentResolver();
        List<XMStepEntity> steps = new LinkedList<>();
        int count = 0;

        try {
            Cursor cursor = contentResolver.query(XMStepConstants.CONTENT_URI, XMStepConstants.projection, null, args,
                    XMStepConstants.DEFAULT_SORT_ORDER);

            if (cursor.moveToFirst()) {
                do {
                    XMStepEntity s = new XMStepEntity(cursor.getInt(0), cursor.getLong(1), cursor.getLong(2),
                            cursor.getInt(3),
                            cursor.getInt(4));
                    steps.add(s);
                } while (cursor.moveToNext());
            }

            for (XMStepEntity tempStepEntity : steps) {
                Log.d("111---", tempStepEntity.toString());

                if (isToday(tempStepEntity.getmBeginTime()) && isToday(tempStepEntity.getmEndTime())) {
                    if (tempStepEntity.getmMode() == 2 || tempStepEntity.getmMode() == 3) {
                        count += tempStepEntity.getmSteps();
                    }
                }

            }
        } catch (Exception e) {
            Log.d("111---", e.toString());
        }

        return count;
    }

    private static boolean getBoolean(String name, boolean defaultValue) {
        try {
            Class featureParserClass = Class.forName("miui.util.FeatureParser");
            Method method = featureParserClass.getMethod("getBoolean", String.class, boolean.class);
            return (Boolean) method.invoke(null, name, defaultValue);
        } catch (Exception e) {

        }
        return defaultValue;
    }


    public static boolean isToday(long time) {

        // 时间戳（秒）
        long timestamp = time / 1000; // 当前时间戳，为了示例我们直接取当前时间

        // 获取系统默认时区的Calendar实例
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTimeInMillis(timestamp * 1000); // 注意：Calendar需要毫秒

        Log.d("111---", "time---" + time + "---" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "-" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));

        // 获取今天的Calendar实例
        Calendar today = Calendar.getInstance(TimeZone.getDefault());
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        // 比较两个Calendar的日期部分是否相等
        boolean isToday = calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);

        return isToday;
    }


}

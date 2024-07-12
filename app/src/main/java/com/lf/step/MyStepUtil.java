package com.lf.step;

/**
 * @date: 2024/7/10
 */
public class MyStepUtil {

    /*
        1.小米：接入小米计步器接口，小米通过ContentProvider读取步数信息，相对比较简单，
        官方文档 https://dev.mi.com/console/doc/detail?pId=2487

        2.华为：需要申请Health Kit，并导入sdk，相对麻烦一些，
        官方文档：https://developer.huawei.com/consumer/cn/doc/development/HMSCore-Guides/steps-delta-scene-0000001050822049
        HMS Core   https://developer.huawei.com/consumer/cn/doc/HMSCore-References/autorecordercontroller-0000001050091345

     */


    /*
        目前android计步有两种方式

        系统计步芯片
            在Android4.4版本之后，部分机型实现了Sensor.TYPE_STEP_COUNTER传感器，用于纪录用户行走的步数。从手机开机开始纪录，手机关机时重置为0。
            这个记步芯片是系统级别的，相对之前老版本的传感器记步，性能有一些优化：
            不会因为App单独用了记步的功能而额外耗电
            系统芯片记步是持续的，能够优化部分机型后台不记步的问题。

        加速度传感器计算方式
            加速度传感器非常耗电，导致App的耗电量很高，影响用户体验。
            需要后台实时运行才能实现记步的功能，如果App进程被系统或者安全软件杀死，导致记步功能没办法使用

            项目地址：https://github.com/jiahongfei/TodayStepCounter

        根据以上两种方式实现计步，手机提供计步传感器就使用Sensor.TYPE_STEP_COUNTER方式（app后台关闭也可以计步），如果不提供就使用SensorManager.SENSOR_DELAY_UI方式（app需要保持后台运行）。
     */
}

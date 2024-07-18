# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


# 华为运动
-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.huawei.hianalytics.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}
#end

#荣耀运动
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keep class com.hihonor.mcs.fitness.health.HealthKit* { *; }
-keep class com.hihonor.mcs.fitness.health.exception.** { *; }
-keep class com.hihonor.mcs.fitness.health.constants.** { *; }
-keep class com.hihonor.mcs.fitness.health.data.** { *; }
-keep class com.hihonor.mcs.fitness.health.realtimedata.** { *; }
-keep class com.hihonor.mcs.fitness.health.datastore.** { *; }
-keep class com.hihonor.mcs.fitness.health.datastruct.** { *; }
-keep class com.hihonor.mcs.fitness.health.goals.** { *; }
-keep class com.hihonor.mcs.fitness.health.task.** { *; }
#end


#vivo
-dontwarn com.vivo.healthservice.kit.bean.**
-keep class com.vivo.healthservice.kit.** { *;}
-keep class com.vivo.healthservice.kit.cloud.AuthResult { *;}
-keep class com.vivo.healthservice.kit.CallResult { *;}
#end

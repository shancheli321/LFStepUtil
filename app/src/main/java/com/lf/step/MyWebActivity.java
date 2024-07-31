package com.lf.step;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;

import java.security.Permissions;
import java.util.List;

public class MyWebActivity extends AppCompatActivity {

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web);

        mContext = this;

        WebView myWebView = findViewById(R.id.webview);
        myWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 对于 Android 5.0+ 启用摄像头和麦克风权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
                myWebView.setWebChromeClient(new WebChromeClient() {
                    // 根据需要实现 onPermissionRequest 方法


                    @Override
                    public void onPermissionRequest(PermissionRequest request) {

                        request.grant(request.getResources());
                        // 检查请求的权限类型，并决定是否授予权限
//                        for (String permission : request.getResources()) {
//                            if (PermissionRequest.RESOURCE_AUDIO_CAPTURE.equals(permission) ||
//                                    PermissionRequest.RESOURCE_VIDEO_CAPTURE.equals(permission)
//                            ) {
//                                // 授予麦克风权限
//                                request.grant(new String[]{permission});
//                            }
//                        }
                    }

                });
            }
        }
//        myWebView.loadUrl("https://yourwebsite.com");

//        webView.loadUrl("file:///android_asset/index.html");

        String[] permissions;
        permissions = new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA", "android.permission.RECORD_AUDIO"};

        XXPermissions.with(mContext)
                .permission(permissions)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                        if (!allGranted) {
                            Toast.makeText(mContext, "没有权限，无法使用该功能", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        myWebView.loadUrl("file:///android_asset/video.html");

                    }

                    @Override
                    public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                        if (doNotAskAgain) {
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(mContext, permissions);
                        } else {

                        }
                    }
                });



    }
}
package com.wildma.mqttandroidclient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.wildma.mqttandroidclient.utils.DialogHelper;
import com.wildma.mqttandroidclient.utils.PermissionConstants;
import com.wildma.mqttandroidclient.utils.PermissionUtils;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
    }

    public void publish(View view) {
        //模拟闸机设备发送消息过来
        MyMqttService.publish("tourist enter " + new Date().toString());
    }

    /**
     * 申请权限
     */
    public void requestPermission() {
        PermissionUtils.permission(PermissionConstants.PHONE)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(final ShouldRequest shouldRequest) {
                        Log.d(TAG, "onDenied: 权限被拒绝后弹框提示");
                        DialogHelper.showRationaleDialog(shouldRequest, MainActivity.this);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        mIntent = new Intent(MainActivity.this, MyMqttService.class);
                        //开启服务
                        startService(mIntent);
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever,
                                         List<String> permissionsDenied) {
                        Log.d(TAG, "onDenied: 权限被拒绝");
                        if (!permissionsDeniedForever.isEmpty()) {
                            DialogHelper.showOpenAppSettingDialog(MainActivity.this);
                        }
                    }
                })
                .request();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止服务
        stopService(mIntent);
    }
}

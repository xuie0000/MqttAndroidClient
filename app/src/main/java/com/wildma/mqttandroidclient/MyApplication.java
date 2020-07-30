package com.wildma.mqttandroidclient;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.wildma.mqttandroidclient.utils.PreferencesUtil;

public class MyApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        PreferencesUtil.init(this);
    }

    public static Context getContext() {
        return mContext;
    }
}

package com.wildma.mqttandroidclient;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.wildma.mqttandroidclient.utils.PreferencesUtil;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.UUID;

/**
 * Author       wildma
 * Github       https://github.com/wildma
 * CreateDate   2018/11/08
 * Desc	        ${MQTT服务}
 */
public class MyMqttService extends Service {
    private static final String TAG = "MyMqttService";

    @SuppressLint("StaticFieldLeak")
    private static MqttAndroidClient mqttAndroidClient;
    /**
     * 服务器地址（协议+地址+端口号）TODO 填写测试IP
     */
    public static final String IP = "xx.xx.xx.xx";
    public static final String HOST = "tcp://" + IP + ":1883";
    public static final String USERNAME = "admin";
    public static final String PASSWORD = "public";

    private MqttConnectOptions mMqttConnectOptions;
    /**
     * 发布主题
     */
    public static String PUBLISH_TOPIC = "tourist_enter";
    /**
     * 响应主题
     */
    public static String RESPONSE_TOPIC = "message_arrived";
    /**
     * 客户端ID https://developer.android.com/training/articles/user-data-ids
     */
    public static final String CLIENT_ID = PreferencesUtil.getString("uuid", UUID.randomUUID().toString());

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 发布 （模拟其他客户端发布消息）
     *
     * @param message 消息
     */
    public static void publish(String message) {
        String topic = PUBLISH_TOPIC;
        int qos = 2;
        try {
            //参数分别为：主题、消息的字节数组、服务质量、是否在服务器保留断开连接后的最后一条消息
            mqttAndroidClient.publish(topic, message.getBytes(), qos, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 响应 （收到其他客户端的消息后，响应给对方告知消息已到达或者消息有问题等）
     *
     * @param message 消息
     */
    public void response(String message) {
        String topic = RESPONSE_TOPIC;
        int qos = 2;
        try {
            //参数分别为：主题、消息的字节数组、服务质量、是否在服务器保留断开连接后的最后一条消息
            mqttAndroidClient.publish(topic, message.getBytes(), (int) qos, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化
     */
    private void init() {
        Log.d(TAG, "host:" + HOST + ", client_id:" + CLIENT_ID);
        mqttAndroidClient = new MqttAndroidClient(this, HOST, CLIENT_ID);
        //设置监听订阅消息的回调
        mqttAndroidClient.setCallback(mqttCallback);
        mMqttConnectOptions = new MqttConnectOptions();
        //设置是否清除缓存
        mMqttConnectOptions.setCleanSession(true);
        //设置超时时间，单位：秒
        mMqttConnectOptions.setConnectionTimeout(10);
        //设置心跳包发送间隔，单位：秒
        mMqttConnectOptions.setKeepAliveInterval(20);
        //设置用户名
        mMqttConnectOptions.setUserName(USERNAME);
        //设置密码
        mMqttConnectOptions.setPassword(PASSWORD.toCharArray());

        // last will message
        boolean doConnect = true;
        String message = "{\"terminal_uid\":\"" + CLIENT_ID + "\"}";
        String topic = PUBLISH_TOPIC;
        int qos = 2;
        // 最后的遗嘱
        try {
            mMqttConnectOptions.setWill(topic, message.getBytes(), qos, false);
        } catch (Exception e) {
            Log.e(TAG, "Exception Occured", e);
            doConnect = false;
            iMqttActionListener.onFailure(null, e);
        }
        if (doConnect) {
            doClientConnection();
        }
    }

    /**
     * 连接MQTT服务器
     */
    private void doClientConnection() {
        if (!mqttAndroidClient.isConnected() && isConnectIsNomarl()) {
            try {
                mqttAndroidClient.connect(mMqttConnectOptions, null, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断网络是否连接
     */
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "当前网络名称：" + name);
            return true;
        } else {
            Log.i(TAG, "没有可用网络");
            /*没有可用网络的时候，延迟3秒再尝试重连*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doClientConnection();
                }
            }, 3000);
            return false;
        }
    }

    /**
     * MQTT是否连接成功的监听
     */
    private final IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "连接成功 ");
            try {
                mqttAndroidClient.subscribe(PUBLISH_TOPIC, 2);//订阅主题，参数：主题、服务质量
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            Log.i(TAG, "连接失败 ");
            doClientConnection();//连接失败，重连（可关闭服务器进行模拟）
        }
    };

    /**
     * 订阅主题的回调
     */
    private final MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, MqttMessage message) {
            Log.i(TAG, "收到消息： " + new String(message.getPayload()));
            //收到消息，这里弹出Toast表示。如果需要更新UI，可以使用广播或者EventBus进行发送
            Toast.makeText(getApplicationContext(), "messageArrived: " + new String(message.getPayload()), Toast.LENGTH_LONG).show();
            //收到其他客户端的消息后，响应给对方告知消息已到达或者消息有问题等
            response("message arrived");
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        public void connectionLost(Throwable arg0) {
            Log.i(TAG, "连接断开 ");
            doClientConnection();//连接断开，重连
        }
    };

    @Override
    public void onDestroy() {
        try {
            //断开连接
            mqttAndroidClient.disconnect();
            mqttAndroidClient = null;
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}

package com.wildma.mqttandroidclient.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesUtil {
    public final static String NAME = "PreferencesUtil";

    private static SharedPreferences sharedPreferences;

    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    private static Editor getEditor() {
        return sharedPreferences.edit();
    }

    @Deprecated
    public static boolean contains(Context context, String key) {
        return sharedPreferences.contains(key);
    }

    public static boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * 获取配置文件
     *
     * @param ctx
     * @param param
     * @return
     */
    @Deprecated
    public static String get(Context ctx, String param) {
        SharedPreferences settings = ctx.getSharedPreferences(NAME, 0);
        String result = settings.getString(param, "");
        return result;
    }

    public static String get(String param) {
        String result = null;
        try {
            result = sharedPreferences.getString(param, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 删除配置文件
     *
     * @param ctx
     * @param param
     */
    @Deprecated
    public static void removePreference(Context ctx, String param) {
        SharedPreferences settings = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        Editor editor = settings.edit();
        editor.remove(param);
        editor.commit();
    }

    /**
     * 删除配置文件
     *
     * @param param
     */
    public static void removePreference(String param) {
        Editor editor = getEditor();
        editor.remove(param);
        editor.commit();
    }

    @Deprecated
    public static void putInt(Context context, String key, int value) {
        Editor editor = context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putInt(String key, int value) {
        Editor editor = getEditor();
        editor.putInt(key, value);
        editor.commit();
    }

    @Deprecated
    public static void putLong(Context context, String key, long value) {
        Editor editor = context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void putLong(String key, long value) {
        Editor editor = getEditor();
        editor.putLong(key, value);
        editor.commit();
    }

    @Deprecated
    public static void putBoolean(Context context, String key, boolean value) {
        Editor editor = context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void putBoolean(String key, boolean value) {
        Editor editor = getEditor();
        editor.putBoolean(key, value);
        // 修改为commit，反馈说50%几率发生点击了switch后显示为未选中，退出界面再进入看到还是选中状态
        editor.commit();
    }

    @Deprecated
    public static void putString(Context context, String key, String value) {
        Editor editor = context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void putString(String key, String value) {
        Editor editor = getEditor();
        editor.putString(key, value);
        editor.commit();
    }

    public static void putFloat(String key, float value) {
        Editor editor = getEditor();
        editor.putFloat(key, value);
        editor.commit();
    }

    @Deprecated
    public static int getInt(Context context, String key, int defValue) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            int value = sharedPreferences.getInt(key, defValue);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public static int getInt(String key, int defValue) {
        try {
            int value = sharedPreferences.getInt(key, defValue);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    @Deprecated
    public static long getLong(Context context, String key, long defValue) {

        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            long value = sharedPreferences.getLong(key, defValue);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;
    }

    public static long getLong(String key, long defValue) {

        try {
            long value = sharedPreferences.getLong(key, defValue);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defValue;
    }

    @Deprecated
    public static boolean getBoolean(Context context, String key, boolean defValue) {

        try {
            SharedPreferences spsharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            boolean value = spsharedPreferences.getBoolean(key, defValue);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defValue;
    }

    public static boolean getBoolean(String key, boolean defValue) {

        try {
            boolean value = sharedPreferences.getBoolean(key, defValue);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return defValue;
    }

    @Deprecated
    public static String getString(Context context, String key, String defValue) {

        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
            String value = sharedPreferences.getString(key, defValue);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;

    }

    public static String getString(String key, String defValue) {

        try {
            String value = sharedPreferences.getString(key, defValue);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;

    }

    public static float getFloat(String key, float defValue) {

        try {
            float value = sharedPreferences.getFloat(key, defValue);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defValue;

    }

}

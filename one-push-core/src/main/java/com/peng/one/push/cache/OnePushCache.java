package com.peng.one.push.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.peng.one.push.OnePush;

import java.util.Locale;

/**
 * 保存消息推送注册的时候，返回的唯一标示
 * Created by pyt on 2017/5/16.
 */

public class OnePushCache {

    private static final String FILE_ONE_PUSH_CACHE = "one_push_cache";
    private static final String KEY_TOKEN = "token_%d";
    private static final String KEY_PLATFORM = "platform";

    public static void putToken(Context context, String token) {
        getSharedPreferences(context).edit().putString(String.format(Locale.ENGLISH,KEY_TOKEN, OnePush.getPushPlatFormCode()), token).apply();
    }

    public static String getToken(Context context) {
        return getSharedPreferences(context).getString(String.format(Locale.ENGLISH,KEY_TOKEN, OnePush.getPushPlatFormCode()), null);
    }

    public static void delToken(Context context) {
        getSharedPreferences(context).edit().remove(String.format(Locale.ENGLISH,KEY_TOKEN, OnePush.getPushPlatFormCode())).apply();
    }

    public static void putPlatform(Context context, String platform) {
        getSharedPreferences(context).edit().putString(KEY_PLATFORM, platform).apply();
    }

    public static String getPlatform(Context context) {
        return getSharedPreferences(context).getString(KEY_PLATFORM, null);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(FILE_ONE_PUSH_CACHE, Context.MODE_PRIVATE);
    }

    public static void delPlatform(Context context) {
        getSharedPreferences(context).edit().remove(KEY_PLATFORM).apply();
    }
}

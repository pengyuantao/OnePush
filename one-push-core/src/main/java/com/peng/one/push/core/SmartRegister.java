package com.peng.one.push.core;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

import com.peng.one.push.OnePush;
import com.peng.one.push.OneRepeater;
import com.peng.one.push.log.OneLog;
import com.peng.one.push.utils.MetaDataUtils;
import com.peng.one.push.utils.PlatformCodeUtils;
import com.peng.one.push.utils.RomUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by pengyt on 2018/2/4.
 */
public class SmartRegister {

    public static final String PLATFORM_HUAWEI_HMS = "com.peng.one.push.huawei.hms.HMSPushClient";
    public static final String PLATFORM_XIAOMI = "com.peng.one.push.xiaomi.XiaomiPushClient";
    public static final String PLATFORM_MEIZU = "com.peng.one.push.meizu.MeizuPushClient";

    private static class Single {
        static SmartRegister sInstance = new SmartRegister();
    }

    public static SmartRegister getInstance() {
        return Single.sInstance;
    }

    private IHookTransmitListener backupHookTransmitListener;


    /**
     * 初始化
     *
     * @param application        application
     * @param targetPlatformCode 目标注册平台(默认0)
     * @param backupPlatformCode 备用推送通道
     * @param registerErrorTimes 注册失败多少次，启用备用通道
     */
    public void init(Application application, int targetPlatformCode, int backupPlatformCode, int registerErrorTimes) {
        if (application == null) {
            throw new IllegalArgumentException("application can't be null!");
        }
        ApplicationInfo applicationInfo = application.getApplicationInfo();
        String packageName = applicationInfo.packageName;
        String processName = applicationInfo.processName;
        if (packageName.equals(processName) || packageName.concat(":channel").equals(processName)) {
            //从meta中读取用户定义的推送平台信息
            if (targetPlatformCode > 0) {
                OnePush.init(application, new TargetRegisterListener(targetPlatformCode));
            } else {
                if (backupHookTransmitListener == null) {
                    backupHookTransmitListener = new BackupHookTransmitListener(application, backupPlatformCode, registerErrorTimes);
                    OneRepeater.attachHookTransmitListener(backupHookTransmitListener);
                }
                //根据当前手机Rom，进行智能注册
                HashMap<String, String> onePushMetaData = MetaDataUtils.getOnePushMetaData(application);
                if (onePushMetaData == null || onePushMetaData.isEmpty()) {
                    throw new IllegalArgumentException("can't find one push register message!");
                }
                smartRegisterByRom(application, onePushMetaData, backupPlatformCode);
            }
        }
    }

    private void smartRegisterByRom(Application application, HashMap<String, String> onePushMetaData, int backupPlatformCode) {
        int[] platformArray = new int[3];
        Iterator<Map.Entry<String, String>> iterator = onePushMetaData.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String key = next.getKey();
            String value = next.getValue();
            if (PLATFORM_XIAOMI.equals(value)) {
                platformArray[0] = PlatformCodeUtils.getPlatformCodeByMetaName(key);
            } else if (PLATFORM_MEIZU.equals(value)) {
                platformArray[1] = PlatformCodeUtils.getPlatformCodeByMetaName(key);
            } else if (PLATFORM_HUAWEI_HMS.equals(value)) {
                platformArray[2] = PlatformCodeUtils.getPlatformCodeByMetaName(key);
            }
        }
        //获取当前系统的信息
        int registerPlatformCode = 0;
        if (RomUtils.isMiuiRom() && onePushMetaData.containsValue(PLATFORM_XIAOMI)) {
            registerPlatformCode = platformArray[0];
        } else if (RomUtils.isFlymeRom() && onePushMetaData.containsValue(PLATFORM_MEIZU)) {
            registerPlatformCode = platformArray[1];
        } else if (RomUtils.isEmuiSupportHMS() && onePushMetaData.containsValue(PLATFORM_HUAWEI_HMS)) {
            registerPlatformCode = platformArray[2];
        }
        if (registerPlatformCode == 0) {
            registerPlatformCode = backupPlatformCode;
        }
        OnePush.init(application, new TargetRegisterListener(registerPlatformCode));
    }


    private static class TargetRegisterListener implements OnOnePushRegisterListener {

        private int mTargetPlatformCode;

        public TargetRegisterListener(int targetPlatformCode) {
            this.mTargetPlatformCode = targetPlatformCode;
        }

        @Override
        public boolean onRegisterPush(int platformCode, String platformName) {
            return platformCode == this.mTargetPlatformCode;
        }
    }

    private static class BackupHookTransmitListener implements IHookTransmitListener {

        private Application mApplication;
        private int mBackupPlatformCode;
        private int mRegisterErrorTimes;
        private int mRegisterCount;

        public BackupHookTransmitListener(Application application,int backupPlatformCode, int registerErrorTimes) {
            this.mApplication = application;
            this.mBackupPlatformCode = backupPlatformCode;
            this.mRegisterErrorTimes = registerErrorTimes;
        }

        @Override
        public void transmitCommandResult(int type, int resultCode, String token, String extraMsg, String error) {
            if (type == OnePush.TYPE_REGISTER) {
                //代表当前注册失败
                if (resultCode == OnePush.RESULT_ERROR || TextUtils.isEmpty(token)) {
                    mRegisterCount++;
                }
                if (mRegisterCount < mRegisterErrorTimes) {
                    OnePush.register();
                } else if (OnePush.getPushPlatFormCode() != this.mBackupPlatformCode) {
                    OnePush.unRegister();
                    SmartRegister.getInstance().init(mApplication, this.mBackupPlatformCode, 0, 4);
                } else {
                    OneLog.i("OnePush retry register error(backup push platform register error)");
                }
            }
        }
    }



}

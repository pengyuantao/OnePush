![logo](http://upload-images.jianshu.io/upload_images/1460021-ac600dbd1d991fa1.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
##### 想看吐槽的点链接：
*****************************
[一步步走来的消息推送](http://www.jianshu.com/p/1ff15a072fdf)

[详细集成指南，请点这里！](http://www.jianshu.com/p/91adbbde31e0)

##### android消息推送的好消息：
*******************
[安卓统一推送标准 已取得阶段性成果](http://mp.weixin.qq.com/s/qMfUm2fsOS6EHHaa1nbdpw)

[实验室开展基于安卓操作系统统一推送工作的相关Q&A](http://mp.weixin.qq.com/s/Gni8zu75nJMPKAo3gfTeJQ)
**********

|小米推送|华为推送|友盟推送
|:-------:|:-------:|:-------:|
|![](http://upload-images.jianshu.io/upload_images/1460021-b84daf61d5b52ad6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![](http://upload-images.jianshu.io/upload_images/1460021-b99dc8a580ca5aeb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![](http://upload-images.jianshu.io/upload_images/1460021-5dc28971978853fa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|

### 前言
看了 " 泰尔终端实验室 "说要统一android的消息推送了，差点激动的掉眼泪！
仔细看了下当前的统一推送的进度，说实话，个人感觉真的需要一段时间啊，而且就算将来完成整个统一的推送标准，那么还是会有部分手机得不到升级，还得继续时候第三方推送。不过能至少到时候集成的推送少点了。
根据泰尔实验室的文章，有那么一段，说要限制透传消息，个人觉得透传消息还是很有用的，如果要限制估计还是有点坑，不知道你是怎么看的？
```
问题五：统一推送是否能减少手机耗电？

目前，推送消息过程中电量浪费一方面来自其自建长连接通道消耗的功耗，另一方面来自应用为接收消息“自启动”、“相互拉起”等“保活”行为造成的资源消耗。在建立统一推送的机制后，推送链路将会合并成为一条，同时，应用不需要为了接收推送消息而“保活”，从而节约手机能耗和系统资源。
此外，由于应用的“日活”数据对开发者和投资人非常重要，一些开发者会利用透传消息，在用户不知情的情况下激活应用，造成耗电和资源浪费，因此，透传消息激活应用的做法也会被限制。
```


### 快速集成指南

1. 添加OnePush主要依赖（必须添加）
```
dependencies {
      compile 'com.peng.library:one-push-core:1.0.1'
}
```
2. 添加第三方推送依赖（根据自己的需求进行添加，当然也可以全部添加）
```
dependencies {
      compile 'com.peng.library:one-push-huawei:1.0.1'
      compile 'com.peng.library:one-push-xiaomi:1.0.1'
      compile 'com.peng.library:one-push-umeng:1.0.1'
}
```

3.  继承BaseOnePushReceiver重写里面的方法，并在AndroidManifest.xml中注册
```
<receiver android:name="com.peng.openpush.TestPushReceiver">
            <intent-filter>
                 <action android:name="com.peng.one.push.ACTION_RECEIVE_NOTIFICATION" />
                <action android:name="com.peng.one.push.ACTION_RECEIVE_NOTIFICATION_CLICK" />
                <action android:name="com.peng.one.push.ACTION_RECEIVE_MESSAGE" />
                <action android:name="com.peng.one.push.ACTION_RECEIVE_COMMAND_RESULT" />
            </intent-filter>
</receiver>
```
4. 在AndroidManifest.xml的application标签下，添加第三方推送实现类
```
 <!--如果引入了one-push-huawei类库-->
        <meta-data
            android:name="OnePush_HuaWei_102"
            android:value="com.peng.one.push.huawei.HuaweiPushClient" />

 <!--如果引入了one-push-xiaomi库-->
        <meta-data
            android:name="OnePush_XiaoMi_101"
            android:value="com.peng.one.push.xiaomi.XiaomiPushClient" />

 <!--如果引入了one-push-umeng库-->
        <meta-data
            android:name="OnePush_UMENG_103"
            android:value="com.peng.one.push.umeng.UMengPushClient" />
```
关于<meta-data/>标签书写规则：
 * android:name    必须是以“ OnePush ”开头，并且以"\_"进行分割(OnePush_平台名称_平台标识码)，在初始化OnePush 的时候，根据标识码和当前手机系统，动态的使用不同平台消息推送。
 *  android:value    这个是继承IPushClient实现类，全类名路径。

5. 添加第三方AppKey和AppSecret
如果使用了one-push-xiaomi,那么需要在AndroidManifest.xml添加小米的AppKey和AppSecret（注意下面的“\ ”必须加上，否则获取到的是float而不是String，就会导致id和key获取不到正确的数据）
```
 <!--xiaomi_push需要进行下面的配置-->
        <meta-data
            android:name="MI_PUSH_APP_ID"
            android:value="\ 2215463567096567312" />

        <meta-data
            android:name="MI_PUSH_APP_KEY"
            android:value="\ 9889423330043400" />

 <!--umeng_push需要进行下面配置-->
        <meta-data
            android:name="UMENG_APPKEY"
            android:value="593e2640b27b0a0852000014"/>

        <meta-data
            android:name="UMENG_MESSAGE_SECRET"
            android:value="b765e337eedd391603550eb6f922f81b"/>

 <!--huawei_push，在app上不需要配置appkey和secret，需要在华为开发者平台，申请华为推送，并配置包名和证书指纹-->
```

6. 如果OnePush使用了小米推送，需要注册小米推送权限
```
 <!--注意下面的必须修改   -->
    <permission
        android:name="com.peng.one.push.permission.MIPUSH_RECEIVE"
        android:protectionLevel="signature" />
    <!--这里com.peng.one.push改成你的app的包名，以build.gralde中的applicationId为准-->
    <uses-permission android:name="com.peng.one.push.permission.MIPUSH_RECEIVE" />
   <!--这里com.peng.one.push改成你的app的包名，以build.gralde中的applicationId为准-->
```

7. 初始化OnePush
```|
//初始化的时候，回调该方法，可以根据platformCode和当前系统的类型，进行注册
//返回true，则使用该平台的推送，否者就不使用
 OnePush.init(this, ((platformCode, platformName) -> {
                //platformCode和platformName就是在<meta/>标签中，对应的"平台标识码"和平台名称
                if (platformCode == 102 && RomUtils.isHuaweiRom()) {//华为
                    return true;
                } else if (platformCode == 101 && RomUtils.isMiuiRom()) {//小米
                    return true;
                } else if (platformCode == 103) {//友盟
                    return true;
                }
                return false;
            }));
            OnePush.register();
```
8. 说明：
 * 注册友盟推送除了在主进程中，还需要在channel进程中进行注册，具体操作见DEMO（UMeng官方推送就是这样要求的）
 * 友盟推送：后台配置后续动作，为"自定义行为"。
 * 小米推送：后台配置点击后续动作，为"由应用客户端自定义"。
 * 华为推送：后台配置后续行为，为"自定义动作"，具体内容，可由OnePushService包：com.peng.one.push.service.huawei.intent.HWPushIntent生成，如果后台不是java开发的，参照HWPushIntent重新写。
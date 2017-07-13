## 消息推送用OnePush，就够了！

|模块|one-push-core|one-push-huawei|one-push-xiaomi|one-push-umeng|
|-------|-------|-------|-------|-------|
|lastVersion|![](https://api.bintray.com/packages/pengyuantao/maven/one-push-core/images/download.svg)|![](https://api.bintray.com/packages/pengyuantao/maven/one-push-huawei/images/download.svg)|![](https://api.bintray.com/packages/pengyuantao/maven/one-push-xiaomi/images/download.svg)|![](https://api.bintray.com/packages/pengyuantao/maven/one-push-umeng/images/download.svg)|


|小米推送|华为推送|友盟推送|
|:-------:|:-------:|:-------:|
|![](http://upload-images.jianshu.io/upload_images/1460021-b84daf61d5b52ad6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![](http://upload-images.jianshu.io/upload_images/1460021-b99dc8a580ca5aeb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![](http://upload-images.jianshu.io/upload_images/1460021-5dc28971978853fa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|

### 传送门

[一步步走来的消息推送](http://www.jianshu.com/p/1ff15a072fdf)

[安卓统一推送标准 已取得阶段性成果](http://mp.weixin.qq.com/s/qMfUm2fsOS6EHHaa1nbdpw)

[实验室开展基于安卓操作系统统一推送工作的相关Q&A](http://mp.weixin.qq.com/s/Gni8zu75nJMPKAo3gfTeJQ)

[一步步走来的消息推送](http://www.jianshu.com/p/1ff15a072fdf)

[更新日志](https://github.com/pengyuantao/OnePush/blob/master/updateLog.md)

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

> 所有的lastVersion对应的是上面表格的最新的版本号，集成的时候，需要进行替换。

#### 1. 添加OnePush主要依赖（必须添加）
```
dependencies {
      compile 'com.peng.library:one-push-core:lastVersion'
}
```
#### 2. 添加第三方推送依赖（根据自己的需求进行添加，当然也可以全部添加）
```
dependencies {
      compile 'com.peng.library:one-push-huawei:lastVersion'
      compile 'com.peng.library:one-push-xiaomi:lastVersion'
      compile 'com.peng.library:one-push-umeng:lastVersion'
}
```

#### 3.  继承BaseOnePushReceiver重写里面的方法，并在AndroidManifest.xml中注册
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
#### 4. 在AndroidManifest.xml的application标签下，添加第三方推送实现类
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

#### 5. 添加第三方AppKey和AppSecret
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

#### 6. 如果OnePush使用了小米推送，需要注册小米推送权限
```
 <!--注意下面的必须修改   -->
    <permission
        android:name="com.peng.one.push.permission.MIPUSH_RECEIVE"
        android:protectionLevel="signature" />
    <!--这里com.peng.one.push改成你的app的包名，以build.gralde中的applicationId为准-->
    <uses-permission android:name="com.peng.one.push.permission.MIPUSH_RECEIVE" />
   <!--这里com.peng.one.push改成你的app的包名，以build.gralde中的applicationId为准-->
```

#### 7. 初始化OnePush
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
#### 8. 后台推送动作说明：
 * 注册友盟推送除了在主进程中，还需要在channel进程中进行注册，具体操作见DEMO（UMeng官方推送就是这样要求的）
 * 友盟推送：后台配置后续动作，为"自定义行为"。
 * 小米推送：后台配置点击后续动作，为"由应用客户端自定义"。
 * 华为推送：后台配置后续行为，为"自定义动作"，具体内容，可由OnePushService包：com.peng.one.push.service.huawei.intent.HWPushIntent生成，如果后台不是java开发的，参照HWPushIntent重新写。

#### 9. 集成  **友盟推送** 的童鞋注意啦
 * OnePush拓展的友盟推送是[版本v3.1.1a](http://dev.umeng.com/push/android/sdk-download)。
 * 关于utdid重复引入的问题，可以通过下面的方案解决
```
//如果utdid和你工程项目里面发生冲突了，请修改成这个依赖
 compile ('com.peng.library:one-push-umeng:lastVersion' ){
        exclude group: 'com.peng.library',module:'one-push-umeng-utdid4all'
    }
```
 * 关于友盟推送so文件处理，OnePush拓展的友盟推送，默认将所有的so文件引入了，这样就导致友盟推送aar文件大小达到2.25M左右，所以下面提供一个裁剪so文件的方法
第一步：在工程根目录的gradle.properties文件中，添加 android.useDeprecatedNdk=true
第二步：在项目（app）的build.gradle节点defaultConfig下添加 
```
 ndk {
            // 设置支持的SO库
            abiFilters 'armeabi'//,'armeabi-v7a', 'x86', 'x86_64', 'arm64-v8a','mips','mips64'
        }
```
根据自己工程的需要，配置不同的so编译，然后Rebuild Project。

 * 最后啰嗦几句，其实只要添加armeabi，就可以了，armeabi在每个平台都是可以用的，俗称万能油。只是在其他CPU平台上，使用armeabi，效率不是很高而已，其实微信也是只使用了armeabi，只不过它为了提高效率，他将v7a也放在了armeabi里面，最后根据具体安装的手机CPU，动态加载而已。

#### 10. 集成  **华为推送**  的童鞋注意啦
 * BaseOnePushReceiver中的onReceiveNotification()方法，在使用的华为推送的时候，该方法不会被调用，因为华为推送没有提供这样的支持。
 *  BaseOnePushReceiver中的onReceiveNotificationClick()方法，在使用华为推送的时候，虽然华为支持，但是如果app被华为一键清理掉后，收到通知，那么点击通知是不会调用华为推送的onEvent（）方法，那么如果我们这里转发，onReceiveNotificationClick（）是不会收到的。
 * 为了解决华为推送，在手机上被清理掉后，onReceiveNotificationClick（）不被调用的情况，OnePush在华为推送上，使用跳转到指定Activity的推送通知，那么服务端必须提供一个Intent序列化的uri，OnePush提供的Java服务端消息推送示例中，已经提供了服务端序列化Intent的uri的实现（详见：com.peng.one.push.service.huawei.intent.HWPushIntent）。

#### 11. 关于将来拓展其他平台消息推送说明
  * 个人感觉，除了厂商的推送，其他的第三方推送只需要集成一个就可以了，假如你想使用OnePush，但是目前OnePush拓展的消息推送平台，没有你目前使用的怎么办呢，可以参照OnePush拓展详细说明，进行集成。
 * 如果你已经拓展其他平台的消息推送，并且测试通过，可以将代码Push过来，我检查过后，合并进来，这样可以方便大家。

#### 12. 拓展其他平台说明
关于添加其他消息推送SDK具体操作（如果你不满足OnePush提供的小米、华为推送，可根据下面步骤，将其他厂商提供的推送，添加到OnePush里面）
 * 创建XXXClient 实现IPushClient接口，并且重写对应的方法，initContext(Context),会在初始化的使用进行调用，可以在这里进行获取第三方推送注册需要的ID，KEY或者其他操作，第三方推送ID、KEY，建议在AndroidManifest.xml中的Application标签下添加<meta/>，然后在initContext(Context)中进行获取。

 * 创建和重写三方消息推送的Receiver或者IntentService（一般第三方会让你继承他的receiver，这里指的就是他），重写三方推送的的接收透传消息和通知的方法，调用OneRepeater的transmitXXX方法，将通知、透传消息、通知点击事件、以及其他事件，转发到OnePush。

 * 记得在OnePush注册的时候，进行消息推送平台的选择。

 * 具体操作方法：详见one-push-xiaomi

> 三、相关api介绍

<h6 align = "left">OnePush详细api</h6>

|方法名称|描述及解释|
|---------|:-------:|
|init(Context , OnOnePushRegisterListener)|初始化OnePush，建议在Application中onCreate()方法|
|register()|注册消息推送|
|unregister()|取消注册消息推送|
|bindAlias(String)|绑定别名|
|unBindAlias(String)|取消绑定别名|
|addTag(String)|添加标签|
|deleteTag(String)|删除标签|
|getPushPlatFormCode()|获取推送平台code(AndroidManifest.xml中<meta/>注册)|
|getPushPlatFormName()|获取推送平台name(AndroidManifest.xml中<meta/>注册)|
|setDebug(boolean)|设置是否为debug模式|

</br>
<h6 align = "left">OneRepeater详细api</h6>

|方法名称|描述及解释|
|---------|:-------:|
|transmitCommandResult(Context,int,int,String,String,String)|转发操作反馈（具体type在OnePush.TYPE_XXX）|
|transmitMessage(Context,String,String,Map<String,String>)|转发透传消息|
|transmitNotification(Context,int,String,String,Sting,Map<String,String>)|转发通知|
|transmitNotificationClick(Context,int,String,String,Sting,Map<String,String>)|转发通知点击事件|

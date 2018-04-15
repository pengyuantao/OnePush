## 目前OnePush在Android8.0上不兼容的问题，OnePush暂时不进行维护了，建议大家使用友盟的聚合推送，上面已经有各大厂商的推送。


## 消息推送用OnePush，就够了！

|模块|one-push-core|one-push-huawei|one-push-xiaomi|one-push-meizu|one-push-huawei-hms|
|-------|-------|-------|-------|-------|-------|
|lastVersion|![](https://api.bintray.com/packages/pengyuantao/maven/one-push-core/images/download.svg)|![](https://api.bintray.com/packages/pengyuantao/maven/one-push-huawei/images/download.svg)|![](https://api.bintray.com/packages/pengyuantao/maven/one-push-xiaomi/images/download.svg)|![](https://api.bintray.com/packages/pengyuantao/maven/one-push-meizu/images/download.svg)|![](https://api.bintray.com/packages/pengyuantao/maven/one-push-huawei-hms/images/download.svg)|

|模块|one-push-umeng|one-push-getui|one-push-jpush|
|-------|-------|------|------|
|lastVersion|![](https://api.bintray.com/packages/pengyuantao/maven/one-push-umeng/images/download.svg)|![](https://api.bintray.com/packages/pengyuantao/maven/one-push-getui/images/download.svg)|![](https://api.bintray.com/packages/pengyuantao/maven/one-push-jpush/images/download.svg)|
##### QQ交流群(459480065)
[![QQ群:459480065](http://upload-images.jianshu.io/upload_images/1460021-56c575cd47406c51.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
](http://shang.qq.com/wpa/qunwpa?idkey=09286469a726bda9ab1a79be3330542a9468689626432a1312882f9567265b06)

##### 手机系统级推送
|小米推送|华为推送|魅族推送|
|:-------:|:-------:|:-------:|
|![](http://upload-images.jianshu.io/upload_images/1460021-b84daf61d5b52ad6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![](http://upload-images.jianshu.io/upload_images/1460021-b99dc8a580ca5aeb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![](http://upload-images.jianshu.io/upload_images/1460021-0ab4b8f97fd166a4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|

##### 第三方平台推送
|友盟推送|个推推送|极光推送|
|:-------:|:-----:|:----:|
|![](http://upload-images.jianshu.io/upload_images/1460021-5dc28971978853fa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![](http://upload-images.jianshu.io/upload_images/1460021-638ce19c5df35038.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![](http://upload-images.jianshu.io/upload_images/1460021-c62f01fdee0b6027.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|



### 前言
OnePush从诞生到目前600多的star，由当初的不知名到现在很多朋友都应用到商业项目中，我真的很高兴，当然也有部分的朋友质疑，说：“OnePush有啥用呀，工信部都已经开始统一Android的推送了，而且已经召集各大厂商开始讨论了”，我想说的是，工信部的确准备一统Android的推送，但是这些工作还在进行中，那我们的应用，不可能等到他们统一以后再发布吧，其次，就算统一了，那么可能会涉及到手机系统升级的问题，没有升级的手机，是没办法享受到统一推送的渠道，你在看看，目前厂商几年前生产的手机，绝大部分的手机，系统是得不到官方的升级的，那么这些用户我们在统一推送之后，就不管他们了？显然是不行的，OnePush的诞生，和当初**[NineOldAndroids](https://github.com/JakeWharton/NineOldAndroids)**的出现有点类似，可以说NineOldAndroids，当初在我们开发兼容2.3的应用做了很大的贡献，虽然我们现在应用不再兼容到2.3，但是NineOldAndroids的功劳是不可磨灭的。


### 快速集成指南
> 如果集成的是1.0版本的OnePush建议升级到1.2版本。
> [1.0版本集成文档](https://github.com/pengyuantao/OnePush/blob/master/Readme_10.md)
> 所有的lastVersion对应的是上面表格的最新的版本号，集成的时候，需要进行替换。
> 设置Deubg模式：OneLog.setDebug(),该方法需要在OnePush.init()方法前调用。

#### 1. 添加OnePush主要依赖（必须添加）
项目project的build.gradle
```
allprojects {
    repositories {
        jcenter()
        maven { url 'https://jitpack.io' }
        //由于魅族个推等第三方库使用了不同的仓库，需要加上这个
        maven { url 'http://oss.jfrog.org/artifactory/oss-snapshot-local/' }
        maven { url "http://mvn.gt.igexin.com/nexus/content/repositories/releases/" }
        maven { url 'http://developer.huawei.com/repo/' }
    }
}

```
工程module的build.gradle
```
dependencies {
      compile 'com.peng.library:one-push-core:lastVersion'
}
```
#### 2. 添加第三方推送依赖（根据自己的需求进行添加，当然也可以全部添加）
```
dependencies {
    //华为推送和HMS服务只能选择其中的一个
    //compile 'com.peng.library:one-push-huawei:lastVersion'
    compile 'com.peng.library:one-push-huawei-hms:lastVersion'
    compile 'com.peng.library:one-push-xiaomi:lastVersion'
    compile 'com.peng.library:one-push-umeng:lastVersion'
    compile 'com.peng.library:one-push-getui:lastVersion'
    compile 'com.peng.library:one-push-meizu:lastVersion'
    compile 'com.peng.library:one-push-jpush:lastVersion'

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

                <category android:name="${applicationId}" />
            </intent-filter>
</receiver>
```
#### 4. 在AndroidManifest.xml的application标签下，添加第三方推送实现类
```
    <!--如果引入了one-push-xiaomi库-->
    <meta-data
        android:name="OnePush_XiaoMi_101"
        android:value="com.peng.one.push.xiaomi.XiaomiPushClient"/>


    <!--如果引入了one-push-huawei类库-->
<!--        <meta-data
            android:name="OnePush_HuaWei_102"
            android:value="com.peng.one.push.huawei.HuaweiPushClient"/>-->

    <!--如果引入了one-push-meizu库-->
    <meta-data
        android:name="OnePush_MeiZu_103"
        android:value="com.peng.one.push.meizu.MeizuPushClient"/>

    <!--如果引入了one-push-umeng类库-->
    <meta-data
        android:name="OnePush_UMENG_104"
        android:value="com.peng.one.push.umeng.UMengPushClient"/>

    <!--如果引入了one-push-getui库-->
    <meta-data
        android:name="OnePush_GeTui_105"
        android:value="com.peng.one.push.getui.GeTuiPushClient"/>

    <!--如果引入了one-push-库-->
    <meta-data
        android:name="OnePush_JPush_106"
        android:value="com.peng.one.push.jpush.JPushClient"/>

    <!--如果引入了one-push-huawei-hms库-->
    <meta-data
        android:name="OnePush_HMSPush_107"
        android:value="com.peng.one.push.huawei.hms.HMSPushClient"/>

```
关于<meta-data/>标签书写规则：
 * android:name    必须是以“ OnePush ”开头，并且以"\_"进行分割(OnePush_平台名称_平台标识码)，在初始化OnePush 的时候，根据标识码和当前手机系统，动态的使用不同平台消息推送。
 *  android:value    这个是继承IPushClient实现类，全类名路径。

#### 5. 添加第三方AppKey和AppSecret
如果使用了one-push-xiaomi,那么需要在AndroidManifest.xml添加小米的AppKey和AppSecret（注意下面的“\ ”必须加上，否则获取到的是float而不是String，就会导致id和key获取不到正确的数据）
```
    <!--友盟推送静态注册-->
    <meta-data
        android:name="UMENG_APPKEY"
        android:value="59c87440734be47b6700001b"/>

    <meta-data
        android:name="UMENG_MESSAGE_SECRET"
        android:value="b856987f2ca836a784698e150d36ff2a"/>

    <!--小米推送静态注册-->
    <meta-data
        android:name="MI_PUSH_APP_ID"
        android:value="\ 2882303761517577233"/>

    <meta-data
        android:name="MI_PUSH_APP_KEY"
        android:value="\ 5701757717233"/>

    <!--个推推送静态注册-->
    <meta-data
        android:name="PUSH_APPID"
        android:value="edX56uUoQ7ASr4ru4c3rVA"/>
    <meta-data
        android:name="PUSH_APPKEY"
        android:value="Crl1UEA5Fd8tooEDmS5aA3"/>
    <meta-data
        android:name="PUSH_APPSECRET"
        android:value="D9hcD0r6Ec5ANGSLY92XP3"/>

    <!--魅族推送静态注册-->
    <meta-data
        android:name="MEIZU_PUSH_APP_ID"
        android:value="111338"/>

    <meta-data
        android:name="MEIZU_PUSH_APP_KEY"
        android:value="db1659369a85459abe5384814123ab5a"/>

    <!--极光推送静态注册-->
    <meta-data
        android:name="JPUSH_CHANNEL"
        android:value="developer"/>
    <meta-data
        android:name="JPUSH_APPKEY"
        android:value="41bb9f84d2158a7be9de3b47"/>

    <!--华为HMS推送静态注册-->
    <meta-data
        android:name="com.huawei.hms.client.appid"
        android:value="100099491"/>

 <!--华为老版本推送不需要静态注册，在app上不需要配置appkey和secret，需要在华为开发者平台，申请华为推送，并配置包名和证书指纹-->
```


#### 6. 初始化OnePush
```|
//初始化的时候，回调该方法，可以根据platformCode和当前系统的类型，进行注册
//返回true，则使用该平台的推送，否者就不使用
//只在主进程中注册(注意：umeng推送，除了在主进程中注册，还需要在channel中注册)
        if (BuildConfig.APPLICATION_ID.equals(currentProcessName) || BuildConfig.APPLICATION_ID.concat(":channel").equals(currentProcessName)) {
            OnePush.init(this, ((platformCode, platformName) -> {
                //platformCode和platformName就是在<meta/>标签中，对应的"平台标识码"和平台名称
                if (RomUtils.isMiuiRom()) {
                    return platformCode == 101;
                } else if (RomUtils.isHuaweiRom()) {
                    return platformCode == 102;
                } else if (RomUtils.isFlymeRom()) {
                    return platformCode == 105;
                }else {
                    return platformCode == 104;
                }
            }));
            OnePush.register();
}
```
#### 7. 后台推送动作说明：
 * 注册友盟推送除了在主进程中，还需要在channel进程中进行注册，具体操作见DEMO（UMeng官方推送就是这样要求的）
 * 友盟推送：后台配置后续动作，为"自定义行为"。
 * 小米推送：后台配置点击后续动作，为"由应用客户端自定义"。
 * 魅族推送：后台配置点击动作，为"应用客户端自定义"
* 个推推送：后台配置后续动作为打开应用，如果你发送的通知，为了保证你点击通知栏能收到在NotificationClick的回调，每一个通知必须都带有one-push规定格式的透传消息，如果你只发送透传，那就不必按照下面的格式。
```
个推通知中透传消息json:
    {
        "onePush":true,
         "title":"通知标题",
         "content":"通知内容",
         "extraMsg":"额外信息",
         "keyValue":{
            "key1":"value1",
            "key2":"value2",
            "key3":"value3"
           }
      }

```
 * 华为推送和华为HMS服务：后台配置后续行为，为"自定义动作"，具体内容，可由OnePushService包：com.peng.one.push.service.huawei.intent.HWPushIntent生成，如果后台不是java开发的，参照HWPushIntent重新写。

#### 8. 集成  **友盟推送** 的童鞋注意啦
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

#### 9. 集成  **华为推送**  的童鞋注意啦
 * BaseOnePushReceiver中的onReceiveNotification()方法，在使用的华为推送的时候，该方法不会被调用，因为华为推送没有提供这样的支持。
 *  BaseOnePushReceiver中的onReceiveNotificationClick()方法，在使用华为推送的时候，虽然华为支持，但是如果app被华为一键清理掉后，收到通知，那么点击通知是不会调用华为推送的onEvent（）方法，那么如果我们这里转发，onReceiveNotificationClick（）是不会收到的。
 * 为了解决华为推送，在手机上被清理掉后，onReceiveNotificationClick（）不被调用的情况，OnePush在华为推送上，使用跳转到指定Activity的推送通知，那么服务端必须提供一个Intent序列化的uri，OnePush提供的Java服务端消息推送示例中，已经提供了服务端序列化Intent的uri的实现（详见：com.peng.one.push.service.huawei.intent.HWPushIntent）。

#### 10. 关于将来拓展其他平台消息推送说明
  * 个人感觉，除了厂商的推送，其他的第三方推送只需要集成一个就可以了，假如你想使用OnePush，但是目前OnePush拓展的消息推送平台，没有你目前使用的怎么办呢，可以参照OnePush拓展详细说明，进行集成。
 * 如果你已经拓展其他平台的消息推送，并且测试通过，可以将代码Push过来，我检查过后，合并进来，这样可以方便大家。

#### 11. 拓展其他平台说明
关于添加其他消息推送SDK具体操作（如果你不满足OnePush提供的小米、华为推送，可根据下面步骤，将其他厂商提供的推送，添加到OnePush里面）
 * 创建XXXClient 实现IPushClient接口，并且重写对应的方法，initContext(Context),会在初始化的使用进行调用，可以在这里进行获取第三方推送注册需要的ID，KEY或者其他操作，第三方推送ID、KEY，建议在AndroidManifest.xml中的Application标签下添加<meta/>，然后在initContext(Context)中进行获取。

 * 创建和重写三方消息推送的Receiver或者IntentService（一般第三方会让你继承他的receiver，这里指的就是他），重写三方推送的的接收透传消息和通知的方法，调用OneRepeater的transmitXXX方法，将通知、透传消息、通知点击事件、以及其他事件，转发到OnePush。

 * 记得在OnePush注册的时候，进行消息推送平台的选择。

 * 具体操作方法：详见one-push-xiaomi

#### 12. 代码混淆

```
-dontoptimize
-dontpreverify
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-dontwarn com.peng.one.push.**
-dontwarn com.igexin.**
-dontwarn cn.jpush.**
-dontwarn cn.jiguang.**
-keepattributes *Annotation*

-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }
-keep class cn.jiguang.** { *; }
-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class com.hianalytics.android.** {*;}
-keep class com.meizu.cloud.**{*;}
-keep class org.apache.thrift.** {*;}
-keep class com.igexin.** { *; }
-keep class org.json.** { *; }
-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class **.R$*{
   public static final int *;
}

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }

 # OnePush的混淆
-keep class * extends com.peng.one.push.core.IPushClient{*
```


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
#### License
```
   Copyright 2017 pengyuantao

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

### 传送门
[一步步走来的消息推送](http://www.jianshu.com/p/1ff15a072fdf)
[安卓统一推送标准 已取得阶段性成果](http://mp.weixin.qq.com/s/qMfUm2fsOS6EHHaa1nbdpw)
[实验室开展基于安卓操作系统统一推送工作的相关Q&A](http://mp.weixin.qq.com/s/Gni8zu75nJMPKAo3gfTeJQ)
[更新日志](https://github.com/pengyuantao/OnePush/blob/master/updateLog.md)
### 感谢
[Youzh](https://github.com/youmu178)    提供one-push-huawei-hms拓展


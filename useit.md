![logo](http://upload-images.jianshu.io/upload_images/1460021-ac600dbd1d991fa1.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

想看吐槽的点链接：
[一步步走来的消息推送](http://www.jianshu.com/p/1ff15a072fdf)

**********
> 一、为啥会有OnePush

消息推送，在国内，要么自己做，要么集成第三方的sdk，现在第三方推送的sdk，有很多可以选择，友盟，极光，小米等，我们在选择消息推送的时候，肯定是需要一个能及时把消息推送给用户的sdk，那么问题来了，很多国产手机厂商都各自根据android修改的UI系统，各种的进程清理，导致消息推送的后台无法存活，特别是小米、华为的手机上，只要用户清理，基本上推送后台服务就死绝了，当然这个也不能怪人家手机厂商，主要是手机上的app，基本上每个app都有自己的后台推送服务，而且有的流氓程序，甚至还附加了很多其他的后台服务，这样就会导致用户手机耗电、卡顿，手机厂商为了解决既不耗电，也能及时收到消息通知，就推出了自己的推送SDK，比如小米，华为，魅族。如果你的app集成了该厂商的消息推送sdk，那么在该厂商手机上，就属于系统的级别的服务，就算手机被一键清理掉，消息还是能准时的推送到用户的手机上，但是在国内，生产手机的厂家不不止一个，而且每家都有自己的消息推送，如何做到快速的接入和切换推送呢？那么OnePush，就是解决根据不同的厂商手机，集成不同的推送，从而保证消息的及时送达，目前OnePush提供的小米推送，华为推送的实现，如果你还需要使用其他推送SDK，最多写两个类，就可以轻松接入OnePush。


> 二、怎么集成和使用OnePush

1. 添加依赖
```
dependencies {
      compile 'com.peng.library:one-push-core:1.0.1'
      compile 'com.peng.library:one-push-huawei:1.0.1'
      compile 'com.peng.library:one-push-xiaomi:1.0.1'
      compile 'com.peng.library:one-push-umeng:1.0.1'
}
```

2.  继承BaseOnePushReceiver重写里面的方法，并在AndroidManifest.xml中注册
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
3. 在AndroidManifest.xml的application标签下，添加第三方推送实现类

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

4. 添加第三方AppKey和AppSecret
如果使用了one-push-xiaomi,那么需要在AndroidManifest.xml添加小米的AppKey和AppSecret（注意下面的“\ ”必须加上，否则获取到的是float而不是String，就会导致id和key获取不到正确的数据）
```
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
```

5. 如果OnePush使用了小米推送，需要注册小米推送权限

```
 <!--注意下面的必须修改   -->
    <permission
        android:name="com.peng.one.push.permission.MIPUSH_RECEIVE"
        android:protectionLevel="signature" />
    <!--这里com.peng.one.push改成你的app的包名，以build.gralde中的applicationId为准-->
    <uses-permission android:name="com.peng.one.push.permission.MIPUSH_RECEIVE" />
   <!--这里com.peng.one.push改成你的app的包名，以build.gralde中的applicationId为准-->

```

6. 初始化OnePush
```|
//初始化的时候，回调该方法，可以根据platformCode和当前系统的类型，进行注册
//返回true，则使用该平台的推送，否者就不使用
OnePush.init(this, new OnOnePushRegisterListener() {
                @Override
                public boolean onRegisterPush(int platformCode, String platformName) {
                    //platformCode和platformName就是在<meta/>标签中，对应的"平台标识码"和平台名称
                    //华为手机使用华为推送，其他手机使用小米推送
                    if (RomUtils.isHuaweiRom() && platformCode == 102) {
                        Log.i(TAG, "onRegisterPush: 华为推送");
                        return true;
                    } else if (platformCode == 101) {
                        Log.i(TAG, "onRegisterPush: 小米推送");
                        return true;
                    }
                    return false;
                }
            });
```

7. 关于添加其他消息推送SDK具体操作（如果你不满足OnePush提供的小米、华为推送，可根据下面步骤，将其他厂商提供的推送，添加到OnePush里面）
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


> 四、OnePush消息推送测试

OnePush提供一个Java服务端消息推送的示例，大家可以使用它进行消息推送的测试。

> 五、使用注意

* BaseOnePushReceiver中的onReceiveNotification()方法，在使用的华为推送的时候，该方法不会被调用，因为华为推送没有提供这样的支持。
*  BaseOnePushReceiver中的onReceiveNotificationClick()方法，在使用华为推送的时候，虽然华为支持，但是如果app被华为一键清理掉后，收到通知，那么点击通知是不会调用华为推送的onEvent（）方法，那么如果我们这里转发，onReceiveNotificationClick（）是不会收到的。
* 为了解决华为推送，在手机上被清理掉后，onReceiveNotificationClick（）不被调用的情况，OnePush在华为推送上，使用跳转到指定Activity的推送通知，那么服务端必须提供一个Intent序列化的uri，OnePush提供的Java服务端消息推送示例中，已经提供了服务端序列化Intent的uri的实现（详见：com.peng.one.push.service.huawei.intent.HWPushIntent）。
* 使用OnePushService测试app的时候，需要修改Constant类的第三方推送id，否则将无法推送！当然你也可以使用第三方推送的web后台。

> 六、华为推送服务端Intent序列化示例

```
intent://com.peng.one.push/notification?title=标题&content=通知内容&extraMsg=额外信息&keyValue={"key1":"value1","key2":"value2","key3":"value3"}#Intent;scheme=OnePush;launchFlags=0x10000000;end
```

> 七、开源地址

[OnePush源代码](https://github.com/pengyuantao/OnePush)
[使用介绍](https://github.com/pengyuantao/OnePush/blob/master/Readme.md)

> 八、附点图吧，前面太多文字看着太累

|小米推送|华为推送|友盟推送
|:-------:|:-------:|:-------:|
|![](http://upload-images.jianshu.io/upload_images/1460021-b84daf61d5b52ad6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![](http://upload-images.jianshu.io/upload_images/1460021-b99dc8a580ca5aeb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|![](http://upload-images.jianshu.io/upload_images/1460021-5dc28971978853fa.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)|
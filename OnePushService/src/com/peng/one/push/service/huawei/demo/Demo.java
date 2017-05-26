package com.peng.one.push.service.huawei.demo;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import nsp.NSPClient;
import nsp.OAuth2Client;
import nsp.support.common.AccessToken;
import nsp.support.common.NSPException;

public class Demo {
    public static final String TIMESTAMP_NORMAL = "yyyy-MM-dd HH:mm:ss";
    private static final String TEST_DEVICES = "0a000005984ecae0300000463600CN01";


    /**
     * 方法表述
     *
     * @param args void
     * @throws NSPException
     */
    public static void main(String[] args)
            throws NSPException {
        try {
            
            /*
             * 获取token的方法 appId为开发者联盟上面创建应用的APP ID appKey为开发者联盟上面创建应用的 APP SECRET
             * APP ID：appid100       应用包名：com.open.test    |   APP SECRET：xxxxdtsb4abxxxlz2uyztxxxfaxxxxxx
             */
            OAuth2Client oauth2Client = new OAuth2Client();
//            oauth2Client.initKeyStoreStream(Demo.class.getResource("/mykeystorebj.jks").openStream(), "123456");
            oauth2Client.initKeyStoreStream(new FileInputStream("mykeystorebj.jks"), "123456");
            String appId = "100009109";
            String appKey = "6ad1e15b05faef5db4d8cd9206dbd973";

            AccessToken access_token = oauth2Client.getAccessToken("client_credentials", appId, appKey);

            System.err.println("access token :" + access_token.getAccess_token() + ",expires time[access token 过期时间]:"
                    + access_token.getExpires_in());

            NSPClient client = new NSPClient(access_token.getAccess_token());
            client.initHttpConnections(30, 50);//设置每个路由的连接数和最大连接数
//            client.initKeyStoreStream(Demo.class.getResource("mykeystorebj.jks").openStream(), "123456");//如果访问https必须导入证书流和密码
            client.initKeyStoreStream(new FileInputStream("mykeystorebj.jks"), "123456");//如果访问https必须导入证书流和密码

            //调用push单发接口
            single_send(client);

            //调用群发push消息接口
            batch_send(client);

            //调用设置用户标签接口
//            set_user_tag(client);

            //调用删除用户标签接口
            //delete_user_tag(client);

            //调用查询用户标签接口
            //query_user_tag(client);

            //调用查询应用标签接口
//            query_app_tags(client);

            //调用地理围栏消息接口
            //lbs_send(client);

            //调用通知栏消息接口
            notification_send(client);

            //调用查询查询消息发送结果接口
            //query_msg_result(client);

            //调用根据日期获取token文件接口
            //get_token_by_date(client);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单发消息
     *
     * @param client
     * @throws NSPException
     */
    public static void single_send(NSPClient client)
            throws NSPException {
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat dataFormat = new SimpleDateFormat(TIMESTAMP_NORMAL);

        //目标用户，必选。
        //由客户端获取， 32 字节长度。手机上安装了push应用后，会到push服务器申请token，申请到的token会上报给应用服务器
        String token = "0a000005984ecae0300000463600CN01";

        //发送到设备上的消息，必选
        //最长为4096 字节（开发者自定义，自解析）
        String message = "hello~~ you got a push message";

        //必选
        //0：高优先级
        //1：普通优先级
        //缺省值为1
        int priority = 0;

        //消息是否需要缓存，必选
        //0：不缓存
        //1：缓存
        //  缺省值为0
        int cacheMode = 1;

        //标识消息类型（缓存机制），必选
        //由调用端赋值，取值范围（1~100）。当TMID+msgType的值一样时，仅缓存最新的一条消息
        int msgType = 1;

        //可选
        //如果请求消息中，没有带，则MC根据ProviderID+timestamp生成，各个字段之间用下划线连接
        String requestID = "1_1362472787848";

        //unix时间戳，可选
        //格式：2013-08-29 19:55
        // 消息过期删除时间
        //如果不填写，默认超时时间为当前时间后48小时
        String expire_time = dataFormat.format(currentTime + 3 * 60 * 60 * 1000);

        //构造请求
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("deviceToken", token);
        hashMap.put("message", message);
        hashMap.put("priority", priority);
        hashMap.put("cacheMode", cacheMode);
        hashMap.put("msgType", msgType);
        hashMap.put("requestID", requestID);
        hashMap.put("expire_time", expire_time);

        //设置http超时时间
        client.setTimeout(10000, 15000);
        //接口调用
        PushRet resp = client.call("openpush.message.single_send", hashMap, PushRet.class);
        System.out.println(hashMap.toString());
        //打印响应
        System.err.println("单发接口消息响应:" + resp.getResultcode() + ",message:" + resp.getMessage());
    }

    /**
     * 群发消息
     *
     * @param client
     * @throws NSPException
     */
    public static void batch_send(NSPClient client)
            throws NSPException {
        //目标用户列表，必选
        //最多填1000个，每个目标用户为32字节长度，由系统分配的合法TMID。手机上安装了push应用后，会到push服务器申请token，申请到的token会上报给应用服务器
        String[] deviceTokenList = {"0a000005984ecae0300000463600CN01"};

        //发送到设备上的消息，必选
        //最长为4096 字节（开发者自定义，自解析）
        String message = "hello~~ you got a push message";

        //消息是否需要缓存，必选
        //0：不缓存
        //1：缓存
        // 缺省值为0
        Integer cacheMode = 1;

        //标识消息类型（缓存机制），必选
        //由调用端赋值，取值范围（1~100）。当TMID+msgType的值一样时，仅缓存最新的一条消息
        Integer msgType = 1;

        //unix时间戳，可选
        //格式：2013-08-29 19:55
        // 消息过期删除时间
        //如果不填写，默认超时时间为当前时间后48小时
        String expire_time = "2013-09-30 19:55";

        //构造请求
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("deviceTokenList", deviceTokenList);
        hashMap.put("message", message);
        hashMap.put("cacheMode", cacheMode);
        hashMap.put("msgType", msgType);
//        hashMap.put("expire_time", expire_time);

        //设置http超时时间
        client.setTimeout(10000, 15000);
        //接口调用
        PushRet resp = client.call("openpush.message.batch_send", hashMap, PushRet.class);
        System.out.println(hashMap.toString());
        //打印响应
        System.err.println("群发接口消息响应: resultcode：" + resp.getResultcode() + ",message:" + resp.getMessage());
    }

    /**
     * 地理围栏消息接口
     *
     * @param client
     * @throws NSPException
     */
    public static void lbs_send(NSPClient client)
            throws NSPException {
        //地理围栏信息，必选
        //Latlng表示经纬度列表，多个经纬度围城一个封闭区域，必须按照顺序填写，否则会影响定位准确度；
        //纬度在前，经度在后，纬度、经度之间用英文“,”分隔。
        //Type表示封闭区域类型，目前仅支持1，表示多边形
        String location =
                "{\"location\":{\"type\":\"1\",\"coordinates\":[{\"lat\":\"32.57844\",\"lng\":\"117.502087\"},{\"lat\":\"30.848528\",\"lng\":\"118.458746\"},{\"lat\":\"31.33934\",\"lng\":\"120.316873\"},{\"lat\":\"32.656293\",\"lng\":\"119.580981\"}]}}";

        //目标用户，该字段暂不生效
        String tokens = "";

        //标签，可选
        String tags = "{\"tags\":[{\"location\":[\"ShangHai\",\"GuangZhou\"]},{\"age\":[\"20\",\"30\"]}]}";

        //排除的标签，可选
        String exclude_tags = "{\"exclude_tags\":[{\"music\":[\"blue\"]},{\"fruit\":[\"apple\"]}]}";

        //消息内容，必选
        //该样例是点击通知消息打开url连接。更多的android样例请参考http://developer.huawei.com/ -> 资料中心 -> Push服务 -> API文档 -> 4.2.1 android结构体
        String android =
                "{\"notification_title\":\"the good news!\",\"notification_content\":\"Price reduction!\",\"doings\":3,\"url\":\"vmall.com\"}";

        //消息发送时间，可选
        //如果不携带该字段，则表示消息实时生效。实际使用时，该字段精确到分
        //消息发送时间戳，timestamp格式ISO 8601：2013-06-03T17:30:08+08:00
        String send_time = "2013-09-03T17:30:08+08:00";

        //消息过期时间，必选
        //timestamp格式ISO 8601：2013-06-03T17:30:08+08:00
        String expire_time = "2013-09-05T17:30:08+08:00";

        //构造请求
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("location", location);
        hashMap.put("tokens", tokens);
        hashMap.put("tags", tags);
        hashMap.put("exclude_tags", exclude_tags);
        hashMap.put("android", android);
        hashMap.put("send_time", send_time);
        hashMap.put("expire_time", expire_time);

        //设置http超时时间
        client.setTimeout(10000, 15000);
        //接口调用
        String rsp = client.call("openpush.openapi.lbs_send", hashMap, String.class);

        //打印响应
        //响应样例：{"result_code":0,"request_id":"1380075113","result_desc":"success"}
        System.err.println("地理围栏消息接口响应：" + rsp);
    }

    /**
     * 通知栏消息接口
     *
     * @param client
     * @throws NSPException
     */
    public static void notification_send(NSPClient client)
            throws NSPException {
        //推送范围，必选
        //1：指定用户，必须指定tokens字段
        //2：所有人，无需指定tokens，tags，exclude_tags 
        //3：一群人，必须指定tags或者exclude_tags字段
        Integer push_type = 1;

        //目标用户，可选
        //当push_type=1时，该字段生效
//        String tokens = "0a000005984ecae0300000463600CN01,00000000000000000000000000000000";
        String tokens = "0a000005984ecae0300000463600CN01";

//        String tokens = TEST_DEVICES;
        //标签，可选
        //当push_type的取值为2时，该字段生效
        String tags = "{\"tags\":[{\"location\":[\"ShangHai\",\"GuangZhou\"]},}\"age\":[\"20\",\"30\"]}]}";

        //排除的标签，可选
        //当push_type的取值为2时，该字段生效
//        String exclude_tags = "{\"exclude_tags\":[{\"music\":[\"blue\"]},{\"fruit\":[\"apple\"]}]}";

        //消息内容，必选
        //该样例是点击通知消息打开url连接。更多的android样例请参考http://developer.huawei.com/ -> 资料中心 -> Push服务 -> API文档 -> 4.2.1 android结构体
//        String android =
//                "{\"notification_title\":\"the good news!\",\"notification_content\":\"Price reduction!\",\"doings\":2,\"intent\":\"intent://com.peng.one.push/notification?title=title&content=content&extra={\"11111\":\"22222222\"}#Intent;scheme=OnePush;launchFlags=0x10000000;end\"}";

        String android ="{\"notification_title\":\"test\",\"notification_content\":\"test\",\"extras\":[{\"ffff\":\"ffff\"},{\"ddddd\":\"ddddd\"}],\"doings\":2,\"intent\":\"intent://com.peng.one.push/notification?title=title&content=content&extra={\\\"11111\\\":\\\"22222222\\\"}#Intent;scheme=OnePush;launchFlags=0x10000000;end\"}";


        //消息发送时间，可选
        //如果不携带该字段，则表示消息实时生效。实际使用时，该字段精确到分
        //消息发送时间戳，timestamp格式ISO 8601：2013-06-03T17:30:08+08:00
//        String send_time = "2013-09-03T17:30:08+08:00";

        //消息过期时间，可选
        //timestamp格式ISO 8601：2013-06-03T17:30:08+08:00
//        String expire_time = "2013-09-05T17:30:08+08:00";

        //构造请求
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("push_type", push_type);
        hashMap.put("tokens", tokens);
//        hashMap.put("tags", tags);
//        hashMap.put("exclude_tags", exclude_tags);
        hashMap.put("android", android);
//        hashMap.put("send_time", send_time);
//        hashMap.put("expire_time", expire_time);

        //设置http超时时间
        client.setTimeout(10000, 15000);
        //接口调用
        String rsp = client.call("openpush.openapi.notification_send", hashMap, String.class);
        System.out.println(hashMap.toString());
        //打印响应
        //响应样例：{"result_code":0,"request_id":"1380075138"}
        System.err.println("通知栏消息接口响应：" + rsp);
    }

    /**
     * 设置用户标签
     *
     * @param client
     * @throws NSPException
     */
    public static void set_user_tag(NSPClient client)
            throws NSPException {
        //deviceToken，必选
        //由客户端获取， 32 字节长度。手机上安装了push应用后，会到push服务器申请token，申请到的token会上报给应用服务器
        String token = "00000000000000000000000000000000";

        //标签类型，必选
        String tag_key = "location";

        //标签值，必选
        String tag_value = "NanJing";

        //构造请求
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("token", token);
        hashMap.put("tag_key", tag_key);
        hashMap.put("tag_value", tag_value);

        //设置http超时时间
        client.setTimeout(10000, 15000);
        String rsp = client.call("openpush.openapi.set_user_tag", hashMap, String.class);

        //打印响应
        //响应样例：{"result_code":"0","result_desc":"success","request_id":"1380075009"}
        System.err.println("设置标签接口响应：" + rsp);
    }

    /**
     * 查询应用标签接口
     *
     * @param client
     * @throws NSPException
     */
    public static void query_app_tags(NSPClient client)
            throws NSPException {
        //设置http超时时间
        client.setTimeout(10000, 15000);
        //接口调用
        String rsp = client.call("openpush.openapi.query_app_tags", new HashMap<String, Object>(), String.class);

        //打印响应
        //响应样例：{"request_id":"1373593606","tags":[{"应用激活状态":["已激活"]},{"name":["1","hkdajc","hshshsh","jkg\n","rt","tyu","yu"]},{"age":["1","25","45","56","6644","gh","容易"]}]}
        System.err.println("查询应用标签接口响应：" + rsp);

    }

    /**
     * 删除用户标签
     *
     * @param client
     * @throws NSPException
     */
    public static void delete_user_tag(NSPClient client)
            throws NSPException {
        //deviceToken，必选
        //由客户端获取， 32 字节长度。手机上安装了push应用后，会到push服务器申请token，申请到的token会上报给应用服务器
        String token = "00000000000000000000000000000000";

        //标签类型，必选
        String tag_key = "age";

        //构造请求
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("token", token);
        hashMap.put("tag_key", tag_key);

        //设置http超时时间
        client.setTimeout(10000, 15000);
        //接口调用
        String rsp = client.call("openpush.openapi.delete_user_tag", hashMap, String.class);

        //打印响应
        //响应样例：{"result_code":"0","result_desc":"success","request_id":"1380075041"}
        System.err.println("删除用户标签接口响应：" + rsp);
    }

    /**
     * 查询用户标签
     *
     * @param client
     * @throws NSPException
     */
    public static void query_user_tag(NSPClient client)
            throws NSPException {
        //deviceToken，必选
        //由客户端获取， 32 字节长度。手机上安装了push应用后，会到push服务器申请token，申请到的token会上报给应用服务器
        String token = "00000000000000000000000000000000";

        //构造请求
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("token", token);

        //设置http超时时间
        client.setTimeout(10000, 15000);
        //接口调用
        String rsp = client.call("openpush.openapi.query_user_tag", hashMap, String.class);

        //打印响应
        //响应样例：{"request_id":"1380075076","tags":[{"应用激活状态":"已激活"},{"location":"NanJing"},{"name":"yewei"}]}
        System.err.println("查询用户标签接口响应：" + rsp);
    }

    /**
     * 调用查询查询消息发送结果接口
     *
     * @param client
     * @throws NSPException
     */
    public static void query_msg_result(NSPClient client)
            throws NSPException {
        //开发者调用sengle_send和batch_send接口时返回的requestID字段值
        String request_id = "";

        //用户标识
        //如果携带该字段，则表示查询request_id中的token对应的消息结果；如果不携带该字段，则查询request_id对应的所有token的消息结果
        String token = "";

        //构造请求
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("request_id", request_id);
        hashMap.put("token", token);

        //设置http超时时间
        client.setTimeout(10000, 15000);
        //接口调用
        String rsp = client.call("openpush.openapi.query_msg_result", hashMap, String.class);

        //打印响应
        //响应样例：{"result":[{"status":0,"token":"00000000000000000000000000000000"}],"request_id":"123456"}
        System.err.println("查询查询消息发送结果接口：" + rsp);

    }

    /**
     * @param client
     * @throws NSPException
     */
    public static void get_token_by_date(NSPClient client)
            throws NSPException {
        //构造请求
        //时间最早不能超过2014-01-01，最晚为当天的前一天
        String date = "2014-07-16";

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("date", date);

        //设置http超时时间
        client.setTimeout(10000, 15000);
        String rsp = client.call("openpush.openapi.get_token_by_date", hashMap, String.class);

        //打印响应
        //响应样例：{"request_id":"1399551889637254","result_code":"0","tokenFile_url":"http://huaweipushtoken.dbankcloud.com/0001011454000001/2014-05-07.zip?ts=1399595089&key=e7717b69","unzip_password":"9cd86e68-d4ad-42"}
        System.err.println("查询查询消息发送结果接口：" + rsp);

    }
}

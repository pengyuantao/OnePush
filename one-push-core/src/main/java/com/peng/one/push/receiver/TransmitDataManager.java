package com.peng.one.push.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

/**
 * Forwarding message management class
 * Created by pyt on 2017/5/12.
 */

public class TransmitDataManager {

    //The inside of the Intent of push message tag
    private static final String INTENT_DATA_PUSH = "one_push_data";

    /**
     * Send push data (through radio form to forward)
     *
     * @param context
     * @param action
     * @param data
     */
    public static void sendPushData(Context context, String action, Parcelable data) {
        Intent intent = new Intent(action);
        intent.putExtra(INTENT_DATA_PUSH, data);
        context.sendBroadcast(intent);
    }

    /**
     * Analytical push message data from the Intent
     *
     * @param intent
     * @param <T>
     * @return
     */
    public static <T extends Parcelable> T parsePushData(Intent intent) {
        return intent.getParcelableExtra(INTENT_DATA_PUSH);
    }

}

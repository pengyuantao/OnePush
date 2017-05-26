package com.peng.one.push.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.peng.one.push.core.OnePushCode;

/**
 * push command entity class
 * Created by pyt on 2017/5/10.
 */
public class OnePushCommand implements Parcelable,OnePushCode {

    private int type;

    private int resultCode;

    private String token;

    private String extraMsg;

    private String error;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExtraMsg() {
        return extraMsg;
    }

    public void setExtraMsg(String extraMsg) {
        this.extraMsg = extraMsg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "OnePushCommand{" +
                "type=" + getTypeText(type) +
                ", resultCode=" + resultCode +
                ", token='" + token + '\'' +
                ", extraMsg='" + extraMsg + '\'' +
                ", error='" + error + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeInt(this.resultCode);
        dest.writeString(this.token);
        dest.writeString(this.extraMsg);
        dest.writeString(this.error);
    }

    public OnePushCommand() {
    }

    public OnePushCommand(int type, int resultCode, String token, String extraMsg, String error) {
        this.type = type;
        this.resultCode = resultCode;
        this.token = token;
        this.extraMsg = extraMsg;
        this.error = error;
    }

    protected OnePushCommand(Parcel in) {
        this.type = in.readInt();
        this.resultCode = in.readInt();
        this.token = in.readString();
        this.extraMsg = in.readString();
        this.error = in.readString();
    }

    public static final Creator<OnePushCommand> CREATOR = new Creator<OnePushCommand>() {
        @Override
        public OnePushCommand createFromParcel(Parcel source) {
            return new OnePushCommand(source);
        }

        @Override
        public OnePushCommand[] newArray(int size) {
            return new OnePushCommand[size];
        }
    };

    private String getTypeText(int type) {
        String typeText = null;
        switch (type) {
            case TYPE_REGISTER:
                typeText = "TYPE_REGISTER";
                break;

            case TYPE_UNREGISTER:
                typeText = "TYPE_UNREGISTER";
                break;

            case TYPE_ADD_TAG:
                typeText = "TYPE_ADD_TAG";
                break;
            case TYPE_DEL_TAG:
                typeText = "TYPE_DEL_TAG";
                break;

            case TYPE_BIND_ALIAS:
                typeText = "TYPE_BIND_ALIAS";
                break;

            case TYPE_UNBIND_ALIAS:
                typeText = "TYPE_UNBIND_ALIAS";
                break;
        }
        return typeText;

    }

}

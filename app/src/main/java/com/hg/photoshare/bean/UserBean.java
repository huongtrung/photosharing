package com.hg.photoshare.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nart on 25/10/2016.
 */
public class UserBean implements Parcelable {
    @SerializedName("_id")
    public String id;
    @SerializedName("username")
    public String username;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("is_following")
    public Boolean isFollowing;


    protected UserBean(Parcel in) {
        id = in.readString();
        username = in.readString();
        avatar = in.readString();
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel in) {
            return new UserBean(in);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(avatar);
    }
}

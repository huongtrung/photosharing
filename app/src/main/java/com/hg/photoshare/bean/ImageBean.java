package com.hg.photoshare.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nart on 25/10/2016.
 */
public class ImageBean implements Parcelable {
    @SerializedName("_id")
    @Expose
    public String id;
    @SerializedName("public_id")
    @Expose
    public String publicId;
    @SerializedName("version")
    @Expose
    public Integer version;
    @SerializedName("width")
    @Expose
    public Integer width;
    @SerializedName("height")
    @Expose
    public Integer height;
    @SerializedName("format")
    @Expose
    public String format;
    @SerializedName("resource_type")
    @Expose
    public String resourceType;
    @SerializedName("created_at")
    @Expose
    public long createdAt;
    @SerializedName("bytes")
    @Expose
    public Integer bytes;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("url")
    @Expose
    public String url;
    @SerializedName("secure_url")
    @Expose
    public String secureUrl;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("caption")
    @Expose
    public String caption;
    @SerializedName("location")
    @Expose
    public String location;
    @SerializedName("lat")
    @Expose
    public String lat;
    @SerializedName("long")
    @Expose
    public String _long;
    @SerializedName("hashtag")
    @Expose
    public List<String> hashtag = new ArrayList<>();
    @SerializedName("is_favourite")
    @Expose
    public Boolean isFavourite;

    protected ImageBean(Parcel in) {
        id = in.readString();
        publicId = in.readString();
        format = in.readString();
        resourceType = in.readString();
        createdAt = in.readLong();
        type = in.readString();
        url = in.readString();
        secureUrl = in.readString();
        userId = in.readString();
        caption = in.readString();
        location = in.readString();
        lat = in.readString();
        _long = in.readString();
        hashtag = in.createStringArrayList();
    }

    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel in) {
            return new ImageBean(in);
        }

        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(publicId);
        dest.writeString(format);
        dest.writeString(resourceType);
        dest.writeLong(createdAt);
        dest.writeString(type);
        dest.writeString(url);
        dest.writeString(secureUrl);
        dest.writeString(userId);
        dest.writeString(caption);
        dest.writeString(location);
        dest.writeString(lat);
        dest.writeString(_long);
        dest.writeStringList(hashtag);
    }
}

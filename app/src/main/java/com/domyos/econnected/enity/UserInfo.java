package com.domyos.econnected.enity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by HouWei on 16/8/12.
 */

public class UserInfo implements Serializable {

    private static final long serialVersionUID = -2702259886328890506L;
    private int uid;
    @SerializedName("nickName")
    private String nickname;
    @SerializedName("gender")
    private String sex;
    @SerializedName("birthday")
    private long birthday;
    @SerializedName("height")
    private int height;
    @SerializedName("weight")
    private int weight;
    @SerializedName("headImg")
    private String avatarUrl;
    @SerializedName("token")
    private String token;
    @SerializedName("picId")
    private int picId;
    public UserInfo(int uid, String token) {
        this.token = token;
        this.uid = uid;
    }

    public UserInfo(int uid, String nickname, String sex, long birthday, int height, int weight, String avatarUrl, String token,int picId) {
        this.uid = uid;
        this.nickname = nickname;
        this.sex = sex;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
        this.avatarUrl = avatarUrl;
        this.token = token;
        this.picId = picId;
    }

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}

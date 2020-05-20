package com.domyos.econnected.net.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HouWei on 16/8/17.
 */

public class LoginAndRegisterResult {
    @SerializedName("Exec")
    private boolean exec;
    @SerializedName("INFO")
    private String info;
    @SerializedName("ID")
    private int id;
    @SerializedName("age")
    private long age;
    @SerializedName("height")
    private int height;
    @SerializedName("weight")
    private int weight;
    @SerializedName("picId")
    private int picId;

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
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

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public boolean isExec() {
        return exec;
    }

    public void setExec(boolean exec) {
        this.exec = exec;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public class Register {
        private String username;
        private String password;

        public Register(String phone, String password) {
            this.username = phone;
            this.password = password;
        }

        public String getPhone() {
            return username;
        }

        public void setPhone(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}

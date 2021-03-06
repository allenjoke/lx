package com.domyos.econnected.net.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HouWei on 16/8/17.
 */
public class ResponseDataEntity<T> {
    private boolean exec;
    private String info;
    @SerializedName("age")
    private int age;
    @SerializedName("height")
    private float height;
    @SerializedName("weight")
    private float weight;
    @SerializedName("picId")
    private int picId;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
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

    private int id;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

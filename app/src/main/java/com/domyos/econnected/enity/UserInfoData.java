package com.domyos.econnected.enity;


public class UserInfoData{

    private String username;
    private int id;
    private int age;
    private float height;
    private float weight;
    private int picId;

    public UserInfoData() {

    }

    public UserInfoData(String username, int id, int age, float height, float weight, int picId) {
        this.username = username;
        this.id = id;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.picId = picId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "UserInfoData{" +
                "username='" + username + '\'' +
                ", id='" + id + '\'' +
                ", age='" + age + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", picId='" + picId + '\'' +
                '}';
    }
}

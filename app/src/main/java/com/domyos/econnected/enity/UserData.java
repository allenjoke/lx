package com.domyos.econnected.enity;

public class UserData {
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

    public StatInfo getData() {
        return data;
    }

    public void setData(StatInfo data) {
        this.data = data;
    }

    private boolean exec;
    private String info;
    private StatInfo data;
}

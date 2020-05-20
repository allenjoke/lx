package com.domyos.econnected.net.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HouWei on 16/8/17.
 */

public class RegisterResult {
    @SerializedName("Exec")
    private boolean exec;
    @SerializedName("INFO")
    private String info;

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
}

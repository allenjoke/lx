package com.domyos.econnected.net.request;


import com.google.gson.annotations.SerializedName;

/**
 * Created by HouWei on 16/8/17.
 * 用于制作post请求时 body-json的实体类
 */
public interface RequestBodyEntity {
    public class ChangeScreenShot{
        private long sid;
        private int uid;
        private String screenshot;

        public ChangeScreenShot(long sid, int uid, String screenShot) {
            this.sid = sid;
            this.uid = uid;
            this.screenshot = screenShot;
        }

        public long getSid() {
            return sid;
        }

        public ChangeScreenShot setSid(long sid) {
            this.sid = sid;
            return this;
        }

        public int getUid() {
            return uid;
        }

        public ChangeScreenShot setUid(int uid) {
            this.uid = uid;
            return this;
        }

        public String getScreenshot() {
            return screenshot;
        }

        public ChangeScreenShot setScreenshot(String screenshot) {
            this.screenshot = screenshot;
            return this;
        }
    }

    public class ResetPassword {
        private String phone;
        @SerializedName("setPassword")
        private String password;
        private String smscode;

        public ResetPassword(String phone, String password, String smscode) {
            this.phone = phone;
            this.password = password;
            this.smscode = smscode;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSmscode() {
            return smscode;
        }

        public void setSmscode(String smscode) {
            this.smscode = smscode;
        }
    }

    public class ChangePassword {
        private int uid;
        private String oldPwd;
        private String newPwd;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getOldPwd() {
            return oldPwd;
        }

        public void setOldPwd(String oldPwd) {
            this.oldPwd = oldPwd;
        }

        public String getNewPwd() {
            return newPwd;
        }

        public void setNewPwd(String newPwd) {
            this.newPwd = newPwd;
        }

        public ChangePassword(int uid, String oldPwd, String newPwd) {

            this.uid = uid;
            this.oldPwd = oldPwd;
            this.newPwd = newPwd;
        }
    }

    public class Logout {
        private int uid;

        public Logout(int uid) {
            this.uid = uid;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }

    public class Login {
        private String username;
        private String password;

        public Login(String username, String password) {
            this.username = username;
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

    public class CheckCode {
        private String phone;
        private String smscode;

        public CheckCode(String phone, String smscode) {
            this.phone = phone;
            this.smscode = smscode;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getSmscode() {
            return smscode;
        }

        public void setSmscode(String smscode) {
            this.smscode = smscode;
        }
    }
}

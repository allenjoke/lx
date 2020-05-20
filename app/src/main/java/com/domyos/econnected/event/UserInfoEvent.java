package com.domyos.econnected.event;

/**
 * Created by HouWei on 16/8/12.
 */

public class UserInfoEvent {
    public int action;
    public String message;

    public UserInfoEvent(int action) {
        this.action = action;
    }

    public UserInfoEvent(int action, String message) {
        this.action = action;
        this.message = message;
    }

    /**
     * 从服务器同步用户信息成功
     */
    public static final int ACTION_REQUEST_REFRESH_USER_INFO_SUCCESS = 1;
    /**
     * 从服务器同步用户信息失败
     */
    public static final int ACTION_REQUEST_REFRESH_USER_INFO_FAILED = 2;
    /**
     * 向服务器发送修改用户信息请求成功
     */
    public static final int ACTION_REQUEST_EDIT_USER_INFO_SUCCESS = 3;

    /**
     * 向服务器发送修改用户信息请求失败
     */
    public static final int ACTION_REQUEST_EDIT_USER_INFO_FAILED = 4;


    /**
     * 登录成功
     */
    public static final int ACTION_LOGIN_SUCCESS = 5;
    /**
     * 登录失败
     */
    public static final int ACTION_LOGIN_FAILED = 6;

    /**
     * 修改密码成功
     */
    public static final int ACTION_CHANGE_PASSWORD_SUCCESS = 7;
    /**
     * 修改密码失败
     */
    public static final int ACTION_CHANGE_PASSWORD_FAILED = 8;

    /**
     * 登出成功
     */
    public static final int ACTION_LOGOUT_SUCCESS = 9;

    /**
     * 获取注册验证码成功
     */
    public static final int ACTION_GET_REGISTER_CHECK_CODE_SUCCESS = 10;
    /**
     * 获取注册验证码失败
     */
    public static final int ACTION_GET_REGISTER_CHECK_CODE_FAILED = 11;
    /**
     * 注册成功
     */
    public static final int ACTION_REGISTER_SUCCESS = 12;
    /**
     * 注册失败
     */
    public static final int ACTION_REGISTER_FAILED = 13;

    /**
     * 获取重置密码验证码成功
     */
    public static final int ACTION_GET_RESET_PASSWORD_CHECK_CODE_SUCCESS = 14;
    /**
     * 获取重置密码验证码失败
     */
    public static final int ACTION_GET_RESET_PASSWORD_CHECK_CODE_FAILED = 15;
    /**
     * 重置密码成功
     */
    public static final int ACTION_RESET_PASSWORD_SUCCESS  = 16;
    /**
     * 充值密码失败
     */
    public static final int ACTION_RESET_PASSWORD_FAILED = 17;
    /**
     * 用户信息 返回主页
     */
    public static final int ACTION_USER_BACK = 18;

}

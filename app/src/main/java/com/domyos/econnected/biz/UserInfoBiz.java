package com.domyos.econnected.biz;

import com.domyos.econnected.biz.listener.LogoutCallBack;
import com.domyos.econnected.biz.listener.RegisterCallBack;
import com.domyos.econnected.biz.listener.ResetPsCallBack;
import com.domyos.econnected.biz.listener.UpProfileCallBack;
import com.domyos.econnected.biz.listener.UserLoginCallBack;
import com.domyos.econnected.enity.UserInfo;
import com.domyos.econnected.enity.UserInfoData;

public interface UserInfoBiz {
    /**
     * 获取当前的登录状态,会静默调用getUserInfo()
     *
     * @return
     */
    boolean isLogin();
    /**
     * 获取当前登录的用户信息(本地)  未登录返回null
     *
     * @return
     */
    UserInfo getUserInfo();

    /**
     * 获取token 用于网络访问
     * 已登录状态下会返回用户token,未登录状态会返回公用token
     *
     * @return
     */
    String getToken();

    //请求登录
    void login(String name, String ps, UserLoginCallBack loginCallBack);
    //注册
    void register(String name, String ps, RegisterCallBack registerCallBack);
    //密码重置，忘记密码
    void resetPs(String name , String ps, ResetPsCallBack resetPsCallBack);
    //注销 登出
    void logout(LogoutCallBack logoutCallBack);
    //上传user
    void UpProfile(UserInfoData userInfoData, UpProfileCallBack upProfileCallBack);

}

package com.domyos.econnected.ui.fragment.user;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.domyos.econnected.R;
import com.domyos.econnected.biz.UserInfoBiz;
import com.domyos.econnected.biz.iml.UserBizImpl;
import com.domyos.econnected.biz.listener.LogoutCallBack;
import com.domyos.econnected.event.UIEvent;
import com.domyos.econnected.event.UserInfoEvent;
import com.domyos.econnected.ui.BaseFragment;
import com.domyos.econnected.utils.SharedPreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class LogoutFragment extends BaseFragment implements LogoutCallBack {
    
    
    @BindView(R.id.userIcon_logout)
    CircleImageView userIcon_logout;
    @BindView(R.id.userNameText_logout)
    TextView userNameText_logout;
    @BindView(R.id.btn_logout)
    Button btn_logout;
    boolean isLogin;
    UserInfoBiz userInfoBiz = new UserBizImpl();
    
    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_logout;
    }
    
    @Override
    protected void initSomething() {
        
        // 偏好设置，通过判断是否已经登录来决定是否打开登录界面
        SharedPreferences sp = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        
        isLogin = SharedPreferenceUtils.get(getContext(), "isLogin", false);
        String name = SharedPreferenceUtils.get(getContext(), "name", "");
        if (isLogin) {
            userNameText_logout.setText(name);
        }
    }
    
    @OnClick({R.id.btn_logout, R.id.homePage_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_logout:
                logout();
                break;
            case R.id.homePage_back:
                removeFragment();
                break;
            
        }
    }
    
    private void logout() {
        if (isLogin) {
            userInfoBiz.logout(this);
        }
    }
    
    public void removeFragment() {
        getFragmentManager().popBackStack();
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changePage(UIEvent event) {
        switch (event.action) {
            case UIEvent.ACTION_LOGIN_FRAGMENT:
                // 偏好设置，当用户登录后，下次进入不需要再次登录
                userNameText_logout.setText(event.eventInfo.getType());
                break;
        }
    }
    
    @Override
    public void logoutSuccess(boolean logout, String msg) {
        userNameText_logout.setText("");
        EventBus.getDefault().post(new UserInfoEvent(UserInfoEvent.ACTION_LOGOUT_SUCCESS));
        removeFragment();
    }
    
    @Override
    public void logoutFailed(String msg) {
    
    }
}

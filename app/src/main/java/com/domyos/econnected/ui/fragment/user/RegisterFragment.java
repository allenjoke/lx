package com.domyos.econnected.ui.fragment.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.domyos.econnected.R;
import com.domyos.econnected.YDApplication;
import com.domyos.econnected.biz.UserInfoBiz;
import com.domyos.econnected.biz.iml.UserBizImpl;
import com.domyos.econnected.biz.listener.RegisterCallBack;
import com.domyos.econnected.event.UIEvent;
import com.domyos.econnected.ui.BaseFragment;
import com.domyos.econnected.utils.SharedPreferenceUtils;
import com.domyos.econnected.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends BaseFragment implements RegisterCallBack {
    
    @BindView(R.id.user_name)
    EditText user_name_edit;
    @BindView(R.id.user_ps)
    EditText user_ps_edit;
    @BindView(R.id.user_confirm_ps)
    EditText user_confirm_ps_edit;
    @BindView(R.id.user_confirm)
    Button user_confirm_ps_btn;
    @BindView(R.id.title_text)
    TextView title_text;
    
    private UserInfoBiz userInfoBiz = new UserBizImpl();
    private String name, psd, confirm_psd;
    
    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_register;
    }
    
    @Override
    protected void initSomething() {
        title_text.setVisibility(View.VISIBLE);
        title_text.setText(getString(R.string.register));
    }
    
    @OnClick({R.id.homePage_back, R.id.user_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homePage_back:
                removeFragment();
                break;
            case R.id.user_confirm:
                register();
                break;
        }
    }
    
    private void register() {
        name = user_name_edit.getText().toString().trim();
        psd = user_ps_edit.getText().toString().trim();
        confirm_psd = user_confirm_ps_edit.getText().toString().trim();
        
        if (null == name || "".equals(name)) {
            ToastUtil.toast(getActivity(), "Please enter valid field");
            return;
        }
        if (null == psd || "".equals(psd)) {
            ToastUtil.toast(getActivity(), "Please enter valid field");
            return;
        }
        if (null == confirm_psd || "".equals(confirm_psd)) {
            ToastUtil.toast(getActivity(), "Please enter valid field");
            return;
        }
        if (!psd.equals(confirm_psd)) {
            ToastUtil.toast(getActivity(), "Please enter the same password");
            return;
        }
        userInfoBiz.register(name, psd, this);
        
    }
    
    @Override
    public void registerSuccess(boolean register, String msg) {
        YDApplication.getInstance().put("edit",0);
       /* SharedPreferenceUtils.put(getActivity(),"name",name);
        SharedPreferenceUtils.put(getActivity(),"psd",psd);
        EventBus.getDefault().post(new UIEvent(UIEvent.ACTION_AGE_FRAGMENT));
        */
        EventBus.getDefault().post(new UIEvent(UIEvent.ACTION_SHOW_LOGINFRAGMENT));
        removeFragment();
    }
    
    @Override
    public void registerFailed(String msg) {
        showDialog(msg, false);
    }
    
    
    public void goLoginPage(boolean register, String msg) {
        user_ps_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        user_confirm_ps_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        showDialog(msg, register);
    }
    
    
    public void showDialog(String msg, boolean bo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.create();
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (bo) {
                    removeFragment();
                }
                
            }
        });
        builder.show();
    }
    
    public void removeFragment() {
        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        //getFragmentManager().popBackStack();
    }
    
}

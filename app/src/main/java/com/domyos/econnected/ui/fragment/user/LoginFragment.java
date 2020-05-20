package com.domyos.econnected.ui.fragment.user;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.domyos.econnected.R;
import com.domyos.econnected.YDApplication;
import com.domyos.econnected.biz.UserInfoBiz;
import com.domyos.econnected.biz.iml.UserBizImpl;
import com.domyos.econnected.biz.listener.ResetPsCallBack;
import com.domyos.econnected.biz.listener.UserLoginCallBack;
import com.domyos.econnected.enity.UIEventInfo;
import com.domyos.econnected.enity.UserInfoData;
import com.domyos.econnected.event.UIEvent;
import com.domyos.econnected.ui.BaseFragment;
import com.domyos.econnected.utils.LogUtils;
import com.domyos.econnected.utils.SharedPreferenceUtils;
import com.domyos.econnected.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment implements UserLoginCallBack, ResetPsCallBack {
    @BindView(R.id.account_number)
    EditText account_number;
    @BindView(R.id.psd)
    EditText psd_edit;
    @BindView(R.id.login)
    Button loginBtn;
    @BindView(R.id.register)
    TextView register_txt;
    @BindView(R.id.forget_psd)
    TextView forget_psd_txt;
    @BindView(R.id.show_psd)
    ImageView show_psd;
    @BindView(R.id.register_layout)
    FrameLayout register_layout;
    @BindView(R.id.getPsd_layout)
    FrameLayout getPsd_layout;
    @BindView(R.id.title_text)
    TextView title_text;
    private UserInfoBiz userInfoBiz = new UserBizImpl();
    private static final String TAG = "LoginFragment";

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initSomething() {
        title_text.setVisibility(View.VISIBLE);
        title_text.setText(R.string.login);
        psd_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());

    }

    private boolean isShow = false;

    @OnClick({R.id.login, R.id.register, R.id.forget_psd, R.id.show_psd, R.id.homePage_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homePage_back:
                account_number.setText("");
                psd_edit.setText("");
                getFragmentManager().popBackStack();
                break;
            case R.id.login:
                login();
                break;
            case R.id.register:
                register();
                break;
            case R.id.forget_psd:
                resetPsd();
                // getPsd();
                break;
            case R.id.show_psd:
                if (isShow) {
                    show_psd.setImageDrawable(getActivity().getDrawable(R.drawable.psd_sel_img));
                    psd_edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    show_psd.setImageDrawable(getActivity().getDrawable(R.drawable.psd_nor_img));
                    psd_edit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                isShow = !isShow;
                break;
        }
    }

    private void getPsd() {
        ToastUtil.toast(getActivity(), "to do");
        /*getPsd_layout.setVisibility(View.VISIBLE);
        getChildFragmentManager().beginTransaction()
                .add(R.id.getPsd_layout, new GetPassWordFragment())
                .commitAllowingStateLoss();*/
    }

    private void register() {
        EventBus.getDefault().post(new UIEvent(UIEvent.ACTION_REGISTER_FRAGMENT));
        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();


    }

    private void login() {
        String account = account_number.getText().toString();
        String psd = psd_edit.getText().toString();

        if (null == account || "".equals(account)) {
            ToastUtil.toast(getActivity(), "Please enter valid field");
            return;
        }
        if (null == psd || "".equals(psd)) {
            ToastUtil.toast(getActivity(), "Please enter valid field");
            return;
        }
        userInfoBiz.login(account, psd, this);
    }

    private void resetPsd() {
        userInfoBiz.resetPs("test01", "12345", this);

    }

    @Override
    public void loginSuccess(boolean login, String msg, UserInfoData userInfoData) {
        if (login) {

            Log.e("LoginFragment","----userInfoData-----"+userInfoData.toString());
            SharedPreferenceUtils.put(getContext(), "isLogin", true);
            SharedPreferenceUtils.put(getContext(), "name", account_number.getText().toString());
            SharedPreferenceUtils.put(getActivity(), "user_id", userInfoData.getId());
            SharedPreferenceUtils.put(getActivity(), "height", (int)userInfoData.getHeight());
            SharedPreferenceUtils.put(getActivity(), "weight", (int)userInfoData.getWeight());
            SharedPreferenceUtils.put(getActivity(), "picId", userInfoData.getPicId());
            YDApplication.getInstance().put("picId",userInfoData.getPicId());
            SharedPreferenceUtils.put(getActivity(), "age", userInfoData.getAge());

            UIEventInfo uiEventInfo = new UIEventInfo();
            uiEventInfo.setType(account_number.getText().toString());
            EventBus.getDefault().post(new UIEvent(UIEvent.ACTION_LOGIN_FRAGMENT, uiEventInfo));
            getFragmentManager().popBackStack();
        } else {
            showDialog(msg);
        }
    }

    @Override
    public void loginFailed(String msg) {
        LogUtils.e("sh_1234" + TAG + "--------loginFailed-----");
        showDialog(msg);
    }

    public void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.create();
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    @Override
    public void resetPsSuccess(boolean reset, String msg) {
        showDialog("resetSuccess ,login again");

    }

    @Override
    public void resetPsFailed(String msg) {
        showDialog(msg);
    }

}

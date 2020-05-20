package com.domyos.econnected.ui.fragment.user;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.domyos.econnected.R;
import com.domyos.econnected.YDApplication;
import com.domyos.econnected.biz.UserInfoBiz;
import com.domyos.econnected.biz.iml.UserBizImpl;
import com.domyos.econnected.biz.listener.LogoutCallBack;
import com.domyos.econnected.constant.UserPicConstant;
import com.domyos.econnected.event.UserEditEvent;
import com.domyos.econnected.event.UserInfoEvent;
import com.domyos.econnected.ui.BaseFragment;
import com.domyos.econnected.ui.fragment.MonthFragment;
import com.domyos.econnected.ui.fragment.WeekFragment;
import com.domyos.econnected.ui.fragment.YearFragment;
import com.domyos.econnected.utils.SharedPreferenceUtils;
import com.domyos.econnected.utils.ToastUtil;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;


public class UserInfoFragment extends BaseFragment implements LogoutCallBack {

    @BindView(R.id.userInfo_logout)
    TextView userInfo_logout;
    @BindView(R.id.userInfo_name)
    TextView userInfo_name;
    @BindView(R.id.userInfo_age)
    TextView userInfo_age;
    @BindView(R.id.userInfo_height)
    TextView userInfo_height;
    @BindView(R.id.userInfo_weight)
    TextView userInfo_weight;
    @BindView(R.id.userInfo_pic)
    ImageView userInfo_pic;
    @BindView(R.id.userInfo_ViewPager)
    ViewPager userInfo_ViewPager;
    @BindView(R.id.userInfo_TabLayout)
    TabLayout userInfo_TabLayout;
    private SetBirthdayFragment setBirthdayFragment = new SetBirthdayFragment();
    private SetHeightFragment setHeightFragment = new SetHeightFragment();
    private SetWeightFragment setWeightFragment = new SetWeightFragment();
    private UserClothesFragment userClothesFragment = new UserClothesFragment();
    private UserInfoBiz userInfoBiz = new UserBizImpl();

    private String[] tabs;
    private int picId;
    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_user_info;
    }

    @Override
    protected void initSomething() {

        if (isVisible()) {
            if (getActivity() != null) {
                tabs = new String[]{getResources().getString(R.string.userinfo_sport_week), getResources().getString(R.string.userinfo_sport_month), getResources().getString(R.string.userinfo_sport_year)};

            String name = SharedPreferenceUtils.get(getActivity(), "name", "");
            String height = SharedPreferenceUtils.get(getActivity(), "height", 0)+"";
            String weight = SharedPreferenceUtils.get(getActivity(), "weight", 0)+"";
            picId = SharedPreferenceUtils.get(getActivity(), "picId", 0);
            String age = SharedPreferenceUtils.get(getActivity(), "age", 0)+"";
            userInfo_name.setText(name);
            userInfo_age.setText(age+"");
            userInfo_height.setText(height+"  cm");
            userInfo_weight.setText(weight+"  kg");
            setUserPic(picId);
            initPager();
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            picId = SharedPreferenceUtils.get(getActivity(), "picId", 0);
            setUserPic(picId);
        }
    }

    private void setUserPic(int picId) {
        if (picId == UserPicConstant.TYPE_01_01) {
            userInfo_pic.setImageDrawable(getActivity().getDrawable(R.drawable.pic_01_01_big));
        }
        if (picId==UserPicConstant.TYPE_01_02) {
            userInfo_pic.setImageDrawable(getActivity().getDrawable(R.drawable.pic_01_02_big));
        }
        if (picId==UserPicConstant.TYPE_01_03) {
            userInfo_pic.setImageDrawable(getActivity().getDrawable(R.drawable.pic_01_03_big));
        }
        if (picId==UserPicConstant.TYPE_02_01) {
            userInfo_pic.setImageDrawable(getActivity().getDrawable(R.drawable.pic_02_01_big));
        }
        if (picId==UserPicConstant.TYPE_02_02) {
            userInfo_pic.setImageDrawable(getActivity().getDrawable(R.drawable.pic_02_02_big));
        }
        if (picId==UserPicConstant.TYPE_02_03) {
            userInfo_pic.setImageDrawable(getActivity().getDrawable(R.drawable.pic_02_03_big));
        }
        if (picId==UserPicConstant.TYPE_03_01) {
            userInfo_pic.setImageDrawable(getActivity().getDrawable(R.drawable.pic_03_01_big));
        }
        if (picId==UserPicConstant.TYPE_03_02) {
            userInfo_pic.setImageDrawable(getActivity().getDrawable(R.drawable.pic_03_02_big));
        }
        if (picId==UserPicConstant.TYPE_03_03) {
            userInfo_pic.setImageDrawable(getActivity().getDrawable(R.drawable.pic_03_03_big));
        }

    }

    private void initPager() {

        userInfo_ViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        userInfo_ViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = new Fragment();
                if (fragment != null) {
                    switch (position) {
                        case 0:
                            fragment = new WeekFragment();
                            break;
                        case 1:
                            fragment = new MonthFragment();
                            break;
                        case 2:
                            fragment = new YearFragment();
                            break;
                    }
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        userInfo_TabLayout.setupWithViewPager(userInfo_ViewPager);
        userInfo_ViewPager.setCurrentItem(0);
        userInfo_TabLayout.getTabAt(0).setCustomView(getTabView(0));
        userInfo_TabLayout.getTabAt(1).setCustomView(getTabView(1));
        userInfo_TabLayout.getTabAt(2).setCustomView(getTabView(2));

        initTab();
    }

    private void initTab() {

        userInfo_TabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView textView = view.findViewById(R.id.txt_tab_task);
                textView.setTextColor(Color.parseColor("#000000"));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView textView = view.findViewById(R.id.txt_tab_task);
                textView.setTextColor(Color.parseColor("#000000"));

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    private View getTabView(int position) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.tab_task_item, null);
        TextView textView = view.findViewById(R.id.txt_tab_task);
        textView.setText(tabs[position]);
        if (position == 0) {
            textView.setTextColor(Color.parseColor("#000000"));
        }
        return view;
    }



    @OnClick({R.id.userInfo_back, R.id.userInfo_logout,R.id.userInfo_age,R.id.userInfo_height,R.id.userInfo_weight,R.id.userInfo_pic})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userInfo_logout:
                userInfoBiz.logout(this);
                break;

            case R.id.userInfo_back:
                removeFragment();
                break;

            case R.id.userInfo_age:
                showAgeFragment();
                break;
            case R.id.userInfo_height:
                showHeightFragment();
                break;
            case R.id.userInfo_weight:
                showWeightFragment();
                break;
            case R.id.userInfo_pic:
                showClothesFragment();
                break;
        }
    }

    private void showClothesFragment() {
      //  EventBus.getDefault().post(new UserEditEvent(UserEditEvent.ACTION_EDIT_PIC));
        YDApplication.getInstance().put("edit",1);
        getChildFragmentManager().beginTransaction().replace(R.id.setPage_Fragment, userClothesFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private void showWeightFragment() {
       // EventBus.getDefault().post(new UserEditEvent(UserEditEvent.ACTION_EDIT_WEIGHT));
        YDApplication.getInstance().put("edit",1);

        getChildFragmentManager().beginTransaction().replace(R.id.setPage_Fragment, setWeightFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    private void showHeightFragment() {
        //EventBus.getDefault().post(new UserEditEvent(UserEditEvent.ACTION_EDIT_HEIGHT));
        YDApplication.getInstance().put("edit",1);

        getChildFragmentManager().beginTransaction().replace(R.id.setPage_Fragment, setHeightFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();

    }

    private void showAgeFragment() {
       // EventBus.getDefault().post(new UserEditEvent(UserEditEvent.ACTION_EDIT_AGE));
        YDApplication.getInstance().put("edit",1);
        getChildFragmentManager().beginTransaction().replace(R.id.setPage_Fragment, new SetBirthdayFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    @Override
    public void logoutSuccess(boolean logout, String msg) {
        ToastUtil.toast(getActivity(), "Logout Success");
        removeFragment();
        EventBus.getDefault().post(new UserInfoEvent(UserInfoEvent.ACTION_LOGOUT_SUCCESS));

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getChangeData(UserEditEvent event){
        switch (event.action){

            case UserEditEvent.ACTION_AGE_BACK:
                String ageN = SharedPreferenceUtils.get(getActivity(), "age", 0)+"";
                userInfo_age.setText(ageN);
                break;
            case UserEditEvent.ACTION_HEIGHT_BACK:
                String heightN = SharedPreferenceUtils.get(getActivity(), "height", 0)+"";
                userInfo_height.setText(heightN);
                break;
            case UserEditEvent.ACTION_WEIGHT_BACK:
                String weight = SharedPreferenceUtils.get(getActivity(), "weight", 0)+"";
                userInfo_weight.setText(weight);
                break;
            case UserEditEvent.ACTION_PIC_BACK:
                int picIdN =  SharedPreferenceUtils.get(getActivity(), "picId", 0);
                setUserPic(picIdN);
                break;


        }
    }

    @Override
    public void logoutFailed(String msg) {

    }

    public void removeFragment() {

        EventBus.getDefault().post(new UserInfoEvent(UserInfoEvent.ACTION_USER_BACK,picId+""));
        getFragmentManager().popBackStack();
    }


}

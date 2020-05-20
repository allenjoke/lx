package com.domyos.econnected.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.domyos.econnected.R;
import com.domyos.econnected.event.CommonEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
public abstract class BaseFragment extends Fragment {


    public static final int NONE_ANIMATION_ID = -1;

    protected View rootView;
    protected int mLayoutId;
    Unbinder unbind;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mLayoutId = getRootViewLayoutId();
        rootView = inflater.inflate(mLayoutId, container, false);
        unbind = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        unbind.unbind();
        super.onDestroyView();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initSomething();
    }


    /**
     * 返回布局文件id
     *
     * @return
     */
    protected abstract int getRootViewLayoutId();

    /**
     * 一些界面的初始化操作
     */
    protected abstract void initSomething();


    /**
     * 所有的界面都接受CommonEvent
     * UI线程执行
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonEventMain(CommonEvent event) {

    }

    /**
     * 所有的界面都接受CommonEvent
     * 后台线程执行
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onCommonEventBackground(CommonEvent event) {

    }

    /**
     * 设置进入动画
     *
     * @return
     */
    public int getEnterAnimationResource() {
        return R.anim.slide_in_from_left;
    }

    /**
     * 设置退出动画
     *
     * @return
     */
    public int getOutAnimationResouce() {
        return R.anim.slide_out_to_right;
    }

    /**
     * if you need add animation listener for the fragment
     * please use this method
     */
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //todo 暂时不显示动画
        if (false) {
            return null;
        }

        Animation anim = null;
        if (enter) {
            if (getEnterAnimationResource() == NONE_ANIMATION_ID) {
                return null;
            }
            try {
                anim = AnimationUtils.loadAnimation(getActivity(),
                        getEnterAnimationResource());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (getOutAnimationResouce() == NONE_ANIMATION_ID) {
                return null;
            }
            try {
                anim = AnimationUtils.loadAnimation(getActivity(),
                        getOutAnimationResouce());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return anim;
    }

}

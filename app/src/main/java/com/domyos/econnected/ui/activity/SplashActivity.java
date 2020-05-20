package com.domyos.econnected.ui.activity;

import android.content.Intent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.domyos.econnected.ui.BaseActivity;
import com.domyos.econnected.R;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {

    /**
     * 闪屏页
     */
    @BindView(R.id.rl_splash)
    RelativeLayout rlSplash;


    @Override
    protected void initSomething() {
        startAnim();
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_splash;
    }

    /**
     * 启动动画
     */
    private void startAnim() {
        // 渐变动画,从完全透明到完全不透明
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        // 持续时间 2 秒
        alpha.setDuration(2000);
        // 动画结束后，保持动画状态
        alpha.setFillAfter(true);

        // 设置动画监听器
        alpha.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            // 动画结束时回调此方法
            @Override
            public void onAnimationEnd(Animation animation) {
                // 跳转到下一个页面
                Intent intent = new Intent(SplashActivity.this,ShowActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 启动动画
        rlSplash.startAnimation(alpha);
    }

}

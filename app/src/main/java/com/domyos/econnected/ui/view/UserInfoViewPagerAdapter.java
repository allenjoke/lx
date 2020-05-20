package com.domyos.econnected.ui.view;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.domyos.econnected.R;
import com.domyos.econnected.enity.UserInfoViewPagerData;


public class UserInfoViewPagerAdapter extends PagerAdapter {

    //上下文
    private Context mContext;
    //数据
    private UserInfoViewPagerData mData;

    public UserInfoViewPagerAdapter(Context mContext, UserInfoViewPagerData mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    /**
     * 从当前container中删除指定位置（position）的View
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // super.destroyItem(container,position,object); 这一句要删除，否则报错
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = View.inflate(mContext, R.layout.userinfo_viewpager_item, null);
        TextView userInfo_distance = view.findViewById(R.id.userInfo_distance);
        TextView userInfo_kcal = view.findViewById(R.id.userInfo_kcal);
        TextView userInfo_time = view.findViewById(R.id.userInfo_time);
        TextView userInfo_speed = view.findViewById(R.id.userInfo_speed);
        TextView userInfo_rpm = view.findViewById(R.id.userInfo_rpm);
        userInfo_distance.setText(mData.getDistance());
        userInfo_kcal.setText(mData.getKcal());
        userInfo_time.setText(mData.getTime());
        userInfo_speed.setText(mData.getSpeed());
        userInfo_rpm.setText(mData.getRpm());
        container.addView(view);
        return view;
    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}

package com.domyos.econnected.utils.datapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerAdapter extends PagerAdapter {

    private ImageView[][] mImageViews;

    private int[] mImageRes;

    public ViewPagerAdapter(ImageView[][] imageViews, int[] imageRes) {
        this.mImageViews = imageViews;
        this.mImageRes = imageRes;
    }


    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (mImageRes.length == 1) {
            return mImageViews[position / mImageRes.length % 2][0];
        } else {
            ((ViewPager) container).addView(mImageViews[position
                    / mImageRes.length % 2][position % mImageRes.length], 0);
        }
        return mImageViews[position / mImageRes.length % 2][position
                % mImageRes.length];
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (mImageRes.length == 1) {
            ((ViewPager) container).removeView(mImageViews[position
                    / mImageRes.length % 2][0]);
        } else {
            ((ViewPager) container).removeView(mImageViews[position
                    / mImageRes.length % 2][position % mImageRes.length]);
        }
    }
}

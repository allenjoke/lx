package com.domyos.econnected.ui.fragment.user;

import android.view.View;

import com.domyos.econnected.ui.BaseFragment;
import com.domyos.econnected.R;
import com.domyos.econnected.ui.fragment.running.SportEquipmentFragment;

import butterknife.OnClick;


public class GetPassWordFragment extends BaseFragment {

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_get_pass_word;
    }

    @Override
    protected void initSomething() {

    }


    @OnClick({R.id.img_register_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_register_close:
                getFragmentManager()
                        .beginTransaction()
                        .remove(this)
                        .commitAllowingStateLoss();
                break;

        }

    }
}

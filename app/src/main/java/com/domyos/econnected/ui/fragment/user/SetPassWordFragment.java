package com.domyos.econnected.ui.fragment.user;


import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.domyos.econnected.ui.BaseFragment;
import com.domyos.econnected.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetPassWordFragment extends BaseFragment {

    @BindView(R.id.img_register_title)
    TextView img_register_title;
    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_set_pass_word;
    }

    @Override
    protected void initSomething() {
        img_register_title.setText("找回密码");
    }
    @OnClick({R.id.img_register_close})
    public void onClick(View view ){
        switch(view.getId()){
            case R.id.img_register_close:
                getFragmentManager()
                        .beginTransaction()
                        .remove(this)
                        .commitAllowingStateLoss();
                break;

        }

    }
}

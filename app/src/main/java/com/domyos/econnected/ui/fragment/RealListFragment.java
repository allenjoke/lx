package com.domyos.econnected.ui.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.LongSparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.domyos.econnected.R;
import com.domyos.econnected.YDApplication;
import com.domyos.econnected.constant.UserPicConstant;
import com.domyos.econnected.event.EquipmentEvent;
import com.domyos.econnected.ui.BaseFragment;
import com.domyos.econnected.ui.fragment.running.SportEquipmentFragment;
import com.domyos.econnected.utils.SharedPreferenceUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class RealListFragment extends BaseFragment {

    @BindView(R.id.lasi)
    LinearLayout item_01;
    @BindView(R.id.yali)
    LinearLayout item_02;
    @BindView(R.id.realPlayFrameLayout)
    FrameLayout realPlayFrameLayout;
    @BindView(R.id.top_bar_img)
    ImageView top_bar_img;
    @BindView(R.id.top_bar_name)
    TextView top_bar_name;
    @BindView(R.id.top_bar_equipmentImg)
    ImageView top_bar_equipmentImg;
    @BindView(R.id.top_bar_equipmentNameText)
    TextView top_bar_equipmentNameText;
    @BindView(R.id.top_bar_lianjie)
    ImageView top_bar_lianjie;
    @BindView(R.id.top_bar_Fragment)
    LinearLayout top_bar_Fragment;

    public static final String LOGINFRAGMENT = "com.domyos.econnected.ui.fragment.RealPlayFragment";
    /*private RealPlayFragment realPlayFragment0 = new RealPlayFragment(0);
    private RealPlayFragment realPlayFragment1 = new RealPlayFragment(1);*/
    private RealPlayDemoFragment realPlayFragment0 = new RealPlayDemoFragment(0);
    private RealPlayFragment realPlayFragment1 = new RealPlayFragment(1);

    private int ITEM_PLAU = 0;

    public static final String SHOW_EQUIPMENT_FRAGMENT = "com.domyos.econnected.ui.fragment.running.SportEquipmentFragment";
    private SportEquipmentFragment sportEquipmentFragment = new SportEquipmentFragment();

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_real_list;
    }

    @Override
    protected void initSomething() {
        if (isVisible()) {
            int picId = SharedPreferenceUtils.get(YDApplication.getInstance(), "picId", 0);
            setAvtar(picId);
            String ew_name = (String) YDApplication.getInstance().get("ew_name");
            if (ew_name != null) {
                top_bar_lianjie.setVisibility(View.GONE);
                top_bar_Fragment.setVisibility(View.VISIBLE);
                if (ew_name.contains("JS")) {
                    top_bar_equipmentImg.setImageDrawable(getActivity().getDrawable(R.drawable.bike));
                } else if (ew_name.contains("EP")) {

                    top_bar_equipmentImg.setImageDrawable(getActivity().getDrawable(R.drawable.icon_equipment_ep));

                } else if (ew_name.contains("TM")) {

                    top_bar_equipmentImg.setImageDrawable(getActivity().getDrawable(R.drawable.treadmill));
                }

                top_bar_equipmentNameText.setText(ew_name);
            }

            if (SharedPreferenceUtils.get(getContext(), "isLogin", false)) {
                top_bar_name.setText(SharedPreferenceUtils.get(getContext(), "name", ""));
            }
        }
        realPlayFrameLayout.setVisibility(View.VISIBLE);
    }
    public void setAvtar(int picId){

        if(picId== UserPicConstant.TYPE_01_01){
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_01_01));

        }

        if(picId== UserPicConstant.TYPE_01_02){
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_01_02));

        }

        if(picId== UserPicConstant.TYPE_01_03){
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_01_03));

        }
        if(picId== UserPicConstant.TYPE_02_01){
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_02_01));

        }

        if(picId== UserPicConstant.TYPE_02_02){
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_02_02));

        }

        if(picId== UserPicConstant.TYPE_02_03){
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_02_03));

        }
        if(picId== UserPicConstant.TYPE_03_01){
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_03_01));

        }

        if(picId== UserPicConstant.TYPE_03_02){
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_03_02));

        }

        if(picId== UserPicConstant.TYPE_03_03){
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.pic_03_03));

        }
        if(picId==0){
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.touxiang_img));

        }

    }


    @OnClick({R.id.top_bar_back, R.id.lasi, R.id.yali, R.id.top_bar_lianjie})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_bar_back:
                removeFragment();
                break;
            case R.id.lasi:
                showRealPlayFragment0();
                break;
            case R.id.yali:
                showRealPlayFragment1();
                break;
            case R.id.top_bar_lianjie:
                showEWEquipment();
                break;
        }
    }

    private void showEWEquipment() {
        FragmentTransaction transition = getChildFragmentManager().beginTransaction();
        if (!isFragmentAdded(SHOW_EQUIPMENT_FRAGMENT)) {
            transition.add(R.id.selectorEquipment_real_list, sportEquipmentFragment, SHOW_EQUIPMENT_FRAGMENT);
            transition.show(sportEquipmentFragment);
        } else {
            transition.show(sportEquipmentFragment);
        }
        transition.commitAllowingStateLoss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void equipmentEvent(EquipmentEvent event) {
        switch (event.action) {
            case EquipmentEvent.ACTION_EQUIPMENT_CONNECTED:
                if (isVisible()) {
                    top_bar_lianjie.setVisibility(View.GONE);
                    top_bar_Fragment.setVisibility(View.VISIBLE);
                    top_bar_equipmentNameText.setText(event.ewEquipment.getPeripheral().getName());

                    if (event.ewEquipment.getPeripheral().getName().contains("EW-JS")) {
                        top_bar_equipmentImg.setImageDrawable(getActivity().getDrawable(R.drawable.bike));
                    } else if (event.ewEquipment.getPeripheral().getName().contains("EW-EP")) {

                        top_bar_equipmentImg.setImageDrawable(getActivity().getDrawable(R.drawable.icon_equipment_ep));

                    } else if (event.ewEquipment.getPeripheral().getName().contains("EW-TM")) {

                        top_bar_equipmentImg.setImageDrawable(getActivity().getDrawable(R.drawable.treadmill));
                    }
                }
                break;
            case EquipmentEvent.ACTION_EQUIPMENT_DISCONNECT:
                top_bar_lianjie.setVisibility(View.VISIBLE);
                top_bar_Fragment.setVisibility(View.GONE);
                showDialog(getActivity().getString(R.string.reconnect));

                break;
        }
    }
    public void showDialog(String msg) {
        if (getActivity() != null) {
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

    }


    public void showRealPlayFragment0() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (isFragmentAdded(LOGINFRAGMENT)) {
            transaction.show(realPlayFragment0);
        } else {
            transaction.add(R.id.realPlayFrameLayout, realPlayFragment0, LOGINFRAGMENT);
            transaction.show(realPlayFragment0);
        }
        transaction.commitAllowingStateLoss();
    }

    public void showRealPlayFragment1() {

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (isFragmentAdded(LOGINFRAGMENT)) {
            transaction.show(realPlayFragment1);
        } else {
            transaction.add(R.id.realPlayFrameLayout, realPlayFragment1, LOGINFRAGMENT);
            transaction.show(realPlayFragment1);
        }
        transaction.commitAllowingStateLoss();
    }

    public boolean isFragmentAdded(String tag) {
        return getChildFragmentManager().findFragmentByTag(tag) != null;
    }


    private void removeFragment() {
        getFragmentManager().popBackStack();
    }


}

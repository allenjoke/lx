package com.domyos.econnected.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class ZhiBoListFragment extends BaseFragment {
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
    @BindView(R.id.fragment_01)
    FrameLayout frameLayout_01;
    @BindView(R.id.fragment_02)
    FrameLayout frameLayout_02;
    @BindView(R.id.fragment_03)
    FrameLayout frameLayout_03;
    @BindView(R.id.fragment_04)
    FrameLayout frameLayout_04;




    public static final String SHOW_EQUIPMENT_FRAGMENT = "com.domyos.econnected.ui.fragment.running.SportEquipmentFragment";
    private SportEquipmentFragment sportEquipmentFragment = new SportEquipmentFragment();
    public static final String ZHI_BO_FRAGMENT = "com.domyos.econnected.ui.fragment.ZhiBoShowFragment";
    private ZhiBoShowFragment zhiBoShowFragment = new ZhiBoShowFragment();
    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_zhi_bo_list;
    }

    @Override
    protected void initSomething() {
        if (isVisible()) {
            int picId = SharedPreferenceUtils.get(YDApplication.getInstance(), "picId", 0);
            setAvtar(picId);
            String ew_name =  (String)YDApplication.getInstance().get("ew_name");
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
    }


    public void setAvtar(int picId){
        if(picId==0){
            top_bar_img.setImageDrawable(getActivity().getDrawable(R.drawable.touxiang_img));

        }
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

    }

    @OnClick({R.id.fragment_04,R.id.fragment_03,R.id.fragment_02,R.id.fragment_01,R.id.top_bar_back, R.id.top_bar_lianjie,R.id.zhibo_btn_01,R.id.zhibo_btn_02,R.id.zhibo_btn_03,R.id.zhibo_btn_04})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_bar_back:
                removeFragment();
                break;
            case R.id.top_bar_lianjie:
                showEWEquipment();
                break;
            case R.id.zhibo_btn_01:
                setBg(1);
                showZhiboPlayFragment();

                break;
            case R.id.zhibo_btn_02:

                setBg(2);
                showZhiboPlayFragment();
                break;
            case R.id.zhibo_btn_03:

                setBg(3);
                showZhiboPlayFragment();
                break;
            case R.id.zhibo_btn_04:

                setBg(4);
                showZhiboPlayFragment();
                break;

            case R.id.fragment_01:
                setBg(1);
                break;
            case R.id.fragment_02:
                setBg(2);
                break;
            case R.id.fragment_03:
                setBg(3);
                break;
            case R.id.fragment_04:
                setBg(4);
                break;
        }
    }

    public void setBg(int id){

        if(id==1){
            frameLayout_01.setBackground(getActivity().getDrawable(R.drawable.yellow_bg));
            frameLayout_02.setBackground(getActivity().getDrawable(R.drawable.grey_bg));
            frameLayout_03.setBackground(getActivity().getDrawable(R.drawable.grey_bg));
            frameLayout_04.setBackground(getActivity().getDrawable(R.drawable.grey_bg));

        }

        if(id==2){
            frameLayout_01.setBackground(getActivity().getDrawable(R.drawable.grey_bg));
            frameLayout_02.setBackground(getActivity().getDrawable(R.drawable.yellow_bg));
            frameLayout_03.setBackground(getActivity().getDrawable(R.drawable.grey_bg));
            frameLayout_04.setBackground(getActivity().getDrawable(R.drawable.grey_bg));

        }
        if(id==3){
            frameLayout_01.setBackground(getActivity().getDrawable(R.drawable.grey_bg));
            frameLayout_02.setBackground(getActivity().getDrawable(R.drawable.grey_bg));
            frameLayout_03.setBackground(getActivity().getDrawable(R.drawable.yellow_bg));
            frameLayout_04.setBackground(getActivity().getDrawable(R.drawable.grey_bg));

        }

        if(id==4){
            frameLayout_01.setBackground(getActivity().getDrawable(R.drawable.grey_bg));
            frameLayout_02.setBackground(getActivity().getDrawable(R.drawable.grey_bg));
            frameLayout_03.setBackground(getActivity().getDrawable(R.drawable.grey_bg));
            frameLayout_04.setBackground(getActivity().getDrawable(R.drawable.yellow_bg));

        }



    }

    private void removeFragment() {
        getFragmentManager().popBackStack();
    }

    private void showZhiboPlayFragment() {


        FragmentTransaction transition = getChildFragmentManager().beginTransaction();
        if (!isFragmentAdded(ZHI_BO_FRAGMENT)) {
            transition.add(R.id.zhibo_play_fragment, zhiBoShowFragment, ZHI_BO_FRAGMENT);
            transition.show(zhiBoShowFragment);
        } else {
            transition.show(zhiBoShowFragment);
        }
        transition.commitAllowingStateLoss();

    }

    private void showEWEquipment() {
        FragmentTransaction transition = getChildFragmentManager().beginTransaction();
        if (!isFragmentAdded(SHOW_EQUIPMENT_FRAGMENT)) {
            transition.add(R.id.selectorEquipment_fragment, sportEquipmentFragment, SHOW_EQUIPMENT_FRAGMENT);
            transition.show(sportEquipmentFragment);
        } else {
            transition.show(sportEquipmentFragment);
        }
        transition.commitAllowingStateLoss();
    }

    public boolean isFragmentAdded(String tag) {
        return getChildFragmentManager().findFragmentByTag(tag) != null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void equipmentEvent(EquipmentEvent event) {
        switch (event.action) {
            case EquipmentEvent.ACTION_EQUIPMENT_CONNECTED:
            if (isVisible()) {
                    top_bar_lianjie.setVisibility(View.GONE);
                    top_bar_Fragment.setVisibility(View.VISIBLE);
                    top_bar_equipmentNameText.setText(event.ewEquipment.getPeripheral().getName());

                    if (event.ewEquipment.getPeripheral().getName().contains("JS")) {
                        top_bar_equipmentImg.setImageDrawable(getActivity().getDrawable(R.drawable.bike));
                    } else if (event.ewEquipment.getPeripheral().getName().contains("EP")) {

                        top_bar_equipmentImg.setImageDrawable(getActivity().getDrawable(R.drawable.icon_equipment_ep));

                    } else if (event.ewEquipment.getPeripheral().getName().contains("TM")) {

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
}

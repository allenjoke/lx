package com.domyos.econnected.ui.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.domyos.econnected.R;
import com.domyos.econnected.YDApplication;
import com.domyos.econnected.constant.UserPicConstant;
import com.domyos.econnected.event.EquipmentEvent;
import com.domyos.econnected.event.ThirdAppEvent;
import com.domyos.econnected.ui.BaseFragment;
import com.domyos.econnected.ui.fragment.running.SportEquipmentFragment;
import com.domyos.econnected.utils.SharedPreferenceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

import static com.domyos.econnected.ui.fragment.RaceFragment.SHOW_EQUIPMENT_FRAGMENT;

public class MultimediaFragment extends BaseFragment {
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

    private SportEquipmentFragment sportEquipmentFragment = new SportEquipmentFragment();

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_multimedia;
    }

    @Override
    protected void initSomething() {
        if (isVisible()) {
            int picId = SharedPreferenceUtils.get(YDApplication.getInstance(), "picId", 0);
            setAavatar(picId);
            String ew_name =  (String) YDApplication.getInstance().get("ew_name");
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
    @OnClick({R.id.multimedia_netflix, R.id.multimedia_hulu, R.id.multimedia_amazon, R.id.multimedia_youtube,R.id.top_bar_lianjie,R.id.top_bar_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_bar_back:
                removeFragment();
                break;
            case R.id.top_bar_lianjie:
                showEWEquipment();
                break;
            case R.id.multimedia_netflix:
                EventBus.getDefault().post(new ThirdAppEvent(ThirdAppEvent.ACTION_NETFLIX));
                break;
            case R.id.multimedia_hulu:
                EventBus.getDefault().post(new ThirdAppEvent(ThirdAppEvent.ACTION_HULU));
                break;
            case R.id.multimedia_amazon:
                EventBus.getDefault().post(new ThirdAppEvent(ThirdAppEvent.ACTION_AMAZON));
                break;
            case R.id.multimedia_youtube:
                EventBus.getDefault().post(new ThirdAppEvent(ThirdAppEvent.ACTION_YOUTUBE));
                break;

        }
    }


    private void removeFragment() {
        getFragmentManager().popBackStack();
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

    private void showEWEquipment() {
        FragmentTransaction transition = getChildFragmentManager().beginTransaction();
        if (!isFragmentAdded(SHOW_EQUIPMENT_FRAGMENT)) {
            transition.add(R.id.multimedia_layout_container, sportEquipmentFragment, SHOW_EQUIPMENT_FRAGMENT);
            transition.show(sportEquipmentFragment);
        } else {
            transition.show(sportEquipmentFragment);
        }
        transition.commitAllowingStateLoss();
    }
    public boolean isFragmentAdded(String tag) {
        return getChildFragmentManager().findFragmentByTag(tag) != null;
    }
    public void setAavatar(int picId){

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

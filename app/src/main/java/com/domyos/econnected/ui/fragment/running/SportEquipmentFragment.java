package com.domyos.econnected.ui.fragment.running;


import android.app.ProgressDialog;
import android.bluetooth.BluetoothGattService;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.domyos.econnected.R;
import com.domyos.econnected.constant.RunningConnectType;
import com.domyos.econnected.enity.EquipmentInfo;
import com.domyos.econnected.enity.UIEventInfo;
import com.domyos.econnected.event.EquipmentEvent;
import com.domyos.econnected.event.UIEvent;
import com.domyos.econnected.ui.BaseFragment;
import com.domyos.econnected.utils.ShowProgressDialog;
import com.domyos.econnected.utils.datapter.EquipmentListAdapter;
import com.ew.ble.library.callback.EwEquipmentManagerCallback;
import com.ew.ble.library.command.BluetoothBikeController;
import com.ew.ble.library.command.BluetoothTmController;
import com.ew.ble.library.entity.TreadmillDeviceInfo;
import com.ew.ble.library.equipment.EWBikeEquipment;
import com.ew.ble.library.equipment.EWEPEquipment;
import com.ew.ble.library.equipment.EWEquipment;
import com.ew.ble.library.equipment.EWSTEquipment;
import com.ew.ble.library.equipment.EWTMEquipment;
import com.ew.ble.library.equipment.EwEquipmentManager;
import com.ew.ble.library.service.BluetoothService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SportEquipmentFragment extends BaseFragment {
    
    @BindView(R.id.equipmentListView)
    ListView equipmentListView;
    @BindView(R.id.refresh_img)
    ImageView selector_equipment;
    @BindView(R.id.equipmentProgressBar)
    ProgressBar equipmentProgressBar;
    private List<EWEquipment> datas;
    private EquipmentListAdapter equipmentListAdapter;
    private ProgressDialog progressDialog;
    private BluetoothService bleService;
    
    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_sport_equipment;
    }
    
    @Override
    protected void initSomething() {
       // equipmentProgressBar.setVisibility(View.VISIBLE);
        selector_equipment.setVisibility(View.VISIBLE);
        progressDialog = new ProgressDialog(getActivity());
        datas = new ArrayList<>();
        scan();
        EwEquipmentManager.getInstance().setCallBack(new EwEquipmentManager.FindEwEquipmentCallback() {
            @Override
            public void equipmentManagerDidInitialized() {
                EwEquipmentManager.getInstance().startLeScan();
            }
            
            @Override
            public void equipmentManagerDidDiscoverEquipment(EWEquipment equipment) {
                datas.clear();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                //equipmentProgressBar.setVisibility(View.GONE);
                EWEquipment[] equipments = new EWEquipment[EwEquipmentManager.getInstance().getEquipments().size()];
                Iterator<EWEquipment> iterator = EwEquipmentManager.getInstance().getEquipments().iterator();
                while (iterator.hasNext()) {
                    EWEquipment dcEquipment = iterator.next();
                    if (dcEquipment instanceof EWTMEquipment || dcEquipment instanceof EWBikeEquipment || dcEquipment instanceof EWEPEquipment || dcEquipment instanceof EWSTEquipment) {
                        datas.add(dcEquipment);
                    } else {
                        Log.e("sh_1234", "EWBikeEquipment is null");
                    }
                }
                equipmentListAdapter.notifyDataSetChanged();
            }
        });
        
        equipmentListAdapter = new EquipmentListAdapter(datas, getActivity());
        equipmentListView.setAdapter(equipmentListAdapter);
        equipmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //连接运动设备
                EWEquipment ewEquipment = datas.get(i);
                EquipmentInfo equipmentInfo = new EquipmentInfo();
                equipmentInfo.setEwEquipment(ewEquipment);
                EventBus.getDefault().post(new EquipmentEvent(EquipmentEvent.ACTION_EQUIPMENT_SEARCH, equipmentInfo));
                backToHome();
            }
        });
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUIEvent(UIEvent event) {
        switch (event.action) {
            case UIEvent.ACTION_RUN_EQUIPMENT:
                break;
            
        }
    }
    
    public void scan() {
        ShowProgressDialog.showDialog(progressDialog, "loading...", getActivity());
        if (!EwEquipmentManager.getInstance().getInitializationState()) {
            EwEquipmentManager.getInstance().initialize(getActivity());
        } else {
            EwEquipmentManager.getInstance().startLeScan();
        }
    }
    
    
    @OnClick({R.id.homePage_back,R.id.refresh_img})
    public void onCLick(View view) {
        switch (view.getId()) {
            case R.id.homePage_back:
                backToHome();
                break;
            case R.id.refresh_img:
                scan();
                break;
        }
    }
    
    public void backToHome() {
        EwEquipmentManager.getInstance().stopLeScan();
        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
    }
    
    
    private void connectEquipment(EWEquipment mEwEquipment) {
        ShowProgressDialog.showDialog(progressDialog, "loading...", getActivity());
        EwEquipmentManager.getInstance().connectEquipment(mEwEquipment);
        EwEquipmentManager.getInstance().setEquipmentManagerCallBack(new EwEquipmentManagerCallback() {
            @Override
            public void equipmentManagerDidConnectEquipment(final EWEquipment ewEquipment) {
                bleService = ewEquipment.getPeripheral().getBleService();
                
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShowProgressDialog.showDialog(progressDialog, "Connected, checking...", getActivity());
                        
                    }
                });
            }
            
            @Override
            public void equipmentManagerDidDisconnectEquipment(EWEquipment ewEquipment) {
            
            }
            
            @Override
            public void equipmentManagerLinkMachineSuccessful(EWEquipment ewEquipment) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShowProgressDialog.disProgressDialog(progressDialog);
                    }
                });
                UIEventInfo uiEventInfo = new UIEventInfo();
                uiEventInfo.setType(RunningConnectType.RUNNING_MAIN);
                EventBus.getDefault().post(new UIEvent(UIEvent.ACTION_RUN_EQUIPMENT, uiEventInfo));
                backToHome();
                
                
            }
            
            @Override
            public void equipmentManagerGetDeviceInfoSuccessful(EWEquipment ewEquipment) {
            
            }
            
            @Override
            public void equipmentManagerDidDiscoverServices(EWEquipment
                                                                    ewEquipment, BluetoothGattService[] services, int status) {
                bleService = ewEquipment.getPeripheral().getBleService();
                TreadmillDeviceInfo.macAddress = ewEquipment.getPeripheral().getAddress();
                if (ewEquipment instanceof EWTMEquipment) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BluetoothTmController.getInstance(bleService).initDeviceConnection();
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BluetoothBikeController.getInstance(bleService).initDeviceConnection();
                        }
                    });
                }
            }
            
            @Override
            public void equipmentManagerAvailBLEData(final EWEquipment ewEquipment,
                                                     final byte[] bReceived) {
                if (ewEquipment instanceof EWTMEquipment) {
                    EwEquipmentManager.getInstance().parseTmEquipmentData(ewEquipment, bReceived);

                   /* getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EwEquipmentManager.getInstance().parseTmEquipmentData(ewEquipment, bReceived);
                        }
                    });*/
                }
            }
        });
    }
    
    @Override
    public void onDestroyView() {
        EwEquipmentManager.getInstance().stopLeScan();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDestroyView();
    }
    
    @Override
    public void onDestroy() {
        EwEquipmentManager.getInstance().stopLeScan();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }
    
}

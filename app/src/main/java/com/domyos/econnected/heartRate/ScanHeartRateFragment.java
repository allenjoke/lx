package com.domyos.econnected.heartRate;import android.app.ProgressDialog;import android.bluetooth.BluetoothAdapter;import android.bluetooth.BluetoothDevice;import android.bluetooth.BluetoothManager;import android.content.Context;import android.content.Intent;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.AdapterView;import android.widget.BaseAdapter;import android.widget.ImageView;import android.widget.ListView;import android.widget.TextView;import androidx.fragment.app.Fragment;import com.domyos.econnected.R;import com.domyos.econnected.event.EquipmentEvent;import com.domyos.econnected.ui.BaseFragment;import com.domyos.econnected.utils.ShowProgressDialog;import org.greenrobot.eventbus.EventBus;import java.util.ArrayList;import java.util.List;import butterknife.BindView;import butterknife.OnClick;/** * A simple {@link Fragment} subclass. */public class ScanHeartRateFragment extends BaseFragment {    @BindView(R.id.equipmentListView)    ListView equipmentListView;    @BindView(R.id.refresh_img)    ImageView refresh_img;    private BluetoothAdapter mBluetoothAdapter = null;    private BluetoothManager mBluetoothManager = null;    private DeviceAdapter deviceAdapter;    private List<BluetoothDevice> devices = new ArrayList<>();    private List<String> mDeviceAddresses = new ArrayList<>();    private ProgressDialog progressDialog;    @Override    protected int getRootViewLayoutId() {        return R.layout.fragment_scan_heart_rate;    }    @Override    protected void initSomething() {        refresh_img.setVisibility(View.VISIBLE);        deviceAdapter = new DeviceAdapter();        equipmentListView.setAdapter(deviceAdapter);        init();        equipmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {            @Override            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {                BluetoothDevice device = devices.get(position);                EventBus.getDefault().post(new EquipmentEvent(device));                removeFragment();            }        });        progressDialog = new ProgressDialog(getActivity());    }    @OnClick({R.id.refresh_img, R.id.homePage_back})    public void onClick(View view) {        switch (view.getId()) {            case R.id.refresh_img:                scan();                break;            case R.id.homePage_back:                removeFragment();                break;        }    }    private void init() {        mBluetoothManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);        mBluetoothAdapter = mBluetoothManager.getAdapter();        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);            startActivity(intent);        }        scan();    }    private boolean isScanning = false;    private void scan() {        if (mBluetoothAdapter != null && !isScanning) {            devices.clear();            mDeviceAddresses.clear();            isScanning = mBluetoothAdapter.startLeScan(mLeScanCallback);        }        ShowProgressDialog.showDialog(progressDialog, "loading...", getActivity());    }    private void stopLeScan() {        if (mBluetoothAdapter != null) {            ShowProgressDialog.disProgressDialog(progressDialog);            mBluetoothAdapter.stopLeScan(mLeScanCallback);            isScanning = false;        }    }    private void removeFragment() {        stopLeScan();        ShowProgressDialog.disProgressDialog(progressDialog);        getFragmentManager().popBackStack();    }    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {        @Override        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {            //   if (device.getName() != null && device.getName().equals("Heart Rate Sensor") && !mDeviceAddresses.contains(device.getAddress())) {            if (device.getName() != null && !mDeviceAddresses.contains(device.getAddress())) {                mDeviceAddresses.add(device.getAddress());                devices.add(device);                deviceAdapter.notifyDataSetChanged();            }        }    };    class DeviceAdapter extends BaseAdapter {        @Override        public int getCount() {            return devices.size();        }        @Override        public Object getItem(int position) {            return devices.get(position);        }        @Override        public long getItemId(int position) {            return position;        }        @Override        public View getView(int position, View convertView, ViewGroup parent) {            ViewHolder viewHolder;            if (convertView == null) {                viewHolder = new ViewHolder();                convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_list_row, null);                viewHolder.device_rssi = convertView.findViewById(R.id.device_rssi);                viewHolder.device_name = convertView.findViewById(R.id.device_name);                viewHolder.device_address = convertView.findViewById(R.id.device_address);                convertView.setTag(viewHolder);            } else {                viewHolder = (ViewHolder) convertView.getTag();            }            BluetoothDevice device = devices.get(position);            viewHolder.device_rssi.setImageResource(R.drawable.title_heart);            viewHolder.device_name.setText(device.getName());            viewHolder.device_address.setText(device.getAddress());            return convertView;        }        class ViewHolder {            ImageView device_rssi;            TextView device_name, device_address;        }    }}
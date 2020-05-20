package com.domyos.econnected.utils.datapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.domyos.econnected.R;
import com.ew.ble.library.equipment.EWEquipment;

import java.util.List;


public class EquipmentListAdapter extends BaseAdapter {
    
    private List<EWEquipment> list;
    private Context context;
    private LayoutInflater inflater;
    
    public EquipmentListAdapter(List<EWEquipment> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    
    @Override
    public int getCount() {
        return list.size();
    }
    
    @Override
    public Object getItem(int i) {
        return list.get(i);
    }
    
    @Override
    public long getItemId(int i) {
        return i;
    }
    
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyHolder myHolder;
        if (view == null) {
            myHolder = new MyHolder();
            view = inflater.inflate(R.layout.device_list_row, null);
            myHolder.imageView = view.findViewById(R.id.device_rssi);
            myHolder.text_title = view.findViewById(R.id.device_name);
            myHolder.text_des = view.findViewById(R.id.device_address);
            view.setTag(myHolder);
        } else {
            myHolder = (MyHolder) view.getTag();
        }
        BluetoothDevice device = list.get(i).getPeripheral().getBluetoothDevice();
        myHolder.text_title.setText(device.getName());
        myHolder.text_des.setText(device.getAddress());
        Log.e("sunny", "deviceName:  " + device.getName().toLowerCase());
        if (device.getName().toLowerCase().startsWith("ew-js") || device.getName().toLowerCase().startsWith("ew-bk")) {
            myHolder.imageView.setImageResource(R.drawable.icon_equipment_bike);
        } else if (device.getName().toLowerCase().startsWith("ew-st") || device.getName().toLowerCase().startsWith("ew-ep")) {
            myHolder.imageView.setImageResource(R.drawable.icon_equipment_ep);
        } else if (device.getName().toLowerCase().startsWith("ew-tm")) {
            myHolder.imageView.setImageResource(R.drawable.icon_equipment_treadmill);
        } else if (device.getName().toLowerCase().startsWith("heart")) {
            myHolder.imageView.setImageResource(R.drawable.title_heart);
        }
        return view;
    }
    
    class MyHolder {
        ImageView imageView;
        TextView text_title, text_des;
        
    }
}

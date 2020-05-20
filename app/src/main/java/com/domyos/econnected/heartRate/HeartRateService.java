package com.domyos.econnected.heartRate;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.domyos.econnected.event.HeartRateEvent;
import com.domyos.econnected.utils.SharedPreferenceUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.UUID;

public class HeartRateService extends Service {
    
    private final IBinder mBinder = new HeartRateService.LocalBinder();
    
    private final static String TAG = "####guolong" + HeartRateService.class.getSimpleName();
    
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
        close();
        return super.onUnbind(intent);
    }
    
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;
    
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    
    
    public final static String EXTRA_DATA =
            "com.nordicsemi.nrfUART.EXTRA_DATA";
    
    public static final UUID CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final UUID RX_SERVICE_UUID = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    //00002902    0000fff0
    //RX对于下控是接受收据
    //注意，这里的RX对于CC254x来说的接收，也就是0xfff1
    public static final UUID RX_CHAR_UUID = UUID.fromString("0000fff2-0000-1000-8000-00805f9b34fb");
    //注意，这里的TX对于CC254x来说的发送，也就是0xfff4，notify方式。
    public static final UUID TX_CHAR_UUID = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    
    public static final UUID HEART_RATE_SERVICE_UUID = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
    public static final UUID TX_CHAR_HEART_RATE_UUID = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
    
    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                mConnectionState = STATE_CONNECTED;
                if (heartRateCallBack != null) {
                    heartRateCallBack.connected();
                    Log.w(TAG, "mBluetoothGatt = connected");

                }
                mBluetoothGatt.discoverServices();
                
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                mConnectionState = STATE_DISCONNECTED;
                if (heartRateCallBack != null) {
                    heartRateCallBack.disconnected();
                    Log.w(TAG, "mBluetoothGatt = disconnected");
                    //心率蓝牙断开了
                    SharedPreferenceUtils.put(HeartRateService.this,"heart","");
                    EventBus.getDefault().post(new HeartRateEvent("",HeartRateEvent.DISCONNTED_HEART_RATE));
                }
            }
        }
        
        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            
        }
        
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.w(TAG, "mBluetoothGatt = " + mBluetoothGatt);
                enableTXNotification();
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }
        
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
        }
        
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            
            if (characteristic.getUuid().equals(TX_CHAR_HEART_RATE_UUID)) {
                byte[] raw = characteristic.getValue();
                System.out.println("心率****=" + raw);
                int index = ((raw[0] & 0x01) == 1) ? 2 : 1;
                int format = (index == 1) ? BluetoothGattCharacteristic.FORMAT_UINT8 : BluetoothGattCharacteristic.FORMAT_UINT16;
                int value = characteristic.getIntValue(format, index);
                final String description = value + " bpm";
                Log.e("sunny", description);
                Intent intent = new Intent();
                intent.setAction("com.domyos.econnected.SEND_HEART_RATE");
                intent.putExtra("heart_rate", value);
                sendBroadcast(intent);
            }
        }
    };
    
    
    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        if (TX_CHAR_UUID.equals(characteristic.getUuid())) {
            intent.putExtra(EXTRA_DATA, characteristic.getValue());
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    
    public class LocalBinder extends Binder {
        public HeartRateService getService() {
            return HeartRateService.this;
        }
    }
    
    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        return true;
    }
    
    private HeartRateCallBack heartRateCallBack;
    
    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The connection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address, HeartRateCallBack callBack) {
        heartRateCallBack = callBack;
        if (mBluetoothAdapter == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = mBluetoothManager.getAdapter();
        }
        if (address == null) {
            return false;
        }
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }
        
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }
    
    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.disconnect();
    }
    
    public void discoverServices(BluetoothDevice device) {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.discoverServices();
    }
    
    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothDeviceAddress = null;
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }
    
    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }
    
    /**
     * Enable TXNotification
     *
     * @return
     */
    public void enableTXNotification() {
        if (mBluetoothGatt == null) {
            return;
        }
        BluetoothGattService RxService = mBluetoothGatt.getService(HEART_RATE_SERVICE_UUID);
        if (RxService == null) {
            return;
        }
        BluetoothGattCharacteristic TxChar = RxService.getCharacteristic(TX_CHAR_HEART_RATE_UUID);
        if (TxChar == null) {
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(TxChar, true);
        BluetoothGattDescriptor descriptor = TxChar.getDescriptor(CCCD);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);
        
    }
    
    public boolean writeRXCharacteristic(byte[] value) {
        
        if (mBluetoothGatt == null) {
            return false;
        }
        BluetoothGattService RxService = mBluetoothGatt.getService(RX_SERVICE_UUID);
        //showMessage("mBluetoothGatt null"+ mBluetoothGatt);
        if (RxService == null) {
            return false;
        }
        BluetoothGattCharacteristic RxChar = RxService.getCharacteristic(RX_CHAR_UUID);
        if (RxChar == null) {
            return false;
        }
        RxChar.setValue(value);
        boolean status = mBluetoothGatt.writeCharacteristic(RxChar);
        return status;
    }
    
    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;
        return mBluetoothGatt.getServices();
    }
    
    
    public interface HeartRateCallBack {
        
        void connected();
        
        void disconnected();
    }
}


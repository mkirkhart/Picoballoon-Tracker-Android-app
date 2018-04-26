
/*
 * Copyright (c) 2015, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package edu.umich.mkirkhar.PicoballoonTracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    private static final int REQUEST_SELECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int UART_PROFILE_READY = 10;
    public static final String TAG = "Picoballoon Tracker";
    private static final int UART_PROFILE_CONNECTED = 20;
    private static final int UART_PROFILE_DISCONNECTED = 21;
    private static final int STATE_OFF = 10;

    private int mState = UART_PROFILE_DISCONNECTED;
    private UartService mService = null;
    private BluetoothDevice mDevice = null;
    private BluetoothAdapter mBtAdapter = null;

    private ListView messageListView;
    private TrackerSummaryAdapter trackerSummaryAdapter;
    private List<Bundle> TIDPacketDataBundle;

    private Button btnConnectDisconnect;

    private TIDPacketParser TIDParser = null;
    private TIDPacketData TIDPacketDataInstance = null;

    private LocationManager locationManager;
    private LocationListener LocationListener;
    private static final long MINIMUM_LOCATION_UPDATE_TIME_MILLISECONDS = 1000 * 5;
    private static final float MINIMUM_LOCATION_UPDATE_DISTANCE = 0.0f;

    private Location LastKnownTrackerLocation = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBtAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        messageListView = (ListView) findViewById(R.id.listMessage);

        // create array of Bundles to hold tracker data for use by custom listView adapter
        TIDPacketDataBundle = new ArrayList<Bundle>();
        // create custom listView adapter
        trackerSummaryAdapter = new TrackerSummaryAdapter(this, TIDPacketDataBundle);
        // set listView to use our custom adapter
        messageListView.setAdapter(trackerSummaryAdapter);

        btnConnectDisconnect = (Button) findViewById(R.id.btn_select);
        service_init();

        // Handle Disconnect & Connect button
        btnConnectDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (!mBtAdapter.isEnabled()) {
                Log.i(TAG, "onClick - BT not enabled yet");
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            } else {
                if (btnConnectDisconnect.getText().equals("Connect")) {
                    //Connect button pressed, open DeviceListActivity class, with popup windows that scan for devices
                    Intent newIntent = new Intent(MainActivity.this, DeviceListActivity.class);
                    startActivityForResult(newIntent, REQUEST_SELECT_DEVICE);
                } else {
                    //Disconnect button pressed
                    if (mDevice != null) {
                        mService.disconnect();
                    }
                }
            }
            }
        });

        // set up periodic location updates
        // kind of a kludge, but the tracker does get its position from GPS ...
        LastKnownTrackerLocation = new Location(LocationManager.GPS_PROVIDER);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(null != locationManager)
        {
            LocationListener = new MyLocationListener();

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINIMUM_LOCATION_UPDATE_TIME_MILLISECONDS, MINIMUM_LOCATION_UPDATE_DISTANCE, LocationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MINIMUM_LOCATION_UPDATE_TIME_MILLISECONDS, MINIMUM_LOCATION_UPDATE_DISTANCE, LocationListener);
        }
    }

    //UART service connected/disconnected
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            mService = ((UartService.LocalBinder) rawBinder).getService();
            Log.d(TAG, "onServiceConnected mService= " + mService);
            if (!mService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }

            // create a packet parser
            if (null != TIDParser) {
                TIDParser = null;
            }
            TIDParser = new TIDPacketParser();

            // create a packet data object
            if (null != TIDPacketDataInstance) {
                TIDPacketDataInstance = null;
            }

            TIDPacketDataInstance = new TIDPacketData();
        }

        public void onServiceDisconnected(ComponentName classname) {
            mService = null;
            // dispose of packet parser
            TIDParser = null;
        }
    };

    private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

        if (null != action) {
            //*********************//
            if (action.equals(UartService.ACTION_GATT_CONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                    Log.d(TAG, "UART_CONNECT_MSG");
                    btnConnectDisconnect.setText(R.string.disconnect);
                    String newText = mDevice.getName() + " - ready";
                    ((TextView) findViewById(R.id.deviceName)).setText(newText);

                    mState = UART_PROFILE_CONNECTED;
                    }
                });
            }

            //*********************//
            if (action.equals(UartService.ACTION_GATT_DISCONNECTED)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                    Log.d(TAG, "UART_DISCONNECT_MSG");
                    btnConnectDisconnect.setText(R.string.connect);
                    ((TextView) findViewById(R.id.deviceName)).setText(R.string.not_connected);

                    mState = UART_PROFILE_DISCONNECTED;
                    mService.close();
                    }
                });
            }

            //*********************//
            if (action.equals(UartService.ACTION_GATT_SERVICES_DISCOVERED)) {
                mService.enableTXNotification();
            }
            //*********************//
            // This is what handles reception of data from the peripheral
            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {
                final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
                // here is where we add to the packet parser, and if we have new data, it will let us know
                boolean haveNewPacket = TIDParser.AddBytesToParser(txValue, true, TIDPacketDataInstance);
                if (haveNewPacket) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                // create a Bundle from the tracker data
                                Bundle newBundle = TIDPacketDataInstance.CreateTIDDataBundle();

                                // add the Bundle to our Bundle ArrayList
                                TIDPacketDataBundle.add(newBundle);

                                // indicate to our listView adapter we just added data
                                trackerSummaryAdapter.notifyDataSetChanged();
                                // scroll listView to the bottom of the list
                                messageListView.smoothScrollToPosition(trackerSummaryAdapter.getCount() - 1);

                                // update last known tracker location
                                if(TIDPacketDataInstance.GPSIsGood())
                                {
                                    LastKnownTrackerLocation.reset();

                                    LastKnownTrackerLocation.setLatitude((double)TIDPacketDataInstance.GetTrackerGPSLatitude());
                                    LastKnownTrackerLocation.setLongitude((double)TIDPacketDataInstance.GetTrackerGPSLongitude());
                                    LastKnownTrackerLocation.setAltitude((double)TIDPacketDataInstance.GetTrackerGPSAltitude());
                                }


                            } catch (Exception e) {
                                Log.e(TAG, e.toString());
                            }
                        }
                    });
                }
            }
            //*********************//
            if (action.equals(UartService.DEVICE_DOES_NOT_SUPPORT_UART)) {
                showMessage("Device doesn't support UART. Disconnecting");
                mService.disconnect();
            }
        }
        }
    };

    private void service_init() {
        Intent bindIntent = new Intent(this, UartService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        LocalBroadcastManager.getInstance(this).registerReceiver(UARTStatusChangeReceiver, makeGattUpdateIntentFilter());
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(UartService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(UartService.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter;
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");

        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(UARTStatusChangeReceiver);
        } catch (Exception ignore) {
            Log.e(TAG, ignore.toString());
        }
        unbindService(mServiceConnection);
        mService.stopSelf();
        mService = null;
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        if (!mBtAdapter.isEnabled()) {
            Log.i(TAG, "onResume() - BT not enabled yet");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged()");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_SELECT_DEVICE:
                //When the DeviceListActivity return, with the selected device address
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String deviceAddress = data.getStringExtra(BluetoothDevice.EXTRA_DEVICE);
                    mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddress);

                    Log.d(TAG, "... onActivityResultdevice.address==" + mDevice + "mserviceValue" + mService);
                    String newText = mDevice.getName() + " - connecting";
                    ((TextView) findViewById(R.id.deviceName)).setText(newText);
                    mService.connect(deviceAddress);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, "Bluetooth has turned on ", Toast.LENGTH_SHORT).show();

                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, "Problem in BT Turning ON ", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                Log.e(TAG, "wrong request code");
                break;
        }
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (mState == UART_PROFILE_CONNECTED) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            String messageString = getString(R.string.background_popup_message);
            showMessage(messageString);
        } else {
            new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.popup_title)
                .setMessage(R.string.popup_message)
                .setPositiveButton(R.string.popup_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.popup_no, null)
                .show();
        }
    }

    // custom listView adapter
    class TrackerSummaryAdapter extends BaseAdapter {
        private Context context;
        private List<Bundle> TIDPacketDataBundleList;
        private LayoutInflater inflater;

        private TrackerSummaryAdapter(Context context, List<Bundle> TIDPacketDataBundleList) {
            this.context = context;
            this.inflater = LayoutInflater.from(this.context);
            this.TIDPacketDataBundleList = TIDPacketDataBundleList;
        }

        @Override
        public int getCount() {
            return TIDPacketDataBundleList.size();
        }

        @Override
        public Object getItem(int position) {
            return TIDPacketDataBundleList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewGroup viewGroup;

            if (null != convertView)
            {
                viewGroup = (ViewGroup) convertView;
            }
            else
            {
                viewGroup = (ViewGroup)inflater.inflate(R.layout.tracker_summary_element, parent, false);
            }

            Bundle TIDPacketDataBundle = TIDPacketDataBundleList.get(position);

            // get references to view elements
            final TextView tvDateTimeReceived = (TextView) viewGroup.findViewById(R.id.date_time_received);
            final TextView tvTrackerID = (TextView) viewGroup.findViewById(R.id.tracker_id);
            final TextView tvTrackerRSSI = (TextView) viewGroup.findViewById(R.id.tracker_rssi);
            final TextView tvTrackerLatitude = (TextView) viewGroup.findViewById(R.id.tracker_latitude);
            final TextView tvTrackerLongitude = (TextView) viewGroup.findViewById(R.id.tracker_longitude);
            final TextView tvTrackerAltitude = (TextView) viewGroup.findViewById(R.id.tracker_altitude);
            final TextView tvTrackerHDOP = (TextView) viewGroup.findViewById(R.id.tracker_hdop);
            final TextView tvTrackerPressure = (TextView) viewGroup.findViewById(R.id.tracker_pressure);
            final TextView tvTrackerThermistorTemperature = (TextView) viewGroup.findViewById(R.id.tracker_thermistor_temperature);
            final TextView tvTrackerTransmitBattery_mV = (TextView) viewGroup.findViewById(R.id.tracker_battery_mv);
            final TextView tvTrackerTransmitBattery_mA = (TextView) viewGroup.findViewById(R.id.tracker_battery_ma);

            // show date/time of reception in UTC time
            long DateTimeReceived = TIDPacketData.GetDateTimeReceived(TIDPacketDataBundle);
            Date date = new Date(DateTimeReceived);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            simpleDateFormat.setTimeZone(timeZone);
            String dateTimeString = simpleDateFormat.format(date);
            tvDateTimeReceived.setText(dateTimeString);

            String TrackerID = TIDPacketData.GetTrackerIDFromBundle(TIDPacketDataBundle);
            tvTrackerID.setText(TrackerID);

            float RSSI = TIDPacketData.GetTIDRSSIindBmFromBundle(TIDPacketDataBundle);
            tvTrackerRSSI.setText(String.format(Locale.US,"%1.1f", RSSI));

            float Latitude = TIDPacketData.GetTrackerGPSLatitudeFromBundle(TIDPacketDataBundle);
            tvTrackerLatitude.setText(String.format(Locale.US,"%1.5f", Latitude));

            float Longitude = TIDPacketData.GetTrackerGPSLongitudeFromBundle(TIDPacketDataBundle);
            tvTrackerLongitude.setText(String.format(Locale.US,"%1.5f", Longitude));

            float Altitude = TIDPacketData.GetTrackerGPSAltitudeFromBundle(TIDPacketDataBundle);
            tvTrackerAltitude.setText(String.format(Locale.US,"%1.1f", Altitude));

            float HDOP = TIDPacketData.GetTrackerGPSHDOPFromBundle(TIDPacketDataBundle);
            tvTrackerHDOP.setText(String.format(Locale.US,"%1.1f", HDOP));

            int Pressure = TIDPacketData.GetTrackerBMP180PressureFromBundle(TIDPacketDataBundle);
            tvTrackerPressure.setText(String.valueOf(Pressure));

            float ThermistorTemperature = TIDPacketData.GetTrackerThermistorTemperatureFromBundle(TIDPacketDataBundle);
            tvTrackerThermistorTemperature.setText(String.format(Locale.US,"%1.1f", ThermistorTemperature));

            int TransmitBattery_mV = TIDPacketData.GetTrackerTransmitBatteryMillivoltsFromBundle(TIDPacketDataBundle);
            tvTrackerTransmitBattery_mV.setText(String.valueOf(TransmitBattery_mV));

            int TransmitBattery_mA = TIDPacketData.GetTrackerTransmitBatteryMilliampsFromBundle(TIDPacketDataBundle);
            tvTrackerTransmitBattery_mA.setText(String.valueOf(TransmitBattery_mA));

            return viewGroup;
        }
    }

    private class MyLocationListener implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location)
        {
            if((null != LastKnownTrackerLocation) && (0.0 != LastKnownTrackerLocation.getLatitude()) && (0.0 != LastKnownTrackerLocation.getLongitude()))
            {
                final float DISTANCE_FOR_SWITCH_FROM_METERS_TO_KILOMETERS = 1000.0f;

                float bearingInDegrees = location.bearingTo(LastKnownTrackerLocation);
                // why, oh why is Location.bearingTo() returning negative values?
                if(bearingInDegrees < 0.0)
                {
                    bearingInDegrees += 360.0f;
                }

                float distanceInMeters = location.distanceTo(LastKnownTrackerLocation);

                String distanceUnits;
                if(distanceInMeters > DISTANCE_FOR_SWITCH_FROM_METERS_TO_KILOMETERS)
                {
                    distanceUnits = "km";
                    distanceInMeters /= 1000.0f;
                }
                else
                {
                    distanceUnits = "m";
                }

                final TextView tvDistanceBearing = (TextView) findViewById(R.id.distance_bearing);
                String distanceBearingString = String.format(Locale.US,"%1.1f %s/%1.1f\u00B0", distanceInMeters, distanceUnits, bearingInDegrees);
                tvDistanceBearing.setText(distanceBearingString);
            }

            String logString = String.format(Locale.US,"Device lat = %1.5f, long = %1.5f, provider = ", location.getLatitude(), location.getLongitude()) + location.getProvider();
            Log.d(TAG, logString);
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            String logString = provider + " disabled";
            Log.d(TAG, logString);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            String logString = provider + " disabled";
            Log.d(TAG, logString);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }
    }
}

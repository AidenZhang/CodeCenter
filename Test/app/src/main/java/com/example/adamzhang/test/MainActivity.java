package com.example.adamzhang.test;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter adapter;
    List<String> blueToothNames = new ArrayList<>();
    List<BluetoothDevice> blueToothDevices = new ArrayList<>();
    ListView listView;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                blueToothNames.add(device.getName());
                blueToothDevices.add(device);
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Button scanButton = (Button) findViewById(R.id.scanButton);
                scanButton.setText(R.string.scan_device);
                scanButton.setEnabled(true);
                listView.setAdapter(new ArrayAdapter<String>(context,R.layout.list_view_item,blueToothNames));
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = BluetoothAdapter.getDefaultAdapter();
        if(adapter!=null) {
            if(!adapter.isEnabled()) {
                adapter.enable();
            }

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
            intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
            intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            this.registerReceiver(receiver, intentFilter);

        }
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, blueToothNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void searchBlueTooch(View v) {
        blueToothNames.clear();
        blueToothDevices.clear();
        adapter.startDiscovery();
        ((TextView) v).setText(R.string.scanning_text);
        v.setEnabled(false);
    }
}

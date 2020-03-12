package com.example.bluetooths;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button buttonON, buttonOFF, buttonPair, buttonScan;
    BluetoothAdapter myBluetoothAdapter;
    ListView listView, scanListView;
    ArrayList<String> stringArrayList = new ArrayList<String>();

    Intent btEnablingIntent;
    int requestCodeForEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonON = (Button) findViewById(R.id.btON);
        buttonOFF = (Button) findViewById(R.id.btOFF);
        buttonPair = (Button) findViewById(R.id.btPair);
        buttonScan = (Button) findViewById(R.id.btScan);
        listView = (ListView) findViewById(R.id.listview);
        scanListView = (ListView) findViewById(R.id.secondList);

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btEnablingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForEnable = 1;

        bluetoothONMethod();
        bluetoothOFFMethod();
        pairedDeviceButton();
        scanDeviceButton();
    }

    /* Get list of nearby bluetooth devices into a list view */
    private void scanDeviceButton() {
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /* Get list of all paired devices into a list view */
    private void pairedDeviceButton() {
        buttonPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<BluetoothDevice> bt = myBluetoothAdapter.getBondedDevices();
                String[] strings = new String[bt.size()];
                int index = 0;

                if(bt.size() > 0) {
                    for(BluetoothDevice device:bt) {
                        strings[index] = device.getName();
                        index++;
                    }
                    ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, strings) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent){
                            // Get the Item from ListView
                            View view = super.getView(position, convertView, parent);

                            // Initialize a TextView for ListView each Item
                            TextView tv = (TextView) view.findViewById(android.R.id.text1);

                            // Set the text color of TextView (ListView Item)
                            tv.setTextColor(Color.BLACK);

                            // Generate ListView Item using TextView
                            return view;
                        }
                    };
                    listView.setAdapter(arrayAdapter);
                }
            }
        });
    }

    /* Turn off the bluetooth when OFF button is clicked */
    private void bluetoothOFFMethod() {
        buttonOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBluetoothAdapter.isEnabled()) {
                    myBluetoothAdapter.disable();
                }
            }
        });
    }

    /* Match the bluetooth connection request code with result code to decide if connection is established or not */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCodeForEnable) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Bluetooth is enabled.", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Bluetooth enabling cancelled.", Toast.LENGTH_LONG).show();
            }
        }
    }

    /* Turn off the bluetooth when OFF button is clicked */
    private void bluetoothONMethod () {
        buttonON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myBluetoothAdapter==null)
                {
                    Toast.makeText(getApplicationContext(), "Bluetooth not support on this device.", Toast.LENGTH_LONG).show();
                }
                else {
                    if(!myBluetoothAdapter.isEnabled()) {
                        startActivityForResult(btEnablingIntent, requestCodeForEnable);
                    }
                }
            }

        });
    }

}

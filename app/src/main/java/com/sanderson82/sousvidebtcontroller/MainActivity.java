package com.sanderson82.sousvidebtcontroller;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener, BTMessageListener, View.OnClickListener {

    private SeekBar seekbar1;
    private int desiredTemp;
    private TextView result;
    private Button connectButton;
    private BluetoothController btController;
    private Handler handler;
    private TextView status;
    private TextView currentTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekbar1 = (SeekBar)findViewById(R.id.seekBar1);
        result = (TextView)findViewById(R.id.tvResult);
        status = (TextView)findViewById(R.id.statusMessage);
        currentTemp = (TextView)findViewById(R.id.currentTemp);
        connectButton = (Button)findViewById(R.id.connectButton);
        connectButton.setOnClickListener(this);

        //set change listener
        seekbar1.setOnSeekBarChangeListener(this);

        handler = new Handler(Looper.myLooper());

        btController = new BluetoothController(this, handler);
        btController.setBTListener(this);
        connectService();
 ;

    }

    public void connectService(){
        try {
            message("Connecting...");
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter.isEnabled()) {
                btController.start();
                btController.connectDevice("HC-06");
                message("Btservice started - listening");
            } else {
                message("Btservice started - bluetooth is not enabled");
            }
        } catch(Exception e){
            message("Unable to connect bt ");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        desiredTemp = progress;
        result.setText("Value: " + desiredTemp);
        btController.sendMessage(String.valueOf(desiredTemp));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void message(String message) {
        status.append(message+"\n");
        if(message.contains("Received Message:"))
        {
            String temp = message.substring(17);
            currentTemp.setText(temp.trim());
        }
    }

    @Override
    public void onClick(View v) {
        if(v == connectButton)
            connectService();

    }
}

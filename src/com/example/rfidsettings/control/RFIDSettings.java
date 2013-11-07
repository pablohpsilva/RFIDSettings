package com.example.rfidsettings.control;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.AudioManager;
import android.net.wifi.WifiManager;

public class RFIDSettings {
	private Context context;
	private WifiManager wifimgmt = null;
	private AudioManager audiomgmt = null;
	private BluetoothAdapter bluetoothmgmt = null;
	
	public RFIDSettings(Context cont){
		this.context = cont;
	}
	
	public void changeWifi(boolean enabled){
		if(this.wifimgmt == null)
			this.wifimgmt = (WifiManager) this.context.getSystemService(Context.WIFI_SERVICE);
		
		if(enabled)
			this.wifimgmt.setWifiEnabled(true);
		else
			this.wifimgmt.setWifiEnabled(false);
		this.wifimgmt.saveConfiguration();
	}
	
	public void changeVolume(boolean enabled){
		if(this.audiomgmt == null)
			this.audiomgmt = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
		
		if(enabled)
			this.audiomgmt.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		else
			this.audiomgmt.setRingerMode(AudioManager.RINGER_MODE_SILENT);
	}
	
	public void changeVibrate(boolean enabled){
		if(this.audiomgmt == null)
			this.audiomgmt = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
		
		if(enabled)
			this.audiomgmt.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		else
			this.audiomgmt.setRingerMode(AudioManager.RINGER_MODE_SILENT);
	}
	
	public void changeBluetooth(boolean enabled){
		if(this.bluetoothmgmt == null)
			this.bluetoothmgmt = BluetoothAdapter.getDefaultAdapter();
		
		if(enabled)
			this.bluetoothmgmt.enable();
		else
			this.bluetoothmgmt.disable();
	}
}

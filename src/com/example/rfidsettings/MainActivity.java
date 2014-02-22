package com.example.rfidsettings;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.Toast;
import com.example.rfidsettings.control.GlobalSingletonPool;
import com.example.rfidsettings.model.RFIDTag;

public class MainActivity extends Activity {
	static String TAG = "NFCREADER";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    
	    //The line below is necessary for the well being of the project
	    GlobalSingletonPool.getInstance().setMainObjects(this);
	    
	    // 0 -> TRUE
	    // 1 -> FALSE
	    // String TagID, String name, Integer threeG, Integer bluetooth, Integer wifi, Integer volume, Integer vibrate
	    GlobalSingletonPool.getInstance().getRFIDTagDAO().insert(new RFIDTag("-45-5615-79", "CardA", 0, 0, 0, 1, 0));
	    //GlobalSingletonPool.getInstance().getRFIDTagDAO().insert(new RFIDTag("-9370-47-100", "CardB", 0, 1, 1, 0, 1));
	    //GlobalSingletonPool.getInstance().getRFIDTagDAO().insert(new RFIDTag("-92-128850", "StickerA", 0, 0, 0, 0, 1));
	    GlobalSingletonPool.getInstance().getRFIDTagDAO().insert(new RFIDTag("-10761850", "StickerB", 0, 0, 1, 1, 0));
	    
	    setContentView(R.layout.activity_main);
	    //info = (TextView)findViewById(R.id.action_settings);
	}
	
	public void onStart(){
		super.onStart();
	}
	
	public void onPause() {
	    super.onPause();
	    
	    GlobalSingletonPool.getInstance().getNFCForegroundUtil().disableForeground();
	}   

	public void onResume() {
	    super.onResume();
	    
	    //On resume, I have to reset the MainObjects. Reason: Thread onResume() is not the same as on onCreate()
	    GlobalSingletonPool.getInstance().setMainObjects(this);
	    GlobalSingletonPool.getInstance().getNFCForegroundUtil().enableForeground();

	    this.checkNFC();
	}
		
	public void onNewIntent(Intent intent) {
		GlobalSingletonPool.getInstance().setObject("tag",intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
		
		//Testing purposes... Checking all the elements registered inside the DB.
		/*
		for(RFIDTag elem : GlobalSingletonPool.getInstance().getRFIDTagDAO().getAll())
	    	System.out.println(elem.getName());
	    */
		
	    StringBuilder sb = this.parseBits();
	    
	    RFIDTag tag = GlobalSingletonPool.getInstance().getRFIDTagDAO().get(sb.toString());
	    
	    this.activateTag(tag);
		
		// Testing purposes... Just checking how to open an application.
		/*
		if(tag.getTagID().equals("-10761850")){
			Intent intent2 = new Intent(Intent.ACTION_VIEW);
		    intent2.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
		    startActivity(intent2);
		}
		*/
	}
	
	private void activateTag(RFIDTag tag){
		if (tag == null)
			System.out.println("Open magic window to register this tag");
		else{
			// Apply all the changes from the tag.
		    System.out.println("Tag registered with the ID: " + tag.getTagID());
			GlobalSingletonPool.getInstance().getRFIDSettings().applyChanges(tag);
		}
	}
	
	private StringBuilder parseBits(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < GlobalSingletonPool.getInstance().getTag().getId().length; i++){
	    	sb.append(Integer.valueOf(GlobalSingletonPool.getInstance().getTag().getId()[i]));
	    }
		return sb;
	}
	
	private void checkNFC(){
		if (!GlobalSingletonPool.getInstance().getNFCForegroundUtil().getNfc().isEnabled()){
	        Toast.makeText(getApplicationContext(), "Please activate NFC and press Back to return to the application!", Toast.LENGTH_LONG).show();
	        startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
	    }
	}
}
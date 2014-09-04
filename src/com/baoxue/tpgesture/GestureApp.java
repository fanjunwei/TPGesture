package com.baoxue.tpgesture;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;

public class GestureApp extends Application {
	private static IGestureService mService;
	private static GestureApp thisApp;

	public static IGestureService getService() {
		if (mService == null) {
			thisApp.initService(); 
		}
		return mService;
	}

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = IGestureService.Stub.asInterface(service);
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(thisApp); 
			boolean gestureEnable = preferences.getBoolean(
					Settings.GestureWakeupKey, false);
			try {
				mService.setGestureEnable(gestureEnable);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
			initService();
		}
	};

	public void initService() {
		bindService(new Intent(this, GestureService.class), serviceConnection,
				Context.BIND_AUTO_CREATE);

	}

	@Override
	public void onCreate() {
		super.onCreate();
		thisApp = this;
		initService();

	}
}

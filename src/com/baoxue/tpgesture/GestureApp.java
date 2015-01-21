package com.baoxue.tpgesture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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

	private void initDefautlPreferences() {
		if (!loadConfigFromFile()) {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(thisApp);
			if (!preferences.getBoolean("loaded_default", false)) {
				Editor editor = preferences.edit();
				editor.putBoolean("gesture_wakeup",
						getResources().getBoolean(R.bool.gesture_wakeup));
				editor.putBoolean("gesture_double_click", getResources()
						.getBoolean(R.bool.gesture_double_click));
				editor.putBoolean("gesture_c",
						getResources().getBoolean(R.bool.gesture_c));
				editor.putBoolean("gesture_e",
						getResources().getBoolean(R.bool.gesture_e));
				editor.putBoolean("gesture_W",
						getResources().getBoolean(R.bool.gesture_W));
				editor.putBoolean("gesture_O",
						getResources().getBoolean(R.bool.gesture_O));
				editor.putBoolean("gesture_M",
						getResources().getBoolean(R.bool.gesture_M));
				editor.putBoolean("gesture_Z",
						getResources().getBoolean(R.bool.gesture_Z));
				editor.putBoolean("gesture_V",
						getResources().getBoolean(R.bool.gesture_V));
				editor.putBoolean("gesture_S",
						getResources().getBoolean(R.bool.gesture_S));
				editor.putBoolean("gesture_up",
						getResources().getBoolean(R.bool.gesture_up));
				editor.putBoolean("gesture_down",
						getResources().getBoolean(R.bool.gesture_down));
				editor.putBoolean("gesture_left",
						getResources().getBoolean(R.bool.gesture_left));
				editor.putBoolean("gesture_right",
						getResources().getBoolean(R.bool.gesture_right));

				editor.putBoolean("loaded_default", true);
				editor.commit();
			}
		}
	}

	private boolean loadConfigFromFile() {
		String path = "/system/gesture_default";
		File file = new File(path);
		if (!file.exists()) {
			return false;
		} else {
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(thisApp);
			if (!preferences.getBoolean("loaded_default", false)) {
				Editor editor = preferences.edit();
				try {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(new FileInputStream(file)));
					String line;
					while ((line = reader.readLine()) != null) {
						String args[] = line.split("\\|");
						if (args.length == 3) {
							String key = args[0];
							if ("1".equals(args[1])) {
								editor.putBoolean(key, true);
							} else {
								editor.putBoolean(key, false);
							}
							editor.putString(key + "_fun", args[2]);
						}
					}
					reader.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				editor.commit();
			}
			return true;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
		thisApp = this;
		initDefautlPreferences();
		initService();

	}
}

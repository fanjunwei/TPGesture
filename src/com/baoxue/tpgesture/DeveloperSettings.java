package com.baoxue.tpgesture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.widget.Toast;

public class DeveloperSettings extends PreferenceFragment {

	Preference last_gesture;
	Preference save_config;
	IGestureService mService = GestureApp.getService();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting_developer);
		last_gesture = findPreference("last_gesture");
		save_config = findPreference("save_config");
		setLastGesture();

	}

	void setLastGesture() {
		if (mService != null) {
			try {
				String key = mService.readLastGestureKey();
				if (key != null) {
					last_gesture.setSummary(key);
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (preference == last_gesture) {
			setLastGesture();
		} else if (preference == save_config) {
			saveConfig();
		}
		return true;
	}

	@Override
	public void onResume() {
		setLastGesture();
		super.onResume();
	}

	private String getGestureConfig(String key) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		String res = key + "|";
		if (preferences.getBoolean(key, false)) {
			res += "1|";
		} else {
			res += "0|";
		}
		res += preferences.getString(key + "_fun", Settings.MODE_WAKEUP);
		return res;

	}

	private String getSDPath() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		} else {
			return null;
		}
	}

	private void saveConfig() {
		String sdpath = getSDPath();
		if (sdpath != null) {
			String path = sdpath + "/gesture_default";
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
			try {
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(
						new FileOutputStream(path)));
				writer.println(getGestureConfig("gesture_wakeup"));
				writer.println(getGestureConfig("gesture_double_click"));
				writer.println(getGestureConfig("gesture_c"));
				writer.println(getGestureConfig("gesture_e"));
				writer.println(getGestureConfig("gesture_W"));
				writer.println(getGestureConfig("gesture_O"));
				writer.println(getGestureConfig("gesture_M"));
				writer.println(getGestureConfig("gesture_Z"));
				writer.println(getGestureConfig("gesture_V"));
				writer.println(getGestureConfig("gesture_S"));
				writer.println(getGestureConfig("gesture_up"));
				writer.println(getGestureConfig("gesture_down"));
				writer.println(getGestureConfig("gesture_left"));
				writer.println(getGestureConfig("gesture_right"));
				writer.close();
				Toast.makeText(getActivity(), "Complate", Toast.LENGTH_SHORT).show();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}

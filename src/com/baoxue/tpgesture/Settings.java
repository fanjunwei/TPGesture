package com.baoxue.tpgesture;

import android.os.Bundle;
import android.os.RemoteException;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

public class Settings extends PreferenceFragment {

	public static String GestureWakeupKey = "gesture_wakeup";
	SwitchPreference gesture_wakeup;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);
		gesture_wakeup = (SwitchPreference) findPreference(GestureWakeupKey);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (gesture_wakeup == preference) {
			if (GestureApp.getService() != null) {
				try {
					GestureApp.getService().setGestureEnable(
							gesture_wakeup.isChecked());
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

}

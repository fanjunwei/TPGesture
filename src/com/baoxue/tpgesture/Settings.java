package com.baoxue.tpgesture;

import android.os.Bundle;
import android.os.RemoteException;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

public class Settings extends PreferenceFragment {

	public static String GestureWakeupKey = "gesture_wakeup";
	public static String GesturesGroup = "gestures_group";
	SwitchPreference gesture_wakeup;
	PreferenceCategory gestures_group;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);
		gesture_wakeup = (SwitchPreference) findPreference(GestureWakeupKey);

		gestures_group = (PreferenceCategory) findPreference(GesturesGroup);
		gestures_group.setEnabled(gesture_wakeup.isChecked());

		gesture_wakeup
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference arg0,
							Object arg1) {
						if (GestureApp.getService() != null) {
							gestures_group.setEnabled(!gesture_wakeup
									.isChecked());
							try {
								GestureApp.getService().setGestureEnable(
										!gesture_wakeup.isChecked());
							} catch (RemoteException e) {
								e.printStackTrace();
							}
						}
						return true;
					}
				});
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		return true;
	}

}

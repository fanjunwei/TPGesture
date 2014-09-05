package com.baoxue.tpgesture;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

public class Settings extends PreferenceFragment implements
		OnPreferenceClickListener {

	public static String GestureWakeupKey = "gesture_wakeup";
	public static String GesturesGroup = "gestures_group";
	SwitchPreference gesture_wakeup;
	PreferenceCategory gestures_group;

	Preference gesture_double_click;
	Preference gesture_c;
	Preference gesture_e;
	Preference gesture_W;
	Preference gesture_O;
	Preference gesture_M;
	Preference gesture_Z;
	Preference gesture_V;
	Preference gesture_S;
	Preference gesture_up;
	Preference gesture_down;
	Preference gesture_left;
	Preference gesture_right;

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
		gesture_double_click = findPreference("gesture_double_click");
		gesture_double_click.setOnPreferenceClickListener(this);

		gesture_c = findPreference("gesture_c");
		gesture_c.setOnPreferenceClickListener(this);

		gesture_e = findPreference("gesture_e");
		gesture_e.setOnPreferenceClickListener(this);

		gesture_W = findPreference("gesture_W");
		gesture_W.setOnPreferenceClickListener(this);

		gesture_O = findPreference("gesture_O");
		gesture_O.setOnPreferenceClickListener(this);

		gesture_M = findPreference("gesture_M");
		gesture_M.setOnPreferenceClickListener(this);

		gesture_Z = findPreference("gesture_Z");
		gesture_Z.setOnPreferenceClickListener(this);

		gesture_V = findPreference("gesture_V");
		gesture_V.setOnPreferenceClickListener(this);

		gesture_S = findPreference("gesture_S");
		gesture_S.setOnPreferenceClickListener(this);

		gesture_up = findPreference("gesture_up");
		gesture_up.setOnPreferenceClickListener(this);

		gesture_down = findPreference("gesture_down");
		gesture_down.setOnPreferenceClickListener(this);

		gesture_left = findPreference("gesture_left");
		gesture_left.setOnPreferenceClickListener(this);

		gesture_right = findPreference("gesture_right");
		gesture_right.setOnPreferenceClickListener(this);

	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		final FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
//		ModeSettings modeSettings=new ModeSettings();
//		Bundle bundle=new Bundle();
//		bundle.putString("key", preference.getKey());
//		bundle.putString("title", preference.getTitle().toString());
//		modeSettings.setArguments(bundle);
//		transaction.replace(android.R.id.content, modeSettings);
//		transaction.addToBackStack(null);
//		transaction.commit();
		return false;
	}

}

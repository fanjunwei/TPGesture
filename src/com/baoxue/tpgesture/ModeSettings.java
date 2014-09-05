package com.baoxue.tpgesture;

import android.os.Bundle;
import android.os.RemoteException;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

public class ModeSettings extends PreferenceFragment {

	Preference mode_group;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting_mode);
		mode_group = findPreference("mode_group");
		mode_group.setTitle(getArguments().getString("title"));

	}

}

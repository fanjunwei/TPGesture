package com.baoxue.tpgesture;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();

		actionBar.setDisplayShowHomeEnabled(true);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new GestureSwitch()).commit();
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//	    MenuInflater inflater = getMenuInflater();
//	    inflater.inflate(R.menu.main_activity, menu);
//	    return true;
//	}

	public static class GestureSwitch extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.setting);
		}

	}
}

package com.baoxue.tpgesture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Settings extends PreferenceFragment implements
		OnPreferenceClickListener {

	public static final String GestureWakeupKey = "gesture_wakeup";
	public static final String GesturesGroup = "gestures_group";
	SwitchPreference gesture_wakeup;
	PreferenceCategory gestures_group;

	public static final String MODE_DEFAUT = "wakeup";
	public static final String MODE_WAKEUP = "wakeup";
	public static final String MODE_UNLCOK = "unlcok";
	public static final String MODE_PLAY = "play";
	public static final String MODE_PREVIOUS = "previous";
	public static final String MODE_NEXT = "next";
	public static final String MODE_RUN = "run";

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

	PreferenceScreen developer;

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

		developer = (PreferenceScreen) findPreference("developer");
		if (!"eng".equals(Build.TYPE)) {
			getPreferenceScreen().removePreference(developer);
		}

	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if (preference == developer) {
			FragmentTransaction transaction = getFragmentManager()
					.beginTransaction();
			transaction.replace(android.R.id.content, new DeveloperSettings());
			transaction.addToBackStack(null);
			transaction.commit();
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	private void setSummary(String key) {
		Preference preference = findPreference(key);
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		String func_config = preferences.getString(key + "_fun", MODE_DEFAUT);
		String mode = func_config.split(";")[0];
		String[] mode_values = getResources().getStringArray(
				R.array.mode_values);
		String[] mode_names = getResources().getStringArray(
				R.array.mode_entries);
		String mode_name = null;

		for (int i = 0; i < mode_values.length; i++) {
			if (mode_values[i].equals(mode)) {
				mode_name = mode_names[i];
				break;
			}
		}
		if (mode_name != null) {
			if (MODE_RUN.equals(mode)) {
				String args[] = func_config.split(";");
				if (args.length >= 2) {
					String comName = args[1];
					for (int i = 0; i < mlistAppInfo.size(); i++) {
						try {
							if (comName.equals(mlistAppInfo.get(i)
									.getComponentName())) {
								mode_name += ":"
										+ mlistAppInfo.get(i).getAppLabel();
							}
						} catch (Throwable ex) {
						}
					}
				}
			}
			preference.setSummary(mode_name);

		}
	}

	private void setAllSummary() {
		setSummary("gesture_double_click");
		setSummary("gesture_c");
		setSummary("gesture_e");
		setSummary("gesture_W");
		setSummary("gesture_O");
		setSummary("gesture_M");
		setSummary("gesture_Z");
		setSummary("gesture_V");
		setSummary("gesture_S");
		setSummary("gesture_up");
		setSummary("gesture_down");
		setSummary("gesture_left");
		setSummary("gesture_right");

	}

	List<AppInfo> mlistAppInfo = new ArrayList<AppInfo>();
	List<String> mlistAppNames = new ArrayList<String>();

	public void initAllActivity() {
		mlistAppInfo.clear();
		mlistAppNames.clear();
		PackageManager pm = getActivity().getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,
				PackageManager.MATCH_DEFAULT_ONLY);
		Collections.sort(resolveInfos,
				new ResolveInfo.DisplayNameComparator(pm));
		for (ResolveInfo reInfo : resolveInfos) {
			String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
			String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
			String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
			Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
			// 为应用程序的启动Activity 准备Intent
			Intent launchIntent = new Intent();
			launchIntent.setComponent(new ComponentName(pkgName, activityName));
			AppInfo appInfo = new AppInfo();
			appInfo.setAppLabel(appLabel);
			appInfo.setPkgName(pkgName);
			appInfo.setAppIcon(icon);
			appInfo.setIntent(launchIntent);
			mlistAppInfo.add(appInfo);
			mlistAppNames.add(appLabel);
		}

	}

	private void setPreferenceFun(String key, String fun) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		Editor edit = preferences.edit();
		edit.putString(key + "_fun", fun);
		edit.commit();
	}

	String selected_mode;
	String selected_activity;
	int curr_fun_set_state;
	AlertDialog dialog;

	@Override
	public boolean onPreferenceClick(final Preference preference) {
		ListView view = new ListView(getActivity());
		final List<String> data = new ArrayList<String>(
				Arrays.asList(getActivity().getResources().getStringArray(
						R.array.mode_entries)));
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_dropdown_item,
				data);
		view.setAdapter(adapter);
		curr_fun_set_state = 0;
		view.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// data.clear();
				// data.addAll(Arrays.asList(tem_array));
				// adapter.notifyDataSetChanged();
				if (curr_fun_set_state == 0) {
					String[] mode_values = getResources().getStringArray(
							R.array.mode_values);
					selected_mode = mode_values[position];
					if (MODE_RUN.equals(selected_mode)) {
						curr_fun_set_state = 1;
						data.clear();
						data.addAll(mlistAppNames);
						adapter.notifyDataSetChanged();
					} else {
						String key = preference.getKey();
						setPreferenceFun(key, selected_mode);
						setSummary(key);
						dialog.dismiss();

					}
				} else {
					String key = preference.getKey();
					AppInfo app = mlistAppInfo.get(position);
					selected_activity = app.getComponentName();
					setPreferenceFun(key, selected_mode + ";"
							+ selected_activity);
					setSummary(key);
					dialog.dismiss();
				}

			}
		});
		dialog = new AlertDialog.Builder(getActivity())
				.setTitle(R.string.func_config)
				.setView(view)
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						}).show();
		return false;
	}

	@Override
	public void onResume() {
		initAllActivity();
		setAllSummary();
		super.onResume();
	}

}

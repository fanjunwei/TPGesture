package com.baoxue.tpgesture;

import android.content.Context;
import android.preference.SwitchPreference;
import android.util.AttributeSet;

public class HftSwitchPreference extends SwitchPreference {

	public HftSwitchPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public HftSwitchPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HftSwitchPreference(Context context) {
		super(context);
	}

	@Override
	protected void onClick() {
//		OnPreferenceClickListener listener= getOnPreferenceClickListener();
//		if(listener!=null)
//		{
//			listener.onPreferenceClick(this);
//		}
	}


	
	
	
	

}

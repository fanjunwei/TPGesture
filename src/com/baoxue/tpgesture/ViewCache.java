package com.baoxue.tpgesture;

import android.view.View;
import android.widget.TextView;

public class ViewCache {

	private TextView txt;

	View _baseView = null;

	public ViewCache(View baseView) {
		_baseView = baseView;
	}

	public TextView getTxt() {
		if (txt == null) {
			txt = (TextView) _baseView
					.findViewById(R.id.txt);
		}
		return txt;
	}



}

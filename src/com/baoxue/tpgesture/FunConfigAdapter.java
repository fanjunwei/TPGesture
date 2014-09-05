package com.baoxue.tpgesture;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FunConfigAdapter extends BaseAdapter {

	List<String> dataList = null;
	Activity context = null;

	public final static int STATE_DOWNLOAD = 0;
	public final static int STATE_COMPLATE = 1;
	private int currState = STATE_DOWNLOAD;

	public FunConfigAdapter(Activity activity, List<String> dataList) {

		this.dataList = dataList;
		context = activity;
	}

	public Activity getContext() {
		return context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Activity activity = (Activity) getContext();
		View rowView = convertView;
		ViewCache viewCache;
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.func_config_row, null);
			viewCache = new ViewCache(rowView);
			rowView.setTag(viewCache);
		} else {
			viewCache = (ViewCache) rowView.getTag();
		}
		try {
			String item = getItem(position);
			viewCache.getTxt().setText(item);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return rowView;
	}

	@Override
	public int getCount() {
		return dataList.size();

	}

	@Override
	public String getItem(int location) {

		if (location < dataList.size()) {
			return dataList.get(location);
		} else {
			return null;
		}

	}

	@Override
	public long getItemId(int location) {
		return location;
	}

	public int getCurrState() {
		return currState;
	}

	public void setCurrState(int currState) {
		this.currState = currState;
	}

}

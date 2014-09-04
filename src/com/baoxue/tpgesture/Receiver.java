package com.baoxue.tpgesture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {

	static boolean registeredScreenAction = false;
	Context mContext;

	void lockUnlockScreen(boolean isToUnlcok) {

		if (GestureApp.getService() != null) {
			Log.d("tttt", "run service method");
			try {
				if (isToUnlcok) {
					GestureApp.getService().unlockScreen();
				} else {
					GestureApp.getService().lockScreen();
				}
			} catch (RemoteException ex) {
				ex.printStackTrace();
			}
		}

	}

	void initService() {

	}

	public Receiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		KeyEvent keyEvent = (KeyEvent) intent
				.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
		Log.d("tttt", "onReceive");
		if (intent.getAction() == Intent.ACTION_MEDIA_BUTTON) {
			if (KeyEvent.KEYCODE_BUTTON_1 == keyEvent.getKeyCode()
					&& keyEvent.getAction() == KeyEvent.ACTION_UP) {
				if (GestureApp.getService() != null) {
					try {
						GestureApp.getService().runGesture();
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

}

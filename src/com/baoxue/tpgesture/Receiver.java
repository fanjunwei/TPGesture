package com.baoxue.tpgesture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {

	WakeLock mWakelock;
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

	private void readGesture() {
		File dev = new File("/sys/gsl_touchscreen/gesture");
		if (dev.exists()) {
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(new FileInputStream(dev)));
				reader.readLine();
				reader.readLine();
				String line = reader.readLine();
				String[] args = line.split(":");
				if (args.length == 2) {
					Log.d("tttt", args[1]);
					Toast.makeText(mContext.getApplicationContext(), args[1],
							Toast.LENGTH_LONG).show();
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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

				PowerManager pm = (PowerManager) context
						.getSystemService(Context.POWER_SERVICE);
				mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
						| PowerManager.SCREEN_DIM_WAKE_LOCK, "Gesture");
				mWakelock.acquire();
				mWakelock.release();
				lockUnlockScreen(true);
				readGesture();

			}
		}

	}

}

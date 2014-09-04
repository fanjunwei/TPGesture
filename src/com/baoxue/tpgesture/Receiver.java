package com.baoxue.tpgesture;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;

public class Receiver extends BroadcastReceiver {

	WakeLock mWakelock;
	static boolean registeredScreenAction = false;
	Context mContext;

	static IGestureService mService;

	private ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = IGestureService.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}
	};

	void lockUnlockScreen(boolean isToUnlcok) {
		if (mService == null) {
			Log.d("tttt","bind service");
			mContext.getApplicationContext().bindService(new Intent(mContext.getApplicationContext(), GestureService.class),
					serviceConnection, Context.BIND_AUTO_CREATE);
		}
		if (mService != null) {
			Log.d("tttt","run service method");
			try {
				if (isToUnlcok) {
					mService.unlockScreen();
				} else {
					mService.lockScreen();
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

				PowerManager pm = (PowerManager) context
						.getSystemService(Context.POWER_SERVICE);
				mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
						| PowerManager.SCREEN_DIM_WAKE_LOCK, "Gesture");
				mWakelock.acquire();
				mWakelock.release();
				lockUnlockScreen(true);

			}
		}

	}

}

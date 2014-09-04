package com.baoxue.tpgesture;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class GestureService extends Service {

	boolean registeredScreenAction;
	KeyguardLock mKeyguardLock;

	class ScreenOnOffReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction() == Intent.ACTION_SCREEN_OFF) {
				Log.d("tttt", "on off onReceive");
				lockScreenLocked();
			}

		}
	}

	public class GestureServiceImpl extends IGestureService.Stub {

		@Override
		public void unlockScreen() throws RemoteException {
			unlockScreenLocked();

		}

		@Override
		public void lockScreen() throws RemoteException {
			lockScreenLocked();
		}

	}

	@Override
	public void onCreate() {
		super.onCreate();
		registerScreenActionReceiver();

	}

	@Override
	public IBinder onBind(Intent arg0) {
		return new GestureServiceImpl();
	}

	private void registerScreenActionReceiver() {
		if (!registeredScreenAction) {
			registeredScreenAction = true;
			Log.d("tttt", "registerScreenActionReceiver");
			final IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_SCREEN_OFF);
			filter.addAction(Intent.ACTION_SCREEN_ON);
			BroadcastReceiver receiver = new ScreenOnOffReceiver();
			getApplicationContext().registerReceiver(receiver, filter);
		}
	}

	public void unlockScreenLocked() {
		initKeyguardLock();
		if (mKeyguardLock != null) {
			Log.d("tttt", "unlockScreenLocked");
			mKeyguardLock.disableKeyguard();
		}

	}

	public void lockScreenLocked() {
		initKeyguardLock();
		if (mKeyguardLock != null) {
			mKeyguardLock.reenableKeyguard();
		}
	}

	private void initKeyguardLock() {
		if (mKeyguardLock == null) {
			KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);

			mKeyguardLock = keyguardManager.newKeyguardLock("gesture");
			mKeyguardLock.disableKeyguard();
		}
	}

}
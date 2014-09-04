package com.baoxue.tpgesture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class GestureService extends Service {

	boolean registeredScreenAction;
	KeyguardLock mKeyguardLock;
	WakeLock mWakelock;

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

		@Override
		public void setGestureEnable(boolean enable) throws RemoteException {

			setGestureDevEnable(enable);
		}

		@Override
		public void runGesture() throws RemoteException {
			String c = readGesture();
			String key = devGestureToPreferenceKey(c);
			if (key != null) {
				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(GestureService.this);
				boolean enable = preferences.getBoolean(key, false);
				if (enable) {
					wakeUp();
				}

			}
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

	private String devGestureToPreferenceKey(String c) {

		if ("C".equals(c)) {
			return "gesture_c";
		} else if ("E".equals(c)) {
			return "gesture_e";
		} else if ("W".equals(c)) {
			return "gesture_W";
		} else if ("O".equals(c)) {
			return "gesture_O";
		} else if ("M".equals(c)) {
			return "gesture_M";
		} else if ("Z".equals(c)) {
			return "gesture_Z";
		} else if ("V".equals(c)) {
			return "gesture_V";
		} else if ("S".equals(c)) {
			return "gesture_S";
		} else if ("3".equals(c)) {
			return "gesture_up";
		} else if ("2".equals(c)) {
			return "gesture_down";
		} else if ("4".equals(c)) {
			return "gesture_left";
		} else if ("1".equals(c)) {
			return "gesture_right";
		} else if ("*".equals(c)) {
			return "gesture_double_click";
		}
		return null;
	}

	private String readGesture() {
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
					String c = args[1];
					Log.d("tttt", c);
					return c;
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	private void wakeUp() {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.SCREEN_DIM_WAKE_LOCK, "Gesture");
		mWakelock.acquire();
		mWakelock.release();
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

	private void setGestureDevEnable(boolean enable) {
		File dev = new File("/sys/gsl_touchscreen/gesture");
		if (dev.exists()) {
			try {
				FileOutputStream out = new FileOutputStream(dev);
				byte[] buffer;
				if (enable) {
					buffer = new byte[] { '1' };
				} else {
					buffer = new byte[] { '0' };
				}
				out.write(buffer);
				out.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}

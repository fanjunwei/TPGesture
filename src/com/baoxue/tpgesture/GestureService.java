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
import android.content.ComponentName;
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

public class GestureService extends Service {

	boolean registeredScreenAction;
	KeyguardLock mKeyguardLock;
	WakeLock mWakelock;
	long lastRunTime = 0;

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
			synchronized (this) {
				long currTime = System.currentTimeMillis();
				if (currTime - lastRunTime < 2000) {
					return;
				}
				lastRunTime = currTime;
			}
			String c = readGesture();
			String key = devGestureToPreferenceKey(c);
			if (key != null) {
				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(GestureService.this);
				boolean enable = preferences.getBoolean(key, false);
				if (enable) {
					String args[] = preferences.getString(key + "_fun",
							Settings.MODE_WAKEUP).split(";");
					String mode = args[0];
					Log.d("tttt", "run_mode=" + mode);
					if (Settings.MODE_WAKEUP.equals(mode)) {
						wakeUp();
					} else if (Settings.MODE_UNLCOK.equals(mode)) {
						wakeUp();
						unlockScreenLocked();
					} else if (Settings.MODE_RUN.equals(mode)) {
						wakeUp();
						unlockScreenLocked();
						try {
							if (args.length > -2) {
								String com = args[1];
								String com_args[] = com.split("/");
								if (com_args.length == 2) {
									Intent launchIntent = new Intent();
									launchIntent
											.setComponent(new ComponentName(
													com_args[0], com_args[1]));
									launchIntent
											.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									launchIntent.setAction(Intent.ACTION_MAIN);
									getApplication()
											.startActivity(launchIntent);

								}
							}
						} catch (Throwable ex) {
						}
					} else if (Settings.MODE_PLAY.equals(mode)) {
						wakeUp();
						sendKey("MEDIA_PLAY_PAUSE");
					} else if (Settings.MODE_NEXT.equals(mode)) {
						wakeUp();
						sendKey("MEDIA_NEXT");
					} else if (Settings.MODE_PREVIOUS.equals(mode)) {
						wakeUp();
						sendKey("MEDIA_PREVIOUS");
					}
				}

			}
		}

		@Override
		public String readLastGestureKey() throws RemoteException {
			String c = readGesture();
			return devGestureToPreferenceKey(c);

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

	private void sendKey(final String keyName) {
		new Thread() {

			@Override
			public void run() {
				Log.d("tttt", "send:" + keyName);
				String cmd = "input keyevent " + keyName;
				Utility.runCommand(cmd);
			}

		}.start();

	}
}

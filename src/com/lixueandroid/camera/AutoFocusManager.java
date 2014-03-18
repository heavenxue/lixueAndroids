package com.lixueandroid.camera;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.lixue.lixueandroid.R;

/**
 * 自动对焦管理器
 * @author Administrator
 *
 */
public final class AutoFocusManager implements Camera.AutoFocusCallback {

	  private static final String TAG = AutoFocusManager.class.getSimpleName();
	  /**每隔多少时间自动对焦一次，单位为毫秒**/
	  private static final long AUTO_FOCUS_INTERVAL_MS = 2000L;
	  /**对焦模式集合**/
	  private static final Collection<String> FOCUS_MODES_CALLING_AF;
	  static {
	    FOCUS_MODES_CALLING_AF = new ArrayList<String>(2);
	    FOCUS_MODES_CALLING_AF.add(Camera.Parameters.FOCUS_MODE_AUTO);
	    FOCUS_MODES_CALLING_AF.add(Camera.Parameters.FOCUS_MODE_MACRO);//微距（特写）对焦模式。
	  }

	  private boolean active;
	  /**是否允许用户自动对焦**/
	  private final boolean useAutoFocus;
	  private final Camera camera;
	  private AsyncTask<?,?,?> outstandingTask;

	  public AutoFocusManager(Context context, Camera camera) {
	    this.camera = camera;
	    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
	    String currentFocusMode = camera.getParameters().getFocusMode();
	    useAutoFocus =sharedPrefs.getBoolean(context.getString(R.string.isAutoFocus), true) &&FOCUS_MODES_CALLING_AF.contains(currentFocusMode);
	    Log.i(TAG, "Current focus mode '" + currentFocusMode + "'; use auto focus? " + useAutoFocus);
	    start();
	  }

	  @Override
	  public synchronized void onAutoFocus(boolean success, Camera theCamera) {
	    if (active) {
	      outstandingTask = new AutoFocusTask();
	    }
	  }

	  synchronized void start() {
	    if (useAutoFocus) {
	      active = true;
	      try {
	        camera.autoFocus(this);
	      } catch (RuntimeException re) {
	        // Have heard RuntimeException reported in Android 4.0.x+; continue?
	        Log.w(TAG, "Unexpected exception while focusing", re);
	      }
	    }
	  }

	  synchronized void stop() {
	    if (useAutoFocus) {
	      try {
	        camera.cancelAutoFocus();
	      } catch (RuntimeException re) {
	        // Have heard RuntimeException reported in Android 4.0.x+; continue?
	        Log.w(TAG, "Unexpected exception while cancelling focusing", re);
	      }
	    }
	    if (outstandingTask != null) {
	      outstandingTask.cancel(true);
	      outstandingTask = null;
	    }
	    active = false;
	  }

	  private final class AutoFocusTask extends AsyncTask<Object,Object,Object> {
	    @Override
	    protected Object doInBackground(Object... voids) {
	      try {
	        Thread.sleep(AUTO_FOCUS_INTERVAL_MS);
	      } catch (InterruptedException e) {
	        // continue
	      }
	      synchronized (AutoFocusManager.this) {
	        if (active) {
	          start();
	        }
	      }
	      return null;
	    }
	  }
	}
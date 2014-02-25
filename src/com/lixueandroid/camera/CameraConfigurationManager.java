package com.lixueandroid.camera;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Camera;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.lixue.lixueandroid.R;


/**
 * 相机配置管理器
 *
 */
public final class CameraConfigurationManager {

	  private static final String TAG = "CameraConfiguration";

	  //这是比能够支持最小的屏幕稍大一点的尺寸，一般情况下会选择320*240，为防止在分辨率非常低的设备上会不支持
	  private static final int MIN_PREVIEW_PIXELS = 480 * 320; // normal screen
	  //private static final float MAX_EXPOSURE_COMPENSATION = 1.5f;
	  //private static final float MIN_EXPOSURE_COMPENSATION = 0.0f;
	  //最大失真宽高比
	  private static final double MAX_ASPECT_DISTORTION = 0.15;

	  private final Context context;
	  private Point screenResolution;//屏幕分辨率
	  private Point cameraResolution;//相机分辨率

	  CameraConfigurationManager(Context context) {
	    this.context = context;
	  }

	  /**
	   * 初始化相机参数，从所需应用上的相机中只读一次
	   */
	 public  void initFromCameraParameters(Camera camera) {
	    Camera.Parameters parameters = camera.getParameters();
	    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//	    Display display = manager.getDefaultDisplay();
//	    Point theScreenResolution = new Point();
//	    display.getSize(theScreenResolution);
//	    screenResolution = theScreenResolution;
	    DisplayMetrics dm = new DisplayMetrics();   
	    manager.getDefaultDisplay().getMetrics(dm);   
	    screenResolution.set(dm.widthPixels, dm.heightPixels);
	    Log.i(TAG, "Screen resolution: " + screenResolution);
	    cameraResolution = findBestPreviewSizeValue(parameters, screenResolution);
	    Log.i(TAG, "Camera resolution: " + cameraResolution);
	  }

	 public void setDesiredCameraParameters(Camera camera, boolean safeMode) {
	    Camera.Parameters parameters = camera.getParameters();

	    if (parameters == null) {
	      Log.w(TAG, "Device error: no camera parameters are available. Proceeding without configuration.");
	      return;
	    }

	    Log.i(TAG, "Initial camera parameters: " + parameters.flatten());

	    if (safeMode) {
	      Log.w(TAG, "In camera config safe mode -- most settings will not be honored");
	    }

	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

//	    initializeTorch(parameters, prefs, safeMode);

	    String focusMode = null;
	    /**是否自动对焦**/
	    if (prefs.getBoolean(context.getString(R.string.isAutoFocus), true)) {
	    	/**是否连续自动对焦**/
	      if (safeMode || prefs.getBoolean(context.getString(R.string.isContinuedFocus), false)) {
	        focusMode = findSettableValue(parameters.getSupportedFocusModes(), Camera.Parameters.FOCUS_MODE_AUTO);
	      } else {
	        focusMode = findSettableValue(parameters.getSupportedFocusModes(),Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE,Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO,Camera.Parameters.FOCUS_MODE_AUTO);
	      }
	    }
	    // Maybe selected auto-focus but not available, so fall through here:
	    if (!safeMode && focusMode == null) {
	      focusMode = findSettableValue(parameters.getSupportedFocusModes(),Camera.Parameters.FOCUS_MODE_MACRO,Camera.Parameters.FOCUS_MODE_EDOF);
	    }
	    if (focusMode != null) {
	      parameters.setFocusMode(focusMode);
	    }
	    /**是否倒置扫描**/
	    if (prefs.getBoolean(context.getString(R.string.isInvertScan), false)) {
	      String colorMode = findSettableValue(parameters.getSupportedColorEffects(), Camera.Parameters.EFFECT_NEGATIVE);
	      if (colorMode != null) {
	        parameters.setColorEffect(colorMode);
	      }
	    }

	    parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
	    camera.setParameters(parameters);

	    Camera.Parameters afterParameters = camera.getParameters();
	    Camera.Size afterSize = afterParameters.getPreviewSize();
	    if (afterSize!= null && (cameraResolution.x != afterSize.width || cameraResolution.y != afterSize.height)) {
	      Log.w(TAG, "Camera said it supported preview size " + cameraResolution.x + 'x' + cameraResolution.y +", but after setting it, preview size is " + afterSize.width + 'x' + afterSize.height);
	      cameraResolution.x = afterSize.width;
	      cameraResolution.y = afterSize.height;
	    }
	  }

	  public Point getCameraResolution() {
	    return cameraResolution;
	  }

	  public Point getScreenResolution() {
	    return screenResolution;
	  }

	  public boolean getTorchState(Camera camera) {
	    if (camera != null) {
	      Camera.Parameters parameters = camera.getParameters();
	      if (parameters != null) {
	        String flashMode = camera.getParameters().getFlashMode();
	        return flashMode != null &&
	            (Camera.Parameters.FLASH_MODE_ON.equals(flashMode) ||
	             Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode));
	      }
	    }
	    return false;
	  }

	  public void setTorch(Camera camera, boolean newSetting) {
	    Camera.Parameters parameters = camera.getParameters();
	    doSetTorch(parameters, newSetting, false);
	    camera.setParameters(parameters);
	  }

//	  private void initializeTorch(Camera.Parameters parameters, SharedPreferences prefs, boolean safeMode) {
//	    boolean currentSetting = FrontLightMode.readPref(prefs) == FrontLightMode.ON;
//	    doSetTorch(parameters, currentSetting, safeMode);
//	  }

	  private void doSetTorch(Camera.Parameters parameters, boolean newSetting, boolean safeMode) {
	    String flashMode;
	    if (newSetting) {
	      flashMode = findSettableValue(parameters.getSupportedFlashModes(),Camera.Parameters.FLASH_MODE_TORCH,Camera.Parameters.FLASH_MODE_ON);
	    } else {
	      flashMode = findSettableValue(parameters.getSupportedFlashModes(),Camera.Parameters.FLASH_MODE_OFF);
	    }
	    if (flashMode != null) {
	      parameters.setFlashMode(flashMode);
	    }

	    /*
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
	    if (!prefs.getBoolean(PreferencesActivity.KEY_DISABLE_EXPOSURE, false)) {
	      if (!safeMode) {
	        int minExposure = parameters.getMinExposureCompensation();
	        int maxExposure = parameters.getMaxExposureCompensation();
	        if (minExposure != 0 || maxExposure != 0) {
	          float step = parameters.getExposureCompensationStep();
	          int desiredCompensation;
	          if (newSetting) {
	            // Light on; set low exposue compensation
	            desiredCompensation = Math.max((int) (MIN_EXPOSURE_COMPENSATION / step), minExposure);
	          } else {
	            // Light off; set high compensation
	            desiredCompensation = Math.min((int) (MAX_EXPOSURE_COMPENSATION / step), maxExposure);
	          }
	          Log.i(TAG, "Setting exposure compensation to " + desiredCompensation + " / " + (step * desiredCompensation));
	          parameters.setExposureCompensation(desiredCompensation);
	        } else {
	          Log.i(TAG, "Camera does not support exposure compensation");
	        }
	      }
	    }
	     */
	  }

	  /**
	   * 得到相机分辨率
	 * @param parameters
	 * @param screenResolution
	 * @return
	 */
	private Point findBestPreviewSizeValue(Camera.Parameters parameters, Point screenResolution) {
	    List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
	    if (rawSupportedSizes == null) {
	      Log.w(TAG, "Device returned no supported preview sizes; using default");
	      Camera.Size defaultSize = parameters.getPreviewSize();
	      return new Point(defaultSize.width, defaultSize.height);
	    }
	    // Sort by size, descending
	    List<Camera.Size> supportedPreviewSizes = new ArrayList<Camera.Size>(rawSupportedSizes);
	    Collections.sort(supportedPreviewSizes, new Comparator<Camera.Size>() {
		      @Override
		      public int compare(Camera.Size a, Camera.Size b) {
			        int aPixels = a.height * a.width;
			        int bPixels = b.height * b.width;
			        if (bPixels < aPixels) {
			          return -1;
			        }
			        if (bPixels > aPixels) {
			          return 1;
			        }
			        return 0;
			      }
		    });

	    if (Log.isLoggable(TAG, Log.INFO)) {
	      StringBuilder previewSizesString = new StringBuilder();
	      for (Camera.Size supportedPreviewSize : supportedPreviewSizes) {
	        previewSizesString.append(supportedPreviewSize.width).append('x').append(supportedPreviewSize.height).append(' ');
	      }
	      Log.i(TAG, "Supported preview sizes: " + previewSizesString);
	    }
	    //屏幕宽高比
	    double screenAspectRatio = (double) screenResolution.x / (double) screenResolution.y;

	    // 去掉不合适的
	    Iterator<Camera.Size> it = supportedPreviewSizes.iterator();
	    while (it.hasNext()) {
	      Camera.Size supportedPreviewSize = it.next();
	      int realWidth = supportedPreviewSize.width;
	      int realHeight = supportedPreviewSize.height;
	      if (realWidth * realHeight < MIN_PREVIEW_PIXELS) {
	        it.remove();
	        continue;
	      }

	      boolean isCandidatePortrait = realWidth < realHeight;
	      int maybeFlippedWidth = isCandidatePortrait ? realHeight : realWidth;
	      int maybeFlippedHeight = isCandidatePortrait ? realWidth : realHeight;
	      double aspectRatio = (double) maybeFlippedWidth / (double) maybeFlippedHeight;
	      double distortion = Math.abs(aspectRatio - screenAspectRatio);
	      if (distortion > MAX_ASPECT_DISTORTION) {
	        it.remove(); 
	        continue;
	      }

	      if (maybeFlippedWidth == screenResolution.x && maybeFlippedHeight == screenResolution.y) {
	        Point exactPoint = new Point(realWidth, realHeight);
	        Log.i(TAG, "Found preview size exactly matching screen size: " + exactPoint);
	        return exactPoint;
	      }
	    }

	    // If no exact match, use largest preview size. This was not a great idea on older devices because
	    // of the additional computation needed. We're likely to get here on newer Android 4+ devices, where
	    // the CPU is much more powerful.
	    if (!supportedPreviewSizes.isEmpty()) {
	      Camera.Size largestPreview = supportedPreviewSizes.get(0);
	      Point largestSize = new Point(largestPreview.width, largestPreview.height);
	      Log.i(TAG, "Using largest suitable preview size: " + largestSize);
	      return largestSize;
	    }

	    // If there is nothing at all suitable, return current preview size
	    Camera.Size defaultPreview = parameters.getPreviewSize();
	    Point defaultSize = new Point(defaultPreview.width, defaultPreview.height);
	    Log.i(TAG, "No suitable preview sizes, using default: " + defaultSize);
	    return defaultSize;
	  }

	  private static String findSettableValue(Collection<String> supportedValues, String... desiredValues) {
	    Log.i(TAG, "Supported values: " + supportedValues);
	    String result = null;
	    if (supportedValues != null) {
	      for (String desiredValue : desiredValues) {
	        if (supportedValues.contains(desiredValue)) {
	          result = desiredValue;
	          break;
	        }
	      }
	    }
	    Log.i(TAG, "Settable value: " + result);
	    return result;
	  }
	}
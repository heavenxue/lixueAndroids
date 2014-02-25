package com.lixueandroid.camera;

import android.hardware.Camera;
import android.util.Log;

/**
 * 打开摄像头--接口
 * @author LIXUE
 *
 */
public final class OpenCameraInterface {

	  private static final String TAG = OpenCameraInterface.class.getName();

	  private OpenCameraInterface() {
	  }

	  /**
	   * 如果存在多个摄像头，就打开指定的摄像头，否则就只打开摄像头0
	   * Opens a rear-facing camera with {@link Camera#open(int)}, if one exists, or opens camera 0.
	   */
	  public static Camera open() {
	    
	    int numCameras = Camera.getNumberOfCameras();
	    if (numCameras == 0) {
	      Log.w(TAG, "No cameras!");
	      return null;
	    }

	    int index = 0;
	    while (index < numCameras) {
	      Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
	      Camera.getCameraInfo(index, cameraInfo);
	      //相机朝向的方向。如果是后置摄像头就打开它，否则就当找不到
	      if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
	        break;
	      }
	      index++;
	    }
	    
	    Camera camera;
	    if (index < numCameras) {
	      Log.i(TAG, "Opening camera #" + index);
	      camera = Camera.open(index);
	    } else {
	      Log.i(TAG, "No camera facing back; returning camera #0");
	      camera = Camera.open(0);
	    }

	    return camera;
	  }
	}


package com.example.hackgt.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.exceptions.*;
import com.example.hackgt.R;

public class ARActivity extends AppCompatActivity {
    private Session arSession;
    private boolean mUserRequestedInstall = true;
    private static final int CAMERA_PERMISSION_CODE = 0;
    private CameraDevice cameraDevice;

    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            try {
                if (arSession == null) {
                    arSession = new Session(ARActivity.this);
                    Config config = new Config(arSession);
                    config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
                    config.setFocusMode(Config.FocusMode.AUTO);
                    arSession.configure(config);
                }
                // Start the session with the new configuration
                arSession.resume();
            } catch (Exception e) {
                Log.e("ARActivity", "Exception starting AR session", e);
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            closeCamera();
            Log.e("CameraState", "Camera has been disconnected");
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            closeCamera();
            Log.e("CameraState", "Error with camera device: " + error);
            Toast.makeText(ARActivity.this, "Error with camera device.", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!hasCameraPermission()) {
            requestCameraPermission();
        } else {
            initializeAR();
        }
    }

    private void initializeAR() {
        if (arSession == null) {
            try {
                switch (ArCoreApk.getInstance().requestInstall(this, mUserRequestedInstall)) {
                    case INSTALLED:
                        openCamera();
                        break;
                    case INSTALL_REQUESTED:
                        mUserRequestedInstall = false;
                        return;
                }
            } catch (UnavailableException e) {
                handleException(e);
                return;
            }
        }
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeAR();
            } else {
                Toast.makeText(this, "Camera permission is required to use AR features.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = manager.getCameraIdList()[0]; // Assumes back-facing camera
            if (hasCameraPermission()) {
                manager.openCamera(cameraId, stateCallback, null);
            }
        } catch (CameraAccessException e) {
            Log.e("ARActivity", "Failed to open Camera", e);
        }
    }

    private void closeCamera() {
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (arSession != null) {
            arSession.pause();
            arSession.close();
            arSession = null;
        }
    }

    private void handleException(Exception e) {
        Log.e("ARActivity", "Exception in AR setup: " + e.getMessage(), e);
        Toast.makeText(this, "Error initializing ARCore: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (arSession != null) {
            arSession.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeCamera();
    }
}
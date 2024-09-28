package com.example.hackgt.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
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

import java.io.IOException;
import java.io.InputStream;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Point;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.mlkit.common.sdkinternal.model.ModelLoader;


public class ARActivity extends AppCompatActivity {
    private Session arSession;
    private boolean mUserRequestedInstall = true;
    private static final int CAMERA_PERMISSION_CODE = 0;
    private CameraDevice cameraDevice;

    // AR Fragment to handle plane detection and AR UI interactions
    private ArFragment arFragment;
    private ModelRenderable modelRenderable;

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

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ar_fragment);

        // Load the model
        loadModel();

        // Set a tap listener to place the object in AR
        arFragment.setOnTapArPlaneListener((HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
            if (modelRenderable == null) {
                return;
            }

            // Create the Anchor
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());

            // Create the transformable object and attach the model to it
            TransformableNode modelNode = new TransformableNode(arFragment.getTransformationSystem());
            modelNode.setParent(anchorNode);
            modelNode.setRenderable(modelRenderable);
            modelNode.select();
        });
    }

    private void loadModel() {
        ModelRenderable.builder()
                .setSource(this, Uri.parse("pug.glb"))
                .build()
                .thenAccept(renderable -> {
                    modelRenderable = renderable;
                    // setTapListener(); // Removed as it is not defined
                })
                .exceptionally(throwable -> {
                    Log.e("ModelLoading", "Unable to load 3D model", throwable);
                    Toast.makeText(this, "Unable to load 3D model: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    return null;
                });
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
        // Check if ARCore is installed and ready to use
        try {
            switch (ArCoreApk.getInstance().requestInstall(this, mUserRequestedInstall)) {
                case INSTALLED:
                    // ARCore is already installed, continue AR initialization
                    if (arSession == null) {
                        arSession = new Session(this);
                        Config config = new Config(arSession);
                        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
                        arSession.configure(config);
                    }
                    break;
                case INSTALL_REQUESTED:
                    // Request ARCore installation
                    mUserRequestedInstall = false;
                    return;
            }
        } catch (UnavailableArcoreNotInstalledException |
                 UnavailableUserDeclinedInstallationException e) {
            Toast.makeText(this, "Please install ARCore", Toast.LENGTH_LONG).show();
            return;
        } catch (UnavailableApkTooOldException e) {
            Toast.makeText(this, "Please update ARCore", Toast.LENGTH_LONG).show();
            return;
        } catch (UnavailableSdkTooOldException e) {
            Toast.makeText(this, "Please update this app", Toast.LENGTH_LONG).show();
            return;
        } catch (UnavailableDeviceNotCompatibleException e) {
            Toast.makeText(this, "This device does not support AR", Toast.LENGTH_LONG).show();
            return;
        } catch (Exception e) {
            Toast.makeText(this, "Failed to initialize AR session", Toast.LENGTH_LONG).show();
            return;
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

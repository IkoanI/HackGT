package com.example.hackgt.View;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.exceptions.*;
import com.example.hackgt.R;

public class ARActivity extends AppCompatActivity {
    private Session arSession;
    private boolean isARCoreSupportedAndInstalled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);

        // Check if ARCore is installed and supported on the device
        try {
            checkARCoreSupport();
        } catch (UnavailableDeviceNotCompatibleException e) {
            throw new RuntimeException(e);
        } catch (UnavailableUserDeclinedInstallationException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkARCoreSupport() throws UnavailableDeviceNotCompatibleException, UnavailableUserDeclinedInstallationException {
        // Check if ARCore is supported and installed
        ArCoreApk.Availability availability = ArCoreApk.getInstance().checkAvailability(this);
        if (availability.isSupported()) {
            if (availability == ArCoreApk.Availability.SUPPORTED_INSTALLED) {
                try {
                    // Create a new ARCore session and configure it
                    arSession = new Session(this);
                    Config config = new Config(arSession);
                    config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
                    config.setPlaneFindingMode(Config.PlaneFindingMode.HORIZONTAL_AND_VERTICAL);
                    arSession.configure(config);
                    isARCoreSupportedAndInstalled = true;
                } catch (UnavailableException e) {
                    e.printStackTrace();
                    isARCoreSupportedAndInstalled = false;
                }
            } else {
                // ARCore is supported but not installed; prompt user to install it
                ArCoreApk.getInstance().requestInstall(this, true);
                isARCoreSupportedAndInstalled = false;
            }
        } else {
            // ARCore is not supported on this device
            isARCoreSupportedAndInstalled = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (arSession != null) {
            try {
                arSession.resume();
            } catch (CameraNotAvailableException e) {
                Log.e("ARActivity", "Camera not available on resume", e);
                handleCameraError();
            }
        }
    }

    private void handleCameraError() {
        // Implement recovery or fallback logic here
        Toast.makeText(this, "Camera error occurred. Please try again.", Toast.LENGTH_LONG).show();
        finish();  // Close the activity or prompt the user to restart the camera
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
        if (arSession != null) {
            try {
                arSession.close();
            } catch (Exception e) {
                e.printStackTrace();
                // Handle any exception while closing the session
            } finally {
                arSession = null;
            }
        }
    }


}

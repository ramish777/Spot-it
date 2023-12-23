package com.ramish.spotit;
import androidx.appcompat.app.AppCompatActivity;

import com.akexorcist.screenshotdetection.ScreenshotDetectionDelegate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ScreenshotDetectionActivity extends AppCompatActivity implements ScreenshotDetectionDelegate.ScreenshotDetectionListener {

    private ScreenshotDetectionDelegate screenshotDetectionDelegate = new ScreenshotDetectionDelegate(this, this);

    @Override
    protected void onStart() {
        super.onStart();
        screenshotDetectionDelegate.startScreenshotDetection();
    }

    @Override
    protected void onStop() {
        super.onStop();
        screenshotDetectionDelegate.stopScreenshotDetection();
    }

    @Override
    public void onScreenCaptured(String path) {
        // Do something when the screen was captured
    }

    @Override
    public void onScreenCapturedWithDeniedPermission() {
        // Do something when the screen was captured, but read external storage permission was denied
    }

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION = 3009;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkReadExternalStoragePermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                showReadExternalStoragePermissionDeniedMessage();
            }
        }
    }

    private void checkReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestReadExternalStoragePermission();
        }
    }

    private void requestReadExternalStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE_PERMISSION);
    }

    private void showReadExternalStoragePermissionDeniedMessage() {
        Toast.makeText(this, "Read external storage permission has been denied", Toast.LENGTH_SHORT).show();
    }
}
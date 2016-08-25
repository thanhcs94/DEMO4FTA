package com.traceverified;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int MY_PERMISSION_REQUEST_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            new IntentIntegrator(this).initiateScan(); // `this` is the current Activity
        } else {
            requestCameraPermission();
        }
        findViewById(R.id.imageView2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                new IntentIntegrator(MainActivity.this).initiateScan(); // `this` is the current Activity
            }
        });

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                     @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSION_REQUEST_CAMERA) {
            return;
        }
        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(findViewById(R.id.main), "Camera permission was granted.", Snackbar.LENGTH_SHORT).show();
            new IntentIntegrator(this).initiateScan(); // `this` is the current Activity
        } else {
            Snackbar.make(findViewById(R.id.main), "Camera permission request was denied.", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Snackbar.make(findViewById(R.id.main), "Camera access is required to display the camera preview.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override public void onClick(View view) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[] { Manifest.permission.CAMERA }, MY_PERMISSION_REQUEST_CAMERA);
                }
            }).show();
        } else {
            Snackbar.make(findViewById(R.id.main), "Permission is not available. Requesting camera permission.",
                    Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA },
                    MY_PERMISSION_REQUEST_CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String dinhdang = data.getStringExtra("RESULT_FORMAT");
                String ketqua = data.getStringExtra("SCAN_RESULT");
                Log.d("KetQua", dinhdang + " - " + ketqua);
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(ketqua + ""));
                    startActivity(i);
                    overridePendingTransition(0, 0);
                }catch (Exception e){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    Log.wtf("URI" , "http://"+ketqua + "");
                    i.setData(Uri.parse("http://"+ketqua + ""));
                    startActivity(i);
                    overridePendingTransition(0, 0);
                }
            }
        }
    }
}

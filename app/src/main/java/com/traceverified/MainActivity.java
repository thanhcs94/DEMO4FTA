package com.traceverified;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new IntentIntegrator(this).initiateScan(); // `this` is the current Activity
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String dinhdang = data.getStringExtra("RESULT_FORMAT");
                String ketqua = data.getStringExtra("SCAN_RESULT");
                Log.d("KetQua", dinhdang + " - " + ketqua);
                if (ketqua.contains("http")) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(ketqua));
                    startActivity(i);
                    overridePendingTransition(0, 0);
                } else {
                    // show the scanner result into dialog box.
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Scan Result");
                    builder.setMessage(ketqua);
                    AlertDialog alert1 = builder.create();
                    alert1.show();
                }
            }
        }
    }
}

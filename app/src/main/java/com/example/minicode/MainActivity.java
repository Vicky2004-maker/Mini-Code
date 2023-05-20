package com.example.minicode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.minicode.Helper.*;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sfEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        sharedPreferences = MainActivity.this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        sfEditor = sharedPreferences.edit();

        if(sharedPreferences.getBoolean("privacy-policy", false)) {
            if(checkPermissions(MainActivity.this)) {
                loadActivity(1500);
            } else {
                launchPermission(MainActivity.this);
            }
        } else {
            privacyPolicyDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_REQ_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(!sharedPreferences.getBoolean("privacy-policy", false)) {
                    privacyPolicyDialog();
                } else {
                    loadActivity(1500);
                }
            } else {
                launchPermission(MainActivity.this);
            }
        }
    }

    private void privacyPolicyDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Privacy Policy")
                .setMessage("Accept the privacy policy to continue using the app.")
                .setCancelable(false)
                .setNeutralButton("Show", (dialog, which) -> {
                    //TODO: Launch Privacy Policy Website on Browser
                })
                .setPositiveButton("Accept", (dialog, result) -> {
                    sfEditor.putBoolean("privacy-policy", true);
                    sfEditor.apply();
                    if(!Helper.checkPermissions(MainActivity.this)) {
                        Helper.launchPermission(MainActivity.this);
                    }
                })
                .setNegativeButton("Decline", (dialog, result) -> MainActivity.this.finish())
                .create()
                .show();
    }

    private void loadActivity(final int delay) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, CodeViewActivity.class));
            }
        }, delay);
    }
}
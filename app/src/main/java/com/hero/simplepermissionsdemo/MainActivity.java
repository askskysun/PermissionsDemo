package com.hero.simplepermissionsdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.text1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] permissions = {
                        //日历权限
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR};
                boolean hasPermissions = PermissionUtils.requestPermissionAndCheck(MainActivity.this, permissions);
                Log.i(TAG, "检查是否有权限: " + hasPermissions);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtils.GET_PERMISSION_REQUEST) {
            PermissionUtils.requestPermissionsResult(this, permissions, grantResults);
        }
    }
}
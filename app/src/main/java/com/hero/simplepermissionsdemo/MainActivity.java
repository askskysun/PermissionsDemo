package com.hero.simplepermissionsdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //简单方式申请单个权限
        findViewById(R.id.textSimple).setOnClickListener(new View.OnClickListener() {
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

        //RxPermission申请单个权限Simple
        findViewById(R.id.textSimpleRxPermission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] permissions = {
                        //日历权限
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR};
                PermissionUtils.requestPermissionSimpleCallback(MainActivity.this, new PermissionUtils.OnPermissionsSimpleCallback() {
                    @Override
                    public void allow() {
                        Toast.makeText(MainActivity.this, "allow", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void refuse() {
                        Toast.makeText(MainActivity.this, "refuse", Toast.LENGTH_SHORT).show();
                    }
                }, permissions);
            }
        });

        //RxPermission申请单个权限详细
        findViewById(R.id.textEachRxPermission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionUtils.requestPermissionEachCallBack(MainActivity.this, Manifest.permission.READ_CALENDAR, new PermissionUtils.OnPermissionsEachCallback() {
                    @Override
                    public void allow() {
                        Toast.makeText(MainActivity.this, "allow", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void refuse() {
                        Toast.makeText(MainActivity.this, "refuse", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void refuseAndNotAskAgain() {
                        Toast.makeText(MainActivity.this, "refuseAndNotAskAgain", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //RxPermission申请多个权限合并结果
        findViewById(R.id.textEachCombinedRxPermission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] permissions = {
                        //日历权限
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR,
                        Manifest.permission.CAMERA};
                PermissionUtils.requestPermissionEachCombinedCallBack(MainActivity.this, new PermissionUtils.OnPermissionsEachCallback() {
                    @Override
                    public void allow() {
                        Toast.makeText(MainActivity.this, "allow", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void refuse() {
                        Toast.makeText(MainActivity.this, "refuse", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void refuseAndNotAskAgain() {
                        Toast.makeText(MainActivity.this, "refuseAndNotAskAgain", Toast.LENGTH_SHORT).show();
                    }
                }, permissions);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //父类方法不能删除，否则影响rxPermission的回调
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.GET_PERMISSION_REQUEST) {
            PermissionUtils.requestPermissionsResult(this, permissions, grantResults);
        }
    }
}
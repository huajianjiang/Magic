package com.github.huajianjiang.magic.sample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.huajianjiang.magic.core.aspect.permission.PermProcessor;
import com.github.huajianjiang.magic.core.module.RuntimePermissionModule;
import com.github.huajianjiang.magic.core.util.*;

import magic.annotation.RequirePermission;

public class MainActivity extends AppCompatActivity implements RuntimePermissionModule {

    private static final String TAG = MainActivity.class.getSimpleName();

    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPerm("Perm");
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPerm2();
            }
        });

        mRootView = findViewById(R.id.rootView);
    }

    @RequirePermission(value = {Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA}, explain = true)
    private void requestPerm2() {
        Toast.makeText(this, "All permissions granted,do something 2 ", Toast.LENGTH_SHORT).show();
    }

    @RequirePermission(value = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE}, explain = false)
    private void requestPerm(String name) {
        Toast.makeText(this, "All permissions granted,do something", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void showRequestPermissionsRationale(String[] permissions, final PermProcessor processor)
    {
        Toast.makeText(this, "showRequestPermissionsRationale", Toast.LENGTH_SHORT).show();
        for (String perm : permissions) {
            Logger.e(TAG, "showRequestPermissionsRationale=" + perm);
        }

        Snackbar.make(mRootView, "showRequestPermissionsRationale", Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        processor.requestPermissions();
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsDenied(String[] permissions, PermProcessor processor) {
        Toast.makeText(this, "onRequestPermissionsDenied", Toast.LENGTH_SHORT).show();
        for (String perm : permissions) {
            Logger.e(TAG, "onRequestPermissionsDenied=" + perm);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

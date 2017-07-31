package magic.sample;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import magic.core.aspect.permission.PermissionHandler;
import magic.core.module.RuntimePermissionModule;

import magic.annotation.RequirePermission;
import magic.annotation.SingleClick;

public class SecondActivity extends AppCompatActivity implements RuntimePermissionModule {
    private static final String TAG = SecondActivity.class.getSimpleName();

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
                requestPerm();
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPerm2();
            }
        });

        Button singleClickBtn = (Button) findViewById(R.id.singleClick);
        singleClickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            @SingleClick(4000)
            public void onClick(View v) {
                try {
                    Thread.sleep(3000);
                    Logger.e(TAG, "onSingleClick");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        mRootView = findViewById(R.id.rootView);
    }

    @RequirePermission(value = {Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA}, requestCode = 0, explain = true,limit = RequirePermission.ALL)
    private void requestPerm2() {
        Toast.makeText(this, TAG + ">All permissions granted,do something 2 ", Toast.LENGTH_SHORT).show();
    }

    @RequirePermission(value = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode = 1, explain = false,limit = RequirePermission.ANY)
    private void requestPerm() {
        Toast.makeText(this, TAG + ">All permissions granted,do something", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void showRequestPermissionsRationale(@NonNull String[] permissions,
            final PermissionHandler handler)
    {
        Toast.makeText(this, TAG + ">showRequestPermissionsRationale", Toast.LENGTH_SHORT).show();
        for (String perm : permissions) {
            Logger.e(TAG, TAG + ">showRequestPermissionsRationale=" + perm);
        }

        Snackbar.make(mRootView, TAG + ">showRequestPermissionsRationale",
                Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.requestPermissions();
            }
        }).show();
    }

    @Override
    public void onRequestPermissionsGranted(int requestCode,@NonNull String[] grantedPermissions,
            @NonNull String[] deniedPermissions) {
        if (requestCode == 0) {
            requestPerm2();
        } else if (requestCode == 1) {
            requestPerm();
        }
    }

    @Override
    public void onRequestPermissionsDenied(@NonNull String[] grantedPermissions,
            @NonNull String[] deniedPermissions) {
        Toast.makeText(this, TAG + ">onRequestPermissionsDenied", Toast.LENGTH_SHORT).show();
        for (String perm : deniedPermissions) {
            Logger.e(TAG, TAG + ">onRequestPermissionsDenied=" + perm);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

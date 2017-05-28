package xyz.velvetmilk.nyaanyaamusicplayer.activity;

import android.Manifest;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import xyz.velvetmilk.nyaanyaamusicplayer.BuildConfig;
import xyz.velvetmilk.nyaanyaamusicplayer.R;
import xyz.velvetmilk.nyaanyaamusicplayer.ui.dialogfragment.AboutDialogFragment;
import xyz.velvetmilk.nyaanyaamusicplayer.ui.fragment.MusicListFragment;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 0;


    //=========================================================================
    // Activity lifecycle
    //=========================================================================

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if (hasPermissions()) {
            setFragment(MusicListFragment.newInstance());
        }
    }


    //=========================================================================
    // Activity request permissions callbacks
    //=========================================================================

    @Override
    public void onRequestPermissionsResult(final int resultCode, final String[] permissions,
                                           final int[] grantResults) {
        if (BuildConfig.DEBUG) Log.w(TAG, "onCreateOptionsMenu");

        switch (resultCode) {
            case PERMISSION_REQUEST_CODE:
                if (checkPermissionGrantResults(grantResults)) {
                    setFragment(MusicListFragment.newInstance());
                } else {
                    finish();
                }
                break;
            default:
                if (BuildConfig.DEBUG) Log.w(TAG, "onCreateOptionsMenu: Unhandled result code");
        }
    }


    //=========================================================================
    // Activity menu callbacks
    //=========================================================================

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreateOptionsMenu");

        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onOptionsItemSelected");

        int id = item.getItemId();

        switch (id) {
            case R.id.actionbar_homelink:
                Snackbar.make(findViewById(android.R.id.content), "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;

            case R.id.actionbar_settings:
                Toast toast = Toast.makeText(this, R.string.app_name, Toast.LENGTH_LONG);
                toast.show();
                return true;

            case R.id.actionbar_about:
                DialogFragment about = AboutDialogFragment.newInstance();
                setDialogFragment(about);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //=========================================================================
    // Helper functions
    //=========================================================================

    protected void setDialogFragment(final DialogFragment dialog) {
        if (BuildConfig.DEBUG) Log.d(TAG, "setDialogFragment");

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(null);

        dialog.show(ft, null);
    }

    protected void setFragment(final Fragment fragment) {
        if (BuildConfig.DEBUG) Log.d(TAG, "setFragment");

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(android.R.id.content, fragment);
        ft.commit();
    }

    private boolean hasPermissions() {
        if (BuildConfig.DEBUG) Log.d(TAG, "hasPermissions");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;

        String[] permissions = new String[1];
        permissions[0] = Manifest.permission.READ_EXTERNAL_STORAGE;
        List<String> permissionList = new ArrayList<String>();


        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        String[] neededPermissions = new String[permissionList.size()];
        neededPermissions = permissionList.toArray(neededPermissions);

        if (neededPermissions.length > 0) {
            requestPermissions(neededPermissions, PERMISSION_REQUEST_CODE);
        }

        return neededPermissions.length == 0;
    }

    private boolean checkPermissionGrantResults(final int[] grantResults) {
        if (BuildConfig.DEBUG) Log.d(TAG, "checkPermissionGrantResults");

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}

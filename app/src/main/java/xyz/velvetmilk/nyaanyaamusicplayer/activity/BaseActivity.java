package xyz.velvetmilk.nyaanyaamusicplayer.activity;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import xyz.velvetmilk.nyaanyaamusicplayer.BuildConfig;
import xyz.velvetmilk.nyaanyaamusicplayer.R;
import xyz.velvetmilk.nyaanyaamusicplayer.ui.dialogfragment.AboutDialogFragment;
import xyz.velvetmilk.nyaanyaamusicplayer.ui.fragment.MusicListFragment;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        setFragment(MusicListFragment.newInstance("ad", "bc"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreateOptionsMenu");

        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onOptionsItemSelected");

        int id = item.getItemId();
        boolean ret = false;

        switch (id) {
            case R.id.actionbar_homelink:
                Snackbar.make(findViewById(android.R.id.content), "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                ret = true;
                break;
            case R.id.actionbar_settings:
                Toast.makeText(this, R.string.app_name, Toast.LENGTH_LONG);
                ret = true;
                break;
            case R.id.actionbar_about:
                DialogFragment about = AboutDialogFragment.newInstance();
                setDialogFragment(about);
                ret = true;
                break;
            default:
                ret = super.onOptionsItemSelected(item);
        }

        return ret;
    }


    protected void setDialogFragment(DialogFragment dialog) {
        if (BuildConfig.DEBUG) Log.d(TAG, "setDialogFragment");

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(null);

        dialog.show(ft, null);
    }

    protected void setFragment(Fragment fragment) {
        if (BuildConfig.DEBUG) Log.d(TAG, "setFragment");

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(android.R.id.content, fragment);
        ft.commit();
    }
}

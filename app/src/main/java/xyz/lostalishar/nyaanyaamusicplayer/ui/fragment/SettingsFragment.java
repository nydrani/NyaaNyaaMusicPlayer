package xyz.lostalishar.nyaanyaamusicplayer.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.Fragment;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import xyz.lostalishar.nyaanyaamusicplayer.BuildConfig;
import xyz.lostalishar.nyaanyaamusicplayer.R;
import xyz.lostalishar.nyaanyaamusicplayer.util.PreferenceUtils;


/**
 * SettingsFragment for attaching to the activity
 */

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = SettingsFragment.class.getSimpleName();




    public static SettingsFragment newInstance() {
        if (BuildConfig.DEBUG) Log.d(TAG, "newInstance");

        return new SettingsFragment();
    }


    //=========================================================================
    // Fragment lifecycle
    //=========================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        if (BuildConfig.DEBUG) Log.d(TAG, "onPause");
        super.onPause();

        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }


    //=========================================================================
    // PreferenceFragment implementation
    //=========================================================================

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreatePreferences");

        setPreferencesFromResource(R.xml.preferences, rootKey);

        // init the preference
        Preference pref = findPreference(getString(PreferenceUtils.KEY_PREF_ABOUT_VERSION_KEY));
        if (pref != null) {
            pref.setSummary(BuildConfig.VERSION_NAME);
        }

        onSharedPreferenceChanged(PreferenceManager.getDefaultSharedPreferences(getActivity()),
                getString(PreferenceUtils.KEY_PREF_SCREEN_ROTATION_KEY));
        onSharedPreferenceChanged(PreferenceManager.getDefaultSharedPreferences(getActivity()),
                getString(PreferenceUtils.KEY_PREF_THEME_KEY));
    }

    @Override
    public Fragment getCallbackFragment() {
        if (BuildConfig.DEBUG) Log.d(TAG, "getCallbackFragment");

        return this;
    }


    //=========================================================================
    // OnSharedPreferenceChangeListener implementation
    //=========================================================================

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onSharedPreferenceChanged");

        // might need null check
        Preference pref = findPreference(key);
        if (pref == null) {
            return;
        }

        if (key.equals(getString(PreferenceUtils.KEY_PREF_SCREEN_ROTATION_KEY))) {
            ListPreference listPreference = (ListPreference) pref;
            listPreference.setSummary(listPreference.getEntry());

            // set the screen orientation
            String value = listPreference.getValue();
            Log.v(TAG, "orientation value: " + value);

            //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        } else if (key.equals(getString(PreferenceUtils.KEY_PREF_THEME_KEY))) {
            ListPreference listPreference = (ListPreference) pref;
            listPreference.setSummary(listPreference.getEntry());

            // set the theme
            String value = listPreference.getValue();
            Log.v(TAG, "theme value: " + value);

            int index = listPreference.findIndexOfValue(value);
            switch (index) {
                case 1:
                    // switch to light theme
                    break;
                case 2:
                    // switch to dark theme
                    break;
                default:
                    Log.w(TAG, "index: " + index + " not handled!");
            }

        } else if (key.equals(getString(PreferenceUtils.KEY_PREF_ANONYMOUS_DATA_KEY))) {
            SwitchPreference switchPreference = (SwitchPreference) pref;

            if (switchPreference.isChecked()) {
                switchPreference.setSummary(switchPreference.getSwitchTextOn());
            } else {
                switchPreference.setSummary(switchPreference.getSwitchTextOff());
            }
        }
    }
}

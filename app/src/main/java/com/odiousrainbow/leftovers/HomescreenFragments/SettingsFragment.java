package com.odiousrainbow.leftovers.HomescreenFragments;
import com.odiousrainbow.leftovers.BuildConfig;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import com.odiousrainbow.leftovers.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        PreferenceManager preferences = getPreferenceManager();
        int versionCode = BuildConfig.VERSION_CODE;
        //preferences.findPreference("reset").setSummary(versionCode);
    }
}

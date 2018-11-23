package com.odiousrainbow.leftovers.HomescreenFragments;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odiousrainbow.leftovers.Activities.AddStuffDetailsActivity;
import com.odiousrainbow.leftovers.Activities.EditStuffDetailsActivity;
import com.odiousrainbow.leftovers.Activities.MainActivity;
import com.odiousrainbow.leftovers.BuildConfig;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import com.odiousrainbow.leftovers.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        PreferenceManager preferences = getPreferenceManager();
        int versionCode = BuildConfig.VERSION_CODE;
        preferences.findPreference("reset_app").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getActivity()).setTitle("Cài đặt lại")
                        .setMessage("Tất cả danh sách nguyên liệu và món ăn yêu thích sẽ bị xóa. Bạn chắc chắn chứ?")
                        .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key),getActivity().MODE_PRIVATE);
                                sharedPreferences.edit().clear().apply();
                                Intent backToMainScreen = new Intent(getActivity(),MainActivity.class);
                                backToMainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(backToMainScreen);
                            }
                        })
                        .setNegativeButton("Quay lại",null)
                        .show();
                return true;
            }
        });
    }
}

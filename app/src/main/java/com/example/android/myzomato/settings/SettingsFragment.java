
package com.example.android.myzomato.settings;

import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.android.myzomato.R;


/**
 * The SettingsFragment serves as the display for all of the user's settings.
 *
 * You need to modify build gradle by compile 'com.android.support:preference-v7:25.0.1'
 * create fragment from activity_settings
 * create new xml file, where all settings parts will be created
 * add correct preferene into styles: <item name="preferenceTheme">@style/PreferenceThemeOverlay</item>
 *
 *
 */
public class SettingsFragment extends PreferenceFragmentCompat {    // for setting change



    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState,
                                    String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);


        //price_checkbox
        final CheckBoxPreference checkbox2 = (CheckBoxPreference) findPreference("price_checkbox");
        final CheckBoxPreference checkbox1 = (CheckBoxPreference) findPreference("rating_checkbox");

        // check for rating preference
        checkbox1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (checkbox1.isChecked()) {
                    checkbox2.setEnabled(false);
                } else {
                    checkbox2.setEnabled(true);
                }
                return true;
            }
        });

        // check for price preference
        checkbox2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (checkbox2.isChecked()) {
                    checkbox1.setEnabled(false);
                } else {
                    checkbox1.setEnabled(true);
                }
                return true;
            }
        });

        //To persist the change when activity is closed, you need to get the preferences references like this
        persistCheckBoxState(checkbox1, checkbox2);
        persistCheckBoxState(checkbox2, checkbox1);
    }



    public void persistCheckBoxState (CheckBoxPreference first, CheckBoxPreference second) {
        if (first.isChecked ()){
            second.setEnabled(false);
        } else {
            second.setEnabled(true);
        }
    }




}
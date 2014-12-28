/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gb.shamu.settings;

import gb.shamu.settings.R;
import gb.shamu.settings.util.AppMultiSelectListPreference;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.preference.SeekBarPreference;
import android.provider.Settings;
import android.support.v4.preference.PreferenceFragment;
import android.text.TextUtils;

public class ACSBSettings extends PreferenceFragment implements OnPreferenceChangeListener {

	private static final String TAG = "AppCircleSideBar";

	private static final String KEY_ENABLE_APP_CIRCLE_BAR = "enable_app_circle_bar";
	private static final String KEY_INCLUDE_APP_CIRCLE_BAR = "app_circle_bar_included_apps";
	private static final String KEY_TRIGGER_WIDTH = "trigger_width";
	private static final String KEY_TRIGGER_TOP = "trigger_top";
	private static final String KEY_TRIGGER_BOTTOM = "trigger_bottom";

	private AppMultiSelectListPreference mIncludedAppCircleBar;
	private SeekBarPreference mTriggerWidthPref;
	private SeekBarPreference mTriggerTopPref;
	private SeekBarPreference mTriggerBottomPref;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ContentResolver resolver = getActivity().getContentResolver();

		addPreferencesFromResource(R.xml.acsb_settings);
		PreferenceScreen screen = getPreferenceScreen();
		
		screen.findPreference(KEY_ENABLE_APP_CIRCLE_BAR).setOnPreferenceChangeListener(this);

		mIncludedAppCircleBar = (AppMultiSelectListPreference) screen.findPreference(KEY_INCLUDE_APP_CIRCLE_BAR);
		Set<String> includedApps = getIncludedApps();
		if (includedApps != null)
			mIncludedAppCircleBar.setValues(includedApps);
		mIncludedAppCircleBar.setOnPreferenceChangeListener(this);

		mTriggerWidthPref = (SeekBarPreference) findPreference(KEY_TRIGGER_WIDTH);
		mTriggerWidthPref.setProgress(Settings.System
				.getInt(resolver, Settings.System.APP_CIRCLE_BAR_TRIGGER_WIDTH, 10));
		mTriggerWidthPref.setOnPreferenceChangeListener(this);

		mTriggerTopPref = (SeekBarPreference) findPreference(KEY_TRIGGER_TOP);
		mTriggerTopPref.setProgress(Settings.System.getInt(resolver, Settings.System.APP_CIRCLE_BAR_TRIGGER_TOP, 0));
		mTriggerTopPref.setOnPreferenceChangeListener(this);

		mTriggerBottomPref = (SeekBarPreference) findPreference(KEY_TRIGGER_BOTTOM);
		mTriggerBottomPref.setProgress(Settings.System.getInt(resolver, Settings.System.APP_CIRCLE_BAR_TRIGGER_HEIGHT,
				100));
		mTriggerBottomPref.setOnPreferenceChangeListener(this);

	}

	@Override
	public void onPause() {
		super.onPause();
		Settings.System.putInt(getActivity().getContentResolver(), Settings.System.APP_CIRCLE_BAR_SHOW_TRIGGER, 0);
	}

	@Override
	public void onResume() {
		super.onResume();
		Settings.System.putInt(getActivity().getContentResolver(), Settings.System.APP_CIRCLE_BAR_SHOW_TRIGGER, 1);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object objValue) {
		ContentResolver resolver = getActivity().getContentResolver();
		final String key = preference.getKey();
		if (KEY_ENABLE_APP_CIRCLE_BAR.equals(key)) {
			boolean value = (Boolean) objValue;
			Settings.System.putInt(resolver, Settings.System.ENABLE_APP_CIRCLE_BAR, value ? 1 : 0);
		}
		if (KEY_INCLUDE_APP_CIRCLE_BAR.equals(key)) {
			storeIncludedApps((Set<String>) objValue);
		}
		if (preference == mTriggerWidthPref) {
			int width = ((Integer) objValue).intValue();
			Settings.System.putInt(resolver, Settings.System.APP_CIRCLE_BAR_TRIGGER_WIDTH, width);
		}
		if (preference == mTriggerTopPref) {
			int top = ((Integer) objValue).intValue();
			Settings.System.putInt(resolver, Settings.System.APP_CIRCLE_BAR_TRIGGER_TOP, top);
		}
		if (preference == mTriggerBottomPref) {
			int bottom = ((Integer) objValue).intValue();
			Settings.System.putInt(resolver, Settings.System.APP_CIRCLE_BAR_TRIGGER_HEIGHT, bottom);
		}
		return true;
	}

	private Set<String> getIncludedApps() {
		String included = Settings.System.getString(getActivity().getContentResolver(),
				Settings.System.WHITELIST_APP_CIRCLE_BAR);
		if (TextUtils.isEmpty(included)) {
			return null;
		}
		return new HashSet<String>(Arrays.asList(included.split("\\|")));
	}

	private void storeIncludedApps(Set<String> values) {
		StringBuilder builder = new StringBuilder();
		String delimiter = "";
		for (String value : values) {
			builder.append(delimiter);
			builder.append(value);
			delimiter = "|";
		}
		Settings.System.putString(getActivity().getContentResolver(), Settings.System.WHITELIST_APP_CIRCLE_BAR,
				builder.toString());
	}

}

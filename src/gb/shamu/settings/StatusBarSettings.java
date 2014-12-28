package gb.shamu.settings;

import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.v4.preference.PreferenceFragment;

public class StatusBarSettings extends PreferenceFragment implements OnPreferenceChangeListener {

	private static final String KEY_STATUS_BAR_CLOCK_STYLE = "status_bar_clock_style";
	private static final String KEY_STATUS_BAR_SHOW_BATTERY_PERCENT = "status_bar_show_battery_percent";
	private static final String KEY_STATUS_BAR_NETWORK_TRAFFIC_STATE = "status_bar_network_traffic_state";
	private static final String KEY_STATUS_BAR_BRIGHTNESS_CONTROL = "status_bar_brightness_control";
	private static final String KEY_STATUS_BAR_DOUBLE_TAP_SLEEP_GESTURE = "status_bar_double_tap_sleep_gesture";
	private static final String KEY_STATUS_BAR_QUICK_QS_PULLDOWN = "status_bar_quick_qs_pulldown";
	private static final String KEY_STATUS_BAR_SHOW_WEATHER = "status_bar_show_weather";
	private static final String KEY_STATUS_BAR_BLOCK_ON_SECURE_KEYGUARD = "status_bar_locked_on_secure_keyguard";
	private static final String KEY_STATUS_BAR_POWER_MENU = "status_bar_power_menu";

	private ListPreference mStatusBarBattery;
	private ListPreference mStatusBarPowerMenu;
	private SwitchPreference mBlockOnSecureKeyguard;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ContentResolver resolver = getActivity().getContentResolver();

		addPreferencesFromResource(R.xml.status_bar_settings);

		OnPreferenceClickListener subFragmentClickListener = new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				String fragmentName = preference.getFragment();
				((MainActivity) getActivity()).selectChildFragment(fragmentName);
				return true;
			}
		};
		findPreference(KEY_STATUS_BAR_CLOCK_STYLE).setOnPreferenceClickListener(subFragmentClickListener);
		findPreference(KEY_STATUS_BAR_NETWORK_TRAFFIC_STATE).setOnPreferenceClickListener(subFragmentClickListener);

		mStatusBarBattery = (ListPreference) findPreference(KEY_STATUS_BAR_SHOW_BATTERY_PERCENT);
		mStatusBarBattery.setOnPreferenceChangeListener(this);
		int batteryStyle = Settings.System.getInt(resolver, Settings.System.STATUS_BAR_SHOW_BATTERY_PERCENT, 0);
		mStatusBarBattery.setValue(String.valueOf(batteryStyle));
		mStatusBarBattery.setSummary(mStatusBarBattery.getEntry());

		mBlockOnSecureKeyguard = (SwitchPreference) findPreference(KEY_STATUS_BAR_BLOCK_ON_SECURE_KEYGUARD);
		mBlockOnSecureKeyguard.setOnPreferenceChangeListener(this);
		boolean checked = Settings.Secure.getInt(resolver, Settings.Secure.STATUS_BAR_LOCKED_ON_SECURE_KEYGUARD, 1) == 1;
		mBlockOnSecureKeyguard.setChecked(checked);

		mStatusBarPowerMenu = (ListPreference) findPreference(KEY_STATUS_BAR_POWER_MENU);
		mStatusBarPowerMenu.setOnPreferenceChangeListener(this);
		int statusBarPowerMenu = Settings.System.getInt(resolver, KEY_STATUS_BAR_POWER_MENU, 0);
		mStatusBarPowerMenu.setValue(String.valueOf(statusBarPowerMenu));
		mStatusBarPowerMenu.setSummary(mStatusBarPowerMenu.getEntry());

		SwitchPreference brightnessControl = (SwitchPreference) findPreference(KEY_STATUS_BAR_BRIGHTNESS_CONTROL);
		brightnessControl.setOnPreferenceChangeListener(this);
		brightnessControl
				.setChecked(Settings.System.getInt(resolver, Settings.System.STATUS_BAR_BRIGHTNESS_CONTROL, 0) == 1);

		SwitchPreference doubleTapSleep = (SwitchPreference) findPreference(KEY_STATUS_BAR_DOUBLE_TAP_SLEEP_GESTURE);
		doubleTapSleep.setOnPreferenceChangeListener(this);
		doubleTapSleep.setChecked(Settings.System.getInt(resolver, Settings.System.STATUS_BAR_DOUBLE_TAP_SLEEP_GESTURE,
				0) == 1);

		SwitchPreference quickQsPulldown = (SwitchPreference) findPreference(KEY_STATUS_BAR_QUICK_QS_PULLDOWN);
		quickQsPulldown.setOnPreferenceChangeListener(this);
		quickQsPulldown
				.setChecked(Settings.System.getInt(resolver, Settings.System.STATUS_BAR_QUICK_QS_PULLDOWN, 0) == 1);

		SwitchPreference weatherVisible = (SwitchPreference) findPreference(KEY_STATUS_BAR_SHOW_WEATHER);
		weatherVisible.setOnPreferenceChangeListener(this);
		weatherVisible.setChecked(Settings.System.getInt(resolver, Settings.System.STATUS_BAR_SHOW_WEATHER, 0) == 1);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object objValue) {
		ContentResolver resolver = getActivity().getContentResolver();
		final String key = preference.getKey();
		if (KEY_STATUS_BAR_SHOW_BATTERY_PERCENT.equals(key)) {
			int batteryStyle = Integer.valueOf((String) objValue);
			int index = mStatusBarBattery.findIndexOfValue((String) objValue);
			Settings.System.putInt(resolver, Settings.System.STATUS_BAR_SHOW_BATTERY_PERCENT, batteryStyle);
			mStatusBarBattery.setSummary(mStatusBarBattery.getEntries()[index]);
			return true;
		}
		if (KEY_STATUS_BAR_SHOW_WEATHER.equals(key)) {
			boolean value = (Boolean) objValue;
			Settings.System.putInt(resolver, Settings.System.STATUS_BAR_SHOW_WEATHER, value ? 1 : 0);
			return true;
		}
		if (KEY_STATUS_BAR_BRIGHTNESS_CONTROL.equals(key)) {
			boolean value = (Boolean) objValue;
			Settings.System.putInt(resolver, Settings.System.STATUS_BAR_BRIGHTNESS_CONTROL, value ? 1 : 0);
			return true;
		}
		if (KEY_STATUS_BAR_DOUBLE_TAP_SLEEP_GESTURE.equals(key)) {
			boolean value = (Boolean) objValue;
			Settings.System.putInt(resolver, Settings.System.STATUS_BAR_DOUBLE_TAP_SLEEP_GESTURE, value ? 1 : 0);
			return true;
		}
		if (KEY_STATUS_BAR_QUICK_QS_PULLDOWN.equals(key)) {
			boolean value = (Boolean) objValue;
			Settings.System.putInt(resolver, Settings.System.STATUS_BAR_QUICK_QS_PULLDOWN, value ? 1 : 0);
			return true;
		}
		if (KEY_STATUS_BAR_BLOCK_ON_SECURE_KEYGUARD.equals(key)) {
			boolean value = (Boolean) objValue;
			Settings.Secure.putInt(resolver, Settings.Secure.STATUS_BAR_LOCKED_ON_SECURE_KEYGUARD, value ? 1 : 0);
			return true;
		}
		if (KEY_STATUS_BAR_POWER_MENU.equals(key)) {
			String statusBarPowerMenu = (String) objValue;
			int statusBarPowerMenuValue = Integer.parseInt(statusBarPowerMenu);
			Settings.System.putInt(getActivity().getContentResolver(), Settings.System.STATUS_BAR_POWER_MENU,
					statusBarPowerMenuValue);
			int statusBarPowerMenuIndex = mStatusBarPowerMenu.findIndexOfValue(statusBarPowerMenu);
			mStatusBarPowerMenu.setSummary(mStatusBarPowerMenu.getEntries()[statusBarPowerMenuIndex]);
			return true;
		}
		return false;
	}

}

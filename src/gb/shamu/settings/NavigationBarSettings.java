package gb.shamu.settings;

import gb.shamu.settings.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.v4.preference.PreferenceFragment;

public class NavigationBarSettings extends PreferenceFragment implements OnPreferenceChangeListener {

	private static final String TAG = "NavigationBarSettings";

	private static final String KEY_NAVIGATION_BAR_HEIGHT = "navigation_bar_height";
	private static final String KEY_NAVIGATION_SWAP_BACK_RECENTS = "navigation_swap_back_recents";

	private ListPreference mNavigationBarHeight;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Activity activity = getActivity();
		final ContentResolver resolver = activity.getContentResolver();

		addPreferencesFromResource(R.xml.navigation_bar_settings);

		mNavigationBarHeight = (ListPreference) findPreference(KEY_NAVIGATION_BAR_HEIGHT);
		mNavigationBarHeight.setOnPreferenceChangeListener(this);
		int statusNavigationBarHeight = Settings.System.getInt(resolver, Settings.System.NAVIGATION_BAR_HEIGHT, 48);
		mNavigationBarHeight.setValue(String.valueOf(statusNavigationBarHeight));
		mNavigationBarHeight.setSummary(mNavigationBarHeight.getEntry());

		SwitchPreference swapBackRecents = (SwitchPreference) findPreference(KEY_NAVIGATION_SWAP_BACK_RECENTS);
		swapBackRecents.setOnPreferenceChangeListener(this);
		swapBackRecents
				.setChecked(Settings.System.getInt(resolver, Settings.System.NAVIGATION_SWAP_BACK_RECENTS, 0) == 1);

	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object objValue) {
		ContentResolver resolver = getActivity().getContentResolver();
		final String key = preference.getKey();
		if (KEY_NAVIGATION_BAR_HEIGHT.equals(key)) {
			int statusNavigationBarHeight = Integer.valueOf((String) objValue);
			int index = mNavigationBarHeight.findIndexOfValue((String) objValue);
			Settings.System.putInt(resolver, KEY_NAVIGATION_BAR_HEIGHT, statusNavigationBarHeight);
			mNavigationBarHeight.setSummary(mNavigationBarHeight.getEntries()[index]);
		}
		if (KEY_NAVIGATION_SWAP_BACK_RECENTS.equals(key)) {
			boolean value = (Boolean) objValue;
			Settings.System.putInt(resolver, Settings.System.NAVIGATION_SWAP_BACK_RECENTS, value ? 1 : 0);
		}
		return true;
	}

}

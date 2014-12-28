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

public class RecentsSettings extends PreferenceFragment implements OnPreferenceChangeListener {

	private static final String TAG = "RecentsSettings";

	private static final String KEY_RECENTS_SHOW_HIDE_SEARCH_BAR = "recents_show_hide_search_bar";
	private static final String KEY_RECENTS_SHOW_HIDE_CLEAR_ALL = "recents_show_hide_clear_all";
	private static final String KEY_RECENTS_CLEAR_ALL_POSITION = "recents_clear_all_position";

	private ListPreference mClearRecentsPosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Activity activity = getActivity();
		final ContentResolver resolver = activity.getContentResolver();

		addPreferencesFromResource(R.xml.recents_settings);

		SwitchPreference searchBarVisible = (SwitchPreference) findPreference(KEY_RECENTS_SHOW_HIDE_SEARCH_BAR);
		searchBarVisible.setOnPreferenceChangeListener(this);
		searchBarVisible
				.setChecked(Settings.System.getInt(resolver, Settings.System.RECENTS_SHOW_HIDE_SEARCH_BAR, 1) == 1);

		SwitchPreference clearRecentsVisible = (SwitchPreference) findPreference(KEY_RECENTS_SHOW_HIDE_CLEAR_ALL);
		clearRecentsVisible.setOnPreferenceChangeListener(this);
		clearRecentsVisible
				.setChecked(Settings.System.getInt(resolver, Settings.System.RECENTS_SHOW_HIDE_CLEAR_ALL, 0) == 1);

		mClearRecentsPosition = (ListPreference) findPreference(KEY_RECENTS_CLEAR_ALL_POSITION);
		mClearRecentsPosition.setOnPreferenceChangeListener(this);
		int clearAllPosition = Settings.System.getInt(resolver, Settings.System.RECENTS_CLEAR_ALL_POSITION, 0);
		mClearRecentsPosition.setValue(String.valueOf(clearAllPosition));
		mClearRecentsPosition.setSummary(mClearRecentsPosition.getEntry());

	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object objValue) {
		ContentResolver resolver = getActivity().getContentResolver();
		final String key = preference.getKey();
		if (KEY_RECENTS_SHOW_HIDE_SEARCH_BAR.equals(key)) {
			boolean value = (Boolean) objValue;
			Settings.System.putInt(resolver, Settings.System.RECENTS_SHOW_HIDE_SEARCH_BAR, value ? 1 : 0);
		}
		if (KEY_RECENTS_SHOW_HIDE_CLEAR_ALL.equals(key)) {
			boolean value = (Boolean) objValue;
			Settings.System.putInt(resolver, Settings.System.RECENTS_SHOW_HIDE_CLEAR_ALL, value ? 1 : 0);
		}
		if (KEY_RECENTS_CLEAR_ALL_POSITION.equals(key)) {
			int clearAllPosition = Integer.valueOf((String) objValue);
			int index = mClearRecentsPosition.findIndexOfValue((String) objValue);
			Settings.System.putInt(resolver, Settings.System.RECENTS_CLEAR_ALL_POSITION, clearAllPosition);
			mClearRecentsPosition.setSummary(mClearRecentsPosition.getEntries()[index]);
		}
		return true;
	}

}

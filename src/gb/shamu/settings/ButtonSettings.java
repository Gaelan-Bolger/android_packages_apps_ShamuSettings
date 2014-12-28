package gb.shamu.settings;

import gb.shamu.settings.R;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.support.v4.preference.PreferenceFragment;

public class ButtonSettings extends PreferenceFragment implements OnPreferenceChangeListener {

	private static final String TAG = "ButtonSettings";
	private static final String KEY_VOLUME_KEY_CURSOR_CONTROL = "volume_key_cursor_control";

	private ListPreference mVolumeKeyCursorControl;

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		addPreferencesFromResource(R.xml.button_settings);

		mVolumeKeyCursorControl = (ListPreference) findPreference(KEY_VOLUME_KEY_CURSOR_CONTROL);
		mVolumeKeyCursorControl.setOnPreferenceChangeListener(this);
		mVolumeKeyCursorControl.setValue(Integer.toString(Settings.System.getInt(getActivity().getContentResolver(),
				Settings.System.VOLUME_KEY_CURSOR_CONTROL, 0)));
		mVolumeKeyCursorControl.setSummary(mVolumeKeyCursorControl.getEntry());
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (KEY_VOLUME_KEY_CURSOR_CONTROL.equals(preference.getKey())) {
			String volumeKeyCursorControl = (String) newValue;
			int volumeKeyCursorControlValue = Integer.parseInt(volumeKeyCursorControl);
			Settings.System.putInt(getActivity().getContentResolver(), Settings.System.VOLUME_KEY_CURSOR_CONTROL,
					volumeKeyCursorControlValue);
			int volumeKeyCursorControlIndex = mVolumeKeyCursorControl.findIndexOfValue(volumeKeyCursorControl);
			mVolumeKeyCursorControl.setSummary(mVolumeKeyCursorControl.getEntries()[volumeKeyCursorControlIndex]);
		}
		return true;
	}

}

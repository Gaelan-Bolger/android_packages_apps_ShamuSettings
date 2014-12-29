package gb.shamu.settings;

import android.content.ContentResolver;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.v4.preference.PreferenceFragment;

public class LockscreenSettings extends PreferenceFragment implements OnPreferenceChangeListener {

	private static final String TAG = "LockscreenSettings";
	private static final String KEY_QUICK_UNLOCK_CONTROL = "quick_unlock_control";

	private SwitchPreference mQuickUnlockScreen;

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		ContentResolver contentResolver = getActivity().getContentResolver();

		addPreferencesFromResource(R.xml.lockscreen_settings);

		mQuickUnlockScreen = (SwitchPreference) findPreference(KEY_QUICK_UNLOCK_CONTROL);
		mQuickUnlockScreen.setChecked(Settings.Secure.getInt(contentResolver,
				Settings.Secure.LOCKSCREEN_QUICK_UNLOCK_CONTROL, 1) == 1);
		mQuickUnlockScreen.setOnPreferenceChangeListener(this);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String key = preference.getKey();
		if (KEY_QUICK_UNLOCK_CONTROL.equals(key)) {
			Boolean value = (Boolean) newValue;
			Settings.Secure.putInt(getActivity().getApplicationContext().getContentResolver(),
					Settings.Secure.LOCKSCREEN_QUICK_UNLOCK_CONTROL, value ? 1 : 0);
		}
		return true;
	}

}

package gb.shamu.settings;

import android.os.Bundle;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.Settings;
import android.support.v4.preference.PreferenceFragment;
import android.widget.Toast;

public class AnimationSettings extends PreferenceFragment implements OnPreferenceChangeListener {

	private static final String TAG = "AnimationSettings";
	private static final String KEY_TOAST_ANIMATION = "toast_animation";

	private Handler mHandler = new Handler();
	private ListPreference mToastAnimation;
	private ToastTestRunnable mToastTestRunnable;

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		addPreferencesFromResource(R.xml.animation_settings);

		mToastAnimation = (ListPreference) findPreference(KEY_TOAST_ANIMATION);
		mToastAnimation.setOnPreferenceChangeListener(this);
		int currentToastAnimation = Settings.System.getInt(getActivity().getContentResolver(),
				Settings.System.TOAST_ANIMATION, 1);
		mToastAnimation.setValueIndex(currentToastAnimation);
		mToastAnimation.setSummary(mToastAnimation.getEntry());
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String key = preference.getKey();
		if (KEY_TOAST_ANIMATION.equals(key)) {
			int index = mToastAnimation.findIndexOfValue((String) newValue);
			Settings.System.putString(getActivity().getContentResolver(), Settings.System.TOAST_ANIMATION,
					(String) newValue);
			mToastAnimation.setSummary(mToastAnimation.getEntries()[index]);
			testToast();
		}
		return true;
	}

	private void testToast() {
		if (null != mToastTestRunnable) {
			mToastTestRunnable.cancel();
		}
		mHandler.postDelayed(mToastTestRunnable = new ToastTestRunnable(), 300);
	}

	class ToastTestRunnable implements Runnable {

		private Toast sToast;

		@Override
		public void run() {
			sToast = Toast.makeText(getActivity(), "This toast is only a test!", Toast.LENGTH_SHORT);
			sToast.show();
		}

		public void cancel() {
			sToast.cancel();
		}

	}

}

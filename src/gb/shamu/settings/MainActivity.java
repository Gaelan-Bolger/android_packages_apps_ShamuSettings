/*
 *  Copyright (C) 2014 Gaelan Bolger
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package gb.shamu.settings;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.preference.PreferenceFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private static final String TAG = "MainActivity";
	private static final String CURRENT_FRAGMENT = "current";

	private FragmentManager mFm;
	private Toolbar mToolbar;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerList;
	private ArrayAdapter<String> mDrawerAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFm = getSupportFragmentManager();
		setContentView(R.layout.activity_main);
		setSupportActionBar(mToolbar);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (null == getCurrentFragment()) {
			mDrawerLayout.postDelayed(new Runnable() {

				@Override
				public void run() {
					mDrawerLayout.openDrawer(Gravity.LEFT);
				}
			}, 300);
		}
	}

	@Override
	public void onSupportContentChanged() {
		super.onSupportContentChanged();
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle(R.string.app_name);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
				R.string.drawer_close) {

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				PreferenceFragment currentFragment = getCurrentFragment();
				if (null != currentFragment) {
					mToolbar.setTitle(currentFragment.getTitle());
				}
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				mToolbar.setTitle(R.string.app_name);
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.nav_section_item, getResources()
				.getStringArray(R.array.section_titles));
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mDrawerList.setAdapter(mDrawerAdapter);
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (selectFragment(position)) {
					mDrawerLayout.closeDrawer(Gravity.LEFT);
				}
			}
		});

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if (mFm.getBackStackEntryCount() > 0) {
			mFm.popBackStack();
			return;
		}
		if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
			mDrawerLayout.openDrawer(Gravity.LEFT);
			return;
		}
		super.onBackPressed();
	}

	private PreferenceFragment getCurrentFragment() {
		return (PreferenceFragment) mFm.findFragmentByTag(CURRENT_FRAGMENT);
	}

	private boolean selectFragment(int position) {
		Fragment fragment;
		switch (position) {
		case 1: // Nav
			fragment = new NavigationBarSettings();
			break;
		case 2: // Recents
			fragment = new RecentsSettings();
			break;
		case 3: // Buttons
			fragment = new ButtonSettings();
			break;
		case 4: // App circle
			fragment = new ACSBSettings();
			break;
		case 5: // Animation
			fragment = new AnimationSettings();
			break;
		case 0: // Status
		default:
			fragment = new StatusBarSettings();
			break;

		}
		Fragment currentFragment = getCurrentFragment();
		if (null == currentFragment || !fragment.equals(currentFragment)) {
			mFm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			mFm.beginTransaction().replace(R.id.container, fragment, CURRENT_FRAGMENT).commit();
			mDrawerList.setItemChecked(position, true);
			return true;
		}
		return false;
	}

	public void selectChildFragment(String fragmentName) {
		try {
			PreferenceFragment fragment = (PreferenceFragment) Class.forName(fragmentName).newInstance();
			mFm.beginTransaction().replace(R.id.container, fragment, MainActivity.CURRENT_FRAGMENT)
					.addToBackStack(null).commit();
		} catch (java.lang.InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			Toast.makeText(this, "Page not found", Toast.LENGTH_SHORT).show();
		}
	}

}

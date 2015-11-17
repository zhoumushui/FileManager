package com.tchip.filemanager.ui.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tchip.filemanager.Clipboard;
import com.tchip.filemanager.FavouritesManager;
import com.tchip.filemanager.FolderFragment;
import com.tchip.filemanager.FontApplicator;
import com.tchip.filemanager.MyApplication;
import com.tchip.filemanager.R;
import com.tchip.filemanager.util.ListViewUtils;
import com.tchip.filemanager.util.MyLog;
import com.tchip.filemanager.Clipboard.ClipboardListener;
import com.tchip.filemanager.FavouritesManager.FavouritesListener;
import com.tchip.filemanager.R.anim;
import com.tchip.filemanager.R.id;
import com.tchip.filemanager.R.layout;
import com.tchip.filemanager.R.menu;
import com.tchip.filemanager.adapter.ClipboardFileAdapter;
import com.tchip.filemanager.adapter.NavDrawerAdapter;
import com.tchip.filemanager.adapter.NavDrawerAdapter.NavDrawerItem;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity implements OnItemClickListener,
		ClipboardListener, FavouritesListener {

	public static class FolderNotOpenException extends Exception {

	}

	private static final String LOG_TAG = "FolderActivity";

	public static final String EXTRA_DIR = FolderFragment.EXTRA_DIR;

	DrawerLayout drawerLayout;
	// ActionBarDrawerToggle actionBarDrawerToggle;
	File lastFolder = null;
	private FontApplicator fontApplicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.file_activity_main);

		setupDrawers();
		Clipboard.getInstance().addListener(this);

		fontApplicator = new FontApplicator(getApplicationContext(),
				"Font-Roboto-Light.ttf").applyFont(getWindow().getDecorView());
	}

	public FontApplicator getFontApplicator() {
		return fontApplicator;
	}

	@Override
	protected void onDestroy() {
		Clipboard.getInstance().removeListener(this);
		MyApplication application = (MyApplication) getApplication();
		application.getFavouritesManager().removeFavouritesListener(this);
		super.onDestroy();
	}

	public void setLastFolder(File lastFolder) {
		this.lastFolder = lastFolder;
	}

	@Override
	protected void onPause() {
		if (lastFolder != null) {
			// MyApplication application = (MyApplication) getApplication();
			// application.getAppPreferences().setStartFolder(lastFolder)
			// .saveChanges(getApplicationContext());
			// Log.d(LOG_TAG, "Saved last folder " + lastFolder.toString());
		}
		super.onPause();
	}

	public void setActionbarVisible(boolean visible) {
		ActionBar actionBar = getActionBar();
		if (actionBar == null)
			return;
		if (visible) {
			actionBar.show();
			setSystemBarTranslucency(false);
		} else {
			actionBar.hide();
			setSystemBarTranslucency(true);
		}
	}

	protected void setSystemBarTranslucency(boolean translucent) {
		if (Build.VERSION.SDK_INT < 19)// Build.VERSION_CODES.KITKAT
			return;

		if (translucent) {
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		} else {
			WindowManager.LayoutParams params = getWindow().getAttributes();
			params.flags &= (~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			getWindow().setAttributes(params);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}

	private void setupDrawers() {
		this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
		// R.drawable.file_ic_drawer, R.string.open_drawer,
		// R.string.close_drawer) {
		// boolean actionBarShown = false;
		//
		// @Override
		// public void onDrawerOpened(View drawerView) {
		// super.onDrawerOpened(drawerView);
		// setActionbarVisible(true);
		// invalidateOptionsMenu();
		// }
		//
		// @Override
		// public void onDrawerClosed(View drawerView) {
		// actionBarShown = false;
		// super.onDrawerClosed(drawerView);
		// invalidateOptionsMenu();
		// }
		//
		// @Override
		// public void onDrawerSlide(View drawerView, float slideOffset) {
		// super.onDrawerSlide(drawerView, slideOffset);
		// if (slideOffset > 0 && actionBarShown == false) {
		// actionBarShown = true;
		// setActionbarVisible(true);
		// } else if (slideOffset <= 0)
		// actionBarShown = false;
		// }
		// };
		// drawerLayout.setDrawerListener(actionBarDrawerToggle);
		// drawerLayout.setDrawerShadow(R.drawable.file_drawer_shadow,
		// Gravity.START);
		// drawerLayout.setFocusableInTouchMode(false);
		// drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.END);

		// getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(false);
		getActionBar().setDisplayUseLogoEnabled(false);
		// getActionBar().setHomeAsUpIndicator(
		// getResources().getDrawable(
		// R.drawable.shape_file_manager_back_transparent));

		setupNavDrawer();
		setupClipboardDrawer();
	}

	@Override
	public void onBackPressed() {
		if (drawerLayout.isDrawerOpen(GravityCompat.START))
			drawerLayout.closeDrawer(GravityCompat.START);
		else if (drawerLayout.isDrawerOpen(GravityCompat.END))
			drawerLayout.closeDrawer(GravityCompat.END);
		else
			super.onBackPressed();
	}

	void setupNavDrawer() {
		MyApplication application = (MyApplication) getApplication();

		// add listview header to push items below the actionbar
		ListView navListView = (ListView) findViewById(R.id.listNavigation);
		ListViewUtils.addListViewPadding(navListView, this, true);

		loadFavourites(application.getFavouritesManager());
		application.getFavouritesManager().addFavouritesListener(this);
	}

	void setupClipboardDrawer() {
		// add listview header to push items below the actionbar
		ListView clipboardListView = (ListView) findViewById(R.id.listClipboard);
		ListViewUtils.addListViewHeader(clipboardListView, this);
		onClipboardContentsChange(Clipboard.getInstance());
	}

	void loadFavourites(FavouritesManager favouritesManager) {
		ListView listNavigation = (ListView) findViewById(R.id.listNavigation);
		NavDrawerAdapter navDrawerAdapter = new NavDrawerAdapter(this,
				new ArrayList<NavDrawerAdapter.NavDrawerItem>(
						favouritesManager.getFolders()));
		navDrawerAdapter.setFontApplicator(fontApplicator);
		listNavigation.setAdapter(navDrawerAdapter);
		listNavigation.setOnItemClickListener(this);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// actionBarDrawerToggle.syncState();

		if (getFragmentManager().findFragmentById(R.id.fragment) == null) {
			FolderFragment folderFragment = new FolderFragment();
			if (getIntent().hasExtra(EXTRA_DIR)) {
				Bundle args = new Bundle();
				args.putString(FolderFragment.EXTRA_DIR, getIntent()
						.getStringExtra(EXTRA_DIR));
				folderFragment.setArguments(args);
			}

			getFragmentManager().beginTransaction()
					.replace(R.id.fragment, folderFragment).commit();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// if (actionBarDrawerToggle.onOptionsItemSelected(item))
		// return true;
		switch (item.getItemId()) {
		// case R.id.menu_ftp:
		// startActivity(new Intent(getApplicationContext(),
		// FileRemoteControlActivity.class));
		// return true;
		case android.R.id.home:
			backToMain();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			int pageIndex = getFragmentManager().getBackStackEntryCount();
			MyLog.v("[FolderActivity]Count:" + pageIndex);
			if (pageIndex > 0) {
				goBack();
			} else {
				backToMain();
			}
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	private void backToMain() {
		finish();
		overridePendingTransition(R.anim.zms_translate_down_out,
				R.anim.zms_translate_down_in);
	}

	public void showFragment(Fragment fragment) {
		getFragmentManager().beginTransaction().addToBackStack(null)
				.replace(R.id.fragment, fragment).commit();
	}

	public void goBack() {
		getFragmentManager().popBackStack();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.file_main, menu);
		return true;
	}

	public FolderFragment getFolderFragment() {
		Fragment fragment = getFragmentManager()
				.findFragmentById(R.id.fragment);
		if (fragment instanceof FolderFragment)
			return (FolderFragment) fragment;
		else
			return null;

	}

	public File getCurrentFolder() throws FolderNotOpenException {
		FolderFragment folderFragment = getFolderFragment();
		if (folderFragment == null)
			throw new FolderNotOpenException();
		else
			return folderFragment.currentDir;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg0.getId()) {
		case R.id.listNavigation:
			NavDrawerItem item = (NavDrawerItem) arg0.getItemAtPosition(arg2);
			if (item.onClicked(this))
				drawerLayout.closeDrawers();
			break;

		case R.id.listClipboard:
			FolderFragment folderFragment = getFolderFragment();
			if (folderFragment != null) {
				// TODO: paste single file
			}
			break;

		default:
			break;
		}
	}

	public File getLastFolder() {
		return lastFolder;
	}

	@Override
	public void onClipboardContentsChange(Clipboard clipboard) {
		invalidateOptionsMenu();

		ListView clipboardListView = (ListView) findViewById(R.id.listClipboard);

		if (clipboard.isEmpty() && drawerLayout != null)
			drawerLayout.setDrawerLockMode(
					DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.END);
		else {
			drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
					Gravity.END);
			MyApplication application = (MyApplication) getApplication();
			if (clipboardListView != null) {
				ClipboardFileAdapter clipboardFileAdapter = new ClipboardFileAdapter(
						this, clipboard, application.getFileIconResolver());
				clipboardFileAdapter.setFontApplicator(fontApplicator);
				clipboardListView.setAdapter(clipboardFileAdapter);
			}
		}
	}

	@Override
	public void onFavouritesChanged(FavouritesManager favouritesManager) {
		loadFavourites(favouritesManager);
	}

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		Log.d("Key Long Press", event.toString());
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		} else
			return super.onKeyLongPress(keyCode, event);
	}

}

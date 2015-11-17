package com.tchip.filemanager.util;

import android.app.Activity;
import android.widget.AbsListView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

public class ListViewUtils {
	/**
	 * Add header to listview to compensate for translucent navbar and system
	 * bar
	 */
	public static void addListViewHeader(AbsListView listView, Activity activity) {
		addListViewPadding(listView, activity, false);
	}

	/**
	 * Add padding to listview to compensate for translucent navbar and system
	 * bar
	 */
	public static void addListViewPadding(AbsListView listView,
			Activity activity, boolean ignoreRightInset) {
		SystemBarTintManager systemBarTintManager = new SystemBarTintManager(
				activity);
		int headerHeight = systemBarTintManager.getConfig().getPixelInsetTop(
				true);
		int footerHeight = systemBarTintManager.getConfig()
				.getPixelInsetBottom();
		int paddingRight = systemBarTintManager.getConfig()
				.getPixelInsetRight();
		listView.setPadding(listView.getPaddingLeft(), headerHeight,
				ignoreRightInset ? listView.getPaddingRight() : paddingRight,
				footerHeight);
	}
}

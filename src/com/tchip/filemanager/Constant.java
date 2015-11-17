package com.tchip.filemanager;

import android.os.Environment;

public interface Constant {
	/**
	 * Debug：打印Log
	 */
	public static final boolean isDebug = true;

	/**
	 * 日志Tag
	 */
	public static final String TAG = "ZMS";

	/**
	 * SharedPreferences名称
	 */
	public static final String SHARED_PREFERENCES_NAME = "FileManager";

	/**
	 * 路径
	 */
	public static final class Path {

		/**
		 * 字体目录
		 */
		public static final String FONT = "fonts/";
	}
}

package com.tchip.filemanager;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.io.File;

public class FilePreviewCache extends LruCache<File, Bitmap> {
	public static final int DEFAULT_CACHE_SZE = 24 * 1024 * 1024;

	/**
	 * create new cache
	 * 
	 * @param maxSize
	 *            max size in bytes
	 */
	public FilePreviewCache(int maxSize) {
		super(maxSize);
	}

	public FilePreviewCache() {
		this(DEFAULT_CACHE_SZE);
	}

	@Override
	protected int sizeOf(File key, Bitmap value) {
		return value.getByteCount();
	}

}

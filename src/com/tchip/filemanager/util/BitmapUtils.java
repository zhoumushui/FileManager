package com.tchip.filemanager.util;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

public class BitmapUtils {
	private static final String LOG_TAG = "BitmapUtils";

	public static Rect getBestFitRect(Bitmap bitmap, Rect destination) {
		return getBestFitRect(bitmap, (float) destination.width()
				/ (float) destination.height());
	}

	public static Rect getBestFitRect(Bitmap bitmap, float ratio) {
		float bmpRatio = (float) bitmap.getWidth() / (float) bitmap.getHeight();

		if (bmpRatio > ratio) // bmp is wider
		{
			int height = bitmap.getHeight();
			int width = (int) (height * ratio);
			int offset = (bitmap.getWidth() - width) / 2;
			return new Rect(offset, 0, offset + width, height);
		} else if (bmpRatio < ratio) // bmp is taller
		{
			int width = bitmap.getWidth();
			int height = (int) (width / ratio);
			int offset = (bitmap.getHeight() - height) / 2;
			return new Rect(0, offset, width, offset + height);
		} else
			return new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	}

	public static Rect[] layoutImagesInGrid(Bitmap destination, int cols,
			int rows) {
		int numItems = cols * rows;
		int itemWidth = destination.getWidth() / cols, itemHeight = destination
				.getHeight() / rows;

		Rect[] result = new Rect[numItems];

		for (int i = 0; i < numItems; i++) {
			int x = (i % cols) * itemWidth, y = (i / (rows + 1)) * itemHeight;
			Log.d(LOG_TAG, x + "x" + y);
			result[i] = new Rect(x, y, x + itemWidth, y + itemHeight);
		}

		return result;
	}
}

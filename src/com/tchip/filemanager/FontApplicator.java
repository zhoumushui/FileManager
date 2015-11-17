package com.tchip.filemanager;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.tchip.filemanager.Constant;
import com.tchip.filemanager.ViewTraverser.ForeachAction;

/**
 * Applies selected font to the view and all views
 */
public class FontApplicator {
	final private Typeface font;

	public FontApplicator(Typeface font) {
		this.font = font;
	}

	public FontApplicator(Context context, String fontName) {
		this(Typeface.createFromAsset(context.getAssets(), Constant.Path.FONT
				+ fontName));
	}

	public FontApplicator(AssetManager assets, String assetFontName) {
		this.font = Typeface.createFromAsset(assets, assetFontName);
	}

	/**
	 * Applies font to the view and/or its children
	 * 
	 * @param root
	 * @return
	 */
	public FontApplicator applyFont(View root) {
		if (root == null)
			return this;
		new ViewTraverser(root).traverse(new ForeachAction<View>() {

			@Override
			public void onElement(View element) {
				if (element instanceof TextView) {
					((TextView) element).setTypeface(font);
				}
			}
		});

		return this;
	}
}

package com.tchip.filemanager;

import android.util.SparseArray;
import android.view.View;

public class ViewHolder {
	final View root;
	final SparseArray<View> views = new SparseArray<View>(3);

	public ViewHolder(View root) {
		this.root = root;
	}

	@SuppressWarnings("unchecked")
	public <T extends View> T getViewById(int id) {
		T view = (T) views.get(id);
		if (view == null) {
			views.put(id, (view = (T) root.findViewById(id)));
			if (view == null)
				throw new NullPointerException("Cannot find requested view id "
						+ String.valueOf(id));
		}
		return view;
	}

	public boolean hasView(int id) {
		if (views.get(id) != null)
			return true;
		else {
			View view = this.root.findViewById(id);
			if (view == null)
				return false;
			else {
				views.put(id, view);
				return true;
			}
		}
	}
}

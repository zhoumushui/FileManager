package com.tchip.filemanager.adapter;

import com.tchip.filemanager.Clipboard;
import com.tchip.filemanager.FileIconResolver;
import com.tchip.filemanager.R;
import com.tchip.filemanager.R.layout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class ClipboardFileAdapter extends BaseFileAdapter {
	final Clipboard clipboard;

	public ClipboardFileAdapter(Context context, Clipboard clipboard,
			FileIconResolver fileIconResolver) {
		super(context, R.layout.file_list_item_file, clipboard.getFilesList(),
				fileIconResolver);
		this.clipboard = clipboard;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TOOD: implement paste icon
		return super.getView(position, convertView, parent);
	}

}

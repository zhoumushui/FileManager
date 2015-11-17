package com.tchip.filemanager.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import com.tchip.filemanager.FileIconResolver;
import com.tchip.filemanager.R;
import com.tchip.filemanager.ViewHolder;
import com.tchip.filemanager.R.drawable;
import com.tchip.filemanager.R.id;
import com.tchip.filemanager.R.string;
import com.tchip.filemanager.util.FileUtils;

public class BaseFileAdapter extends RobotoAdapter<File> {
	protected final int layoutId;
	final FileIconResolver fileIconResolver;
	private Context context;

	public BaseFileAdapter(Context context, int resource, File[] objects,
			FileIconResolver fileIconResolver) {
		super(context, resource, objects);
		this.layoutId = resource;
		this.fileIconResolver = fileIconResolver;
		this.context = context;

	}

	public BaseFileAdapter(Context context, int resource, List<File> objects,
			FileIconResolver fileIconResolver) {
		super(context, resource, objects);
		this.layoutId = resource;
		this.fileIconResolver = fileIconResolver;
		this.context = context;

	}

	protected int getItemLayoutId(int position) {
		return layoutId;
	}

	protected View buildView(int position, ViewGroup parent) {
		View view = ((LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE)).inflate(
				getItemLayoutId(position), parent, false);
		view.setTag(new ViewHolder(view));
		applyFont(view);
		return view;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null)
			view = buildView(position, parent);

		ViewHolder viewHolder = (ViewHolder) view.getTag();
		final File file = getItem(position);
		TextView tvFileName = viewHolder.getViewById(R.id.tvFileName), tvFileDetails = viewHolder
				.getViewById(R.id.tvFileDetails);
		ImageView imgIcon = viewHolder.getViewById(R.id.imgFileIcon);

		String strFileName = file.getName();
		if ("sdcard0".equals(strFileName)) {
			tvFileName.setText("internal");
		} else {
			tvFileName.setText(strFileName);
		}

		if ("usbotg".equals(strFileName)) {
			view.setVisibility(View.INVISIBLE);
		}

		if (file.isDirectory()) {
			int files = FileUtils.getNumFilesInFolder(file);
			if (files == 0) {
				if (file.getAbsolutePath().equals("/storage/sdcard1")
						|| file.getAbsolutePath().equals("/storage/sdcard2")
						|| file.getAbsolutePath().equals("/storage/usbotg")) {
					tvFileDetails.setText("未安装");
				} else {
					tvFileDetails.setText(R.string.folder_empty);
				}
			} else {
				tvFileDetails.setText(getContext().getString(R.string.folder,
						files));
			}
			imgIcon.setImageResource(FileUtils.getFileIconResource(file));
		} else {
			tvFileDetails.setText(getContext().getString(R.string.size_s,
					FileUtils.formatFileSize(file)));

			// 判断视频是否加锁
			String fileName = file.getName();
			if (fileName.endsWith(".mp4")) {

				int videoLock = 0;
				Uri uri = Uri
						.parse("content://com.tchip.carlauncher.model.DriveVideoProvider/video/name/"
								+ fileName);
				ContentResolver resolve = context.getContentResolver();
				// Uri uri, String[] projection, String selection, String[]
				// selectionArgs, String sortOrder
				Cursor cursor = resolve.query(uri, null, null, null, null);
				if (cursor.getCount() > 0) {
					cursor.moveToFirst();
					videoLock = cursor.getInt(cursor.getColumnIndex("lock"));
					cursor.close();
				} else {
					videoLock = 0;
				}

				if (1 == videoLock) {
					imgIcon.setImageBitmap(BitmapFactory.decodeResource(
							context.getResources(),
							R.drawable.ui_camera_video_lock_normal_small));
				} else {
					imgIcon.setImageBitmap(fileIconResolver.getFileIcon(file));
				}
			} else {
				imgIcon.setImageBitmap(fileIconResolver.getFileIcon(file));
			}
		}

		return view;
	}
}

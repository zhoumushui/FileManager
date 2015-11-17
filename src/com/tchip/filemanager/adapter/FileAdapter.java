package com.tchip.filemanager.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.tchip.filemanager.FileIconResolver;
import com.tchip.filemanager.R;
import com.tchip.filemanager.ViewHolder;
import com.tchip.filemanager.R.drawable;
import com.tchip.filemanager.R.id;
import com.tchip.filemanager.R.layout;


public class FileAdapter extends BaseFileAdapter
{
	Set<File> selectedFiles=null;
	OnFileSelectedListener onFileSelectedListener=null;
	
	public static interface OnFileSelectedListener
	{
		void onFileSelected(File file);
	}
	
	public void setOnFileSelectedListener(
			OnFileSelectedListener onFileSelectedListener)
	{
		this.onFileSelectedListener = onFileSelectedListener;
	}
	
	public FileAdapter(Context context, int layoutId, File[] files, FileIconResolver fileIconResolver)
	{
		super(context, layoutId, files, fileIconResolver);
	}
	
	public FileAdapter(Context context, int layoutId, List<File> files, FileIconResolver fileIconResolver)
	{
		super(context, layoutId, files, fileIconResolver);
	}
	
	public FileAdapter(Context context,	List<File> objects, FileIconResolver fileIconResolver)
	{
		super(context, R.layout.file_list_item_file, objects, fileIconResolver);
	}
	
	public FileAdapter(Context context,	File [] objects, FileIconResolver fileIconResolver)
	{
		super(context, R.layout.file_list_item_file, objects, fileIconResolver);
	}
	
	public FileAdapter(Context context, FileIconResolver fileIconResolver)
	{
		this(context, new ArrayList<File>(0), fileIconResolver);
	}
	
	protected boolean isSelected(File file)
	{
		return (selectedFiles != null && selectedFiles.contains(file));
	}
	
	public void setSelectedFiles(Set<File> selectedFiles)
	{
		this.selectedFiles = selectedFiles;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final View view = super.getView(position, convertView, parent);
		final File file = getItem(position);
		final ViewHolder viewHolder = (ViewHolder) view.getTag();
		final ImageView imgIcon = viewHolder.getViewById(R.id.imgFileIcon);
		
		if (isSelected(file))
		{
			view.setBackgroundResource(R.drawable.file_selector_list_item_selected);
			imgIcon.setImageResource(R.drawable.file_icon_selected);
		}
		else
		{
			view.setBackgroundResource(R.drawable.file_selector_list_item);
		}
		
		if (onFileSelectedListener != null)
		{
			imgIcon.setBackgroundResource(R.drawable.file_selector_list_item);
			imgIcon.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					onFileSelectedListener.onFileSelected(file);					
				}
			});
		}
		
		return view;
	}
	
}

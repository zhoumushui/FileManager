package com.tchip.filemanager;

import android.content.Context;

import com.michaldabski.msqlite.Annotations.ColumnName;
import com.michaldabski.msqlite.Annotations.PrimaryKey;

import java.io.File;

public class FavouriteFolder extends NavDrawerShortcut implements Comparable<FavouriteFolder>
{
	private String label;
	@PrimaryKey
	private String path;
	@ColumnName("item_order")
	Integer order;
	
	public FavouriteFolder()
	{
		
	}
	
	public FavouriteFolder(File folder, String label)
	{
		this();
		if (folder.isDirectory() == false)
			throw new RuntimeException(folder.getName()+" is not a directory");
		this.path = folder.getAbsolutePath();
		this.label = label;
	}
	
	public FavouriteFolder(File folder)
	{
		this(folder, folder.getName());
	}
	
	public FavouriteFolder(String path, String label) 
	{
		this(new File(path), label);
	}

	public File getFile()
	{
		return new File(path);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof FavouriteFolder)
			return ((FavouriteFolder) o).path.equals(path);
		else if (o instanceof File)
			return o.equals(getFile());
		return super.equals(o);
	}
	
	@Override
	public String toString()
	{
		return label;
	}
	
	@Override
	public int hashCode()
	{
		return getFile().hashCode();
	}
	
	public void setOrder(int order)
	{
		this.order = order;
	}
	
	public Integer getOrder()
	{
		return order;
	}
	
	public boolean hasValidOrder()
	{
		return order != null;
	}
	
	public boolean exists()
	{
		return getFile().exists();
	}

	@Override
	public CharSequence getTitle(Context context)
	{
		return label;
	}

	@Override
	public int compareTo(FavouriteFolder another)
	{
		if (order == null)
			return -1;
		if (another.order == null)
			return 1;
		return order.compareTo(another.order);
	}

}

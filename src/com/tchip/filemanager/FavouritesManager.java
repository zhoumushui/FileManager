package com.tchip.filemanager;

import android.content.Context;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavouritesManager {
	public static class FolderAlreadyFavouriteException extends Exception {

		public FolderAlreadyFavouriteException(FavouriteFolder folder) {
			super(folder.toString() + " is already bookmarked");
		}

	}

	public static interface FavouritesListener {
		void onFavouritesChanged(FavouritesManager favouritesManager);
	}

	private final List<FavouriteFolder> folders;
	private final SQLiteHelper sqLiteHelper;
	private final Set<FavouritesListener> favouritesListeners;

	public FavouritesManager(Context context) {
		this.sqLiteHelper = new SQLiteHelper(context);
		this.favouritesListeners = new HashSet<FavouritesManager.FavouritesListener>();
		this.folders = sqLiteHelper.selectAll(FavouriteFolder.class);
		sort();
		fixFavouritesOrder();
	}

	public void sort() {
		Collections.sort(folders);
	}

	private void fixFavouritesOrder() {
		int lastOrder = 0;
		for (FavouriteFolder folder : folders) {
			if (folder.hasValidOrder() == false
					|| folder.getOrder() <= lastOrder) {
				folder.setOrder(lastOrder + 1);
			}

			lastOrder = folder.getOrder();
		}
	}

	public void addFavouritesListener(FavouritesListener favouritesListener) {
		favouritesListeners.add(favouritesListener);
	}

	public void removeFavouritesListener(FavouritesListener favouritesListener) {
		favouritesListeners.remove(favouritesListener);
	}

	void notifyListeners() {
		for (FavouritesListener listener : favouritesListeners)
			listener.onFavouritesChanged(this);
	}

	public List<FavouriteFolder> getFolders() {
		return folders;
	}

	public void addFavourite(FavouriteFolder favouriteFolder)
			throws FolderAlreadyFavouriteException {
		long id = sqLiteHelper.insert(favouriteFolder);
		if (id == -1)
			throw new FolderAlreadyFavouriteException(favouriteFolder);
		folders.add(favouriteFolder);
		notifyListeners();
	}

	public void removeFavourite(File file) {
		for (FavouriteFolder folder : folders)
			if (folder.equals(file)) {
				removeFavourite(folder);
				break;
			}
	}

	public void removeFavourite(FavouriteFolder favouriteFolder) {
		folders.remove(favouriteFolder);
		sqLiteHelper.delete(favouriteFolder);
		notifyListeners();
	}

	public boolean isFolderFavourite(File file) {
		for (FavouriteFolder folder : folders)
			if (folder.equals(file))
				return true;

		return false;
	}

}

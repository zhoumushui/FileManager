package com.tchip.filemanager;

public interface FileOperationListener {
	void onFileProcessed(String filename);

	boolean isOperationCancelled();
}

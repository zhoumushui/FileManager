package com.tchip.filemanager;

public interface OnResultListener<T> {
	void onResult(AsyncResult<T> result);
}

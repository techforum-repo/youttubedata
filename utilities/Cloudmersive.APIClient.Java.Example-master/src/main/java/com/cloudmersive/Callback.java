package com.cloudmersive;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cloudmersive.client.invoker.ApiCallback;
import com.cloudmersive.client.invoker.ApiException;

public class Callback<T> implements ApiCallback<T> {

	public void onFailure(ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
		e.printStackTrace();

	}

	public void onSuccess(T result, int statusCode, Map<String, List<String>> responseHeaders) {

		try {
			FileOutputStream fos = new FileOutputStream("output pdf path");
			fos.write((byte[]) result);
			fos.close();
			System.out.println("completed");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {
		System.out.println("Upload Progress: Content Length: " + contentLength + " Bytes Written: " + bytesWritten);

	}

	public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {
		System.out.println("Download Progress: Content Length: " + contentLength + " Bytes Written: " + bytesRead);

	}

}

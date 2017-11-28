package cn.falconnect.shopping.uploader;

public interface UploadProgressListener {
	void onProgress(long progress);

	void onSucceed(byte[] response);

	void onError();
}

package cn.falconnect.shopping.uploader;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.aurora.library.downloader.core.CustomThreadAsyncTask;

import android.content.Context;
import android.widget.ProgressBar;

public class Uploader {
	public static void upload(final Context context, final String stringParam, final String url, final List<File> files,
			final UploadProgressListener listener) {
		new CustomThreadAsyncTask<Void, Integer, Object>() {

			private long total;

			@Override
			protected Object doInBackground(Void... params) {
				byte[] response = null;
				UploadProgressListener listener = new UploadProgressListener() {
					
					@Override
					public void onSucceed(byte[] response) {
					}

					@Override
					public void onProgress(long progressbyte) {
						double per = (float) progressbyte / (float) total;
						int progress = (int) (per * 100);
						publishProgress(progress);
					
					}

					@Override
					public void onError() {
					}
				};
				ProcessMultiPartEntity entity = new ProcessMultiPartEntity(listener);
				
				
				ContentBody contentboy = new ByteArrayBody("OKOKOK".getBytes(), stringParam);
				
				entity.addPart("param", contentboy);
				for (int i = 0; i < files.size(); i++) {
					entity.addPart("fcup_photo", new FileBody(files.get(i)));
					total +=entity.getContentLength();
				}
				try {
					response = ApacheHttpUtils.executeRequest(url, entity, HttpPost.METHOD_NAME);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return response;
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
				if (values != null && values.length == 1) {
					int progress = values[0];
					if (listener != null) {
						listener.onProgress(progress);
					}
				}
			}

			@Override
			protected void onPostExecute(Object result) {
				if (result != null) {
					listener.onSucceed((byte[]) result);
				} else {
					listener.onError();
				}
			}
		}.execute();
	}
}

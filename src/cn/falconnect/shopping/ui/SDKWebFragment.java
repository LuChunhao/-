package cn.falconnect.shopping.ui;

import static cn.falconnect.shopping.constants.Global.BundleKey.DETAIL_URL;

import org.aurora.library.util.NetworkUtil;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import cn.falconnect.shopping.cat.R;

public class SDKWebFragment extends BaseFragment{
	private static final int PROGRESS_MAX_VALUE = 100;
	private String mDetailUrl;
	private ProgressBar mProgressView;
	private WebView mWebView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			mDetailUrl = args.getString(DETAIL_URL);
		}
	}

	@Override
	public void onDestroy() {
		if (mWebView != null) {
			mWebView.destroy();
			mWebView = null;
		}
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_sdk_web, null);
		initViews(contentView);
		return contentView;
	}

	private void initViews(View contentView) {
		mWebView = (WebView) contentView.findViewById(R.id.web_view);
		mProgressView = (ProgressBar) contentView.findViewById(R.id.web_pb);
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setDefaultTextEncodingName("UTF-8");
		settings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		mWebView.setWebViewClient(mWebViewClient);
		mWebView.setWebChromeClient(mWebChoomeClient);
		mWebView.loadUrl(mDetailUrl);
	}

	private WebViewClient mWebViewClient = new WebViewClient() {
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			boolean isNetworkAvailable = NetworkUtil.isNetworkConnected(view
					.getContext());
			if (isNetworkAvailable) {
				mProgressView.setVisibility(View.GONE);
			}
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			mProgressView.setVisibility(View.VISIBLE);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			String errorMsg = "抱歉，找不到页面啦!";
			view.loadUrl("javascript:document.body.innerHTML=\"" + errorMsg
					+ "\"");
		};

	};

	private WebChromeClient mWebChoomeClient = new WebChromeClient() {

		@Override
		public void onProgressChanged(WebView view, final int newProgress) {
			if (PROGRESS_MAX_VALUE == newProgress) {
				mProgressView.setVisibility(View.GONE);
			} else {
				mProgressView.setProgress(newProgress);
			}
		}

	};
}

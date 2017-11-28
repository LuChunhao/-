package cn.falconnect.shopping.ui.search;

import java.util.List;

import org.aurora.library.downloader.core.CustomThreadAsyncTask;
import org.aurora.library.views.Toaster;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.falconnect.shopping.Task;
import cn.falconnect.shopping.Task.AsyncTaskListener;
import cn.falconnect.shopping.adapter.SearchAdapter;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.constants.Global;
import cn.falconnect.shopping.provider.db.HistoryDAO;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.ui.SlidingExitFragment;
import cn.falconnect.shopping.utils.CommonUtil;

public class SearchMainFragment extends SlidingExitFragment {
	public static final String FRAGMENT_TAG = "SearchMainFragment";
	private ImageView mIvDelete;
	private EditText mEtSearch;
	private TextView mBtnCancel;
	private TextView mBtnSearch;
	private TextView mTvClearHistory;
	private ViewGroup mBtnOptsContainer;
	private RelativeLayout mSearch_bar_layout;
	private ViewGroup mHisViewGroup;
	private SearchAdapter mHistoryAdapter;
	private SearchAdapter mHotwordsAdapter;

	@Override
	protected View onChildCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.search_main, null);
		initViews(contentView, inflater);
		updateData(false);
		return contentView;
	}

	void updateData(boolean updatable) {
		if (updatable) {
			mHistoryAdapter.clear();
			mHotwordsAdapter.clear();
			mHisViewGroup.setVisibility(View.VISIBLE);
		}
		querySearchKeys(getActivity(), new AsyncTaskListener<List<String>>() {

			@Override
			public void onSucceed(Context context, List<String> result) {
				if (result.size() > 0) {
					mHistoryAdapter.setData(result);
				} else {
					mHisViewGroup.setVisibility(View.GONE);
				}
			}

			@Override
			public void onError(Context context, String msg) {
			}

			@Override
			public void onFinished(Context context, String msg) {
				getHotWords(context);
			}

		});
	}

	private void initViews(View view, LayoutInflater inflater) {
		mHisViewGroup = (ViewGroup) view.findViewById(R.id.history_container);
		mTvClearHistory = (TextView) view.findViewById(R.id.tv_clear_history);
		mBtnOptsContainer = (ViewGroup) view.findViewById(R.id.rl_btn_opts);
		mSearch_bar_layout = (RelativeLayout) view
				.findViewById(R.id.search_bar_layout);
		mIvDelete = (ImageView) view.findViewById(R.id.delete_word_img);
		mEtSearch = (EditText) view.findViewById(R.id.search_edit);
		mBtnCancel = (TextView) view.findViewById(R.id.btn_cancel);
		mBtnSearch = (TextView) view.findViewById(R.id.btn_search);
		mEtSearch.addTextChangedListener(mTextChangedListener);
		mEtSearch.setOnFocusChangeListener(mInputFocusChangeListener);
		mEtSearch.setOnEditorActionListener(mActionListener);
		mIvDelete.setOnClickListener(mOnClickListener);
		mBtnCancel.setOnClickListener(mOnClickListener);
		mBtnSearch.setOnClickListener(mOnClickListener);
		mTvClearHistory.setOnClickListener(mOnClickListener);
		GridView gv_history = (GridView) view
				.findViewById(R.id.gv_search_history);
		GridView gv_hotwords = (GridView) view
				.findViewById(R.id.gv_search_hotwords);
		mHistoryAdapter = new SearchAdapter();
		mHotwordsAdapter = new SearchAdapter();
		gv_history.setAdapter(mHistoryAdapter);
		gv_hotwords.setAdapter(mHotwordsAdapter);
		gv_history.setOnItemClickListener(new SearchItemClickListener(
				mHistoryAdapter));
		gv_hotwords.setOnItemClickListener(new SearchItemClickListener(
				mHotwordsAdapter));

	}

	@Override
	protected String getTDPageName() {
		return getString(R.string.search);
	}

	private <T> void querySearchKeys(Context context,
			AsyncTaskListener<T> listener) {
		Task<T> task = new Task<T>(context, listener);
		new CustomThreadAsyncTask<Object, Void, Task<T>>() {
			@Override
			protected Task<T> doInBackground(Object... params) {
				@SuppressWarnings("unchecked")
				Task<T> task = (Task<T>) params[0];
				try {
					List<String> data = HistoryDAO.getInstance().queryAll();
					task.msg = (data != null && data.size() > 0) ? "success"
							: ResultCode.NO_MORE_DATA.msg;
					task.result = (T) data;
				} finally {
					task.listener.onFinishInBackgroud(task.context, task.msg,
							task.result);
				}
				return task;
			}

			@Override
			protected void onPostExecute(Task<T> result) {
				String msg = result.msg;
				result.listener.onSucceed(result.context, result.result);
				result.listener.onError(result.context, msg);
				result.listener.onFinished(result.context, msg);
			}
		}.execute(task);
	}

	private void getHotWords(Context context) {
		showLoadingPage(R.id.hot_words_container);
		ProviderFatory.getGoodsProvider().getHotWords(context,
				new ObtainListener<List<String>>() {

					@Override
					public void onSucceed(Context context, List<String> result) {
						if (result.size() > 0) {
							closeLoadingPage();
							mHotwordsAdapter.setData(result);
						} else {
							onError(context, ResultCode.NO_MORE_DATA);
						}

					}

					@Override
					public void onFinished(Context context, ResultCode code) {

					}

					@Override
					public void onError(Context context, ResultCode code) {
						setLoadFailedMessage(code.msg);
					}
				});

	}

	private OnFocusChangeListener mInputFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			mBtnOptsContainer.setVisibility(View.VISIBLE);
			mBtnCancel.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
		}
	};
	private TextWatcher mTextChangedListener = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable word) {
			if (!TextUtils.isEmpty(word)) {
				mIvDelete.setVisibility(View.VISIBLE);
				mBtnSearch.setVisibility(View.VISIBLE);
				mBtnCancel.setVisibility(View.GONE);
			} else {
				mIvDelete.setVisibility(View.GONE);
				mBtnSearch.setVisibility(View.GONE);
				mBtnCancel.setVisibility(View.VISIBLE);
			}
		}
	};
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.delete_word_img:
				mEtSearch.setText("");
				break;
			case R.id.btn_search:
				String name = mEtSearch.getText().toString();
				startToSearchResult(name);
				CommonUtil.hideSoftInput(getActivity(),
						mEtSearch.getWindowToken());
				break;
			case R.id.btn_cancel:
				CommonUtil.hideSoftInput(getActivity(),
						mEtSearch.getWindowToken());
				v.setVisibility(View.GONE);
				mSearch_bar_layout.setFocusable(true);
				mSearch_bar_layout.setFocusableInTouchMode(true);
				mSearch_bar_layout.requestFocus();
				break;
			case R.id.tv_clear_history:
				HistoryDAO.getInstance().deleteAll();
				mHistoryAdapter.clear();
				mHisViewGroup.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}
	};

	private OnEditorActionListener mActionListener = new OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_SEARCH
					|| (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
				if (TextUtils.isEmpty(mEtSearch.getText().toString().trim())) {
					Toaster.toast("请输入搜索条件");
				} else {
					startToSearchResult(mEtSearch.getText().toString().trim());
					CommonUtil.hideSoftInput(getActivity(),
							mEtSearch.getWindowToken());
				}
				return true;
			}
			return false;
		}
	};

	private void startToSearchResult(String name) {
		if (!TextUtils.isEmpty(name)) {
			Bundle args = new Bundle();
			args.putString(Global.BundleKey.GOODS_TYPE_NAME, name);
			startFragment(new SearchResultFragment(), args);
		}
	}

	class SearchItemClickListener implements OnItemClickListener {
		private SearchAdapter mAdapter;

		public SearchItemClickListener(SearchAdapter adapter) {
			this.mAdapter = adapter;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String name = this.mAdapter.getItem(arg2);
			startToSearchResult(name);
		}

	}
}

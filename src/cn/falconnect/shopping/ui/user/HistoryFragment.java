package cn.falconnect.shopping.ui.user;

import static cn.falconnect.shopping.constants.Global.BundleKey.COLLECTS_ENTRY_KEY;
import static cn.falconnect.shopping.constants.Global.BundleKey.GOODS_BEAN;

import java.util.List;

import org.aurora.library.downloader.core.CustomThreadAsyncTask;
import org.aurora.library.views.list.xlistview.XListView;
import org.aurora.library.views.list.xlistview.XListView.IXListViewListener;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.falconnect.shopping.Task;
import cn.falconnect.shopping.Task.AsyncTaskListener;
import cn.falconnect.shopping.adapter.HistoryAdapter;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.constants.Global;
import cn.falconnect.shopping.entity.Goods;
import cn.falconnect.shopping.provider.db.CacheDAO;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.ui.GoodsDetailFragment;
import cn.falconnect.shopping.ui.SlidingExitFragment;

public class HistoryFragment extends SlidingExitFragment {
	private HistoryAdapter mHistoryAdapter;
	private XListView mListView;
	public static final String FRAGMENT_TAG = "HistoryFragment";

	@Override
	public View onChildCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_history, null);
		initViews(contentView);
		queryHistoryData(contentView.getContext(), true);
		return contentView;
	}

	@Override
	public void onReload(Context context) {
		queryHistoryData(context, true);
	}

	public void onUpdate() {
		mIXListViewListener.onRefresh(mListView);
	}

	@Override
	protected String getTDPageName() {
		return getString(R.string.broswer_history);
	}

	private void initViews(View contentView) {
		mListView = (XListView) contentView.findViewById(android.R.id.list);
		mListView.setPullLoadEnable(false);
		mHistoryAdapter = new HistoryAdapter();
		mListView.setAdapter(mHistoryAdapter);
		mListView.setXListViewListener(mIXListViewListener);
		mListView.setOnItemClickListener(mOnItemClickListener);
	}

	private IXListViewListener mIXListViewListener = new IXListViewListener() {

		@Override
		public void onRefresh(XListView xListView) {
			queryHistoryData(xListView.getContext(), true);
		}

		@Override
		public void onLoadMore(XListView xListView) {
			queryHistoryData(xListView.getContext(), false);
		}
	};

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Goods goods = mHistoryAdapter.getItem(position
					- mListView.getHeaderViewsCount());
			Bundle args = new Bundle();
			args.putSerializable(GOODS_BEAN, goods);
			args.putString(COLLECTS_ENTRY_KEY, FRAGMENT_TAG);
			startFragment(new GoodsDetailFragment(), args,
					GoodsDetailFragment.FRAGMENT_TAG);
		}
	};

	private <T> void query(final Context context, final int offset,
			final int count, AsyncTaskListener<T> listener) {
		Task<T> task = new Task<T>(context, listener);
		new CustomThreadAsyncTask<Object, Void, Task<T>>() {
			@Override
			protected Task<T> doInBackground(Object... params) {
				@SuppressWarnings("unchecked")
				Task<T> task = (Task<T>) params[0];
				try {
					String sign = ProviderFatory.getUserProvider()
							.getLoginName(context);
					if (!TextUtils.isEmpty(sign)) {
						List<Goods> data = CacheDAO.getInstance().query(offset,
								count, Global.HISTORY_CACHE_TYPE, sign);
						task.msg = (data != null && data.size() > 0) ? "success"
								: ResultCode.NO_MORE_DATA.msg;
						task.result = (T) data;
					}
				} finally {
					task.listener.onFinishInBackgroud(task.context, task.msg,
							task.result);
				}
				return task;
			}

			@Override
			protected void onPostExecute(Task<T> result) {
				String msg = result.msg;
				if ("success".equals(msg)) {
					result.listener.onSucceed(result.context, result.result);
				} else {
					result.listener.onError(result.context, msg);
				}
				result.listener.onFinished(result.context, msg);
			}
		}.execute(task);
	}

	private void queryHistoryData(Context context, final boolean isRefresh) {
		if (mHistoryAdapter.getCount() == 0) {
			showLoadingPage(R.id.history_container);
		}
		int index = isRefresh ? 0 : mHistoryAdapter.getCount();
		query(context, index, REQUEST_SIZE,
				new AsyncTaskListener<List<Goods>>() {

					@Override
					public void onSucceed(Context context, List<Goods> result) {
						if (result != null && result.size() > 0) {
							closeLoadingPage();
							if (isRefresh || mHistoryAdapter.getCount() == 0) {
								mHistoryAdapter.setData(result);
							} else {
								mHistoryAdapter.addItems(result);
							}
						} else {
							onError(context, ResultCode.NO_MORE_DATA.msg);
						}
						mListView.setPullLoadEnable(result != null
								&& result.size() >= REQUEST_SIZE);
					}

					@Override
					public void onError(Context context, String msg) {
						setLoadFailedMessage(msg);
					}

					@Override
					public void onFinished(Context context, String msg) {
						mListView.stopLoadMore();
						mListView.stopRefresh();
					}

				});
	}
}

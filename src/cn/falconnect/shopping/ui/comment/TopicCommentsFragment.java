package cn.falconnect.shopping.ui.comment;

import static cn.falconnect.shopping.constants.Global.BundleKey.ID;

import java.util.List;

import org.aurora.library.views.Toaster;
import org.aurora.library.views.list.xlistview.XListView;
import org.aurora.library.views.list.xlistview.XListView.IXListViewListener;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.falconnect.shopping.adapter.CommentsAdapter;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.Feedback;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.ui.BaseFragment;

public class TopicCommentsFragment extends BaseFragment {
	public static final String FRAGMENT_TAG = "TopicCommentsFragment";
	private View mContentView;
	private XListView mCommentsListView;
	private CommentsAdapter mCommentsAdapter;
	private int mTopicId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			mTopicId = args.getInt(ID);
		}
	}

	@Override
	protected String getTDPageName() {
		return getString(R.string.topic_comment);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater
					.inflate(R.layout.comments_list_layout, null);
			initViews();
			requestTopicComments(getActivity(), true);
		}
		return mContentView;
	}

	@Override
	public void onReload(Context context) {
		requestTopicComments(context, true);
	}

	public void onUpdate() {
		mIXListViewListener.onRefresh(mCommentsListView);
	}

	private void initViews() {
		mCommentsListView = (XListView) mContentView
				.findViewById(android.R.id.list);
		mCommentsListView.setPullLoadEnable(false);
		mCommentsListView.setOnItemClickListener(mOnItemClickListener);
		mCommentsListView.setXListViewListener(mIXListViewListener);
		mCommentsAdapter = new CommentsAdapter();
		mCommentsListView.setAdapter(mCommentsAdapter);
	}

	private void requestTopicComments(Context context, final boolean isRefresh) {
		if (mCommentsAdapter.getCount() == 0) {
			showLoadingPage(R.id.comments_container);
		}
		int offset = isRefresh ? 0 : mCommentsAdapter.getCount();
		ProviderFatory.getShowProvider().getShowFeedback(context, mTopicId,
				offset, REQUEST_SIZE, new ObtainListener<List<Feedback>>() {

					@Override
					public void onSucceed(Context context, List<Feedback> result) {
						if (result != null && result.size() > 0) {
							closeLoadingPage();
							if (isRefresh || mCommentsAdapter.getCount() == 0) {
								mCommentsAdapter.setData(result);
							} else {
								mCommentsAdapter.addItems(result);
							}
						} else {
							onError(context, ResultCode.NO_MORE_DATA);
						}
						mCommentsListView.setPullLoadEnable(result != null
								&& result.size() >= REQUEST_SIZE);
					}

					@Override
					public void onFinished(Context context, ResultCode code) {
						mCommentsListView.stopLoadMore();
						mCommentsListView.stopRefresh();
					}

					@Override
					public void onError(Context context, ResultCode code) {
						if (mCommentsAdapter.getCount() == 0) {
							setLoadFailedMessage(code.msg);
						} else {
							Toaster.toast(code.msg);
						}
					}
				});
	}

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

		}

	};

	private IXListViewListener mIXListViewListener = new IXListViewListener() {

		@Override
		public void onRefresh(XListView xListView) {
			requestTopicComments(xListView.getContext(), true);
		}

		@Override
		public void onLoadMore(XListView xListView) {
			requestTopicComments(xListView.getContext(), false);
		}
	};
}

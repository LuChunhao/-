package cn.falconnect.shopping.ui.topic;

import static cn.falconnect.shopping.constants.Global.BundleKey.SHOW_BEAN;

import java.util.List;

import org.aurora.library.views.Toaster;
import org.aurora.library.views.waterfall.PLA.lib.internal.PLA_AdapterView;
import org.aurora.library.views.waterfall.PLA.lib.internal.PLA_AdapterView.OnItemClickListener;
import org.aurora.library.views.waterfall.PLA.view.PlaWaterfallListView;
import org.aurora.library.views.waterfall.PLA.view.PlaWaterfallListView.IXListViewListener;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import cn.falconnect.shopping.adapter.ShowPicAdapter;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.Show;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.ui.BaseFragment;

public class TopicFragment extends BaseFragment implements OnItemClickListener {
	public static final String FRAGMENT_TAG = "TopicFragment";
	private View mContentView;
	private PlaWaterfallListView mShowListView;
	private ShowPicAdapter mShowAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_topic, null);
			initViews();
			getAllShow(mContentView.getContext(), true);
		}
		return mContentView;
	}

	@Override
	protected String getTDPageName() {
		return getString(R.string.topic);
	}

	@Override
	public void onReload(Context context) {
		super.onReload(context);
		getAllShow(context, true);
	}

	private void initViews() {
		mShowListView = (PlaWaterfallListView) mContentView
				.findViewById(android.R.id.list);
		mShowListView.setPullLoadEnable(false);
		mShowAdapter = new ShowPicAdapter();
		mShowListView.setAdapter(mShowAdapter);
		mShowListView.setXListViewListener(mIXListener);
		mShowListView.setOnItemClickListener(this);
		ViewGroup viewgroup_go_show = (ViewGroup) mContentView
				.findViewById(R.id.rl_go_show);
		viewgroup_go_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean logined = ProviderFatory.getUserProvider().isLogined(
						getActivity());
				if (!logined) {
					Toaster.toast("只有登录才能晒美图哦");
					return;
				}
				Bundle bundle = new Bundle();
				bundle.putString(PublishShowFragment.FRAGMENT_TAG, FRAGMENT_TAG);
				startFragment(new PublishShowFragment(), bundle,
						PublishShowFragment.class.getName(),
						PublishShowFragment.FRAGMENT_TAG);
			}
		});
	}

	private void getAllShow(Context context, final boolean isRefresh) {
		if (mShowAdapter.getCount() == 0) {
			showLoadingPage(R.id.fragment_topic_container);
		}
		int offset = isRefresh ? 0 : mShowAdapter.getCount();
		ProviderFatory.getShowProvider().getAllShow(context, offset,
				REQUEST_SIZE, new ObtainListener<List<Show>>() {

					@Override
					public void onSucceed(Context context, List<Show> result) {
						if (result != null && result.size() > 0) {
							closeLoadingPage();
							if (isRefresh || mShowAdapter.getCount() == 0) {
								mShowAdapter.setData(result);
							} else {
								mShowAdapter.addItems(result);
							}
						} else {
							onError(context, ResultCode.NO_MORE_DATA);
						}
						mShowListView.setPullLoadEnable(result != null
								&& result.size() >= REQUEST_SIZE);
					}

					@Override
					public void onFinished(Context context, ResultCode code) {
						mShowListView.stopLoadMore();
						mShowListView.stopRefresh();
					}

					@Override
					public void onError(Context context, ResultCode code) {
						if (mShowAdapter.getCount() == 0) {
							setLoadFailedMessage(code.msg);
						} else {
							Toaster.toast(code.msg);
						}
					}
				});
	}

	public void onUpdate() {
		mIXListener.onRefresh(mShowListView);
	}

	private IXListViewListener mIXListener = new IXListViewListener() {

		@Override
		public void onRefresh(PlaWaterfallListView xListView) {
			getAllShow(xListView.getContext(), true);
		}

		@Override
		public void onLoadMore(PlaWaterfallListView xListView) {
			getAllShow(xListView.getContext(), false);
		}
	};

	@Override
	public void onItemClick(PLA_AdapterView<?> parent, View view, int position,
			long id) {
		Show show = (Show) parent.getItemAtPosition(position);
		Bundle args = new Bundle();
		args.putSerializable(SHOW_BEAN, show);
		startFragment(new TopicMainDetailFragment(), args);
	}
}

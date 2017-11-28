package cn.falconnect.shopping.ui.user;

import static cn.falconnect.shopping.constants.Global.BundleKey.COLLECTS_ENTRY_KEY;
import static cn.falconnect.shopping.constants.Global.BundleKey.GOODS_BEAN;

import java.util.List;

import org.aurora.library.views.list.xlistview.XListView;
import org.aurora.library.views.list.xlistview.XListView.IXListViewListener;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.falconnect.shopping.adapter.CollectsAdapter;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.Goods;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.ui.GoodsDetailFragment;
import cn.falconnect.shopping.ui.SlidingExitFragment;

public class CollectionsFragment extends SlidingExitFragment {
	public static final String FRAGMENT_TAG = "CollectionsFragment";
	private XListView mCollectsListView;
	private CollectsAdapter mCollectsAdapter;

	@Override
	public View onChildCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater
				.inflate(R.layout.fragment_collections, null);
		initViews(contentView);
		requestCollectsData(contentView.getContext());
		return contentView;
	}

	@Override
	protected String getTDPageName() {
		return getString(R.string.my_collection);
	}
	
	private void initViews(View contentView) {
		mCollectsListView = (XListView) contentView
				.findViewById(android.R.id.list);
		mCollectsListView.setPullLoadEnable(false);
		mCollectsAdapter = new CollectsAdapter();
		mCollectsListView.setAdapter(mCollectsAdapter);
		mCollectsListView.setXListViewListener(mIXListener);
		mCollectsListView.setOnItemClickListener(mOnItemClickListener);
	}

	@Override
	public void onReload(Context context) {
		requestCollectsData(context);
	}

	public void onUpdate() {
		mCollectsAdapter.clear();
		mIXListener.onRefresh(mCollectsListView);
	}

	private IXListViewListener mIXListener = new IXListViewListener() {

		@Override
		public void onRefresh(XListView xListView) {
			requestCollectsData(xListView.getContext());

		}

		@Override
		public void onLoadMore(XListView xListView) {

		}
	};

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Goods goods = mCollectsAdapter.getItem(position
					- mCollectsListView.getHeaderViewsCount());
			Bundle args = new Bundle();
			args.putSerializable(GOODS_BEAN, goods);
			args.putString(COLLECTS_ENTRY_KEY, FRAGMENT_TAG);
			startFragment(new GoodsDetailFragment(), args,GoodsDetailFragment.FRAGMENT_TAG);
		}
	};

	private void requestCollectsData(Context context) {
		if (mCollectsAdapter.getCount() == 0) {
			showLoadingPage(R.id.collections_container);
		}
		ProviderFatory.getGoodsProvider().obtainUserCollectionList(context,
				new ObtainListener<List<Goods>>() {

					@Override
					public void onSucceed(Context context, List<Goods> result) {
						if (result != null && result.size() > 0) {
							closeLoadingPage();
							mCollectsAdapter.setData(result);
						} else {
							onError(context, ResultCode.NO_MORE_DATA);
						}
					}

					@Override
					public void onError(Context context, ResultCode code) {
						if (mCollectsAdapter.getCount() == 0) {
							setLoadFailedMessage(code.msg);
//							if (code == ResultCode.LOGIN) {
//								MainFragment mainFragment = (MainFragment) getActivity()
//										.getSupportFragmentManager()
//										.findFragmentByTag(
//												MainFragment.FRAGEMNT_TAG);
//								PersonalCenterFragment centerFragment = (PersonalCenterFragment) mainFragment
//										.findFragmentByTag(PersonalCenterFragment.FRAGEMNT_TAG);
//								if (centerFragment != null) {
//									ProviderFatory.getUserProvider().logout(
//											context);
//									centerFragment.update();
//								}
//							}
						}
					}

					@Override
					public void onFinished(Context context, ResultCode code) {
						mCollectsListView.stopRefresh();
					}
				});
	}

}

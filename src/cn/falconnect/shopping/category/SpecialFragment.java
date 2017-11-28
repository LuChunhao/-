package cn.falconnect.shopping.category;

import static cn.falconnect.shopping.constants.Global.BundleKey.GOODS_BEAN;

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
import cn.falconnect.shopping.adapter.HomeGoodsAdapter;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.constants.Global;
import cn.falconnect.shopping.entity.Goods;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.ui.BaseFragment;
import cn.falconnect.shopping.ui.GoodsDetailFragment;

public class SpecialFragment extends BaseFragment {
	static final String EXTRA_SORT_TYPE = "sort_type";
	private XListView mListView;
	private HomeGoodsAdapter mGoodsAdapter;
	private int mId;
	private int mSortType;
	private View mContentView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			mId = bundle.getInt(Global.BundleKey.ID);
			mSortType = bundle.getInt(SpecialFragment.EXTRA_SORT_TYPE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.common_sort_goods_layout,
					null);
			initViews();
			obtainGoodsList(mContentView.getContext(), true);
		}
		return mContentView;
	}

	private void initViews() {
		mListView = (XListView) mContentView.findViewById(R.id.listView);
		mListView.setPullEnable(true);
		mGoodsAdapter = new HomeGoodsAdapter();
		mListView.setAdapter(mGoodsAdapter);
		setListeners();
	}

	private void setListeners() {
		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh(XListView xListView) {
				obtainGoodsList(xListView.getContext(), true);
			}

			@Override
			public void onLoadMore(XListView xListView) {
				obtainGoodsList(xListView.getContext(), false);
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Goods goods = (Goods) parent.getItemAtPosition(position);
				Bundle args = new Bundle();
				args.putSerializable(GOODS_BEAN, goods);
				startFragment(new GoodsDetailFragment(), args,GoodsDetailFragment.FRAGMENT_TAG);
			}
		});
	}

	@Override
	public void onReload(Context context) {
		super.onReload(context);
		obtainGoodsList(context, true);
	}

	void refreshBySort(Context context, int sort) {
		this.mSortType = sort;
		onReload(context);
	}

	private void obtainGoodsList(Context context, final boolean isRefresh) {
		if (mGoodsAdapter.getCount() == 0) {
			showLoadingPage();
		}
		int offset= isRefresh ? 0 : mGoodsAdapter.getCount();
		ProviderFatory.getGoodsProvider().obtainGoodsByType(context, offset,
				REQUEST_SIZE, mId, mSortType,
				new ObtainListener<List<Goods>>() {

					@Override
					public void onSucceed(Context context, List<Goods> result) {
						if (result != null && result.size() > 0) {
							closeLoadingPage();
							if (isRefresh || mGoodsAdapter.getCount() == 0) {
								mGoodsAdapter.setData(result);
							} else {
								mGoodsAdapter.addItems(result);
							}
						}else {
							onError(context, ResultCode.NO_MORE_DATA);
						}
						mListView.setPullLoadEnable(result != null
								&& result.size() >= REQUEST_SIZE);
					}

					@Override
					public void onFinished(Context context, ResultCode code) {
						mListView.stopLoadMore();
						mListView.stopRefresh();
					}

					@Override
					public void onError(Context context, ResultCode code) {
						if (mGoodsAdapter.getCount() == 0) {
							setLoadFailedMessage(code.msg);
						} else {
							Toaster.toast(code.msg);
						}
					}
				});
	}
}

package cn.falconnect.shopping.ui.recommend;

import static cn.falconnect.shopping.constants.Global.BundleKey.DETAIL_URL;
import static cn.falconnect.shopping.constants.Global.BundleKey.GOODS_BEAN;

import java.util.List;

import org.aurora.library.views.Toaster;
import org.aurora.library.views.list.xlistview.XListView;
import org.aurora.library.views.list.xlistview.XListView.IXListViewListener;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.falconnect.shopping.adapter.AutoFlingBannerAdapter;
import cn.falconnect.shopping.adapter.AutoFlingBannerAdapter.OnBannerClickListener;
import cn.falconnect.shopping.adapter.HomeGoodsAdapter;
import cn.falconnect.shopping.banner.Banner;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.Goods;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.ui.BaseFragment;
import cn.falconnect.shopping.ui.GoodsDetailFragment;
import cn.falconnect.shopping.utils.CommonUtil;

public class RecommendFragment extends BaseFragment implements
		OnBannerClickListener {
	public static final String FRAGMENT_TAG = "RecommendFragment";
	private Banner mBanner;
	private AutoFlingBannerAdapter mAutoFlingBannerAdapter;
	private HomeGoodsAdapter mGoodsAdapter;
	private XListView mListView;
	private View mContentView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_home, null);
			mListView = (XListView) mContentView.findViewById(R.id.listView);
			mListView.setPullLoadEnable(false);
			mBanner = createBanner(mContentView.getContext());
			mAutoFlingBannerAdapter = new AutoFlingBannerAdapter(this);
			mBanner.setAdapter(mAutoFlingBannerAdapter);
			mGoodsAdapter = new HomeGoodsAdapter();
			mListView.setAdapter(mGoodsAdapter);
			mListView.addHeaderView(mBanner);
			setListeners();
			obtainBanners(mContentView.getContext());
			obtainGoods(mContentView.getContext(), true);
		}
		return mContentView;
	}

	private Banner createBanner(Context context) {
		Banner banner = new Banner(context);
		banner.setLayoutParams(new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT, CommonUtil
						.convertDipToPx(context, 160)));
		banner.changeIndicatorStyle(Banner.Rule.CENTER, 30, Color.TRANSPARENT);
		return banner;
	}

	@Override
	public void onClick(Goods goods) {
		Bundle args = new Bundle();
		if (!TextUtils.isEmpty(goods.detailUrl)) {
			args.putString(DETAIL_URL, goods.detailUrl);
			startFragment(new GoodsDetailFragment(), args,
					GoodsDetailFragment.FRAGMENT_TAG);
		}
	}

	private void setListeners() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position >= mListView.getHeaderViewsCount()) {
					Goods goods = (Goods) parent.getItemAtPosition(position);
					Bundle args = new Bundle();
					if (!TextUtils.isEmpty(goods.detailUrl)) {
						args.putSerializable(GOODS_BEAN, goods);
						startFragment(new GoodsDetailFragment(), args,
								GoodsDetailFragment.FRAGMENT_TAG);
					}
				}
			}
		});
		mListView.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh(XListView xListView) {
				obtainBanners(xListView.getContext());
				obtainGoods(xListView.getContext(), true);
			}

			@Override
			public void onLoadMore(XListView xListView) {
				obtainGoods(xListView.getContext(), false);
			}
		});
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mBanner != null) {
			mBanner.start();
		}
	}

	@Override
	public void onPause() {
		if (mBanner != null) {
			mBanner.stop();
		}
		super.onPause();
	}

	@Override
	protected String getTDPageName() {
		return getString(R.string.recommend);
	}

	@Override
	public void onReload(Context context) {
		super.onReload(context);
		obtainBanners(context);
		obtainGoods(context, true);
	}

	private void obtainBanners(Context context) {
		ProviderFatory.getGoodsProvider().obtainBanner(context,
				new ObtainListener<List<Goods>>() {

					@Override
					public void onSucceed(Context context, List<Goods> result) {
						mAutoFlingBannerAdapter.setData(result);
					}

					@Override
					public void onFinished(Context context, ResultCode code) {
					}

					@Override
					public void onError(Context context, ResultCode code) {
					}

				});

	}

	private void obtainGoods(Context context, final boolean isRefresh) {
		if (mGoodsAdapter.getCount() == 0) {
			showLoadingPage(R.id.rl_home_goods);
		}
		int offset = isRefresh ? 0 : mGoodsAdapter.getCount();
		ProviderFatory.getGoodsProvider().obtainRecommendGoodsList(context,
				offset, REQUEST_SIZE, new ObtainListener<List<Goods>>() {

					@Override
					public void onSucceed(Context context, List<Goods> result) {
						if (result != null && result.size() > 0) {
							closeLoadingPage();
							if (isRefresh || mGoodsAdapter.getCount() == 0) {
								mGoodsAdapter.setData(result);
							} else {
								mGoodsAdapter.addItems(result);
							}
						} else {
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

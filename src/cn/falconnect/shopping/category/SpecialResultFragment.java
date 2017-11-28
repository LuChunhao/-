package cn.falconnect.shopping.category;

import java.util.ArrayList;

import org.aurora.library.views.slicenoodles.InterceptTouchEventListener;
import org.aurora.library.views.slicenoodles.SlicedNoodlesLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.constants.Global;
import cn.falconnect.shopping.tabs.BaseTabBar.OnTabItemClickListener;
import cn.falconnect.shopping.tabs.BaseTabGroup.TabChangedListener;
import cn.falconnect.shopping.tabs.FragmentPagerTabGroup;
import cn.falconnect.shopping.tabs.FragmentPagerTabGroup.TabSpec;
import cn.falconnect.shopping.title.TitleBar;
import cn.falconnect.shopping.ui.SlidingExitFragment;

public class SpecialResultFragment extends SlidingExitFragment {
	private View mContentView;
	private FragmentPagerTabGroup mTabGroup;
	private String mName;
	private int mId;
	private boolean mFlag;
	private int arrowRes = R.drawable.arrow_price_up;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		mName = bundle.getString(Global.BundleKey.GOODS_TYPE_NAME);
		mId = bundle.getInt(Global.BundleKey.ID);
	}

	@Override
	public View onChildCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater
					.inflate(R.layout.common_search_result, null);
			initViews();
		}
		return mContentView;
	}

	private InterceptTouchEventListener createInterceptAdapter() {
		return new InterceptTouchEventListener() {

			@Override
			public boolean isIntercept(MotionEvent motionEvent, float moveX,
					float moveY, boolean superIntercept, int location) {
				if (location == SlicedNoodlesLayout.LOCATION_LEFT_DOWN
						|| location == SlicedNoodlesLayout.LOCATION_RIGHT_UP) {
					return true;
				}
				if (mTabGroup.getPager().getCurrentItem() == 3) {
					return moveX < 0;
				}
				if (mTabGroup.getPager().getCurrentItem() == 0) {
					return moveX > 0;
				}
				return false;
			}
		};
	}

	private void initViews() {
		setInterceptTouchEventAdapter(createInterceptAdapter());
		TitleBar titleBar = (TitleBar)mContentView.findViewById(R.id.title_bar);
		if (titleBar != null) {
			titleBar.setTitle(mName);
		}
		mTabGroup = (FragmentPagerTabGroup) mContentView
				.findViewById(R.id.result_tabgroup);
		mTabGroup.setupInFragment(this, FragmentPagerTabGroup.LOCATION_TOP,
				false);
		mTabGroup.setPagerOffscreenPageLimit(1);
		buildTabs();
		mTabGroup.getTabWidgetBar().setOnTabItemClickListener(
				new OnTabItemClickListener() {

					@Override
					public void OnItemClick(View currentView, View parent,
							int position) {
						if (2 == position) {
							ImageView arrow = (ImageView) currentView
									.findViewById(R.id.arrow);
							arrowRes = mFlag ? R.drawable.arrow_price_up
									: R.drawable.arrow_price_down;
							arrow.setImageResource(arrowRes);
							SpecialFragment fragment = (SpecialFragment) mTabGroup
									.getFragmentByPosition(position);
							fragment.refreshBySort(parent.getContext(),
									mFlag ? Global.Asortment.BY_PRICE_ASC
											: Global.Asortment.BY_PRICE_DESC);
							mFlag = !mFlag;
						} else {
							ImageView arrow = (ImageView) parent
									.findViewById(R.id.arrow);
							arrow.setImageResource(R.drawable.arrow_default);
						}
					}
				});
		mTabGroup.setOnTabChangeListener(new TabChangedListener() {

			@Override
			public void onTabClick(int position) {

			}

			@Override
			public void onTabChanged(int position, View parent) {
				ImageView arrow = (ImageView) parent.findViewById(R.id.arrow);
				if (position == 2) {
					arrow.setImageResource(arrowRes);
					SpecialFragment fragment = (SpecialFragment) mTabGroup
							.getFragmentByPosition(position);
					fragment.refreshBySort(
							parent.getContext(),
							R.drawable.arrow_price_up == arrowRes ? Global.Asortment.BY_PRICE_ASC
									: Global.Asortment.BY_PRICE_DESC);
				} else {
					arrow.setImageResource(R.drawable.arrow_default);
				}
			}
		});
	}

	private void buildTabs() {
		View priceIndicator = LayoutInflater.from(getActivity()).inflate(
				R.layout.price_indicator, null);
		String[] tabs = getResources().getStringArray(R.array.result_tab_array);
		int[] sorts = { Global.Asortment.BY_DEFAULT, Global.Asortment.BY_SALES,
				Global.Asortment.BY_PRICE_ASC, Global.Asortment.BY_LATEST };
		ArrayList<TabSpec> tabSpecList = new ArrayList<FragmentPagerTabGroup.TabSpec>();
		for (int i = 0; i < tabs.length; i++) {
			TabSpec tabBarSpec = mTabGroup.newTabSpec(tabs[i]);
			tabBarSpec.setContent(SpecialFragment.class, buildBundle(sorts[i]));
			if (i == 2) {
				tabBarSpec.setIndicator(priceIndicator);
			} else {
				tabBarSpec.setIndicator(tabs[i]);
			}
			tabSpecList.add(tabBarSpec);
		}
		mTabGroup.addTabs(tabSpecList);
		mTabGroup.setCurrentTab(0);

	}

	private Bundle buildBundle(int sort) {
		Bundle bundle = new Bundle();
		bundle.putInt(Global.BundleKey.ID, mId);
		bundle.putInt(SpecialFragment.EXTRA_SORT_TYPE, sort);
		return bundle;
	}

}

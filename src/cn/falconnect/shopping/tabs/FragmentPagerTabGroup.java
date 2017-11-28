package cn.falconnect.shopping.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.falconnect.shopping.cat.R;

public class FragmentPagerTabGroup extends FragmentTabGroup implements
		OnPageChangeListener {
	private boolean mPageScrollAnimTrigger = true;
	private ViewPager mViewPager;
	private TabPagerAdapter mPagerAdapter;
	private SparseArray<Map<View, Rect>> mUnFlingViews = new SparseArray<Map<View, Rect>>();

	public FragmentPagerTabGroup(Context activity, int layoutId) {
		super(activity, layoutId);
	}

	public FragmentPagerTabGroup(Context activity, AttributeSet attrs) {
		super(activity, attrs);
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		super.onRestoreInstanceState(null);
	}

	public ViewPager getPager() {
		return mViewPager;
	}

	@Override
	public void onPageSelected(int position) {
		if (mTabBar != null) {
			mTabBar.seleteItem(position);
			mCurrentPosition = position;
			if (mTabChangedListener != null) {
				mTabChangedListener.onTabChanged(position,mTabBar);
			}
		}

	}

	public Fragment getCurrentFragment() {
		return mPagerAdapter.getFragmentByPosition(mCurrentPosition);
	}

	public Fragment getFragmentByPosition(int position) {
		return mPagerAdapter.getFragmentByPosition(position);

	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if (state == ViewPager.SCROLL_STATE_IDLE) {
			mPageScrollAnimTrigger = true;
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		if (mPageScrollAnimTrigger && mTabBar != null
				&& mTabBar.getAnimDrawable() != null) {
			// mTabBar.animImageOffset(position, positionOffset);
		}
	}

	@Override
	public void setup(int tabBarLocation, boolean isNeedScroll) {
		super.setup(tabBarLocation, isNeedScroll);
	}

	@Override
	protected ViewGroup createContainerLayout() {
		mPagerAdapter = new TabPagerAdapter(getFragmentManager());
		mViewPager = createViewPager(getContext());
		mViewPager.setId(this.getId());
		setId(View.NO_ID);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
		return mViewPager;
	}

	protected ViewPager createViewPager(Context context) {
		return new TabPager(context);
	}

	public void addUnflingView(final int pageIndex, final View view) {
		if (mViewPager instanceof TabPager) {
			Map<View, Rect> map = mUnFlingViews.get(pageIndex);
			if (map == null) {
				map = new HashMap<View, Rect>();
			}
			if (!map.containsKey(view)) {
				map.put(view, new Rect());
			}
			mUnFlingViews.put(pageIndex, map);
		}
	}

	public void setPagerOffscreenPageLimit(int limit) {
		mViewPager.setOffscreenPageLimit(limit);
	}

	public void clearTab() {
		mTabBar.clearWidget();
		mTabs.clear();
		mPagerAdapter.notifyDataSetChanged();
	}

	public void addTab(String tag, Class<?> fragmentClass, Bundle args) {
		TabInfo tabInfo = new TabInfo(fragmentClass, args);
		tabInfo.tag = !TextUtils.isEmpty(tag) ? tag : getContainerId() + ":"
				+ mTabs.size() + ":" + fragmentClass.hashCode();
		// check if already have the fragment for this tab in manager
		Fragment fragment = getFragmentManager().findFragmentByTag(tabInfo.tag);
		if (fragment != null && !fragment.isDetached()) {
			getFragmentManager().beginTransaction().detach(fragment).commit();
		}
		mTabs.add(tabInfo);
		mPagerAdapter.notifyDataSetChanged();
	}

	@Override
	public void addTab(Class<?> fragmentClass, Bundle args) {
		addTab(null, fragmentClass, args);
	}

	public void addTab(View view, String tag, Class<?> fragmentClass,
			Bundle args) {
		addTab(tag, fragmentClass, args);
		if (view != null) {
			mTabBar.addWidget(view);
		}
		mPagerAdapter.notifyDataSetChanged();
	}

	public void addTab(TabSpec tabBarSpec) {
		if (tabBarSpec.mFragmentClass == null) {
			throw new IllegalArgumentException(
					"you must specify a way to create the tab content");
		}
		addTab(tabBarSpec.createIndicatorView(), tabBarSpec.mTag,
				tabBarSpec.mFragmentClass, tabBarSpec.mbundle);
	}

	public void addTabs(ArrayList<TabSpec> tabBarSpecs) {
		View[] views = new View[tabBarSpecs.size()];
		for (int i = 0; i < tabBarSpecs.size(); i++) {
			addTab(tabBarSpecs.get(i).mTag, tabBarSpecs.get(i).mFragmentClass,
					tabBarSpecs.get(i).mbundle);
			views[i] = tabBarSpecs.get(i).createIndicatorView();
		}
		mTabBar.addWidgets(views);
		mPagerAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onTabSelected(int position) {
		position = Math.min(position, mTabs.size() - 1);
		position = Math.max(0, position);
		if (mCurrentPosition != -1) {
			mPageScrollAnimTrigger = false;
		}
		mCurrentPosition = position;
		mViewPager.setCurrentItem(position);
	}

	private boolean isInUnflingArea(int x, int y) {
		if (mUnFlingViews.get(mCurrentPosition) != null) {
			Map<View, Rect> map = mUnFlingViews.get(mCurrentPosition);
			for (Map.Entry<View, Rect> entry : map.entrySet()) {
				Rect rect = entry.getValue();
				entry.getKey().getHitRect(rect);
				if (rect.contains(x, y)) {
					return true;
				}
			}
		}
		return false;
	}

	class TabPager extends ViewPager {

		public TabPager(Context context) {
			super(context);
		}

		@Override
		public boolean onInterceptTouchEvent(MotionEvent event) {
			// 排除滑屏的区域
			if (isInUnflingArea((int) event.getX(), (int) event.getY())) {
				return false;
			}
			return super.onInterceptTouchEvent(event);
		}
	}

	class TabPagerAdapter extends MyFragmentStatePagerAdapter {
		private FragmentManager manager;

		public TabPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
			manager = fragmentManager;
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			super.setPrimaryItem(container, position, object);
			if (mTabs != null && !mTabs.isEmpty()) {
				TabInfo info = mTabs.get(position);
				if (info.tag == null) {
					info.tag = ((Fragment) object).getTag();
				}
			}
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public Fragment getItem(int position) {

			TabInfo info = mTabs.get(position);
			Fragment fragment = manager.findFragmentByTag(info.tag);
			if (fragment == null) {
				fragment = Fragment.instantiate(getContext(),
						info.fragmentClass.getName(), info.args);
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

	}

	public TabSpec newTabSpec(String tag) {
		return new TabSpec(tag);
	}

	public class TabSpec {
		public String mTag;
		private IndicatorStrategy mIndicatorStrategy;
		public Class<?> mFragmentClass;
		public Bundle mbundle;

		public void setContent(Class<?> fragmentClass, Bundle bundle) {
			this.mFragmentClass = fragmentClass;
			this.mbundle = bundle;
		}

		public TabSpec(String mTag) {
			this.mTag = mTag;
		}

		public void setIndicator(String label) {
			mIndicatorStrategy = new LabelIndicatorStrategy(label);
		}

		public void setIndicator(View view) {
			mIndicatorStrategy = new ViewIndicatorStrategy(view);
		}

		public View createIndicatorView() {
			if (mIndicatorStrategy == null) {
				throw new IllegalArgumentException(
						"you must specify a way to create the tab indicator.");
			}
			return mIndicatorStrategy.createIndicatorView();
		}

		private class ViewIndicatorStrategy implements IndicatorStrategy {

			private final View mView;

			private ViewIndicatorStrategy(View view) {
				mView = view;
			}

			public View createIndicatorView() {
				return mView;
			}
		}

		private class LabelIndicatorStrategy implements IndicatorStrategy {

			private final CharSequence mLabel;

			private LabelIndicatorStrategy(CharSequence label) {
				mLabel = label;
			}

			public View createIndicatorView() {
				LayoutInflater inflater = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				TextView textview = (TextView) inflater.inflate(
						R.layout.top_tab_view, null);
				textview.setText(mLabel);
				return textview;
			}
		}

	}

	public static interface IndicatorStrategy {
		View createIndicatorView();
	}
}

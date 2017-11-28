package cn.falconnect.shopping.tabs;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import cn.falconnect.shopping.tabs.BaseTabBar.OnViewSwitchedListener;

public abstract class BaseTabGroup extends RelativeLayout implements
		OnViewSwitchedListener {

	public static final int LOCATION_BOTTOM = 0;
	public static final int LOCATION_TOP = 1;
	private static final int WIDGETBAR_ID = 10000;
	protected int mTabBarLocation = LOCATION_BOTTOM;
	protected ViewGroup mContainerLayout;
	protected BaseTabBar mTabBar;
	protected TabChangedListener mTabChangedListener;

	public interface TabChangedListener {
		void onTabChanged(int position, View parent);

		void onTabClick(int position);
	}

	public BaseTabGroup(Context context, int layoutId) {
		super(context);
		this.setId(layoutId);
	}

	public BaseTabGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/** @see #setup(int, android.view.ViewGroup) */
	public void setup() {
		setup(LOCATION_BOTTOM, false);
	}

	/** @see #setup(int, android.view.ViewGroup) */
	public void setup(int tabBarLocation) {
		setup(tabBarLocation, false);
	}

	public void setup(boolean autoScroll) {
		setup(LOCATION_BOTTOM, autoScroll);
	}

	public void setup(int tabBarLocation, boolean autoScroll) {
		if (getId() == View.NO_ID) {
			throw new IllegalArgumentException(this.getClass().getName()
					+ " id could not be none...");
		}
		ViewGroup tabBar = (ViewGroup) findViewById(android.R.id.tabs);
		removeAllViews();
		if (tabBar != null) {
			if (tabBar instanceof BaseTabBar) {
				mTabBar = (BaseTabBar) tabBar;
			} else {
				if (autoScroll) {
					mTabBar = new HorizontalTabBar(getContext(), tabBar);
				} else {
					mTabBar = new HostTabBar(getContext(), tabBar);
				}
			}
			mTabBar.setOnViewSwitchedListener(this);
			mTabBar.setId(WIDGETBAR_ID);
		}
		mContainerLayout = createContainerLayout();
		LayoutParams containerLayoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		if (mTabBar != null) {
			LayoutParams tabBarLayoutParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			int widgetBarRule = (tabBarLocation == LOCATION_TOP) ? RelativeLayout.ALIGN_PARENT_TOP
					: RelativeLayout.ALIGN_PARENT_BOTTOM;
			tabBarLayoutParams.addRule(widgetBarRule);
			int containerRule = (tabBarLocation == LOCATION_TOP) ? RelativeLayout.BELOW
					: RelativeLayout.ABOVE;
			containerLayoutParams.addRule(containerRule, WIDGETBAR_ID);
			addView(mTabBar, tabBarLayoutParams);
		}
		addView(mContainerLayout, containerLayoutParams);
	}

	public abstract void addTab(Class<?> clazz, Bundle args);

	public abstract int getCurrentPosition();

	protected abstract void onTabSelected(int position);

	@Override
	public void onWidgetSwitched(int position, View selectedView) {
		onTabSelected(position);
		if (mTabChangedListener != null) {
			mTabChangedListener.onTabChanged(position, mTabBar);
		}
	}

	public void setCurrentTab(final int position, final boolean isStartAnimation) {
		if (mTabBar != null) {
			mTabBar.selectItem(position, isStartAnimation, true);
		} else {
			onTabSelected(position);
			if (mTabChangedListener != null) {
				mTabChangedListener.onTabChanged(position, mTabBar);
			}
		}
	}

	public void setCurrentTab(int position) {
		setCurrentTab(position, false);
	}

	public void setOnTabChangeListener(TabChangedListener listener) {
		mTabChangedListener = listener;
	}

	public BaseTabBar getTabWidgetBar() {
		return mTabBar;
	}

	public int getContainerId() {
		return mContainerLayout.getId();
	}

	protected ViewGroup createContainerLayout() {
		FrameLayout containerLayout = new FrameLayout(getContext());
		containerLayout.setId(this.getId());
		setId(View.NO_ID);
		return containerLayout;
	}

}

package cn.falconnect.shopping.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.category.CategoryFragment;
import cn.falconnect.shopping.tabs.FragmentHostTabGroup;
import cn.falconnect.shopping.tabs.FragmentPagerTabGroup;
import cn.falconnect.shopping.ui.mystica.MySticaFragment;
import cn.falconnect.shopping.ui.recommend.RecommendFragment;
import cn.falconnect.shopping.ui.topic.TopicFragment;

public class HomeFragment extends BaseFragment {
	public static final String FRAGEMNT_TAG = "HomeFragment";
	private FragmentPagerTabGroup mTabGroup;
	private View mContentView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.main_layout, null);
			setupContentView();
		}
		return mContentView;
	}
		
	private void setupContentView() {
		mTabGroup = (FragmentPagerTabGroup) mContentView
				.findViewById(R.id.main_tabgroup);
		mTabGroup.setup(FragmentHostTabGroup.LOCATION_TOP);
		mTabGroup.getTabWidgetBar().setAnimDrawable(
				getResources().getDrawable(R.drawable.vpi_tab_focus), true);
		mTabGroup.addTab(RecommendFragment.FRAGMENT_TAG,
				RecommendFragment.class, null);
		mTabGroup.addTab(TopicFragment.FRAGMENT_TAG, TopicFragment.class, null);
		mTabGroup.addTab(MySticaFragment.FRAGMENT_TAG, MySticaFragment.class,
				null);
		mTabGroup.addTab(CategoryFragment.FRAGMENT_TAG, CategoryFragment.class,
				null);
		mTabGroup.setPagerOffscreenPageLimit(4);
		mTabGroup.setCurrentTab(0);
	}
	
	public Fragment getCurrentFragment() {
		return mTabGroup.getCurrentFragment();
	}

	public Fragment getFragmentByPosition(int position) {
		return mTabGroup.getFragmentByPosition(position);
	}
}

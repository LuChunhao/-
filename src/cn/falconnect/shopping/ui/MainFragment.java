package cn.falconnect.shopping.ui;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.support.v7.app.DrawerArrowDrawableToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.ui.search.SearchMainFragment;

public class MainFragment extends BaseFragment {
	public static final String FRAGMENT_TAG = "MainFragment";
	private DrawerLayout mDrawerLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_main, null);
		initViews(contentView);
		initContentLayout();
		initDrawerLayout();
		return contentView;
	}

	private void initViews(View contentView) {
		// 侧滑菜单
		mDrawerLayout = (DrawerLayout) contentView
				.findViewById(R.id.drawerlayout);
		// 设置侧滑菜单背景图及菜单位置
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_left_shadow,
				GravityCompat.START);
		DrawerArrowDrawableToggle slider = new DrawerArrowDrawableToggle(
				getActivity(), contentView.getContext());
		contentView.findViewById(R.id.search_tag).setOnClickListener(
				mOnClickListener);
		final ImageView toggleView = (ImageView) contentView
				.findViewById(R.id.toggle_tag);
		toggleView.setImageDrawable(slider);
		toggleView.setOnClickListener(mOnClickListener);
		mDrawerLayout.setDrawerListener(new DrawerListener() {

			@Override
			public void onDrawerStateChanged(int state) {

			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				((DrawerArrowDrawableToggle) toggleView.getDrawable())
						.setPosition(Math.min(1f, Math.max(0, slideOffset)));
			}

			@Override
			public void onDrawerOpened(View drawerView) {
			}

			@Override
			public void onDrawerClosed(View drawerView) {
			}
		});
	}

	@Override
	public void toggleDrawer() {
		if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
			mDrawerLayout.closeDrawer(GravityCompat.START);
		} else {
			mDrawerLayout.openDrawer(GravityCompat.START);
		}
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.toggle_tag:
				toggleDrawer();
				break;
			case R.id.search_tag:
				startFragment(new SearchMainFragment(), null,
						SearchMainFragment.FRAGMENT_TAG);
				break;
			default:
				break;
			}
		}
	};

	private void initContentLayout() {
		HomeFragment homeFragment = new HomeFragment();
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.content_container, homeFragment,
						HomeFragment.FRAGEMNT_TAG).commitAllowingStateLoss();
	}

	private void initDrawerLayout() {
		SettingFragment drawerFragment = new SettingFragment();
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.drawer_container, drawerFragment,
						SettingFragment.FRAGMENT_TAG).commitAllowingStateLoss();
	}

}

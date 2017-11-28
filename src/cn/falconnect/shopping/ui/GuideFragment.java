package cn.falconnect.shopping.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.falconnect.shopping.TalkingDataHelper;
import cn.falconnect.shopping.adapter.GuidePageAdapter;
import cn.falconnect.shopping.adapter.GuidePageAdapter.onClickSplashButtonListener;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.utils.ImageHelper;

public class GuideFragment extends BaseFragment implements OnPageChangeListener {
//	public static final String FRAGMENT_TAG = "SettingFragment";
//	private static final String SHAREDPREFERENCES_NAME = "first_pref";
//	private GuidePageAdapter mPageAdapter;
//	private ViewPager mVp_splash;
//	private int mCurrentIndex;
//	private ImageView[] mDots;
//	private List<View> mViews;
//
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
//		View contentView = inflater.inflate(R.layout.guide, container, false);
//		initViews(contentView);
//		initDots(contentView);
//		return contentView;
		return null;
	}
//	private void initDots(View contentView) {
//		LinearLayout ll_dots = (LinearLayout) contentView.findViewById(R.id.ll_dots);
//
//		mDots = new ImageView[mViews.size()];
//		for (int i = 0; i < mViews.size(); i++) {
//			mDots[i] = (ImageView) ll_dots.getChildAt(i);
//			mDots[i].setImageResource(R.drawable.dot_normal);
//		}
//		mCurrentIndex = 0;
//		mDots[mCurrentIndex].setImageResource(R.drawable.dot_press);// 设置选中状态
//	}
//
//	private void setCurrentDot(int position) {
//		if (position < 0 || position > mViews.size() - 1
//				|| mCurrentIndex == position) {
//			return;
//		}
//		mDots[position].setImageResource(R.drawable.dot_press);
//		mDots[mCurrentIndex].setImageResource(R.drawable.dot_normal);
//		mCurrentIndex = position;
//	}
//
//	private void initViews(View contentView) {
//		LayoutInflater inflater = LayoutInflater.from(contentView.getContext());
//		mViews = new ArrayList<View>();
//		mViews.add(inflater.inflate(R.layout.guide_one, null));
//		mViews.add(inflater.inflate(R.layout.guide_two, null));
//		mViews.add(inflater.inflate(R.layout.guide_three, null));
//		mPageAdapter = new GuidePageAdapter();
//		mPageAdapter.setButtonListener(new onClickSplashButtonListener() {
//
//			@Override
//			public void onClick() {
//
//				SharedPreferences preferences = getActivity().getSharedPreferences(
//						MainActivity.SHAREDPREFERENCE_NAME, Activity.MODE_PRIVATE);
//				boolean isFirstIn = preferences.getBoolean("isFirstIn", true);
//				if (isFirstIn) {
//					goHome();
//					setGuided();
//					finishFragment();
//				} else {
//					finishFragment();
//				}
//			}
//		});
//		mVp_splash = (ViewPager) contentView.findViewById(R.id.vp_splash);
//		mVp_splash.setOffscreenPageLimit(mViews.size());
//		mPageAdapter.setData(mViews);
//		mVp_splash.setAdapter(mPageAdapter);
//		mVp_splash.setOnPageChangeListener(this);
//	}
//
//	private void setGuided() {
//		SharedPreferences preferences = getActivity().getSharedPreferences(
//				SHAREDPREFERENCES_NAME, Activity.MODE_PRIVATE);
//		Editor editor = preferences.edit();
//		// 存入数据
//		editor.putBoolean("isFirstIn", false);
//		// 提交修改
//		editor.commit();
//	}
//
//	private void goHome() {
//		// 跳转主界面
//		initMainFragment();
//		finishFragment();
//	}
//
//	private void initMainFragment() {
//		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//		Fragment startFragment = fragmentManager
//				.findFragmentByTag(MainFragment.FRAGMENT_TAG);
//		if (startFragment == null) {
//			startFragment = new MainFragment();
//			FragmentTransaction transaction = fragmentManager
//					.beginTransaction();
//			transaction.add(R.id.fragment_container, startFragment,
//					MainFragment.FRAGMENT_TAG);
//			transaction.commitAllowingStateLoss();
//			fragmentManager.executePendingTransactions();
//		}
//
//	}
//	
	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
//		setCurrentDot(position);
	}
//
//	@Override
//	protected String getTDPageName() {
//		return getString(R.string.user_guide);
//	}
}

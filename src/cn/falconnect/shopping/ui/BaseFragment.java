package cn.falconnect.shopping.ui;

import java.io.Serializable;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import cn.falconnect.shopping.TalkingDataHelper;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.title.TitleBar;
import cn.falconnect.shopping.utils.SoftInput;
import cn.falconnect.shopping.widget.LoadingPage;

public class BaseFragment extends Fragment {

	public static final String FRAGMENT_TAG_KEY = "cn.falconnect.catshop.key";

	public static final String FRAGMENT_STACKNAME = "cn.falconnect.fragment.stackName";

	public static final int REQUEST_SIZE = 20;

	private String mLastStackName;

	protected TitleBar mTitleBar;

	private LoadingPage mLoadingPage;

	protected boolean isLoadingPageShown;

	private int mLoadingPageParentId = View.NO_ID;

	private Serializable mParams;

	public Serializable getParams() {
		return mParams;
	}

	public void setParam(Serializable params) {
		this.mParams = params;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (mParams != null) {
			outState.putSerializable("SerializableParams", mParams);
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!TextUtils.isEmpty(getTDPageName())) {
			TalkingDataHelper.reportPageStart(this, getTDPageName());
		}
	}

	@Override
	public void onPause() {
		if (!TextUtils.isEmpty(getTDPageName())) {
			TalkingDataHelper.reportPageEnd(this, getTDPageName());
		}
		SoftInput.hideSoftInput(getActivity().findViewById(R.id.title_bar));
		super.onPause();
	}

	protected String getTDPageName() {
		return "";
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			mParams = savedInstanceState.getSerializable("SerializableParams");
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (isLoadingPageShown) {
			displayLoadingPage(mLoadingPageParentId);
		}
		mTitleBar = (TitleBar) view.findViewById(R.id.title_bar);
		if (mTitleBar != null) {
			mTitleBar.setup(this);
		}
	}

	public TitleBar getTitleBar() {
		return mTitleBar;
	}

	@Override
	public void onDestroyView() {
		Fragment fragment = getTargetFragment();
		if (fragment != null && fragment instanceof BaseFragment) {
			BaseFragment targetFragment = (BaseFragment) fragment;
			targetFragment.onFragmentResult(getTargetRequestCode());
		}
		removeLoadingPage();
		if (getView() != null) {
			((ViewGroup) getView()).removeAllViews();
		}
		super.onDestroyView();
	}

	protected void onFragmentResult(int targetRequestCode) {
	}

	public void onReload(Context context) {
	}

	public void startFragment(Fragment fragment, Bundle args) {
		startFragment(fragment, args, null, null);
	}

	public void startFragment(Fragment fragment, Bundle args, String tag) {
		startFragment(fragment, args, null, tag);
	}

	public void startFragment(Fragment fragment, Bundle args, String stackName,
			String tag) {
		startFragment(fragment, R.id.fragment_container, args, stackName, tag);
	}

	public void startFragment(Fragment fragment, Fragment targetFragment,
			Bundle args, String stackName, String tag) {
		fragment.setTargetFragment(targetFragment, 0);
		startFragment(fragment, R.id.fragment_container, args, stackName, tag);
	}

	public void startFragment(Fragment fragment, int layoutId, Bundle args,
			String stackName, String tag) {
		if (!TextUtils.isEmpty(mLastStackName)) {
			this.finishFragment(mLastStackName);
			mLastStackName = null;
		}
		if (args == null) {
			args = new Bundle();
		}
		if (stackName == null) {
			stackName = "" + System.currentTimeMillis() + fragment.hashCode();
		}
		args.putString(FRAGMENT_STACKNAME, stackName);
		mLastStackName = stackName;
		if (tag == null) {
			tag = stackName;
		}
		args.putString(FRAGMENT_TAG_KEY, tag);
		FragmentActivity activity = getActivity();
		if (activity != null) {
			FragmentTransaction transaction = activity
					.getSupportFragmentManager().beginTransaction();
			// transaction.setCustomAnimations(R.anim.activity_scale_enter,R.anim.activity_scale_exit);
			fragment.setArguments(args);
			transaction.add(layoutId, fragment, tag);
			transaction.addToBackStack(stackName);
			transaction.commitAllowingStateLoss();
			activity.getSupportFragmentManager().executePendingTransactions();
		}
	}

	public void finishFragment() {
		FragmentActivity activity = getActivity();
		if (activity != null) {
			try {
				activity.getSupportFragmentManager().popBackStackImmediate();
			}catch (IllegalStateException ignored) {
			    // There's no way to avoid getting this if saveInstanceState has already been called.
			}
		}
	}

	public void finishFragment(String name) {
		FragmentActivity activity = getActivity();
		if (activity != null) {
			try {
				activity.getSupportFragmentManager().popBackStackImmediate(name,
						FragmentManager.POP_BACK_STACK_INCLUSIVE);
			} catch (IllegalStateException ignored) {
			}
		}
	}

	public void showLoadingPage() {
		showLoadingPage(View.NO_ID);
	}

	public void showLoadingPage(int layoutId) {
		if (!isLoadingPageShown) {
			isLoadingPageShown = true;
			if (mLoadingPage == null) {
				mLoadingPage = createLoadingPage();
			}
			mLoadingPageParentId = layoutId;
			mLoadingPage.loading();
			displayLoadingPage(layoutId);
		}
	}

	public void setLoadFailedMessage(String failedMessage) {
		if (mLoadingPage != null) {
			mLoadingPage.failed(failedMessage);
		}
	}

	public void setLoadFailedMessage(int failedStringId) {
		if (mLoadingPage != null) {
			mLoadingPage.failed(failedStringId);
		}
	}

	public void closeLoadingPage() {
		isLoadingPageShown = false;
		removeLoadingPage();
	}

	private void removeLoadingPage() {
		if (mLoadingPage != null) {
			ViewGroup parent = (ViewGroup) mLoadingPage.getParent();
			if (parent != null) {
				parent.removeView(mLoadingPage);
			}
		}
	}

	private boolean displayLoadingPage(int layoutId) {

		ViewGroup layout = null;
		if (getView() != null) {
			int index = -1;
			if (layoutId != View.NO_ID) {
				layout = (ViewGroup) getActivity().findViewById(layoutId);
				if (layout instanceof LinearLayout) {
					index = 0;
				}
			} else {
				layout = ((ViewGroup) getView());
			}
			if (layout != null) {
				layout.addView(mLoadingPage, index, new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			}
		}
		return layout != null;
	}

	private LoadingPage createLoadingPage() {
		LoadingPage loadingPage = null;
		final Context context = getActivity();
		if (context != null) {
			loadingPage = new LoadingPage(context) {
				@Override
				protected void reload(Context context) {
					onReload(context);
				}
			};
		}
		return loadingPage;
	}

	/**
	 * 打开关闭侧滑菜单
	 */
	public void toggleDrawer() {
	}

}

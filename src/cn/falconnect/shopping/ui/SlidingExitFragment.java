package cn.falconnect.shopping.ui;

import org.aurora.library.views.slicenoodles.InterceptTouchEventListener;
import org.aurora.library.views.slicenoodles.SlicedNoodlesExitLayout;
import org.aurora.library.views.slicenoodles.SlicedNoodlesExitLayout.onSliceExitListener;
import org.aurora.library.views.slicenoodles.SlicedNoodlesLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public abstract class SlidingExitFragment extends BaseFragment {
	private SlicedNoodlesExitLayout mSlidingExitLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Context context = getActivity();
		mSlidingExitLayout = new SlicedNoodlesExitLayout(context);
		mSlidingExitLayout.setSliceExitListener(new onSliceExitListener() {

			@Override
			public void onExit(SlicedNoodlesLayout slicedNoodlesLayout) {
				SlidingExitFragment.this.onExit();
			}
		});
		setInterceptTouchEventAdapter(createInterceptAdapter());
		View contentView = onChildCreateView(inflater, mSlidingExitLayout,
				savedInstanceState);
		mSlidingExitLayout.addView(contentView,
				new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
		return mSlidingExitLayout;
	}

	protected void onExit() {
		finishFragment();
	}

	public void setInterceptTouchEventAdapter(
			InterceptTouchEventListener interceptTouchEventAdapter) {
		mSlidingExitLayout
				.setInterceptTouchEventAdapter(interceptTouchEventAdapter);
	}

	public void setSliceExitListener(onSliceExitListener listener) {
		mSlidingExitLayout.setSliceExitListener(listener);
	}

	protected void slidingToExit() {
		mSlidingExitLayout.sliceToRight();
	}

	protected void closeSlidingMode() {
		if (mSlidingExitLayout != null) {
			mSlidingExitLayout.closeSlidingMode();
		}
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
				return moveX > 0;
			}
		};
	}

	protected abstract View onChildCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);
}

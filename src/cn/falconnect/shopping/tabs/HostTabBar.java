package cn.falconnect.shopping.tabs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class HostTabBar extends BaseTabBar {
	private FrameLayout mMainFrameLayout;
	public HostTabBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}

	public HostTabBar(Context context, ViewGroup widgetsLayout) {
		super(context);
		mBarLayout = widgetsLayout;
		setupViews();
	}

	@Override
	public void addWidget(View view) {
		super.addWidget(view);
		mBarLayout.addView(view, new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.MATCH_PARENT, 1));
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int layoutHeight = mBarLayout.getMeasuredHeight() >= mAnimLayout
				.getMeasuredHeight() ? mBarLayout.getMeasuredHeight()
				: mAnimLayout.getMeasuredHeight();
		this.setMeasuredDimension(mBarLayout.getMeasuredWidth(), layoutHeight);
		int specHeight = MeasureSpec.makeMeasureSpec(layoutHeight,
				MeasureSpec.EXACTLY);
		mBarLayout.measure(widthMeasureSpec, specHeight);
		View selectedView = getSelectedView();
		if (selectedView != null) {
			int widthMeasure = MeasureSpec.makeMeasureSpec(
					selectedView.getMeasuredWidth(), MeasureSpec.EXACTLY);
			int heightMeasure = MeasureSpec.makeMeasureSpec(
					selectedView.getMeasuredHeight(), MeasureSpec.EXACTLY);
			mAnimImage.measure(widthMeasure, mPaddingWithBar ? specHeight
					: heightMeasure);
		}
	}

	@Override
	protected void setupViews() {
		super.setupViews();
		mMainFrameLayout = createFrameLayout();
		mMainFrameLayout.addView(mAnimLayout);
		this.addView(mMainFrameLayout);
		this.addView(mBarLayout);
	}

	@Override
	public void animImageOffset(int position, float positionOffset) {
		int newX = (int) (mBarLayout.getChildAt(position).getWidth() * (position + positionOffset));
		int lastX = mAnimImage.getLeft();
		mAnimImage.setTag(newX);
		mAnimImage.offsetLeftAndRight(newX - lastX);
		mAnimImage.postInvalidate();
	}

}

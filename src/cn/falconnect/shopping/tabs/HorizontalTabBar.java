package cn.falconnect.shopping.tabs;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.falconnect.shopping.cat.R;

public class HorizontalTabBar extends BaseTabBar {
	private HorizontalScrollView mHorizontalScrollView;
	private FrameLayout mMainFrameLayout;

	public HorizontalTabBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		setupViews();
	}

	public HorizontalTabBar(Context context, ViewGroup widgetsLayout) {
		super(context);
		mBarLayout = widgetsLayout;
		setupViews();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int layoutHeight = mBarLayout.getMeasuredHeight() >= mAnimLayout
				.getMeasuredHeight() ? mBarLayout.getMeasuredHeight()
				: mAnimLayout.getMeasuredHeight();
		int specHeight = MeasureSpec.makeMeasureSpec(layoutHeight,
				MeasureSpec.EXACTLY);
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
		mHorizontalScrollView = createHorizontalScrollView();
		mMainFrameLayout.addView(mAnimLayout);
		mMainFrameLayout.addView(mBarLayout);
		mHorizontalScrollView.addView(mMainFrameLayout);
		this.addView(mHorizontalScrollView);
	}
	
	private HorizontalScrollView createHorizontalScrollView() {
		HorizontalScrollView horizontalScrollView = new HorizontalScrollView(
				getContext());
		horizontalScrollView.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		horizontalScrollView.setHorizontalScrollBarEnabled(false);
		return horizontalScrollView;
	}

	@Override
	protected void seleteItem(int position, boolean isStartAnimation) {
		super.seleteItem(position, isStartAnimation);
		smoothScroll(position);
	}

	@Override
	protected void seleteItem(int position) {
		super.seleteItem(position);
		smoothScroll(position);
	}

	private void smoothScroll(int position) {
		int left = mBarLayout.getChildAt(position).getLeft()
				- mBarLayout.getChildAt(0).getLeft();
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		mHorizontalScrollView.smoothScrollTo(left - (dm.widthPixels / 2)
				+ (mBarLayout.getChildAt(position).getWidth() / 2), 0);
	}

	@Override
	public void addWidgets(View[] views) {
		int itemWidth = getItemWidth(views.length);
		if (views.length == 4) {
			for (int i = 0; i < views.length; i++) {
				if (views[i] instanceof TextView) {
					TextView textView = (TextView) views[i];
					if (textView.getText().length() >= 3) {
						itemWidth = LinearLayout.LayoutParams.WRAP_CONTENT;
						break;
					}
				} else {
					itemWidth = LinearLayout.LayoutParams.WRAP_CONTENT;
				}
			}
		} else if (views.length > 4) {
			itemWidth = LinearLayout.LayoutParams.WRAP_CONTENT;
		}
		for (int i = 0; i < views.length; i++) {
			if (itemWidth == LinearLayout.LayoutParams.WRAP_CONTENT) {
				int padding = (int) getResources().getDimension(R.dimen.dp18);
				views[i].setPadding(padding, 0, padding, 0);
			}
			addWidget(views[i]);
			mBarLayout.addView(views[i], itemWidth,
					LinearLayout.LayoutParams.MATCH_PARENT);
		}
	}

	@Override
	public void addWidget(View view) {
		super.addWidget(view);
	}
	
	private int getItemWidth(int viewLength) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		return dm.widthPixels / viewLength;
	}

	@Override
	public void animImageOffset(int position, float positionOffset) {
		int left = mBarLayout.getChildAt(position).getLeft();
		int newX = (int) (left + mBarLayout.getChildAt(position).getWidth()
				* positionOffset);
		int lastX = mAnimImage.getLeft();
		mAnimImage.setTag(newX);
		mAnimImage.offsetLeftAndRight(newX - lastX);
		mAnimImage.postInvalidate();
	}
}

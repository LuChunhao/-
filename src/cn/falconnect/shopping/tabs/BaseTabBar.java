package cn.falconnect.shopping.tabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseTabBar extends FrameLayout {
	protected int mCurrentPosition = -1;
	protected ViewGroup mBarLayout;
	protected ImageView mAnimImage;
	protected Drawable mAnimDrawable;
	protected Animation mImageAnimation;
	protected Interpolator mAnimInterpolator = new AccelerateDecelerateInterpolator();
	protected long mAnimationDuration = 400;
	protected OnViewSwitchedListener mViewSwitchedListener;
	protected FrameLayout mAnimLayout;
	protected boolean mPaddingWithBar;
	protected OnTabItemClickListener mItemClickListener;

	public interface OnTabItemClickListener {
		void OnItemClick(View selectedView, View parent, int position);
	}

	public interface OnViewSwitchedListener {
		void onWidgetSwitched(int position, View selectedView);
	}

	public BaseTabBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BaseTabBar(Context context) {
		super(context);
	}

	protected void setupViews() {
		this.setLayoutParams(new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		if (mBarLayout == null) {
			mBarLayout = createViewGroup();
		}
		int count = mBarLayout.getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = mBarLayout.getChildAt(i);

			final int position = i;
			child.setTag(position);
			child.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onItemSelected(v, child);
				}
			});
		}
		mAnimLayout = createFrameLayout();
		mAnimImage = createAnimImageView();
		mAnimLayout.addView(mAnimImage);
		mBarLayout.setBackgroundResource(android.R.color.transparent);
	}

	public String getChildItemContent(int position) {
		View view = mBarLayout.getChildAt(position);
		if (view instanceof TextView) {
			return ((TextView) view).getText().toString();
		} else if (view instanceof Button) {
			return ((Button) view).getText().toString();
		} else {
			throw new IllegalArgumentException(this.getClass().getName()
					+ " the widget can not get content...");
		}
	}

	private LinearLayout createViewGroup() {
		LinearLayout groupLayout = new LinearLayout(getContext());
		groupLayout.setBackgroundResource(android.R.color.transparent);
		groupLayout.setVisibility(VISIBLE);
		return groupLayout;
	}

	private void onItemSelected(View selectedView, View child) {
		int position = (Integer) selectedView.getTag();
		selectItem(position, true, true);
		if (mItemClickListener != null) {
			mItemClickListener.OnItemClick(child, mBarLayout,position);
		}
	}

	public void addWidget(final View view) {
		int position = mBarLayout.getChildCount();
		view.setTag(position);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onItemSelected(v, view);
			}
		});
	}

	public void addWidgets(View[] views) {
		for (int i = 0; i < views.length; i++) {
			addWidget(views[i]);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (getSelectedView() != null
				&& mAnimImage.getLeft() != getSelectedView().getLeft()) {
			mAnimImage.offsetLeftAndRight(getSelectedView().getLeft()
					- this.getLeft());
		}
	}

	@SuppressLint("ResourceAsColor")
	protected void selectItem(int position, boolean isStartAnimation,
			boolean isCallSwitchedListener) {
		position = Math.min(position, mBarLayout.getChildCount() - 1);
		position = Math.max(0, position);
		if (mCurrentPosition != position) {
			View selectedView = mBarLayout.getChildAt(position);
			View lastView = mBarLayout.getChildAt(mCurrentPosition);
			if (isCallSwitchedListener) {
				if (mViewSwitchedListener != null) {
					mViewSwitchedListener.onWidgetSwitched(position,
							selectedView);
				}
			}
			triggerSelector(selectedView, lastView);
			mCurrentPosition = position;
			if (isStartAnimation && lastView != null) {
				startImageAnimation(lastView.getLeft(), selectedView.getLeft(),
						lastView.getTop(), selectedView.getTop());
			}
		}
	}

	protected void seleteItem(int position, boolean isStartAnimation) {
		selectItem(position, isStartAnimation, false);
	}

	protected void seleteItem(int position) {
		selectItem(position, true, false);
	}

	private void triggerSelector(View selectedView, View lastView) {
		if (selectedView instanceof CompoundButton) {
			if (lastView != null) {
				((CompoundButton) lastView).setChecked(false);
			}
			((CompoundButton) selectedView).setChecked(true);
		} else {
			if (lastView != null) {
				lastView.setSelected(false);
			}
			selectedView.setSelected(true);
		}
	}

	protected FrameLayout createFrameLayout() {
		FrameLayout animLayout = new FrameLayout(getContext());
		animLayout.setBackgroundResource(android.R.color.transparent);
		animLayout.setVisibility(VISIBLE);
		return animLayout;
	}

	protected ImageView createAnimImageView() {
		ImageView imageView = new ImageView(getContext());
		imageView.setScaleType(ScaleType.FIT_XY);
		return imageView;
	}

	public void startImageAnimation(int startX, int endX, int startY, int endY) {
		if (mAnimDrawable != null) {
			mAnimImage.requestLayout();
			mImageAnimation = getAnimation(startX - endX, 0, 0, 0);
			mAnimImage.clearAnimation();
			mAnimImage.startAnimation(mImageAnimation);
		}
	}

	private Animation getAnimation(int startX, int endX, int startY, int endY) {
		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnim = new TranslateAnimation(startX, endX,
				startY, endY);
		animationSet.addAnimation(translateAnim);
		if (mAnimInterpolator != null) {
			animationSet.setInterpolator(mAnimInterpolator);
		}
		animationSet.setDuration(mAnimationDuration);
		animationSet.setFillAfter(true);
		return animationSet;
	}

	public void setOnTabItemClickListener(OnTabItemClickListener l) {
		mItemClickListener = l;
	}

	public void setOnViewSwitchedListener(OnViewSwitchedListener listener) {
		mViewSwitchedListener = listener;
	}

	public int getViewCount() {
		return mBarLayout.getChildCount();
	}

	public View getSelectedView() {
		mCurrentPosition = Math.min(mCurrentPosition,
				mBarLayout.getChildCount() - 1);
		mCurrentPosition = Math.max(0, mCurrentPosition);
		return mBarLayout.getChildAt(mCurrentPosition);
	}

	public int getCurrentPosition() {
		return mCurrentPosition;
	}

	public Drawable getAnimDrawable() {
		return mAnimDrawable;
	}

	public void setAnimDrawable(Drawable animDrawable, boolean paddingWithBar) {
		if (animDrawable != null) {
			mPaddingWithBar = paddingWithBar;
			mAnimDrawable = animDrawable;
			mAnimImage.setImageDrawable(animDrawable);
			mAnimLayout.setVisibility(VISIBLE);
		}
	}

	public Interpolator getAnimInterpolator() {
		return mAnimInterpolator;
	}

	public void setAnimInterpolator(Interpolator animInterpolator) {
		this.mAnimInterpolator = animInterpolator;
	}

	public long getAnimationDuration() {
		return mAnimationDuration;
	}

	public void setAnimationDuration(long animationDuration) {
		this.mAnimationDuration = animationDuration;
	}

	public abstract void animImageOffset(int position, float positionOffset);

	public void clearWidget() {
		mBarLayout.removeAllViews();
		mCurrentPosition = -1;
	}

	public void setChildViewTextColor(int colorRes) {
		for (int i = 0; i < mBarLayout.getChildCount(); i++) {
			View childView = mBarLayout.getChildAt(i);
			if (childView instanceof TextView) {
				((TextView) childView).setTextColor(getResources()
						.getColorStateList(colorRes));
			} else if (childView instanceof Button) {
				((Button) childView).setTextColor(getResources()
						.getColorStateList(colorRes));
			}
		}
		invalidate();
	}

}

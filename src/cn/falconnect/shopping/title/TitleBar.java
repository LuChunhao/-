package cn.falconnect.shopping.title;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.ui.BaseFragment;
import cn.falconnect.shopping.ui.search.SearchMainFragment;
import cn.falconnect.shopping.utils.CommonUtil;

public class TitleBar extends BaseTitleBar {
	private Mode mMode;
	private BaseFragment mBaseFragment;

	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context, attrs);
	}

	public void setup(BaseFragment baseFragment) {
		mBaseFragment = baseFragment;
	}

	private void initViews(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ActionBar);
		try {
			String title = null;
			if (a.hasValue(R.styleable.ActionBar_actionBarTitle)) {
				title = a.getString(R.styleable.ActionBar_actionBarTitle);
			}
			setTitle(TextUtils.isEmpty(title) ? getResources().getString(
					R.string.app_name) : title);
			if (a.hasValue(R.styleable.ActionBar_actionBarMode)) {
				mMode = Mode.getEnum(a.getInteger(
						R.styleable.ActionBar_actionBarMode, 0x3));
			}
		} finally {
			a.recycle();
			a = null;
		}
		switch (mMode) {
		case SECONDARY:
			addView(createBackAction(), LOCATION_LEFT, createOnclickListener());
			addView(createOverFlowAction(), LOCATION_RIGHT,
					createOnclickListener());
			break;
		case NONE:
			addView(createBackAction(), LOCATION_LEFT, createOnclickListener());
			break;

		default:
			break;
		}

	}

	private OnClickListener createOnclickListener() {

		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mBaseFragment != null) {
					switch (v.getId()) {
					case R.id.back_tag:
						mBaseFragment.finishFragment();
						break;

					case R.id.search_tag:
						mBaseFragment.startFragment(new SearchMainFragment(),
								null, SearchMainFragment.FRAGMENT_TAG);
						break;
					case R.id.toggle_tag:
						mBaseFragment.toggleDrawer();
						break;
					default:
						break;
					}
				}
			}
		};
	}

	private ImageView createBackAction() {
		ImageView img = new ImageView(getContext());
		img.setId(R.id.back_tag);
		img.setImageResource(R.drawable.ic_back);
		img.setLayoutParams(createParams());
		return img;
	}

	private ImageView createSearchAction() {
		ImageView img = new ImageView(getContext());
		img.setId(R.id.search_tag);
		img.setImageResource(R.drawable.ic_menu_search);
		img.setLayoutParams(createParams());
		return img;
	}

	private ImageView createOverFlowAction() {
		ImageView img = new ImageView(getContext());
		img.setId(R.id.overflow_tag);
		img.setImageResource(R.drawable.ic_overflow);
		img.setLayoutParams(createParams());
		return img;
	}

	private LinearLayout.LayoutParams createParams() {
		int width = CommonUtil.convertDipToPx(getContext(), 44);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width,
				LayoutParams.WRAP_CONTENT);
		return param;
	}

	public static enum Mode {

		SECONDARY(0x2),

		NONE(0x3);

		private int mIntValue;

		Mode(int modeInt) {
			mIntValue = modeInt;
		}

		public static Mode getEnum(int intValue) {
			for (Mode mode : values()) {
				if (mode.mIntValue == intValue) {
					return mode;
				}
			}
			return NONE;
		}
	}

}

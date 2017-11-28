package cn.falconnect.shopping.title;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.falconnect.shopping.cat.R;

public class BaseTitleBar extends RelativeLayout {

	protected TextView mTitle;
	protected LinearLayout mLeftContainer;
	protected LinearLayout mRightContainer;
	static final int LOCATION_LEFT = 0;
	static final int LOCATION_RIGHT = 1;

	public BaseTitleBar(Context context) {
		super(context);
		initViews();
	}

	public BaseTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews();
	}

	public void setTitle(String title) {
		mTitle.setText(title);
	}

	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		inflater.inflate(R.layout.titlebar, this);

		mTitle = (TextView) findViewById(R.id.text_title);
		mLeftContainer = (LinearLayout) findViewById(R.id.left_container);
		mRightContainer = (LinearLayout) findViewById(R.id.right_container);
	}

	public void addView(View view, int location, OnClickListener listener) {
		view.setOnClickListener(listener);
		ViewGroup contatiner = LOCATION_LEFT == location ? mLeftContainer
				: mRightContainer;
		contatiner.addView(view);
	}
}

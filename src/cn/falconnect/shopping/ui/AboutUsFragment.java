package cn.falconnect.shopping.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.falconnect.shopping.cat.R;

public class AboutUsFragment extends SlidingExitFragment {
	
	@Override
	protected View onChildCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_about_us, null);
		initViews(contentView);
		return contentView;
	}

	@Override
	protected String getTDPageName() {
		return getString(R.string.about_us);
	}

	private void initViews(View contentView) {
		((TextView) contentView.findViewById(R.id.tv_about)).setText(R.string.about_desc);
	}
	
}

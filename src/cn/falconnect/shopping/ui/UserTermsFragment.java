package cn.falconnect.shopping.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.falconnect.shopping.cat.R;

public class UserTermsFragment extends SlidingExitFragment {

	@Override
	protected View onChildCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_terms, null);
		initViews(contentView);
		return contentView;
	}

	@Override
	protected String getTDPageName() {
		return getString(R.string.user_protocol);
	}

	private void initViews(View view) {
		TextView content = (TextView) view.findViewById(R.id.tv_content);
		content.setText(getString(R.string.user_terms,
				getString(R.string.app_name)));
	}

}

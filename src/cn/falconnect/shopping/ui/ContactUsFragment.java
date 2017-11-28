package cn.falconnect.shopping.ui;

import org.aurora.library.views.Toaster;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.Feedback;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;

public class ContactUsFragment extends SlidingExitFragment implements
		OnClickListener {
	private EditText et_suggest;
	private EditText et_qq;
	private Button btn_submmit;

	@Override
	protected View onChildCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		
		View contentView = inflater.inflate(R.layout.setting_contact_us, null);
		initViews(contentView);
		return contentView;
	}

	private void initViews(View view) {
		et_suggest = (EditText) view.findViewById(R.id.et_suggest);
		et_qq = (EditText) view.findViewById(R.id.et_qq);
		btn_submmit = (Button) view.findViewById(R.id.btn_submmit);
		btn_submmit.setOnClickListener(this);
	}

	@Override
	protected String getTDPageName() {
		return getString(R.string.feedback);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submmit:
			String suggest_content = et_suggest.getText().toString().trim();
			if (TextUtils.isEmpty(suggest_content)) {
				Toaster.toast(R.string.not_null);
			} else {
				String contact = et_qq.getText().toString().trim();
				ProviderFatory.getFeedbackProvider().submitFeedback(
						v.getContext(), suggest_content, contact,
						new ObtainListener<Feedback>() {

							@Override
							public void onSucceed(Context context,
									Feedback result) {
								Toaster.toast(R.string.commit_success);
								finishFragment();
							}

							@Override
							public void onFinished(Context context,
									ResultCode code) {

							}

							@Override
							public void onError(Context context, ResultCode code) {

							}
						});
			}
			break;
		}
	}
}

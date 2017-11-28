package cn.falconnect.shopping.ui.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aurora.library.views.Toaster;

import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.ui.SlidingExitFragment;
import cn.falconnect.shopping.utils.CommonUtil;
import cn.falconnect.shopping.utils.DialogUtil;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FindPasswdFragment extends SlidingExitFragment {

	private View mContentView;
	private EditText et_account;
	private Dialog mDialog;

	@Override
	protected View onChildCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.find_passwd, null);
			initViews();
		}
		return mContentView;
	}

	@Override
	protected String getTDPageName() {
		return getString(R.string.find_passwd);
	}

	private void initViews() {
		et_account = (EditText) mContentView
				.findViewById(R.id.et_registered_account);
		Button btn_find_passwd = (Button) mContentView
				.findViewById(R.id.btn_find_passwd);
		btn_find_passwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonUtil.hideSoftInput(getActivity(),
						et_account.getWindowToken());
				findPasswd(et_account.getText().toString().trim());
			}
		});
	}

	private void findPasswd(String account) {
		if (TextUtils.isEmpty(account)) {
			Toaster.toast(R.string.account_empty);
			return;
		}
		if (!isAccountEmail(account)) {
			Toaster.toast("请输入正确的邮箱账户");
			return;
		}
		mDialog = DialogUtil.showProgressBar(getActivity(), "正在提交");
		mDialog.show();
		ProviderFatory.getUserProvider().forgetPasswd(
				mContentView.getContext(), account, new ObtainListener<Void>() {

					@Override
					public void onSucceed(Context context, Void result) {
						if (mDialog != null && mDialog.isShowing()) {
							mDialog.dismiss();
						}
						Toaster.toast("请去账户邮箱修改密码");
						finishFragment();
					}

					@Override
					public void onError(Context context, ResultCode code) {

					}

					@Override
					public void onFinished(Context context, ResultCode code) {

					}

				});
	}

	private boolean isAccountEmail(String account) {
		Pattern pattern = Pattern
				.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(account);
		return matcher.matches();
	}

}

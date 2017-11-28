package cn.falconnect.shopping.ui.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aurora.library.views.Toaster;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.User;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.ui.MainFragment;
import cn.falconnect.shopping.ui.SettingFragment;
import cn.falconnect.shopping.ui.SlidingExitFragment;

public class UserRegisterFragment extends SlidingExitFragment {
	private boolean mRegistering = false;
	public static final String FRAGMENT_TAG = "UserRegisterFragment";
	@Override
	protected View onChildCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View contentView = inflater.inflate(R.layout.fragment_user_register, null);
		contentView.findViewById(R.id.btn_register).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				register(contentView);
			}
		});
		return contentView;
	}
	
	@Override
	protected String getTDPageName() {
		return getString(R.string.register);
	}
	
	private void register(final View contentView) {
		final Context context = contentView.getContext();
		EditText et_account = (EditText) contentView.findViewById(R.id.et_register_account);
		String account = et_account.getText().toString().trim();
		if (!isAccountEmail(account)) {
			Toaster.toast("请输入正确的邮箱账户");
			return;
		}
		EditText et_passwd = (EditText) contentView.findViewById(R.id.et_register_passwd);
		String passwd1 = et_passwd.getText().toString();
		EditText et_passwd_confirm = (EditText) contentView.findViewById(R.id.et_register_passwd_confirm);
		String passwd2 = et_passwd_confirm.getText().toString();
		if (!passwd1.equals(passwd2)) {
			Toaster.toast(R.string.passwd_not_same);
			return;
		}
		if (passwd1.length() < 6) {
			Toaster.toast(R.string.password_noenough);
			return;
		}

		if (mRegistering) {
			Toaster.toast(R.string.registering);
			return;
		}
		showLoadingAnimation(contentView);
		mRegistering = true;

		ProviderFatory.getUserProvider().register(context, account, passwd1, new ObtainListener<User>() {

			@Override
			public void onSucceed(Context context, User user) {
				if (user != null) {
					Toaster.toast(R.string.register_succeed);
					if (!TextUtils.isEmpty(user.sign)) {
						ProviderFatory.getUserProvider().saveLoginUser(context, user);
						ProviderFatory.getUserProvider().saveUserAccount(context, user.account);
					}
					MainFragment mainFragment = (MainFragment) getActivity().getSupportFragmentManager().findFragmentByTag(
							MainFragment.FRAGMENT_TAG);
					if (mainFragment != null) {
						SettingFragment settingFragment = (SettingFragment) mainFragment.getFragmentManager().findFragmentByTag(SettingFragment.FRAGMENT_TAG);
						settingFragment.update();
					}
					finishFragment(UserLoginFragment.class.getName());
					finishFragment();
				} else {
					onError(context, ResultCode.NET_ERROR);
				}
			}

			@Override
			public void onFinished(Context context, ResultCode code) {
				mRegistering = false;
				closeLoadingAnimation(contentView);
			}

			@Override
			public void onError(Context context, ResultCode code) {
				Toaster.toast(code.msg);
			}
		});
	}

	private void showLoadingAnimation(View contentView) {
		contentView.findViewById(R.id.register_progress).setVisibility(View.VISIBLE);
	}

	protected void closeLoadingAnimation(View contentView) {
		contentView.findViewById(R.id.register_progress).setVisibility(View.INVISIBLE);
	}

	private boolean isAccountEmail(String account){
		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(account);
		return matcher.matches();
	}
}

package cn.falconnect.shopping.ui.user;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aurora.library.downloader.core.CustomThreadAsyncTask;
import org.aurora.library.views.Toaster;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.falconnect.shopping.Task;
import cn.falconnect.shopping.Task.AsyncTaskListener;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.constants.Global;
import cn.falconnect.shopping.entity.Goods;
import cn.falconnect.shopping.entity.User;
import cn.falconnect.shopping.provider.db.CacheDAO;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.ui.SettingFragment;
import cn.falconnect.shopping.ui.SlidingExitFragment;

public class UserLoginFragment extends SlidingExitFragment {
	private boolean mLoginging = false;
	public static final String FRAGMENT_TAG = "UserLoginFragment";
	private EditText et_account;
	private View mContentView;

	@Override
	protected View onChildCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_user_login, null);
			initViews(mContentView);
		}
		return mContentView;
	}

	@Override
	protected String getTDPageName() {
		return getString(R.string.login);
	}

	private void initViews(final View contentView) {
		et_account = (EditText) contentView.findViewById(R.id.et_login_account);
		Button btn_login = (Button) contentView.findViewById(R.id.btn_login);
		et_account.setText(ProviderFatory.getUserProvider().restoreUserAccount(
				getActivity()));
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				login();
			}
		});
		TextView btn_register = (TextView) contentView
				.findViewById(R.id.tv_register);
		btn_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startFragment(new UserRegisterFragment(), null);
			}
		});
		TextView tv_forget = (TextView) contentView
				.findViewById(R.id.tv_forget);
		tv_forget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startFragment(new FindPasswdFragment(), null);
			}
		});
	}

	private void clearCollects(final String account, Context context) {
		deleteData(context, account, new AsyncTaskListener<Void>() {

			@Override
			public void onSucceed(Context context, Void result) {

			}

			@Override
			public void onError(Context context, String msg) {

			}

			@Override
			public void onFinished(Context context, String msg) {
				requestCollectsData(context, account);
			}

		});
	}

	private <T> void deleteData(Context context, final String account,
			AsyncTaskListener<T> listener) {
		Task<T> task = new Task<T>(context, listener);
		new AsyncTask<Object, Void, Task<T>>() {
			@Override
			protected Task<T> doInBackground(Object... params) {
				@SuppressWarnings("unchecked")
				Task<T> task = (Task<T>) params[0];
				try {
					List<Goods> data = CacheDAO.getInstance().queryAll(
							Global.COLLECTS_CACHE_TYPE, account);
					if (data != null && data.size() > 0) {
						boolean success = CacheDAO.getInstance().deleteAll(
								Global.COLLECTS_CACHE_TYPE, account);
						task.msg = success ? "success" : "failed";
					}
				} finally {
					task.listener.onFinishInBackgroud(task.context, task.msg,
							task.result);
				}
				return task;
			}

			@Override
			protected void onPostExecute(Task<T> result) {
				String msg = result.msg;
				if ("success".equals(msg)) {
					result.listener.onSucceed(result.context, result.result);
				} else {
					result.listener.onError(result.context, msg);
				}
				result.listener.onFinished(result.context, msg);
			}
		}.execute(task);
	}

	private void requestCollectsData(Context context, final String account) {
		ProviderFatory.getGoodsProvider().obtainUserCollectionList(context,
				new ObtainListener<List<Goods>>() {

					@Override
					public void onSucceed(Context context, List<Goods> result) {
						saveCollectsData(account, result, context);
					}

					@Override
					public void onError(Context context, ResultCode code) {
						closeLoadingPage();
//						HomeFragment mainFragment = (HomeFragment) getActivity()
//								.getSupportFragmentManager().findFragmentByTag(
//										HomeFragment.FRAGEMNT_TAG);
//						PersonalCenterFragment centerFragment = (PersonalCenterFragment) mainFragment
//								.findFragmentByTag(PersonalCenterFragment.FRAGEMNT_TAG);
//						Toaster.toast(R.string.login_succeed);
						finishFragment();
					}

					@Override
					public void onFinished(Context context, ResultCode code) {

					}
				});
	}

	private void saveCollectsData(String account, List<Goods> dataList,
			Context context) {
		insertData(context, account, dataList, new AsyncTaskListener<Void>() {

			@Override
			public void onSucceed(Context context, Void result) {
			}

			@Override
			public void onError(Context context, String msg) {
			}

			@Override
			public void onFinished(Context context, String msg) {
				closeLoadingPage();
				// HomeFragment mainFragment = (HomeFragment) getActivity()
				// .getSupportFragmentManager().findFragmentByTag(
				// HomeFragment.FRAGEMNT_TAG);
				// PersonalCenterFragment centerFragment =
				// (PersonalCenterFragment) mainFragment
				// .findFragmentByTag(PersonalCenterFragment.FRAGEMNT_TAG);
				// if (centerFragment!=null) {
				// centerFragment.update();
				// }
//				Toaster.toast(R.string.login_succeed);
				finishFragment();
			}

		});
	}

	private <T> void insertData(Context context, final String account,
			final List<Goods> dataList, AsyncTaskListener<T> listener) {
		Task<T> task = new Task<T>(context, listener);
		new CustomThreadAsyncTask<Object, Void, Task<T>>() {
			@Override
			protected Task<T> doInBackground(Object... params) {
				@SuppressWarnings("unchecked")
				Task<T> task = (Task<T>) params[0];
				try {
					if (dataList != null && dataList.size() > 0) {
						for (Goods goods : dataList) {
							if (!CacheDAO.getInstance().isGoodsExist(
									goods.getGoodsId(),
									Global.COLLECTS_CACHE_TYPE, account)) {
								CacheDAO.getInstance().insert(goods,
										Global.COLLECTS_CACHE_TYPE, account);
							}
						}
					}
				} finally {
					task.listener.onFinishInBackgroud(task.context, task.msg,
							task.result);
				}
				return task;
			}

			@Override
			protected void onPostExecute(Task<T> result) {
				String msg = result.msg;
				if ("success".equals(msg)) {
					result.listener.onSucceed(result.context, result.result);
				} else {
					result.listener.onError(result.context, msg);
				}
				result.listener.onFinished(result.context, msg);
			}
		}.execute(task);
	}

	private void showLoadingAnimation(View contentView) {
		contentView.findViewById(R.id.login_progress).setVisibility(
				View.VISIBLE);
	}

	protected void closeLoadingAnimation(View contentView) {
		contentView.findViewById(R.id.login_progress).setVisibility(
				View.INVISIBLE);
	}

	private void login() {
		String account = et_account.getText().toString().trim();
		if (TextUtils.isEmpty(account)) {
			Toaster.toast(R.string.account_empty);
			return;
		}
		EditText et_passwd = (EditText) mContentView
				.findViewById(R.id.et_login_passwd);
		String passwd = et_passwd.getText().toString().trim();
		if (passwd.length() < 6) {
			Toaster.toast(R.string.password_noenough);
			return;
		}
		if (mLoginging) {
			Toaster.toast(R.string.logining);
			return;
		}
		showLoadingAnimation();
		mLoginging = true;
		ProviderFatory.getUserProvider().login(getActivity(), account, passwd,
				new ObtainListener<User>() {
					@Override
					public void onSucceed(Context context, User user) {
						if (user != null && !TextUtils.isEmpty(user.sign)) {
							ProviderFatory.getUserProvider().saveLoginUser(
									context, user);
							String account = ProviderFatory.getUserProvider()
									.getLoginName(context);
							ProviderFatory.getUserProvider().saveUserAccount(
									context, user.account);
							if (!TextUtils.isEmpty(account)) {
								clearCollects(account, context);
							}
							
							Toaster.toast(R.string.login_succeed);
							update();
							finishFragment();
						} else {
							onError(context, ResultCode.NET_ERROR);
						}
					}

					@Override
					public void onFinished(Context context, ResultCode code) {
						mLoginging = false;
						closeLoadingAnimation();
					}

					@Override
					public void onError(Context context, ResultCode code) {
						Toaster.toast(code.msg);
					}
				});
	}

	private void showLoadingAnimation() {
		mContentView.findViewById(R.id.login_progress).setVisibility(
				View.VISIBLE);
	}

	protected void closeLoadingAnimation() {
		mContentView.findViewById(R.id.login_progress).setVisibility(
				View.INVISIBLE);
	}

	private void update() {
		SettingFragment settingFragment = (SettingFragment) getActivity()
				.getSupportFragmentManager().findFragmentByTag(
						SettingFragment.FRAGMENT_TAG);
		if (settingFragment != null) {
			settingFragment.update();
		}
	}
}

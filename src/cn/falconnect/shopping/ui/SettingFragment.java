package cn.falconnect.shopping.ui;

import org.aurora.library.views.Toaster;
import org.aurora.library.views.text.MarqueeTextView;

import com.adcocoa.sdk.offerwalllibrary.AdcocoaOfferWall;
import com.adcocoa.sdk.offerwalllibrary.AdcocoaOfferWallListener;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.constants.Global;
import cn.falconnect.shopping.entity.NewVersion;
import cn.falconnect.shopping.entity.User;
import cn.falconnect.shopping.provider.web.CatShopApi;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.ui.user.PersonalCenterFragment;
import cn.falconnect.shopping.ui.user.UserLoginFragment;
import cn.falconnect.shopping.utils.CommonUtil;
import cn.falconnect.shopping.utils.DialogUtil;
import cn.falconnect.shopping.utils.ImageHelper;
import cn.falconnect.shopping.utils.SharedPreferencesUtil;
import cn.falconnect.shopping.widget.CircleImageView;

public class SettingFragment extends BaseFragment implements OnClickListener {
	public static final String FRAGMENT_TAG = "SettingFragment";
	// 显示更新的对话框
	private Dialog mDialog = null;
	private String mDownloadUrl;
	private CircleImageView iv_setting_head;
	private MarqueeTextView tv_setting_nickname;
	private boolean isLogined;
	private ImageView mUserBackground;
	private int clickCount;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View contentView = inflater.inflate(R.layout.setting_main, container, false);
		initViews(contentView);
		return contentView;
	}

	private void initViews(View contentView) {
		ViewGroup userGuideItem = (ViewGroup) contentView.findViewById(R.id.rl_user_guide);
		ViewGroup checkUpdateItem = (ViewGroup) contentView.findViewById(R.id.rl_check_update);
		ViewGroup appShareItem = (ViewGroup) contentView.findViewById(R.id.rl_app_share);
		ViewGroup feedbackItem = (ViewGroup) contentView.findViewById(R.id.rl_feedback);
		ViewGroup protocolItem = (ViewGroup) contentView.findViewById(R.id.rl_user_protocol);
		ViewGroup aboutusItem = (ViewGroup) contentView.findViewById(R.id.rl_aboutus);
		ViewGroup adcocoaItem = (ViewGroup) contentView.findViewById(R.id.rl_ad_open);
		mUserBackground = (ImageView) contentView.findViewById(R.id.user_background);
		mUserBackground.setOnClickListener(this);
		adcocoaItem.setVisibility(Global.AD_OPEN ? View.VISIBLE : View.GONE);
		adcocoaItem.setOnClickListener(this);
		userGuideItem.setOnClickListener(this);
		checkUpdateItem.setOnClickListener(this);
		appShareItem.setOnClickListener(this);
		feedbackItem.setOnClickListener(this);
		protocolItem.setOnClickListener(this);
		aboutusItem.setOnClickListener(this);
		Button btn_user_center = (Button) contentView.findViewById(R.id.btn_user_center);
		btn_user_center.setOnClickListener(this);
		iv_setting_head = (CircleImageView) contentView.findViewById(R.id.iv_setting_head);
		iv_setting_head.setOnClickListener(this);
		tv_setting_nickname = (MarqueeTextView) contentView.findViewById(R.id.tv_setting_nickname);
		tv_setting_nickname.setOnClickListener(this);
		update();
	}

	@Override
	public void onClick(View v) {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		MainFragment mainFragment = (MainFragment) getActivity().getSupportFragmentManager()
				.findFragmentByTag(MainFragment.FRAGMENT_TAG);
		if (mainFragment != null && v.getId() != R.id.user_background) {
			mainFragment.toggleDrawer();
		}
		switch (v.getId()) {
		case R.id.rl_ad_open:
			openOfferWall();
			break;
		case R.id.rl_user_guide:
			// 新手引导
			goGuide();
			break;
		case R.id.rl_check_update:
			// 版本更新
			obtainNewVersion(v.getContext());
			break;
		case R.id.rl_app_share:
			// 应用分享
			shareApp(getActivity());
			break;
		case R.id.rl_feedback:
			// 意见反馈
			startFragment(new ContactUsFragment(), null);
			break;
		case R.id.rl_user_protocol:
			// 用户协议
			startFragment(new UserTermsFragment(), null);
			break;
		case R.id.rl_aboutus:
			// 关于我们
			startFragment(new AboutUsFragment(), null);
			break;
		case R.id.iv_setting_head:
		case R.id.tv_setting_nickname:
		case R.id.btn_user_center:
			isLogined = ProviderFatory.getUserProvider().isLogined(getActivity());
			if (isLogined) {
				startFragment(new PersonalCenterFragment(), null, PersonalCenterFragment.class.getName(),
						PersonalCenterFragment.FRAGEMNT_TAG);
			} else {
				startFragment(new UserLoginFragment(), null, UserLoginFragment.class.getName(),
						UserLoginFragment.FRAGMENT_TAG);
			}
			break;
		case R.id.user_background:
			clickCount++;
			if (!SharedPreferencesUtil.getImageURL(v.getContext(), "backgroudurl").equals("backgroudurl")
					&& clickCount % 2 != 0) {
				ImageHelper.displayDefaultCircleIcon(mUserBackground,
						SharedPreferencesUtil.getImageURL(v.getContext(), "backgroudurl"));
				mUserBackground.setScaleType(ScaleType.FIT_XY);
			} else {
				mUserBackground.setImageResource(R.drawable.setting_back);
				mUserBackground.setScaleType(ScaleType.FIT_XY);
			}
			break;
		}
	}

	/**
	 * 新手引导
	 */
	private void goGuide() {
		startFragment(new GuideFragment(), null);
	}

	private void openOfferWall() {

		AdcocoaOfferWall.open(getActivity(), new AdcocoaOfferWallListener<Void>() {

			@Override
			public void onSucceed(Void arg0) {

			}

			@Override
			public void onError(int arg0, String arg1) {

			}
		});
	}

	/**
	 * 版本更新
	 * @param context
	 */
	private void obtainNewVersion(Context context) {
		try {
			final int versionCode = CommonUtil.getVersionCode(context);
			CatShopApi.getNewVersion(context, new ObtainListener<NewVersion>() {

				@Override
				public void onSucceed(Context context, NewVersion result) {
					if (result != null) {
						mDownloadUrl = result.downloadUrl;
						String content = result.appdesc;
						String versionName = result.versionName;
						if (result.versionCode > versionCode) {
							mDialog = DialogUtil.showChoiceDialog(getActivity(), null,
									"更新说明:" + content + "\n" + "版本号:" + versionName, "忽略", "更新", listener);
							mDialog.show();
						} else {
							Toaster.toast(R.string.no_update);
						}
					} else {
						Toaster.toast(R.string.no_update);
					}
				}

				@Override
				public void onFinished(Context context, ResultCode code) {

				}

				@Override
				public void onError(Context context, ResultCode code) {
					Toaster.toast(R.string.no_update);
				}
			});
		} catch (Exception e) {
			Toaster.toast("获取版本号失败");
		}

	}

	/**
	 * 应用分享
	 * @param context
	 */
	private void shareApp(Context context) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		// 分享的数据类型
		intent.setType("text/plain");
		// 分享的内容
		intent.putExtra(Intent.EXTRA_TEXT,
				getString(R.string.app_link, getString(R.string.app_name), getString(R.string.sub_app_name),
						Global.APP_LINK_URL + context.getPackageName() + Global.LINK_END_URL));
		// 允许启动新的Activity
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 目标应用寻找对话框的标题
		context.startActivity(
				Intent.createChooser(intent, getActivity().getResources().getString(R.string.sub_app_name)));
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mDialog != null) {
				mDialog.dismiss();
			}
			switch ((Integer) v.getTag()) {
			case DialogUtil.LEFT_TAG:

				break;
			case DialogUtil.RIGHT_TAG:
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse(mDownloadUrl);
				intent.setData(content_url);
				startActivity(intent);
				break;
			}
		}
	};

	private void getUserInfo(Context context) {
		ProviderFatory.getUserProvider().getUserInfo(context, new ObtainListener<User>() {

			@Override
			public void onSucceed(Context context, User result) {
				if (result != null) {
					int uid = ProviderFatory.getUserProvider().getUserId(context);
					tv_setting_nickname.setText(!TextUtils.isEmpty(result.nickName) ? result.nickName
							: getActivity().getResources().getString(R.string.sub_app_name) + uid);
					ImageHelper.displayDefaultCircleIcon(iv_setting_head, result.headUrl);
				} else {
					tv_setting_nickname.setText("");
					ImageHelper.displayDefaultCircleIcon(iv_setting_head, "");
				}
			}

			@Override
			public void onFinished(Context context, ResultCode code) {

			}

			@Override
			public void onError(Context context, ResultCode code) {

			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void update() {
		isLogined = ProviderFatory.getUserProvider().isLogined(getActivity());
		if (isLogined) {
			iv_setting_head.setVisibility(View.VISIBLE);
			tv_setting_nickname.setVisibility(View.VISIBLE);
		} else {
			iv_setting_head.setVisibility(View.GONE);
			tv_setting_nickname.setVisibility(View.GONE);
		}
		getUserInfo(getActivity());
	}
}

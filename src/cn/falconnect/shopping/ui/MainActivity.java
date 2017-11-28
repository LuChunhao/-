package cn.falconnect.shopping.ui;

import java.util.List;

import org.aurora.library.downloader.core.CustomThreadAsyncTask;
import org.aurora.library.views.Toaster;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import cn.falconnect.shopping.TalkingDataHelper;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.constants.Global;
import cn.falconnect.shopping.entity.LaunchMission;
import cn.falconnect.shopping.entity.NewVersion;
import cn.falconnect.shopping.provider.db.LaunchPageDAO;
import cn.falconnect.shopping.provider.web.CatShopApi;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.utils.CommonUtil;
import cn.falconnect.shopping.utils.DialogUtil;
import cn.falconnect.shopping.utils.ImageHelper;

import com.adcocoa.sdk.offerwalllibrary.AdcocoaOfferWall;
import com.bodong.tools.imageloader.ImageLoader;

public class MainActivity extends FragmentActivity {
	boolean isFirstIn = false;
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	// 延迟3秒
	private static final long SPLASH_DELAY_MILLIS = 2500;
	public static final String SHAREDPREFERENCE_NAME = "first_pref";
	private static final String APP_CENTER_KEY = "adcocoa";
	private static final int DELAY = 2000;
	private boolean isPressedBack = false;

	private ImageView mIv_splash;
	// 显示更新的对话框
	private Dialog mDialog = null;
	private String mDownloadUrl;
	private boolean isForceUpdate;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
//			case GO_GUIDE:
//				goGuide();
//				break;
			}
		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TalkingDataHelper.init(this);
		AdcocoaOfferWall.setPlatformId(getString(R.string.adcocoa_id));
		AdcocoaOfferWall.init(this);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);
		mIv_splash = (ImageView) findViewById(R.id.iv_splash);
		queryLaunchMissionList(this);
		setTextviewVisible();
		getSplashTask(this);
		obtainNewVersion(this);
		if (mDialog != null && !mDialog.isShowing()) {
			init();
		}else if (mDialog == null) {
			init();
		}
	}

	private void setTextviewVisible(){
		CatShopApi.getAppCenterStatus(this, APP_CENTER_KEY, new ObtainListener<String>() {
			
			@Override
			public void onSucceed(Context context, String result) {
				if (!TextUtils.isEmpty(result)) {
					Global.AD_OPEN = result.equals("t");
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
	
	private void obtainNewVersion(final Context activity) {
		try {
			final int versionCode = CommonUtil.getVersionCode(activity);
			CatShopApi.getNewVersion(activity, new ObtainListener<NewVersion>() {
				
				@Override
				public void onSucceed(Context context, NewVersion result) {
					if (result != null) {
						mDownloadUrl = result.downloadUrl;
						String content = result.appdesc;
						String versionName = result.versionName;
						isForceUpdate = (result.forceUpdate == 1);
						if (result.versionCode > versionCode) {
							if (isForceUpdate) {
								mDialog = DialogUtil.showChoiceDialog(activity, "亲，您的客户端版本过低，无法正常使用，请及时更新到最新版本", "更新说明:" + content + "\n" + "版本号:" + result.versionName, "忽略", "更新", listener);
							} else {
								mDialog = DialogUtil.showChoiceDialog(activity, null, "更新说明:" + content + "\n" + "版本号:" + versionName, "忽略", "更新", listener);
							}
							mDialog.show();
						}else {
						}
					}else {
					}
				}
				
				@Override
				public void onFinished(Context context, ResultCode code) {
					
				}
				
				@Override
				public void onError(Context context, ResultCode code) {
				}
			});
		} catch (Exception e) {
		}
		
	}
	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mDialog != null) {
				mDialog.dismiss();
			}
			switch ((Integer) v.getTag()) {
			case DialogUtil.LEFT_TAG:
				if (isForceUpdate) {
					finish();
				}else {
					init();
				}
				break;
			case DialogUtil.RIGHT_TAG:
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse(mDownloadUrl);
				intent.setData(content_url);
				startActivity(intent);
				if (!isForceUpdate) {
					init();
				}
				break;
			}
		}
	};

	private void init() {
		// 读取SharedPreferences中需要的数据
		// 使用SharedPreferences来记录程序的使用次数
		SharedPreferences preferences = getSharedPreferences(SHAREDPREFERENCE_NAME, MODE_PRIVATE);
		isFirstIn = preferences.getBoolean("isFirstIn", true);
		// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
		if (!isFirstIn) {
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		} else {
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		}
	}

	private void goHome() {
		quitFullScreen();
		initMainFragment();
	}
	
	/**   
	 * 动态取消全屏   
	 */   
	private void quitFullScreen(){   
	    final WindowManager.LayoutParams attrs = getWindow().getAttributes();   
	    attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);   
	    getWindow().setAttributes(attrs);   
	    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);   
	}   
	
	private void initMainFragment() {
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		Fragment startFragment = fragmentManager
				.findFragmentByTag(MainFragment.FRAGMENT_TAG);
		if (startFragment == null) {
			startFragment = new MainFragment();
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.add(R.id.fragment_container, startFragment,
					MainFragment.FRAGMENT_TAG);
			transaction.commitAllowingStateLoss();
			fragmentManager.executePendingTransactions();
		}

	}

//	private void goGuide() {
//		quitFullScreen();
//		FragmentManager fragmentManager = getSupportFragmentManager();
//		Fragment guideFragment = fragmentManager.findFragmentByTag(GuideFragment.FRAGMENT_TAG);
//		if (guideFragment == null) {
//			guideFragment = new GuideFragment();
//			FragmentTransaction transaction = fragmentManager
//					.beginTransaction();
//			transaction.add(R.id.fragment_container, guideFragment,
//					GuideFragment.FRAGMENT_TAG);
//			transaction.commitAllowingStateLoss();
//			fragmentManager.executePendingTransactions();
//		}
//	}

	private void getSplashTask(Context context) {
		ProviderFatory.getLaunchPageProvider().obtainLaunchPage(context, new ObtainListener<List<LaunchMission>>() {

			@Override
			public void onSucceed(Context context, List<LaunchMission> result) {
				if (result != null && result.size() > 0) {
					for (LaunchMission launchPage : result) {
						ImageLoader.getInstance().setImageBitmap(launchPage.url, mIv_splash, true);
						LaunchPageDAO.getInstance().detete(launchPage.missionId);
						LaunchPageDAO.getInstance().insert(launchPage);
					}
				}
			}

			@Override
			public void onError(Context context, ResultCode code) {
			}

			@Override
			public void onFinished(Context context, ResultCode code) {

			}

		});
	}

	private static class Task<T> {
		private Context context;
		private T result;
		private ObtainListener<T> listener;

		private Task(Context context, ObtainListener<T> listener) {
			this.listener = listener;
			if (context != null) {
				this.context = context.getApplicationContext();
			}
		}
	}

	private static <T> void queryLaunchMissions(Context context, ObtainListener<T> listener) {
		Task<T> task = new Task<T>(context, listener);
		new CustomThreadAsyncTask<Object, Void, Task<T>>() {
			@Override
			protected Task<T> doInBackground(Object... params) {
				@SuppressWarnings("unchecked")
				Task<T> task = (Task<T>) params[0];
				try {
					List<LaunchMission> data = LaunchPageDAO.getInstance().queryAll();
					task.result = (T) data;

				} finally {
					task.listener.onFinishInBackgroud(task.context, null, task.result);
				}
				return task;
			}

			@Override
			protected void onPostExecute(Task<T> result) {
				result.listener.onSucceed(result.context, result.result);

				result.listener.onFinished(result.context, null);
			}
		}.execute(task);
	}

	private void queryLaunchMissionList(Context context) {
		queryLaunchMissions(context, new ObtainListener<List<LaunchMission>>() {

			@Override
			public void onSucceed(Context context, List<LaunchMission> result) {
				if (result != null && result.size() > 0) {
					long now_time = System.currentTimeMillis();
					for (LaunchMission launchPage : result) {
						if (launchPage.endTime > now_time && launchPage.beginTime < now_time) {
							Bitmap bitmap = ImageLoader.getInstance().getBitmap(launchPage.url, true);
							if (bitmap != null) {
								mIv_splash.setImageBitmap(bitmap);
							} else {
								ImageLoader.getInstance().setImageBitmap(launchPage.url, mIv_splash, true);
							}
						}
					}

				} else {
					mIv_splash.setImageResource(R.drawable.splash);
				}
			}

			@Override
			public void onError(Context context, ResultCode code) {
			}

			@Override
			public void onFinished(Context context, ResultCode code) {

			}

		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			FragmentManager manager = getSupportFragmentManager();
			int count = manager.getBackStackEntryCount();
			if (count == 0) {
				mExitHandler.removeMessages(0);
				mExitHandler.sendEmptyMessageDelayed(0, DELAY);
				if (!isPressedBack) {
					isPressedBack = true;
					Toaster.toast(getString(R.string.back_confirm_text));
					return true;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private Handler mExitHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			isPressedBack = false;
		}

	};
	
	protected void onDestroy() {
		ImageHelper.clearCache();
		super.onDestroy();
	};
	@Override
	protected void onResume() {
		super.onResume();
		TalkingDataHelper.onResume(this);
	}

	@Override
	protected void onPause() {
		TalkingDataHelper.onPause(this);
		super.onPause();
	}
}

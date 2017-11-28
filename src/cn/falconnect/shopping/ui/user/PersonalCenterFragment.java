package cn.falconnect.shopping.ui.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.aurora.library.downloader.core.CustomThreadAsyncTask;
import org.aurora.library.util.MediaStoreUtils;
import org.aurora.library.util.NetworkUtil;
import org.aurora.library.views.Toaster;
import org.aurora.library.views.slicenoodles.InterceptTouchEventListener;
import org.aurora.library.views.slicenoodles.SlicedNoodlesLayout;
import org.aurora.library.views.text.MarqueeTextView;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import cn.falconnect.shopping.TalkingDataHelper;
import cn.falconnect.shopping.Task;
import cn.falconnect.shopping.Task.AsyncTaskListener;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.constants.Global;
import cn.falconnect.shopping.entity.Goods;
import cn.falconnect.shopping.entity.User;
import cn.falconnect.shopping.provider.db.CacheDAO;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.Protocol;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.tabs.FragmentHostTabGroup;
import cn.falconnect.shopping.tabs.FragmentPagerTabGroup;
import cn.falconnect.shopping.ui.HomeFragment;
import cn.falconnect.shopping.ui.SettingFragment;
import cn.falconnect.shopping.ui.SlidingExitFragment;
import cn.falconnect.shopping.ui.topic.MyArticleFragment;
import cn.falconnect.shopping.ui.topic.TopicFragment;
import cn.falconnect.shopping.uploader.UploadProgressListener;
import cn.falconnect.shopping.uploader.Uploader;
import cn.falconnect.shopping.utils.BitmapUtil;
import cn.falconnect.shopping.utils.CommonUtil;
import cn.falconnect.shopping.utils.DialogUtil;
import cn.falconnect.shopping.utils.DialogUtil.CallBack;
import cn.falconnect.shopping.utils.ImageHelper;
import cn.falconnect.shopping.utils.SharedPreferencesUtil;
import cn.falconnect.shopping.widget.CircleImageView;

public class PersonalCenterFragment extends SlidingExitFragment {
	public static final String FRAGEMNT_TAG = "PersonalCenterFragment";
	private final int IMAGE_CODE = 2; // 相册
	private final int TAKE_PICTURE = 1;// 相机
	private final int IMAGE_CROP = 3;// 裁剪
	private final int EDIT_NICKNAME = 1;// 修改昵称
	private final int EDIT_HEAD_PIC = 2;// 修改头像
	private final int PERSON_IMAGE = 4; // 个人头像
	private final int BACKGROUD_IMAGE = 5;// 背景图片
	private final int PERSON_IMAGE_album = 6; // 个人头像从相册选取
	private final int BACKGROUD_IMAGE_album = 7;// 背景图片从相册选取
	private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";// tempfile
	private View mContentView;
	private MarqueeTextView tv_nickName;
	private CircleImageView iv_center_head;
	private FragmentPagerTabGroup mTabGroup;
	private Dialog mDialog;
	private Uri mImageUri = Uri.parse(IMAGE_FILE_LOCATION);// The Uri to store
	private UserActionPopupWindow mActionPopupWindow;
	private ImageView iv_centerbackgroud;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActionPopupWindow = new UserActionPopupWindow(getActivity(),
				mOnClickListener);

	}

	@Override
	protected View onChildCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_user, null);
			initViews();
			getUserInfo();
		}
		return mContentView;
	}

	@Override
	public void onResume() {
		super.onResume();
		TalkingDataHelper.reportPageStart(this, R.string.user);
	}

	@Override
	public void onPause() {
		TalkingDataHelper.reportPageEnd(this, R.string.user);
		super.onPause();
	}

	private void initViews() {
		setInterceptTouchEventAdapter(new InterceptTouchEventListener() {

			@Override
			public boolean isIntercept(MotionEvent motionEvent, float moveX,
					float moveY, boolean superIntercept, int location) {
				if (location == SlicedNoodlesLayout.LOCATION_LEFT_DOWN
						|| location == SlicedNoodlesLayout.LOCATION_RIGHT_UP) {
					return true;
				}
				if (mTabGroup.getPager().getCurrentItem() == 3) {
					return moveX < 0;
				}
				if (mTabGroup.getPager().getCurrentItem() == 0) {
					return moveX > 0;
				}
				return false;
			}
		});
		bg = (RelativeLayout) mContentView.findViewById(R.id.rl_login);
		bg.setOnClickListener(mOnClickListener);
		mContentView.findViewById(R.id.iv_back).setOnClickListener(
				mOnClickListener);
		mContentView.findViewById(R.id.overflow_tag).setOnClickListener(
				mOnClickListener);
		tv_nickName = (MarqueeTextView) mContentView
				.findViewById(R.id.tv_nickName);
		iv_center_head = (CircleImageView) mContentView
				.findViewById(R.id.iv_center_head);
		iv_centerbackgroud = (ImageView) mContentView
				.findViewById(R.id.iv_centerbackgroud_own);
		iv_center_head.setScaleType(ScaleType.CENTER_CROP);
		iv_center_head.setOnClickListener(mOnClickListener);
		mTabGroup = (FragmentPagerTabGroup) mContentView
				.findViewById(R.id.user_tabgroup);
		mTabGroup.setup(FragmentHostTabGroup.LOCATION_TOP);
		mTabGroup.getTabWidgetBar().setAnimDrawable(
				getResources().getDrawable(R.drawable.vpi_tab_focus), true);
		mTabGroup.addTab(CollectionsFragment.FRAGMENT_TAG,
				CollectionsFragment.class, null);
		mTabGroup.addTab(HistoryFragment.FRAGMENT_TAG, HistoryFragment.class,
				null);
		mTabGroup.addTab(MyArticleFragment.FRAGMENT_TAG,
				MyArticleFragment.class, null);
		mTabGroup.setPagerOffscreenPageLimit(3);
		mTabGroup.setCurrentTab(0);

	}

	public Fragment getCurrentFragment() {
		return mTabGroup.getCurrentFragment();
	}

	public Fragment getFragmentByPosition(int position) {
		return mTabGroup.getFragmentByPosition(position);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(final View v) {
			switch (v.getId()) {
			case R.id.iv_back:
				finishFragment();
				break;
			case R.id.overflow_tag:
				mActionPopupWindow.showAsDropDown(mContentView
						.findViewById(R.id.overflow_tag));
				break;
			case R.id.btn_edit_nickname:
				mActionPopupWindow.dismiss();
				mDialog = DialogUtil.editNicknameDialog(getActivity(),
						new CallBack() {

							@Override
							public void setResult(String content) {
								editUserInfo("", "", content, EDIT_NICKNAME);
							}
						});
				break;
			case R.id.btn_logout:
				mActionPopupWindow.dismiss();
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				mDialog = DialogUtil.showChoiceDialog(getActivity(), null,
						"确定要注销当前用户？", "点错了", "继续", mLogoutClickListener);
				mDialog.show();
				break;
			case R.id.iv_center_head:
				new PopupWindows(v.getContext(), mTabGroup, PERSON_IMAGE,
						PERSON_IMAGE_album);
				break;
			case R.id.rl_login:
				AlertDialog alertDialog;
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				View view = inflater.inflate(
						R.layout.updatebackgroudimagedialog, null);
				Button ok = (Button) view.findViewById(R.id.btn_yes);
				Button no = (Button) view.findViewById(R.id.btn_no);
				builder.setView(view).setCancelable(true);
				final AlertDialog alert = builder.create();
				alertDialog = alert;
				ok.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						new PopupWindows(v.getContext(), mTabGroup,
								BACKGROUD_IMAGE, BACKGROUD_IMAGE_album);
						alert.dismiss();
						alert.cancel();
					}
				});
				no.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						alert.dismiss();
						alert.cancel();
					}
				});
				alert.show();
				break;
			}
		}
	};

	private OnClickListener mLogoutClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
			switch ((Integer) v.getTag()) {
			case DialogUtil.LEFT_TAG:

				break;
			case DialogUtil.RIGHT_TAG:
				mDialog = DialogUtil.showProgressBar(getActivity(), "正在注销...");
				mDialog.show();
				String account = ProviderFatory.getUserProvider().getLoginName(
						v.getContext());
				ImageHelper.clearCache();
				clearCollects(account, v.getContext());
				break;
			}
		}
	};
	private RelativeLayout bg;

	private void clearCollects(String account, Context context) {
		deleteData(context, account, new AsyncTaskListener<Void>() {

			@Override
			public void onSucceed(Context context, Void result) {

			}

			@Override
			public void onError(Context context, String msg) {

			}

			@Override
			public void onFinished(Context context, String msg) {
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				ProviderFatory.getUserProvider().logout(context);
				update();
				finishFragment();
			}

		});
	}

	private <T> void deleteData(Context context, final String account,
			AsyncTaskListener<T> listener) {
		Task<T> task = new Task<T>(context, listener);
		new CustomThreadAsyncTask<Object, Void, Task<T>>() {
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

	private void getUserInfo() {
		ProviderFatory.getUserProvider().getUserInfo(mContentView.getContext(),
				new ObtainListener<User>() {
					@Override
					public void onSucceed(Context context, User result) {
						if (result != null) {
							int uid = ProviderFatory.getUserProvider()
									.getUserId(context);
							tv_nickName.setText(!TextUtils
									.isEmpty(result.nickName) ? result.nickName
									: getActivity().getResources().getString(
											R.string.sub_app_name)
											+ uid);
							ImageHelper.displayDefaultCircleIcon(
									iv_center_head, result.headUrl);
							ImageHelper.displayDefaultCircleIcon(
									iv_centerbackgroud, result.backgroundUrl);
							SharedPreferencesUtil.saveImageURL(context,
									"backgroudurl", result.backgroundUrl);
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

	private void editUserInfo(final String headUrl, final String backgroundUrl,
			final String nickName, final int flag) {
		ProviderFatory.getUserProvider().editPersonInfo(getActivity(), headUrl,
				backgroundUrl, nickName, new ObtainListener<Void>() {

					@Override
					public void onSucceed(Context context, Void result) {
						if (mDialog != null && mDialog.isShowing()) {
							mDialog.dismiss();
						}

						if (flag == EDIT_NICKNAME) {
							Toaster.toast("昵称修改成功");
							tv_nickName.setText(nickName);
						} else if (flag == EDIT_HEAD_PIC) {
							File file = new File(IMAGE_FILE_LOCATION);
							if (file.exists()) {
								file.delete();
							}
							ImageHelper.displayDefaultCircleIcon(
									iv_center_head, headUrl);
							ImageHelper.displayDefaultIcon(iv_centerbackgroud,
									backgroundUrl);
							SharedPreferencesUtil.saveImageURL(context,
									"backgroundurl", backgroundUrl);
						}

						MyArticleFragment articleFragment = (MyArticleFragment) getFragmentByPosition(2);
						if (articleFragment != null) {
							articleFragment.onUpdate();
						}
						HomeFragment homeFragment = (HomeFragment) getActivity()
								.getSupportFragmentManager().findFragmentByTag(
										HomeFragment.FRAGEMNT_TAG);
						if (homeFragment != null) {
							TopicFragment topicFragment = (TopicFragment) homeFragment
									.getFragmentByPosition(1);
							if (topicFragment != null) {
								topicFragment.onUpdate();
							}
						}
						update();

					}

					@Override
					public void onFinished(Context context, ResultCode code) {

					}

					@Override
					public void onError(Context context, ResultCode code) {
						Toaster.toast(code.msg);
					}
				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode,
			final Intent data) {

		if (requestCode == PERSON_IMAGE) {
			if (resultCode == -1) {
				cropPhoto(mImageUri);
			}
		}
		if (requestCode == BACKGROUD_IMAGE) {
			if (resultCode == -1) {
				cropPhoto(mImageUri, 9);
			}
		}
		if (requestCode == PERSON_IMAGE_album) {
			handleAlbumPhoto(data, PERSON_IMAGE_album);
		}
		if (requestCode == IMAGE_CROP) {
			handlePhotoFromCamera(data, PERSON_IMAGE);
		}
		if (requestCode == 7) {
			handleAlbumPhoto(data, BACKGROUD_IMAGE_album);
		}
		if (requestCode == 9) {
			handlePhotoFromCamera(data, BACKGROUD_IMAGE);
		}
	}

	private String getTmpPath(Context context) {
		return new File(context.getFilesDir(), "tmp.png").getAbsolutePath();
	}

	private void upload(File file, final int a) {
		String sign = ProviderFatory.getUserProvider().getLoginSign(
				getActivity());
		String json = "";
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			jsonObject.put("sign", sign);
			jsonObject.put("device", Protocol.DEVICE);
			jsonObject.put("packageName", getActivity().getPackageName());
			jsonObject.put("version", CommonUtil.getVersionCode(getActivity()));
			jsonObject
					.put("network", NetworkUtil.getNetworkType(getActivity()));
			json = jsonObject.toString();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		List<File> files = new ArrayList<File>();
		files.add(file);
		Uploader.upload(getActivity(), json, Protocol.ResourceUrl, files,
				new UploadProgressListener() {
					private String headUrl;
					private String backgroudUrl;
					@Override
					public void onSucceed(byte[] response) {
						try {
							JSONObject jsonObject = new JSONObject(new String(
									response));
							if (a == PERSON_IMAGE) {
								headUrl = jsonObject.getString("data");
								ImageHelper.displayDefaultCircleIcon(
										iv_center_head, headUrl);
								mDialog.dismiss();
							}
							if (a == BACKGROUD_IMAGE) {
								backgroudUrl = jsonObject.getString("data");
								ImageHelper.displayDefaultCircleIcon(
										iv_centerbackgroud, backgroudUrl);
								SharedPreferencesUtil.saveImageURL(
										getActivity(), "backgroudurl",
										backgroudUrl);
								mDialog.dismiss();
							}
							if (!TextUtils.isEmpty(headUrl)) {
								editUserInfo(headUrl, backgroudUrl, tv_nickName
										.getText().toString(), EDIT_HEAD_PIC);
							}
							if (!TextUtils.isEmpty(backgroudUrl)) {
								editUserInfo(headUrl, backgroudUrl, tv_nickName
										.getText().toString(), EDIT_HEAD_PIC);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
					@Override
					public void onProgress(long progress) {
					}
					@Override
					public void onError() {
						System.out.println("onerror");
					}
				});
	}

	private void update() {
		FragmentManager fragmentManager = getActivity()
				.getSupportFragmentManager();
		if (fragmentManager != null) {
			SettingFragment settingFragment = (SettingFragment) fragmentManager
					.findFragmentByTag(SettingFragment.FRAGMENT_TAG);
			if (settingFragment != null) {
				settingFragment.update();
			}
		}
	}

	public class PopupWindows extends PopupWindow {
		public PopupWindows(Context mContext, View parent, final int a,
				final int b) {
			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();
			Button btn_camera = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button btn_photo = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button btn_cancel = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			btn_camera.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					takePhoto(a);
					dismiss();
				}
			});
			btn_photo.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					getAlbumPhoto(b);
					dismiss();
				}
			});
			btn_cancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	private void takePhoto(int a) {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
		startActivityForResult(openCameraIntent, a);
	}

	private void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");// 可裁剪
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		intent.putExtra("return-data", true);// 若为false则表示不返回数据
		startActivityForResult(intent, IMAGE_CROP);
	}

	private void cropPhoto(Uri uri, int k) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");// 可裁剪
		intent.putExtra("aspectX", 4);
		intent.putExtra("aspectY", 3);
		intent.putExtra("outputX", 240);
		intent.putExtra("outputY", 180);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		intent.putExtra("return-data", true);// 若为false则表示不返回数据
		startActivityForResult(intent, 9);
	}

	private void getAlbumPhoto(int a) {
		Intent intent_album = new Intent(Intent.ACTION_GET_CONTENT,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// 调用android的图库
		intent_album.setType("image/*");
		intent_album.putExtra("crop", "true");
		intent_album.putExtra("aspectX", 4);// 裁剪框比例
		intent_album.putExtra("aspectY", 3);
		intent_album.putExtra("outputX", 240);// 输出图片大小
		intent_album.putExtra("outputY", 180);
		startActivityForResult(intent_album, a);// 6
	}

	private void handleAlbumPhoto(Intent data, int a) {
		Bitmap bm = null;
		String filePath = MediaStoreUtils.getCapturePathFromCamera(
				getActivity(), data);
		try {
			if (!TextUtils.isEmpty(filePath)) {
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				mDialog = DialogUtil.showProgressBar(getActivity(), "");
				mDialog.show();
				InputStream is = new FileInputStream(filePath);
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inTempStorage = new byte[100 * 1024];
				opts.inPreferredConfig = Bitmap.Config.RGB_565;
				opts.inPurgeable = true;
				opts.inInputShareable = true;
				bm = BitmapFactory.decodeStream(is, null, opts);
				OutputStream outStream = new FileOutputStream(
						getTmpPath(getActivity()));
				bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
				if (a == PERSON_IMAGE_album) {
					upload(new File(getTmpPath(getActivity())), PERSON_IMAGE);
				}
				if (a == BACKGROUD_IMAGE_album) {
					upload(new File(getTmpPath(getActivity())), BACKGROUD_IMAGE);
				}
			}

		} catch (FileNotFoundException e) {
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
			e.printStackTrace();
		}
	}

	private void handlePhotoFromCamera(Intent data, int a) {
		if (data != null) {
			String filePath = MediaStoreUtils.getCapturePathFromCamera(
					this.getActivity(), data);
			if (!TextUtils.isEmpty(filePath)) {
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				mDialog = DialogUtil.showProgressBar(getActivity(), "");
				mDialog.show();
				Bitmap bitmap = null;
				Bitmap bitmap_zoom = null;
				InputStream is = null;
				OutputStream outStream = null;
				try {
					is = new FileInputStream(filePath);
					BitmapFactory.Options opts = new BitmapFactory.Options();
					opts.inTempStorage = new byte[100 * 1024];
					opts.inPreferredConfig = Bitmap.Config.RGB_565;
					opts.inPurgeable = true;
					opts.inInputShareable = true;
					bitmap = BitmapFactory.decodeStream(is, null, opts);
					bitmap_zoom = BitmapUtil.zoomImage(bitmap, 150, 150);
					outStream = new FileOutputStream(filePath);
					bitmap_zoom.compress(Bitmap.CompressFormat.JPEG, 100,
							outStream);
				} catch (IOException e1) {
					e1.printStackTrace();
				} finally {

					if (bitmap_zoom != null && !bitmap_zoom.isRecycled()) {
						bitmap_zoom.recycle();
						bitmap_zoom = null;
					}
					if (bitmap != null && !bitmap.isRecycled()) {
						bitmap.recycle();
						bitmap = null;
					}
					try {
						if (outStream != null) {
							outStream.close();
							outStream = null;
						}
						if (is != null) {
							is.close();
							is = null;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				File file = new File(filePath);
				upload(file, a);
			}
		}
	}
}

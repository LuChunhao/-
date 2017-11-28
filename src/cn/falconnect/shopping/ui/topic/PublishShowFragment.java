package cn.falconnect.shopping.ui.topic;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.aurora.library.util.NetworkUtil;
import org.aurora.library.views.Toaster;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.falconnect.shopping.CatShopApplication;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.ImageItem;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.Protocol;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.ui.AlbumFragment;
import cn.falconnect.shopping.ui.BaseFragment;
import cn.falconnect.shopping.ui.HomeFragment;
import cn.falconnect.shopping.ui.user.PersonalCenterFragment;
import cn.falconnect.shopping.uploader.UploadProgressListener;
import cn.falconnect.shopping.uploader.Uploader;
import cn.falconnect.shopping.utils.AlbumHelper;
import cn.falconnect.shopping.utils.BitmapUtil;
import cn.falconnect.shopping.utils.CommonUtil;
import cn.falconnect.shopping.utils.DialogUtil;

public class PublishShowFragment extends BaseFragment {
	public static final String FRAGMENT_TAG = "PublishShowFragment";
	private static final int TAKE_PICTURE = 1;
	private GridAdapter mAdapter;
	private ArrayList<String> mUrls;
	private EditText et_description;
	private Dialog mDialog;
	private String mTag;
	private String mCurrentPhotoPath;
	private int mImageCount=1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_publish_show, null);
		initViews(contentView);
		mUrls = new ArrayList<String>();
		return contentView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTag = getArguments().getString(FRAGMENT_TAG);
	}

	@Override
	public void onResume() {
		super.onResume();
		mAdapter.notifyDataSetChanged();
		;
	}

	@Override
	public void onReload(Context context) {
		mAdapter.notifyDataSetChanged();
		super.onReload(context);
	}

	@Override
	protected String getTDPageName() {
		return getString(R.string.publish_show);
	}

	private void initViews(View contentView) {
		et_description = (EditText) contentView.findViewById(R.id.et_publish_description);
		final GridView gv_publish_show = (GridView) contentView.findViewById(R.id.gv_publish_show);
		mAdapter = new GridAdapter();
		gv_publish_show.setAdapter(mAdapter);
		gv_publish_show.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (mAdapter.getItem(position).bucketId == null) {
					new PopupWindows(parent.getContext(), gv_publish_show);
				}
			}
		});
		ImageView iv_close_show = (ImageView) contentView.findViewById(R.id.iv_close_show);
		iv_close_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonUtil.hideSoftInput(getActivity(), et_description.getWindowToken());
				finishFragment();
			}
		});
		TextView tv_publish = (TextView) contentView.findViewById(R.id.tv_publish);
		tv_publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mAdapter.getCount() == 0) {
					Toaster.toast("至少晒张图片吧亲");
					return;
				}
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				mDialog = DialogUtil.showProgressBar(getActivity(), "正在发表第"+mImageCount+"张图片",0);
				mDialog.show();
				for (ImageItem item : mAdapter.getImageItems()) {
					if (item.imagePath != null) {
						String path = org.aurora.library.util.BitmapUtil.decodeBitmapFile(item.imagePath);
						File file = new File(path);
						upload(file);
					
					}
					
				}
				mImageCount=1;
			}
		});
	}

	public class GridAdapter extends BaseAdapter {
		private int selectedPosition = -1;// 选中的位置
		private ArrayList<ImageItem> images;

		public GridAdapter() {
			images = new ArrayList<ImageItem>();
			ImageItem imageItem = new ImageItem();
			images.add(imageItem);
		}

		public void setImages(ArrayList<ImageItem> images) {
			this.images = images;
			notifyDataSetChanged();
		}

		public void clear() {
			images.clear();
		}

		public void addImageItem(ImageItem item) {

			images.add(0, item);
			notifyDataSetChanged();
		}

		public ArrayList<ImageItem> getImageItems() {
			return images;
		}

		public void removeImageItem(ImageItem item) {
			for (int i = 0; i < images.size(); i++) {
				if (images.get(i).equals(item)) {
					images.remove(item);
				}
			}
			notifyDataSetChanged();
		}

		public int getCount() {
			return images.size();
		}

		public ImageItem getItem(int position) {

			return images.get(position);
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_published_grida, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ImageItem item = getItem(position);
			holder.bindView(item, images.size());
			return convertView;
		}

		public class ViewHolder {
			private ImageView image;
			private RelativeLayout rl_image;
			private ImageView iv_delete;

			public ViewHolder(View convertView) {
				image = (ImageView) convertView.findViewById(R.id.item_grida_image);
				iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete_image);
				rl_image = (RelativeLayout) convertView.findViewById(R.id.rl_image);
				final int width = CatShopApplication.sScreenWidth;
				convertView.post(new Runnable() {

					@Override
					public void run() {
						rl_image.setLayoutParams(new RelativeLayout.LayoutParams(width / 3, width / 3));
						image.setLayoutParams(new RelativeLayout.LayoutParams(width / 3 - 30, width / 3 - 30));
					}
				});
			}

			public void bindView(ImageItem imageItem, int size) {
				iv_delete.setOnClickListener(new deleteClickListener(imageItem));
				if (imageItem.bucketId == null) {
					image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused));
					iv_delete.setVisibility(View.GONE);
					if (size == 7) {
						rl_image.setVisibility(View.GONE);
						iv_delete.setVisibility(View.GONE);
					}
				} else {
					Bitmap bitmap = org.aurora.library.util.BitmapUtil.decodeBitmap(imageItem.imagePath,
							BitmapUtil.px2dip(getActivity(), 480), BitmapUtil.px2dip(getActivity(), 480));
					image.setImageBitmap(bitmap);
					iv_delete.setVisibility(View.VISIBLE);
				}
			}
		}

		class deleteClickListener implements OnClickListener {
			private ImageItem item;

			public deleteClickListener(ImageItem item) {
				this.item = item;
			}

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.iv_delete_image:
					// 处理删除
					if (!item.bucketId.equals(item.imageId)) {
						AlbumHelper.getHelper().updateIsSelect(item.bucketId, item.imageId);
					}
					mAdapter.removeImageItem(item);
					update();
					break;
				}
			}

		}

	}

	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putInt("count", mAdapter.getImageItems().size());
					bundle.putSerializable("image", mAdapter.getImageItems());
					startFragment(new AlbumFragment(), bundle, AlbumFragment.class.getName(), AlbumFragment.FRAGMENT_TAG);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	public void update() {
		mAdapter.notifyDataSetChanged();
	}

	public void setImageItemList(ArrayList<ImageItem> imageItems) {
		mAdapter.setImages(imageItems);
	}

	private void upload(final File file) {
		String sign = ProviderFatory.getUserProvider().getLoginSign(getActivity());
		String json = "";
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject();
			jsonObject.put("sign", sign);
			jsonObject.put("device", Protocol.DEVICE);
			jsonObject.put("packageName", getActivity().getPackageName());
			jsonObject.put("version", CommonUtil.getVersionCode(getActivity()));
			jsonObject.put("network", NetworkUtil.getNetworkType(getActivity()));
			json = jsonObject.toString();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		final List<File> files = new ArrayList<File>();
		files.add(file);
		Uploader.upload(getActivity(), json, Protocol.ResourceUrl, files, new UploadProgressListener() {

			@Override
			public void onSucceed(byte[] response) {
				mImageCount++;
				try {
					JSONObject jsonObject = new JSONObject(new String(response));
					String url = jsonObject.getString("data");
					mUrls.add(url);
					if (mUrls.size() == mAdapter.getCount() - 1) {
						publishArticles(mUrls);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onProgress(long progress) {
//				 text.setText("progress>>" + progress + "%");
				DialogUtil.setProgress((int) progress,mImageCount);
				
			
			}

			@Override
			public void onError() {
			}
		});
		
	}

	private void publishArticles(ArrayList<String> urls) {
		String description = et_description.getText().toString();
		String[] photoUrls = new String[urls.size()];
		for (int i = 0; i < urls.size(); i++) {
			photoUrls[i] = urls.get(i);
		}
		ProviderFatory.getShowProvider().showSomething(getActivity(), description, photoUrls, new ObtainListener<Void>() {

			@Override
			public void onSucceed(Context context, Void result) {
				mDialog.dismiss();
				if (mTag.equals(TopicFragment.FRAGMENT_TAG)) {
					HomeFragment homeFragment = (HomeFragment) getActivity().getSupportFragmentManager().findFragmentByTag(
							HomeFragment.FRAGEMNT_TAG);
					TopicFragment topicFragment = (TopicFragment) homeFragment.getFragmentByPosition(1);
					topicFragment.onUpdate();
				} else {
					PersonalCenterFragment centerFragment = (PersonalCenterFragment) getActivity().getSupportFragmentManager()
							.findFragmentByTag(PersonalCenterFragment.FRAGEMNT_TAG);
					MyArticleFragment articleFragment = (MyArticleFragment) centerFragment.getCurrentFragment();
					articleFragment.onUpdate();
				}
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		AlbumHelper.getHelper().clear();
	}

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			// 指定存放拍摄照片的位置
			File f = createImageFile();
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			startActivityForResult(openCameraIntent, TAKE_PICTURE);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private File createImageFile() throws IOException {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = format.format(new Date());
		String imageFileName = "fal_" + timeStamp + ".jpg";
		File image = new File(getAlbumDir(), imageFileName);
		mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}

	/**
	 * 获取保存图片的目录
	 * 
	 * @return
	 */
	private File getAlbumDir() {
		File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getAlbumName());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}

	/**
	 * 获取保存 隐患检查的图片文件夹名称
	 * 
	 * @return
	 */
	private static String getAlbumName() {
		return "falcons";
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (!TextUtils.isEmpty(mCurrentPhotoPath)) {
				// int degree = Bimp.readPictureDegree(filePath);
				ImageItem imageItem = new ImageItem();
				imageItem.imagePath = mCurrentPhotoPath;
				imageItem.bucketId = mCurrentPhotoPath;
				imageItem.imageId = mCurrentPhotoPath;
				mAdapter.addImageItem(imageItem);
				// Bitmap bm2 = Bimp.rotateBitmap(bm, degree);
				update();
			}

			break;
		}
	}

	/**
	 * 根据路径删除图片
	 * 
	 * @param path
	 */
	private void deleteTempFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}
}

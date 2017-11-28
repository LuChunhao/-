package cn.falconnect.shopping.ui.topic;

import static cn.falconnect.shopping.constants.Global.BundleKey.SHOW_BEAN;

import java.util.ArrayList;
import java.util.List;

import org.aurora.library.imageloader.core.assist.FailReason;
import org.aurora.library.imageloader.core.listener.ImageLoadingListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.falcon.photogallery.PhotoView;
import cn.falcon.photogallery.PhotoViewAttacher.OnPhotoTapListener;
import cn.falconnect.shopping.banner.GalleryIndicator;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.Show;
import cn.falconnect.shopping.ui.BaseFragment;
import cn.falconnect.shopping.utils.ImageHelper;

public class TopicAtlasDetailFragment extends BaseFragment {
	private Show mShowBean;
	private int mCurrentPosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			mShowBean = (Show) args.getSerializable(SHOW_BEAN);
			mCurrentPosition = args.getInt("position");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_atlas, null);
		initViews(inflater, contentView);
		return contentView;
	}

	private void initViews(LayoutInflater inflater, View contentView) {
		String[] urls = mShowBean.picUrls;
		if (urls != null) {
			ViewPager viewPager = (ViewPager) contentView
					.findViewById(R.id.image_detail_pager);
			List<View> mListViews = new ArrayList<View>();
			for (String url : urls) {
				View convertView = inflater.inflate(R.layout.atlas_pageadapter,
						null);
				PhotoView view = (PhotoView) convertView
						.findViewById(R.id.atlas_detail_image);
				final ViewGroup loadView = (ViewGroup) convertView
						.findViewById(R.id.atlas_loading_rl);
				convertView.setTag(url);
				view.setTag(R.id.tag, loadView);
				mListViews.add(convertView);
			}
			final GalleryIndicator galleryIndicator = (GalleryIndicator) contentView
					.findViewById(R.id.indicator);
			galleryIndicator.setCount(urls.length);
			galleryIndicator.setSeletion(mCurrentPosition);
			viewPager.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int postion) {
					galleryIndicator.setSeletion(postion);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});
			viewPager.setAdapter(new AtlasPageAdapter(mListViews,
					createOnPhotoTapListener()));
			viewPager.setCurrentItem(mCurrentPosition);
		}
	}

	private OnPhotoTapListener createOnPhotoTapListener() {
		return new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				finishFragment();
			}
		};
	}

	private static class AtlasPageAdapter extends PagerAdapter {
		public List<View> mContentViews;
		private OnPhotoTapListener mOnPhotoTapListener;

		public AtlasPageAdapter(List<View> mListViews,
				OnPhotoTapListener listener) {
			this.mContentViews = mListViews;
			this.mOnPhotoTapListener = listener;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mContentViews.get(arg1));
		}

		@Override
		public int getCount() {
			return mContentViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			ViewGroup convertView = (ViewGroup) mContentViews.get(arg1);
			final PhotoView imageView = (PhotoView) convertView
					.findViewById(R.id.atlas_detail_image);
			((ViewPager) arg0).addView(convertView);
			String url = (String) convertView.getTag();
			if (!TextUtils.isEmpty(url)) {
				ImageHelper.displayAtlas(imageView, url,
						createImageLoadingListener());
			}
			imageView.setOnPhotoTapListener(mOnPhotoTapListener);
			return mContentViews.get(arg1);
		}

		private ImageLoadingListener createImageLoadingListener() {
			return new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String imageUri, View view) {

				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {

				}

				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					ViewGroup loadingView = (ViewGroup) view.getTag(R.id.tag);
					if (loadingView != null) {
						loadingView.setVisibility(View.GONE);
					}
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {

				}
			};
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}
	}
}

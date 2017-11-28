package cn.falconnect.shopping.utils;

import org.aurora.library.imageloader.cache.disc.naming.Md5FileNameGenerator;
import org.aurora.library.imageloader.cache.memory.impl.LRULimitedMemoryCache;
import org.aurora.library.imageloader.core.DisplayImageOptions;
import org.aurora.library.imageloader.core.DisplayImageOptions.Builder;
import org.aurora.library.imageloader.core.ImageLoader;
import org.aurora.library.imageloader.core.ImageLoaderConfiguration;
import org.aurora.library.imageloader.core.assist.ImageScaleType;
import org.aurora.library.imageloader.core.assist.QueueProcessingType;
import org.aurora.library.imageloader.core.display.FadeInBitmapDisplayer;
import org.aurora.library.imageloader.core.listener.ImageLoadingListener;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import cn.falconnect.shopping.CatShopApplication;
import cn.falconnect.shopping.cat.R;

public class ImageHelper {
	private static final int MAX_MEMORY_CACHE = 2 * 1024 * 1024;
	private static final int MAX_DISC_CACHE = 50 * 1024 * 1024;
	private ImageHelper() {
	}

	public static void clearCache() {
		ImageLoader.getInstance().clearMemoryCache();
		ImageLoader.getInstance().clearDiskCache();
	}

	public static void init(Context context) {
		DisplayImageOptions options = new DisplayImageOptions.Builder().
				displayer(new FadeInBitmapDisplayer(200)).imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true).cacheOnDisk(true)
				.build();

		int maxImageWidth = (int) (CatShopApplication.sScreenWidth >= 1080 ? CatShopApplication.sScreenWidth / 2.5 : CatShopApplication.sScreenWidth / 2);
		int maxImageHeigth = (int) (CatShopApplication.sScreenHeight >= 1920 ? CatShopApplication.sScreenHeight / 2.5 : CatShopApplication.sScreenHeight / 2);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.FIFO)
				.memoryCache(new LRULimitedMemoryCache(MAX_MEMORY_CACHE)).diskCacheSize(MAX_DISC_CACHE)
				.memoryCacheExtraOptions(maxImageWidth, maxImageHeigth).denyCacheImageMultipleSizesInMemory()
				.memoryCacheSize((int) (2 * 1024 * 1024)).memoryCacheSizePercentage(13).threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(5).defaultDisplayImageOptions(options).build();
		ImageLoader.getInstance().init(config);
		ImageLoader.getInstance().handleSlowNetwork(true);

	}

	private static Builder getDisPlayImageOptions(Context context,
			int drawableId) {
		return new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(drawableId)
				.showImageOnFail(drawableId).showImageOnLoading(drawableId);
	}

	private static Builder getDisPlayImageOptions(Context context) {
		return new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisk(true).showImageForEmptyUri(null)
				.showImageOnFail(null).showImageOnLoading(null);
	}

	public static void displayDefaultIcon(ImageView imageView, String url) {
		display(imageView, url, R.drawable.default_icon);
	}

	public static void displayDefaultBanner(ImageView imageView, String url) {
		display(imageView, url, R.drawable.default_banner);
	}

	public static void displayDefaultWaterfall(ImageView imageView, String url) {
		display(imageView, url, R.drawable.default_icon_large);
	}

	public static void displayDefaultCircleIcon(ImageView imageView, String url) {
		display(imageView, url, R.drawable.default_circle_icon);
	}

	private static void display(ImageView imageView, String url,
			int defaultImgId) {
		if (imageView == null || TextUtils.isEmpty(url)) {
			return;
		}
		if (url.equals(imageView.getTag(R.id.image_url))) {
			imageView.setTag(R.id.image_url, url);
		} else {
			imageView.setTag(R.id.image_url, url);
		}
		DisplayImageOptions options = getDisPlayImageOptions(
				imageView.getContext(), defaultImgId).cacheInMemory(true)
				.cacheOnDisk(true).build();
		ImageLoader.getInstance().displayImage(url, imageView, options, null);
	}

	public static void displayAtlas(ImageView imageView, String url,
			ImageLoadingListener listener) {
		if (imageView == null || TextUtils.isEmpty(url)) {
			return;
		}
		if (url.equals(imageView.getTag(R.id.image_url))) {
			return;
		} else {
			imageView.setTag(R.id.image_url, url);
		}
		DisplayImageOptions options = getDisPlayImageOptions(
				imageView.getContext()).cacheInMemory(true).cacheOnDisk(true)
				.build();
		ImageLoader.getInstance().displayImage(url, imageView, options,
				listener);
	}

}

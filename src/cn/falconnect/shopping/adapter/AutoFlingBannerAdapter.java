package cn.falconnect.shopping.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import cn.falconnect.shopping.banner.AutoFlingPagerAdapter;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.Goods;
import cn.falconnect.shopping.utils.ImageHelper;

public class AutoFlingBannerAdapter extends AutoFlingPagerAdapter<Goods>
		implements OnClickListener {

	private OnBannerClickListener mBannerClickListener;

	public AutoFlingBannerAdapter(OnBannerClickListener listener) {
		super();
		this.mBannerClickListener = listener;
	}

	@Override
	public View instantiateView(Context context) {
		ImageView convertView = new ImageView(context);
		convertView.setScaleType(ScaleType.FIT_XY);
		return convertView;
	}

	@Override
	public void bindView(Goods goods, View view, int position) {
		ImageView imageView = (ImageView) view;
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setTag(R.id.tag, goods);
		imageView.setOnClickListener(this);
		ImageHelper.displayDefaultBanner(imageView, goods.picUrl);
	}

	@Override
	public void onClick(View v) {
		if (mBannerClickListener != null) {
			mBannerClickListener.onClick((Goods) v.getTag(R.id.tag));
		}
	}

	@Override
	public String getTitle(int position) {
		return "";
	}

	public static interface OnBannerClickListener {
		void onClick(Goods goods);
	}

}

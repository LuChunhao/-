package cn.falconnect.shopping.adapter;

import java.util.List;

import org.aurora.library.imageloader.core.ImageLoader;
import org.aurora.library.imageloader.core.assist.FailReason;
import org.aurora.library.imageloader.core.listener.ImageLoadingListener;
import org.aurora.library.views.waterfall.PLA.alternative.widget.ScaleImageView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.cat.R.id;
import cn.falconnect.shopping.entity.Show;
import cn.falconnect.shopping.utils.ImageHelper;

public class ShowPicAdapter extends GenericListAdapter<Show>{

	public void setData(List<Show> list) {
		if (list != null && list.size() > 0) {
			this.mDataList = list;
			notifyDataSetChanged();
		}
	}

	public void addItems(List<Show> list) {
		if (list != null && list.size() > 0) {
			this.mDataList.addAll(list);
			notifyDataSetChanged();
		}
	}

	@SuppressLint("NewApi") @Override
	protected void bindView(View paramView, int position) {
		ViewHolder holder = (ViewHolder) paramView.getTag();
		Show show = getItem(position);
		holder.tvDescription.setVisibility(!TextUtils.isEmpty(show.description)? View.VISIBLE : View.INVISIBLE);
		holder.tvDescription.setText(!TextUtils.isEmpty(show.description)?show.description : "没有描述哦");
		holder.ivGoods
				.setImageHeight((int) (getWidth(paramView.getContext()) * ((float) show.height / (float) show.width)));
		holder.ivGoods.setImageWidth(getWidth(paramView.getContext()));
		ImageHelper.displayDefaultWaterfall(holder.ivGoods, show.picUrls[0]);
	}

	@Override
	protected View newView(Context context, int position) {
		View convertView = LayoutInflater.from(context).inflate(
				R.layout.show_pic_item, null);
		ViewHolder holder = new ViewHolder();
		holder.ivGoods = (ScaleImageView) convertView
				.findViewById(id.iv_show);
		holder.tvDescription = (TextView)convertView.findViewById(id.tv_description);
		convertView.setTag(holder);
		return convertView;
	}

	private static final class ViewHolder {
		private ScaleImageView ivGoods;
		private TextView tvDescription;
	}

	private int getWidth(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(metrics);
		float scale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) ((metrics.widthPixels / scale + 0.5f) - 4) / 2;
	}

}

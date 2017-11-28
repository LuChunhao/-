package cn.falconnect.shopping.adapter;

import java.util.List;

import org.aurora.library.views.waterfall.PLA.alternative.widget.ScaleImageView;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.constants.Global;
import cn.falconnect.shopping.entity.Goods;
import cn.falconnect.shopping.provider.db.CacheDAO;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.utils.ImageHelper;

public class RandomGoodsAdapter extends GenericListAdapter<Goods> implements
		OnClickListener {
	private OnCollectClickListener mOnCollectClickListener;

	public RandomGoodsAdapter(OnCollectClickListener listener) {
		super();
		this.mOnCollectClickListener = listener;
	}

	public void setData(List<Goods> list) {
		if (list != null && list.size() > 0) {
			this.mDataList = list;
			notifyDataSetChanged();
		}
	}

	public void addItems(List<Goods> list) {
		if (list != null && list.size() > 0) {
			this.mDataList.addAll(list);
			notifyDataSetChanged();
		}
	}

	@Override
	protected void bindView(View paramView, int position) {
		ViewHolder holder = (ViewHolder) paramView.getTag();
		Goods goods = getItem(position);
		holder.tvName.setText(goods.name);
		holder.tvDiscount.setText(goods.discount);
		holder.tvPrice.setText(paramView.getResources().getString(
				R.string.goods_price, String.valueOf(goods.currentPrice)));
		holder.ivGoods
				.setImageHeight((int) (getWidth(paramView.getContext()) * ((float) goods.height / (float) goods.width)));
		holder.ivGoods.setImageWidth(getWidth(paramView.getContext()));
		ImageHelper.displayDefaultWaterfall(holder.ivGoods, goods.picUrl);
		boolean isLogin = ProviderFatory.getUserProvider().isLogined(
				paramView.getContext());
		if (isLogin) {
			String sign = ProviderFatory.getUserProvider().getLoginName(
					paramView.getContext());
			boolean exist = CacheDAO.getInstance().isGoodsExist(
					goods.getGoodsId(), Global.COLLECTS_CACHE_TYPE, sign);
			holder.ivCollect
					.setImageResource(exist ? R.drawable.ic_collects_1_focus
							: R.drawable.ic_collects_1);
			goods.isCollected = exist;
		} else {
			holder.ivCollect.setImageResource(R.drawable.ic_collects_1);
		}
		holder.ivCollect.setTag(R.id.tag, goods);
		holder.ivCollect.setOnClickListener(this);
	}

	@Override
	protected View newView(Context context, int position) {
		View convertView = LayoutInflater.from(context).inflate(
				R.layout.random_goods_item, null);
		ViewHolder holder = new ViewHolder();
		holder.ivCollect = (ImageView) convertView
				.findViewById(R.id.iv_collect);
		holder.ivGoods = (ScaleImageView) convertView
				.findViewById(R.id.iv_goods);
		holder.tvPrice = (TextView) convertView
				.findViewById(R.id.tv_goods_price);
		holder.tvDiscount = (TextView) convertView
				.findViewById(R.id.tv_goods_discount);
		holder.tvName = (TextView) convertView.findViewById(R.id.tv_goods_name);
		convertView.setTag(holder);
		return convertView;
	}

	@Override
	public void onClick(View v) {
		if (mOnCollectClickListener != null) {
			ImageView imageView = (ImageView) v;
			mOnCollectClickListener.onClick(((Goods) v.getTag(R.id.tag)),
					imageView);
		}
	}

	private static final class ViewHolder {
		private ImageView ivCollect;
		private ScaleImageView ivGoods;
		private TextView tvName;
		private TextView tvPrice;
		private TextView tvDiscount;
	}

	public static interface OnCollectClickListener {
		void onClick(Goods goods, ImageView imageView);
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

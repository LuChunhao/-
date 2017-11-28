package cn.falconnect.shopping.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.Goods;
import cn.falconnect.shopping.utils.ImageHelper;

public class CollectsAdapter extends GenericListAdapter<Goods> {

	public CollectsAdapter() {
		super();
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
		ImageHelper.displayDefaultIcon(holder.ivPic, goods.picUrl);
		holder.tvGoodsName.setText(goods.name);
		holder.tvDiscount.setText(goods.discount);
		holder.tvCurrentPrice.setText(paramView.getContext().getString(
				R.string.goods_price, goods.currentPrice));
		holder.tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		holder.tvOriginalPrice.setText(paramView.getContext().getString(
				R.string.goods_price, goods.originalPrice));
	}

	@Override
	protected View newView(Context context, int position) {
		View convertView = LayoutInflater.from(context).inflate(
				R.layout.cache_list_item, null);
		ViewHolder holder = new ViewHolder();
		holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
		holder.tvDiscount = (TextView) convertView
				.findViewById(R.id.tv_discount);
		holder.tvGoodsName = (TextView) convertView
				.findViewById(R.id.tv_goods_name);
		holder.tvCurrentPrice = (TextView) convertView
				.findViewById(R.id.tv_current_price);
		holder.tvOriginalPrice = (TextView) convertView
				.findViewById(R.id.tv_original_price);
		convertView.setTag(holder);
		return convertView;
	}

	private static final class ViewHolder {
		private ImageView ivPic;
		private TextView tvGoodsName;
		private TextView tvDiscount;
		private TextView tvCurrentPrice;
		private TextView tvOriginalPrice;
	}
}

package cn.falconnect.shopping.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.Goods;
import cn.falconnect.shopping.utils.ImageHelper;

public class HomeGoodsAdapter extends GenericListAdapter<Goods> {

	public HomeGoodsAdapter() {
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
		ImageHelper.displayDefaultIcon(holder.ivGoodsPic, goods.picUrl);
		holder.tvGoodsName.setText(goods.name);
		holder.tvCurrentPrice.setText(paramView.getContext().getString(
				R.string.goods_price, goods.currentPrice));
		holder.tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		holder.tvOriginalPrice.setText(paramView.getContext().getString(
				R.string.goods_price, goods.originalPrice));
		holder.tvSalesVolume.setText("销量" + goods.salesVolume);
	}

	@Override
	protected View newView(Context context, int position) {
		View convertView = LayoutInflater.from(context).inflate(
				R.layout.home_goods_list_item, null);
		ViewHolder holder = new ViewHolder();
		holder.ivGoodsPic = (ImageView) convertView
				.findViewById(R.id.iv_goods_pic);
		holder.tvGoodsName = (TextView) convertView
				.findViewById(R.id.tv_goods_name);
		holder.tvCurrentPrice = (TextView) convertView
				.findViewById(R.id.tv_current_price);
		holder.tvOriginalPrice = (TextView) convertView
				.findViewById(R.id.tv_original_price);
		holder.tvSalesVolume = (TextView) convertView
				.findViewById(R.id.tv_sales_volume);
		convertView.setTag(holder);
		return convertView;
	}

	private static final class ViewHolder {
		private ImageView ivGoodsPic;
		private TextView tvGoodsName;
		private TextView tvCurrentPrice;
		private TextView tvOriginalPrice;
		private TextView tvSalesVolume;
	}
}

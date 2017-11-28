package cn.falconnect.shopping.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.GoodsType;
import cn.falconnect.shopping.widget.CircleImageView;

public class AllTypeAdapter extends BaseAdapter {
	private List<GoodsType> mGoodsTypes;

	public AllTypeAdapter() {
		mGoodsTypes = new ArrayList<GoodsType>();
	}

	public void setData(List<GoodsType> goodsTypes) {
		mGoodsTypes = goodsTypes;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mGoodsTypes.size();
	}

	@Override
	public GoodsType getItem(int position) {
		return mGoodsTypes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.grid_all_item, null);
			viewHolder.tv_type = (TextView) convertView
					.findViewById(R.id.tv_search_type);
			viewHolder.iv_type_color = (CircleImageView)convertView.findViewById(R.id.iv_type_color);
			viewHolder.iv_type_color.setScaleType(ScaleType.CENTER_CROP);
			WindowManager windowManager = (WindowManager) convertView
					.getContext().getSystemService(Context.WINDOW_SERVICE);
			int width = windowManager.getDefaultDisplay().getWidth();
			viewHolder.tv_type.setLayoutParams(new RelativeLayout.LayoutParams(
					width / 3, width / 3));
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		GoodsType goodsType = getItem(position);
		viewHolder.bindView(goodsType);
		return convertView;
	}

	class ViewHolder {
		CircleImageView iv_type_color;
		TextView tv_type;

		@SuppressWarnings("deprecation")
		public void bindView(final GoodsType goodsType) {
			tv_type.setText(goodsType.name);
			Bitmap bitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(Color.parseColor(goodsType.color));
			canvas.drawCircle(0, 0, 200, paint);
			iv_type_color.setImageBitmap(bitmap);
		}
	}
}

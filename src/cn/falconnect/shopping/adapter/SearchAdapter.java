package cn.falconnect.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.falconnect.shopping.cat.R;

public class SearchAdapter extends BaseAdapter {
	private List<String> mGoodsTypes;

	public SearchAdapter() {
		mGoodsTypes = new ArrayList<String>();
	}

	public void setData(List<String> goodsTypes) {
		mGoodsTypes = goodsTypes;
		notifyDataSetChanged();
	}

	public void clear() {
		mGoodsTypes.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mGoodsTypes.size();
	}

	@Override
	public String getItem(int position) {
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
					R.layout.grid_search_item, null);
			viewHolder.tv_type = (TextView) convertView
					.findViewById(R.id.tv_search);
			WindowManager windowManager = (WindowManager) convertView
					.getContext().getSystemService(Context.WINDOW_SERVICE);
			int width = windowManager.getDefaultDisplay().getWidth() / 3;
			viewHolder.tv_type.setLayoutParams(new RelativeLayout.LayoutParams(
					width, 80));
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String goodsType = getItem(position);
		viewHolder.bindView(goodsType);
		return convertView;
	}

	class ViewHolder {
		TextView tv_type;

		@SuppressWarnings("deprecation")
		public void bindView(final String goodsType) {
			tv_type.setText(goodsType);
		}
	}
}

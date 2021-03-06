package cn.falconnect.shopping.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.ImageItem;
import cn.falconnect.shopping.ui.BitmapCache;
import cn.falconnect.shopping.ui.BitmapCache.ImageCallback;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageGridAdapter extends BaseAdapter {

	private TextCallback textcallback = null;
	final String TAG = getClass().getSimpleName();
	List<ImageItem> dataList = new ArrayList<ImageItem>();
	private Map<String, ImageItem> map = new HashMap<String, ImageItem>();
	BitmapCache cache;
	private Handler mHandler;
	private ArrayList<ImageItem> mSelectedItems;
	ImageCallback callback = new ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				} else {
//					Log.e(TAG, "callback, bmp not match");
				}
			} else {
//				Log.e(TAG, "callback, bmp null");
			}
		}
	};

	public static interface TextCallback {
		public void onListen(int count);
	}

	public void setTextCallback(TextCallback listener) {
		textcallback = listener;
	}

	public ImageGridAdapter(List<ImageItem> list, Handler mHandler,ArrayList<ImageItem> selectedItems) {
		dataList.addAll(list);
		cache = new BitmapCache();
		this.mHandler = mHandler;
		mSelectedItems = selectedItems;
	}
	
	public ArrayList<ImageItem> getSelectedData(){
		return mSelectedItems;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (dataList != null) {
			count = dataList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class Holder {
		private ImageView iv;
		private ImageView selected;
		private TextView text;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;

		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(parent.getContext(), R.layout.item_image_grid, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.image);
			holder.selected = (ImageView) convertView
					.findViewById(R.id.isselected);
			holder.text = (TextView) convertView
					.findViewById(R.id.item_image_grid_text);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final ImageItem item = dataList.get(position);

		holder.iv.setTag(item.imagePath);
		cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath,
				callback);
		if (item.isSelected) {
			holder.selected.setImageResource(R.drawable.icon_data_select);  
			holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
		} else {
			holder.selected.setImageResource(-1);
			holder.text.setBackgroundColor(0x00000000);
		}
		holder.iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String path = dataList.get(position).imagePath;

				if (mSelectedItems.size() < 7) {
					item.isSelected = !item.isSelected;
					if (item.isSelected) {
						holder.selected
								.setImageResource(R.drawable.icon_data_select);
						holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
						mSelectedItems.add(0,item);
						if (textcallback != null)
							textcallback.onListen(mSelectedItems.size()-1);
						getMap().put(path, item);

					} else if (!item.isSelected) {
						holder.selected.setImageResource(-1);
						holder.text.setBackgroundColor(0x00000000);
						mSelectedItems.remove(item);
						if (textcallback != null)
							textcallback.onListen(mSelectedItems.size()-1);
						getMap().remove(path);
					}
				} else if (mSelectedItems.size() > 6) {
					if (item.isSelected == true) {
						item.isSelected = !item.isSelected;
						holder.selected.setImageResource(-1);
						if (textcallback != null)
							textcallback.onListen(mSelectedItems.size()-2);
						mSelectedItems.remove(item);
						getMap().remove(path);
					} else {
						Message message = Message.obtain(mHandler, 0);
						message.sendToTarget();
					}
				}
			}

		});

		return convertView;
	}

	public Map<String, ImageItem> getMap() {
		return map;
	}

	public void setMap(Map<String, ImageItem> map) {
		this.map = map;
	}

}

package cn.falconnect.shopping.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class GenericListAdapter<T> extends BaseAdapter {
	protected List<T> mDataList;

	public GenericListAdapter() {
		mDataList = new ArrayList<T>();
	}

	public List<T> getList() {
		return mDataList;
	}

	public void clear() {
		mDataList.clear();
		notifyDataSetChanged();
	}

	public void remove(T t) {
		if (t != null) {
			mDataList.remove(t);
			notifyDataSetChanged();
		}
	}

	public void addItem(T t) {
		if (t != null) {
			mDataList.add(t);
			notifyDataSetChanged();
		}
	}

	public void addItem(int position, T t) {
		if ((t != null) && (position >= 0 && position < getCount())) {
			mDataList.add(position, t);
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public T getItem(int position) {
		T t = null;
		if (position >= 0 && position < getCount()) {
			t = mDataList.get(position);
		}
		return t;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = newView(parent.getContext(), position);
		}
		bindView(convertView, position);
		return convertView;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	protected abstract void bindView(View paramView, int position);

	@Override
	public boolean hasStableIds() {
		return true;
	}

	public boolean isEmpty() {
		return mDataList.isEmpty();
	}

	protected abstract View newView(Context context, int position);
}

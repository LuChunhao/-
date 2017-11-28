package cn.falconnect.shopping.ui;

import java.io.Serializable;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import cn.falconnect.shopping.adapter.ImageBucketAdapter;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.ImageBucket;
import cn.falconnect.shopping.utils.AlbumHelper;

public class AlbumFragment extends SlidingExitFragment {
	public static final String FRAGMENT_TAG = "AlbumFragment";
	List<ImageBucket> mDataList;
	GridView mGridView;
	ImageBucketAdapter mAdapter;// 自定义的适配器
	AlbumHelper mHelper;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	public static Bitmap bimap;
	
	@Override
	protected View onChildCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_image_bucket, null);
		mHelper = AlbumHelper.getHelper();
		mHelper.init(getActivity().getApplicationContext());
		initData();
		initViews(contentView);
		
		return contentView;
	}

	@Override
	protected String getTDPageName() {
		return getString(R.string.album);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mDataList = mHelper.getImagesBucketList(false);	
		bimap=BitmapFactory.decodeResource(
				getResources(),
				R.drawable.icon_addpic_unfocused);
	}
	private void initViews(View contentView) {
		mGridView = (GridView)contentView.findViewById(R.id.gridview);
		mAdapter = new ImageBucketAdapter(mDataList);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Bundle bundle = getArguments();
				bundle.putSerializable(AlbumPhotosFragment.EXTRA_IMAGE_LIST, (Serializable)mDataList.get(position).imageList);
				startFragment(new AlbumPhotosFragment(), bundle,AlbumPhotosFragment.class.getName(),AlbumPhotosFragment.FRAGMENT_TAG);
			}
		});
		TextView tv_close_album = (TextView)contentView.findViewById(R.id.tv_close_album);
		tv_close_album.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finishFragment();
			}
		});
	}

}

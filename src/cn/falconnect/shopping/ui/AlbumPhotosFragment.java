package cn.falconnect.shopping.ui;

import java.util.ArrayList;
import java.util.List;

import org.aurora.library.views.Toaster;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import cn.falconnect.shopping.adapter.ImageGridAdapter;
import cn.falconnect.shopping.adapter.ImageGridAdapter.TextCallback;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.ImageItem;
import cn.falconnect.shopping.ui.topic.PublishShowFragment;
import cn.falconnect.shopping.utils.AlbumHelper;

public class AlbumPhotosFragment extends SlidingExitFragment {
	public static final String FRAGMENT_TAG = "AlbumPhotosFragment";
	List<ImageItem> mDataList;
	GridView mGridView;
	ImageGridAdapter mAdapter;// 自定义的适配器
	AlbumHelper mHelper;
	private Button btn_choose;
	public static final String EXTRA_IMAGE_LIST = "imagelist";
	
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				Toaster.toast("最多选择6张图片");
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected View onChildCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_image_grid, null);
		mHelper = AlbumHelper.getHelper();
		mHelper.init(getActivity().getApplicationContext());
		mDataList = (List<ImageItem>) getArguments().getSerializable(EXTRA_IMAGE_LIST);
		initViews(contentView);
		return contentView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected String getTDPageName() {
		return getString(R.string.album);
	}

	private void initViews(View contentView) {
		ArrayList<ImageItem> selectedList = (ArrayList<ImageItem>) getArguments().getSerializable("image");
		mGridView = (GridView) contentView.findViewById(R.id.gridview_grid);
		mAdapter = new ImageGridAdapter(mDataList, mHandler,selectedList);
		mAdapter.setTextCallback(new TextCallback() {

			@Override
			public void onListen(int count) {
				btn_choose.setText("完成" + "(" +count +")");
			}
		});
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mAdapter.notifyDataSetChanged();
			}
		});
		btn_choose = (Button) contentView.findViewById(R.id.bt);
		btn_choose.setText("完成" + "(" +(selectedList.size()-1) +")");
		btn_choose.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				PublishShowFragment publishShowFragment = (PublishShowFragment) getActivity().getSupportFragmentManager()
						.findFragmentByTag(PublishShowFragment.FRAGMENT_TAG);
				if (publishShowFragment != null) {
					publishShowFragment.setImageItemList(mAdapter.getSelectedData());
				}
				finishFragment(AlbumFragment.class.getName());
				finishFragment();
			}

		});
		TextView tv_close_album = (TextView)contentView.findViewById(R.id.tv_close_album_photos);
		tv_close_album.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finishFragment();
			}
		});
	}
	
}

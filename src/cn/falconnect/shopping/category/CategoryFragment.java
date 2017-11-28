package cn.falconnect.shopping.category;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import cn.falconnect.shopping.adapter.AllTypeAdapter;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.constants.Global;
import cn.falconnect.shopping.entity.GoodsType;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.ui.BaseFragment;

public class CategoryFragment extends BaseFragment {
	public static final String FRAGMENT_TAG = "CategoryFragment";
	private View mContentView;
	private AllTypeAdapter mAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_category, null);
			initViews();
			getAllLabels(mContentView.getContext());
		}
		return mContentView;
	}

	@Override
	protected String getTDPageName() {
		return getString(R.string.category);
	}

	private void initViews() {
		GridView gv_type = (GridView) mContentView
				.findViewById(R.id.gv_all_types);
		mAdapter = new AllTypeAdapter();
		gv_type.setAdapter(mAdapter);
		gv_type.setOnItemClickListener(clickListener);
	}

	private void getAllLabels(Context context) {
		showLoadingPage();
		ProviderFatory.getGoodsProvider().obtainRandomLabel(context,
				new ObtainListener<List<GoodsType>>() {

					@Override
					public void onSucceed(Context context,
							List<GoodsType> result) {
						closeLoadingPage();
						mAdapter.setData(result);
					}

					@Override
					public void onFinished(Context context, ResultCode code) {
						
					}

					@Override
					public void onError(Context context, ResultCode code) {
						setLoadFailedMessage(code.msg);
					}
				});

	}

	private OnItemClickListener clickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (mAdapter.getItem(position).id != GoodsType.NO_ID) {
				Bundle bundle = new Bundle();
				bundle.putInt(Global.BundleKey.ID, mAdapter.getItem(position).id);
				bundle.putString(Global.BundleKey.GOODS_TYPE_NAME,
						mAdapter.getItem(position).name);
				startFragment(new SpecialResultFragment(), bundle);
			}
		}
	};
	
	public void onResume() {
		super.onResume();
		mAdapter.notifyDataSetChanged();
	};
}

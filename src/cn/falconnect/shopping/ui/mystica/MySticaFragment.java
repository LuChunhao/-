package cn.falconnect.shopping.ui.mystica;

import static cn.falconnect.shopping.constants.Global.BundleKey.GOODS_BEAN;

import org.aurora.library.views.Toaster;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.Goods;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.ui.BaseFragment;
import cn.falconnect.shopping.ui.GoodsDetailFragment;
import cn.falconnect.shopping.utils.DialogUtil;
import cn.falconnect.shopping.widget.LocusPassWordView;
import cn.falconnect.shopping.widget.LocusPassWordView.OnCompleteListener;

public class MySticaFragment extends BaseFragment {
	public static final String FRAGMENT_TAG = "MysticaFragment";
	private View mContentView;
	private LocusPassWordView mPassWordView;
	private Dialog mDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.fragment_mystica, null);
			initViews(mContentView.getContext());
		}
		return mContentView;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mPassWordView != null) {
			mPassWordView.clearPassword();
		}
	}
	
	@Override
	public void onDestroy() {
		if (mPassWordView != null) {
			mPassWordView.clearPassword();
		}
		super.onDestroy();
	}
	
	@Override
	protected String getTDPageName() {
		return getString(R.string.mystica);
	}

	private void initViews(final Context context) {
		mPassWordView = (LocusPassWordView) mContentView
				.findViewById(R.id.lpwv_find);
		mPassWordView.setOnCompleteListener(new OnCompleteListener() {

			@Override
			public void onComplete(String password) {
				mDialog = DialogUtil.showProgressBar(context, "");
				mDialog.setCancelable(true);
				mDialog.setOnCancelListener(new OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						mPassWordView.clearPassword();
					}
				});
				mDialog.show();
				findSomething(mContentView.getContext());
			}
		});
	}

	private void findSomething(Context context) {
		ProviderFatory.getGoodsProvider().findSomething(context,
				new ObtainListener<Goods>() {

					@Override
					public void onSucceed(Context context, Goods result) {
						if (mDialog != null && mDialog.isShowing()) {
							mDialog.dismiss();
						}
						if (result != null) {
							if (!TextUtils.isEmpty(result.detailUrl)) {
								Bundle args = new Bundle();
								args.putSerializable(GOODS_BEAN, result);
								startFragment(new GoodsDetailFragment(), args,
										GoodsDetailFragment.FRAGMENT_TAG);
								mPassWordView.clearPassword();
							}else {
								mPassWordView.clearPassword();
							}
						} else {
							mPassWordView.clearPassword();
							Toaster.toast("很抱歉，什么都没找到!");
						}

					}

					@Override
					public void onFinished(Context context, ResultCode code) {
						
					}

					@Override
					public void onError(Context context, ResultCode code) {
						
					}
				});
	}
}

package cn.falconnect.shopping.ui.user;

import static cn.falconnect.shopping.constants.Global.BundleKey.SHOW_BEAN;

import java.util.List;

import org.aurora.library.views.Toaster;
import org.aurora.library.views.text.MarqueeTextView;
import org.aurora.library.views.waterfall.PLA.lib.internal.PLA_AdapterView;
import org.aurora.library.views.waterfall.PLA.view.PlaWaterfallListView;
import org.aurora.library.views.waterfall.PLA.view.PlaWaterfallListView.IXListViewListener;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.falconnect.shopping.adapter.ShowPicAdapter;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.Show;
import cn.falconnect.shopping.entity.User;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.ui.SlidingExitFragment;
import cn.falconnect.shopping.ui.topic.OtherTopicMainDetailFragment;
import cn.falconnect.shopping.ui.topic.TopicMainDetailFragment;
import cn.falconnect.shopping.utils.ImageHelper;
import cn.falconnect.shopping.widget.CircleImageView;

public class OtherUserCenterFragment extends SlidingExitFragment
		implements
		org.aurora.library.views.waterfall.PLA.lib.internal.PLA_AdapterView.OnItemClickListener {
	public static final String FRAGMENT_TAG = "OtherUserCenterFragment";
	private View mContentView;
	private Show mShowBean;
	private ShowPicAdapter mShowAdapter;
	private PlaWaterfallListView other_show_list;
	private ImageView iv_back;
	private String nickname = "猫奴";
	private String headUrl = "null";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			mShowBean = (Show) args.getSerializable(SHOW_BEAN);
		}
	}

	private void getUserInfo() {
		ProviderFatory.getUserProvider().getUserInfo(mContentView.getContext(),
				new ObtainListener<User>() {

					@Override
					public void onSucceed(Context context, User result) {
						if (result != null) {
							int uid = ProviderFatory.getUserProvider()
									.getUserId(context);
							tv_other_center_name.setText(!TextUtils
									.isEmpty(result.nickName) ? result.nickName
									+ "的晒晒" : getActivity().getResources()
									.getString(R.string.sub_app_name)
									+ uid
									+ "的晒晒");
							nickname = result.nickName;
							headUrl = result.headUrl;
							ImageHelper.displayDefaultCircleIcon(
									iv_center_head, result.headUrl);
							ImageHelper.displayDefaultIcon(iv_centerbackgroud,
									result.backgroundUrl);
						}
					}

					@Override
					public void onFinished(Context context, ResultCode code) {

					}

					@Override
					public void onError(Context context, ResultCode code) {

					}
				}, mShowBean.uid);
	}

	@Override
	protected View onChildCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(R.layout.otherusercenterfragment,
					null);
			initViews();
			getMyShow(mContentView.getContext(), true);
			getUserInfo();
		}
		return mContentView;
	}

	private void initViews() {
		iv_back = (ImageView) mContentView.findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finishFragment();
			}
		});
		tv_other_center_name = (TextView) mContentView
				.findViewById(R.id.tv_other_center_name);
		iv_centerbackgroud = (ImageView) mContentView
				.findViewById(R.id.iv_centerbackgroud);
		iv_center_head = (CircleImageView) mContentView
				.findViewById(R.id.iv_center_head);
		MarqueeTextView tv_other_nickName = (MarqueeTextView) mContentView
				.findViewById(R.id.tv_other_nickName);
		ImageHelper.displayDefaultCircleIcon(iv_center_head, mShowBean.headUrl);
		ImageHelper.displayDefaultIcon(iv_centerbackgroud,
				mShowBean.backgroundUrl);
		tv_other_nickName.setText(mShowBean.nickName);
		tv_other_center_name.setText(mShowBean.nickName + "的空间");
		other_show_list = (PlaWaterfallListView) mContentView
				.findViewById(R.id.other_show_list);
		other_show_list.setPullLoadEnable(false);
		mShowAdapter = new ShowPicAdapter();
		other_show_list.setAdapter(mShowAdapter);
		other_show_list.setXListViewListener(mIXListener);
		other_show_list.setOnItemClickListener(this);
	}

	private void getMyShow(Context context, final boolean isRefresh) {
		if (mShowAdapter.getCount() == 0) {
			showLoadingPage(R.id.my_article_container);
		}
		int offset = isRefresh ? 0 : mShowAdapter.getCount();
		ProviderFatory.getShowProvider().getMyShow(context, offset,
				REQUEST_SIZE, mShowBean.uid, new ObtainListener<List<Show>>() {
					@Override
					public void onSucceed(Context context, List<Show> result) {
						if (result != null && result.size() > 0) {
							closeLoadingPage();
							if (isRefresh || mShowAdapter.getCount() == 0) {
								mShowAdapter.setData(result);
							} else {
								mShowAdapter.addItems(result);
							}
						} else {
							onError(context, ResultCode.NO_MORE_DATA);
						}
						other_show_list.setPullLoadEnable(result != null
								&& result.size() >= REQUEST_SIZE);
					}

					@Override
					public void onFinished(Context context, ResultCode code) {
						other_show_list.stopLoadMore();
						other_show_list.stopRefresh();
					}

					@Override
					public void onError(Context context, ResultCode code) {
						if (mShowAdapter.getCount() == 0) {
							setLoadFailedMessage(code.msg);
						} else {
							Toaster.toast(code.msg);
						}
					}
				});
	}

	public void onUpdate() {
		if (mShowAdapter.getCount() == 1) {
			mShowAdapter.clear();
		}
		mIXListener.onRefresh(other_show_list);
	}

	private IXListViewListener mIXListener = new IXListViewListener() {

		@Override
		public void onRefresh(PlaWaterfallListView xListView) {
			getMyShow(xListView.getContext(), true);
		}

		@Override
		public void onLoadMore(PlaWaterfallListView xListView) {
			getMyShow(xListView.getContext(), false);
		}
	};
	private ImageView iv_centerbackgroud;
	private CircleImageView iv_center_head;
	private TextView tv_other_center_name;

	@Override
	public void onItemClick(PLA_AdapterView<?> parent, View view, int position,
			long id) {
		Show show = (Show) parent.getItemAtPosition(position);
		Bundle args = new Bundle();
		args.putSerializable(SHOW_BEAN, show);
		show.nickName = nickname;
		show.headUrl = headUrl;
		startFragment(new OtherTopicMainDetailFragment(), args);
	}

}

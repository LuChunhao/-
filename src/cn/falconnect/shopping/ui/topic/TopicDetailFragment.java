package cn.falconnect.shopping.ui.topic;

import static cn.falconnect.shopping.constants.Global.BundleKey.ID;
import static cn.falconnect.shopping.constants.Global.BundleKey.SHOW_BEAN;

import org.aurora.library.views.waterfall.PLA.lib.internal.PLA_AdapterView;
import org.aurora.library.views.waterfall.PLA.lib.internal.PLA_AdapterView.OnItemClickListener;
import org.aurora.library.views.waterfall.PLA.view.PlaWaterfallListView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.falconnect.shopping.adapter.GenericListAdapter;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.Show;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.ui.SlidingExitFragment;
import cn.falconnect.shopping.ui.comment.TopicCommentsFragment;
import cn.falconnect.shopping.ui.user.OtherUserCenterFragment;
import cn.falconnect.shopping.utils.ImageHelper;
import cn.falconnect.shopping.widget.CircleImageView;

public class TopicDetailFragment extends SlidingExitFragment {
	public static final String FRAGMENT_TAG = "TopicDetailFragment";
	private Show mShowBean;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			mShowBean = (Show) args.getSerializable(SHOW_BEAN);
		}
	}

	@Override
	protected View onChildCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_topic_detail,
				null);
		initViews(contentView);
		initDrawerLayout();
		return contentView;
	}

	private void initViews(View view) {
		CircleImageView avatarImageView = (CircleImageView) view
				.findViewById(R.id.iv_avatar);
		TextView nickNameTextView = (TextView) view
				.findViewById(R.id.tv_nick_name);
		TextView postContentTextView = (TextView) view
				.findViewById(R.id.tv_post_content);
		ImageHelper
				.displayDefaultCircleIcon(avatarImageView, mShowBean.headUrl);
		int uid = ProviderFatory.getUserProvider().getUserId(getActivity());
		avatarImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle args = new Bundle();
				args.putSerializable(SHOW_BEAN, mShowBean);
				int uid = ProviderFatory.getUserProvider().getUserId(
						getActivity());
				if (uid != mShowBean.uid.intValue()) {
					startFragment(new OtherUserCenterFragment(), args);
				}
			}
		});
		nickNameTextView
				.setText(!TextUtils.isEmpty(mShowBean.nickName) ? mShowBean.nickName
						: getActivity().getResources().getString(
								R.string.sub_app_name)
								+ uid);
		postContentTextView.setText(mShowBean.description);
		postContentTextView.setVisibility(!TextUtils
				.isEmpty(mShowBean.description) ? View.VISIBLE : View.GONE);
		PlaWaterfallListView picWaterFall = (PlaWaterfallListView) view
				.findViewById(R.id.show_list);
		picWaterFall.setOnItemClickListener(mOnItemClickListener);
		picWaterFall.setPullEnable(false);
		PictureAdapter adapter = new PictureAdapter(mShowBean.picUrls);
		picWaterFall.setAdapter(adapter);
	}

	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(PLA_AdapterView<?> parent, View view,
				int position, long id) {
			Bundle args = new Bundle();
			args.putSerializable(SHOW_BEAN, mShowBean);
			args.putInt("position", (int) id);
			startFragment(new TopicAtlasDetailFragment(), args);
		}

	};

	private static class PictureAdapter extends GenericListAdapter<String> {
		private String[] mDataUrls;

		public PictureAdapter(String[] urls) {
			this.mDataUrls = urls;
		}

		@Override
		public String getItem(int position) {
			return mDataUrls[position];
		}

		@Override
		public int getCount() {
			return mDataUrls.length > 6 ? 6 : mDataUrls.length;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		protected void bindView(View paramView, int position) {
			final ViewHolder holder = (ViewHolder) paramView.getTag();
			String picUrl = getItem(position);
			ImageHelper.displayDefaultIcon(holder.ivPic, picUrl);
		}

		@Override
		protected View newView(Context context, int position) {
			View convertView = LayoutInflater.from(context).inflate(
					R.layout.detail_pic_item, null);
			ViewHolder holder = new ViewHolder();
			holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_show);
			convertView.setTag(holder);
			return convertView;
		}

		private static final class ViewHolder {
			private ImageView ivPic;
		}
	}

	private TopicCommentsFragment createDrawerFragment() {
		TopicCommentsFragment frag = new TopicCommentsFragment();
		Bundle args = new Bundle();
		args.putInt(ID, mShowBean.id);
		frag.setArguments(args);
		return frag;
	}

	private void initDrawerLayout() {
		TopicCommentsFragment drawerFragment = createDrawerFragment();
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.comments_layout, drawerFragment,
						TopicCommentsFragment.FRAGMENT_TAG)
				.commitAllowingStateLoss();
	}
}

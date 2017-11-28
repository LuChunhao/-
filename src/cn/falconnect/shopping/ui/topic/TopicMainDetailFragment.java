package cn.falconnect.shopping.ui.topic;

import static cn.falconnect.shopping.constants.Global.BundleKey.ID;
import static cn.falconnect.shopping.constants.Global.BundleKey.SHOW_BEAN;

import org.aurora.library.views.Toaster;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.constants.Global.CommentType;
import cn.falconnect.shopping.entity.Show;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.ui.HomeFragment;
import cn.falconnect.shopping.ui.SlidingExitFragment;
import cn.falconnect.shopping.ui.comment.TopicCommentsFragment;
import cn.falconnect.shopping.ui.user.PersonalCenterFragment;
import cn.falconnect.shopping.utils.DialogUtil;
import cn.falconnect.shopping.utils.SoftInput;

public class TopicMainDetailFragment extends SlidingExitFragment {
	public static final String FRAGMENT_TAG = "TopicDetailFragment";
	private Show mShowBean;
	private EditText mEtContent;
	private ImageView mBtnComment;
	private Dialog mDialog;
	private View mFloatView;
	private View mBottomView;

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
		View contentView = inflater.inflate(
				R.layout.fragment_main_topic_detail, null);
		initViews(contentView);
		initContentLayout();
		return contentView;
	}

	private void initViews(View contentView) {
		ImageView selfDeleteShowView = (ImageView) contentView
				.findViewById(R.id.iv_delete_show);
		selfDeleteShowView.setOnClickListener(mOnClickListener);
		boolean isLogined = ProviderFatory.getUserProvider().isLogined(
				getActivity());
		if (isLogined) {
			String currentUserIdString = String.valueOf(ProviderFatory
					.getUserProvider().getUserId(getActivity()));
			selfDeleteShowView
					.setVisibility(TextUtils.equals(
							String.valueOf(mShowBean.uid), currentUserIdString) ? View.VISIBLE
							: View.GONE);
		}
		mBottomView = contentView.findViewById(R.id.ll_bottom_btn);
		mBottomView.setOnClickListener(mOnClickListener);
		mFloatView = contentView.findViewById(R.id.float_comment_layout);
		mEtContent = (EditText) contentView.findViewById(R.id.comment_edit);
		mEtContent.addTextChangedListener(mTextChangedListener);
		mBtnComment = (ImageView) contentView.findViewById(R.id.btn_comment);
		mBtnComment.setEnabled(false);
		mBtnComment.setOnClickListener(mOnClickListener);


	}

	private TextWatcher mTextChangedListener = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable word) {
			mBtnComment
					.setEnabled(!TextUtils.isEmpty(word.toString().trim()) ? true
							: false);
		}
	};

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_delete_show:
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
				}
				mDialog = DialogUtil.showChoiceDialog(getActivity(), null,
						"确定要删除此晒晒？", "点错了", "删定了", mLogoutClickListener);
				mDialog.show();

				break;
			case R.id.ll_bottom_btn:
				mBottomView.setVisibility(View.GONE);
				mFloatView.setVisibility(View.VISIBLE);
				break;
			case R.id.btn_comment:
				String content = mEtContent.getText().toString();
				mDialog = DialogUtil
						.showProgressBar(getActivity(), "正在提交评论...");
				mDialog.show();
				ProviderFatory.getFeedbackProvider().postFeedback(
						v.getContext(), CommentType.COMMENT_TOPIC,
						mShowBean.id, content, new ObtainListener<Void>() {
							@Override
							public void onSucceed(Context context, Void result) {
								Toaster.toast(R.string.commit_succed);
							}

							@Override
							public void onFinished(Context context,
									ResultCode code) {
								if (mDialog != null && mDialog.isShowing()) {
									mDialog.dismiss();
								}
								mEtContent.setText("");
								TopicCommentsFragment commentsFragment = (TopicCommentsFragment) getActivity()
										.getSupportFragmentManager()
										.findFragmentByTag(
												TopicCommentsFragment.FRAGMENT_TAG);
								if (commentsFragment != null) {
									commentsFragment.onUpdate();
								}
							}

							@Override
							public void onError(Context context, ResultCode code) {
								Toaster.toast(R.string.commit_falied);
							}
						});
				break;
			default:
				break;
			}
		}
	};

	private OnClickListener mLogoutClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.dismiss();
			}
			switch ((Integer) v.getTag()) {
			case DialogUtil.LEFT_TAG:
				break;
			case DialogUtil.RIGHT_TAG:
				mDialog = DialogUtil.showProgressBar(getActivity(), "晒晒删除中");
				mDialog.setCancelable(true);
				mDialog.show();
				ProviderFatory.getShowProvider().deleteMyShow(v.getContext(),
						mShowBean.id, new ObtainListener<Void>() {

							@Override
							public void onSucceed(Context context, Void result) {
								Toaster.toast(R.string.delete_success);
							}

							@Override
							public void onError(Context context, ResultCode code) {
								Toaster.toast(R.string.delete_failed);
							}

							@Override
							public void onFinished(Context context,
									ResultCode code) {
								if (mDialog != null && mDialog.isShowing()) {
									mDialog.dismiss();
								}
								HomeFragment homeFragment = (HomeFragment) getActivity()
										.getSupportFragmentManager()
										.findFragmentByTag(
												HomeFragment.FRAGEMNT_TAG);
								if (homeFragment != null) {
									TopicFragment topicFragment = (TopicFragment) homeFragment
											.getFragmentByPosition(1);
									if (topicFragment != null) {
										topicFragment.onUpdate();
									}
								}
								PersonalCenterFragment centerFragment = (PersonalCenterFragment) getActivity()
										.getSupportFragmentManager()
										.findFragmentByTag(
												PersonalCenterFragment.FRAGEMNT_TAG);
								if (centerFragment != null) {
									MyArticleFragment articleFragment = (MyArticleFragment) centerFragment
											.getFragmentByPosition(2);
									articleFragment.onUpdate();
								}
								finishFragment();
							}

						});
				break;
			}
		}
	};

	@Override
	protected String getTDPageName() {
		return getString(R.string.topic_title);
	}

	private TopicDetailFragment createTopicFragment() {
		TopicDetailFragment frag = new TopicDetailFragment();
		Bundle args = new Bundle();
		args.putSerializable(SHOW_BEAN, mShowBean);
		frag.setArguments(args);
		return frag;
	}
	
	private OtherTopicFragment creatOtherTopicFragment() {
		OtherTopicFragment frag = new OtherTopicFragment();
		Bundle args = new Bundle();
		args.putSerializable(SHOW_BEAN, mShowBean);
		frag.setArguments(args);
		return frag;
	}


	private void initContentLayout() {
		int uid = ProviderFatory.getUserProvider().getUserId(getActivity());
		if (uid==mShowBean.uid.intValue()) {
			
		}
		SlidingExitFragment topicDetailFragment = (uid==mShowBean.uid.intValue()?createTopicFragment():creatOtherTopicFragment());
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.topic_container, topicDetailFragment)
				.commitAllowingStateLoss();
	}


}

package cn.falconnect.shopping.ui;

import static cn.falconnect.shopping.constants.Global.COLLECT;
import static cn.falconnect.shopping.constants.Global.UNCOLLECT;
import static cn.falconnect.shopping.constants.Global.BundleKey.BOOLEAN_KEY;
import static cn.falconnect.shopping.constants.Global.BundleKey.COLLECTS_ENTRY_KEY;
import static cn.falconnect.shopping.constants.Global.BundleKey.DETAIL_URL;
import static cn.falconnect.shopping.constants.Global.BundleKey.GOODS_BEAN;
import static cn.falconnect.shopping.constants.Global.BundleKey.ID;

import org.aurora.library.downloader.core.CustomThreadAsyncTask;
import org.aurora.library.views.Toaster;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.falconnect.shopping.Task;
import cn.falconnect.shopping.Task.AsyncTaskListener;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.constants.Global;
import cn.falconnect.shopping.constants.Global.CommentType;
import cn.falconnect.shopping.entity.Goods;
import cn.falconnect.shopping.provider.db.CacheDAO;
import cn.falconnect.shopping.provider.web.ObtainListener;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.provider.web.ResultCode;
import cn.falconnect.shopping.ui.comment.GoodsCommentsFragment;
import cn.falconnect.shopping.ui.user.CollectionsFragment;
import cn.falconnect.shopping.ui.user.HistoryFragment;
import cn.falconnect.shopping.ui.user.PersonalCenterFragment;
import cn.falconnect.shopping.ui.user.UserLoginFragment;
import cn.falconnect.shopping.utils.DialogUtil;
import cn.falconnect.shopping.utils.SoftInput;

public class GoodsDetailFragment extends BaseFragment {
	public static final String FRAGMENT_TAG = "GoodsDetailFragment";
	private Goods mGoodsBean;
	private boolean mIsGoods;
	private String mDetailUrl;
	private String mBundleExtra;
	private ImageView mBtnGoBack;
	private ImageView mBtnGoForward;
	private ImageView mBtnCollect;
	private DrawerLayout mDrawerLayout;
	private EditText mEtContent;
	private ImageView mBtnComment;
	private ImageView mBtnShowPopup;
	private TextView mTvTitle;
	private Dialog mDialog;
	private GoodsActionPopupWindow mGoodsActionPopuWindow;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		mDialog = DialogUtil.showProgressBar(getActivity(), "正在提交评论...");
		mGoodsActionPopuWindow=new GoodsActionPopupWindow(getActivity(),mOnClickListener);
		if (args != null) {
			mGoodsBean = (Goods) args.getSerializable(GOODS_BEAN);
			mDetailUrl = mGoodsBean != null ? mGoodsBean.detailUrl : args
					.getString(DETAIL_URL);
			mBundleExtra = args.getString(COLLECTS_ENTRY_KEY);
			mIsGoods = mGoodsBean != null && !TextUtils.isEmpty(mGoodsBean.name);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View contentView = inflater.inflate(
				mIsGoods ? R.layout.fragment_web_main
						: R.layout.fragment_web_secondary, null);
		initContentLayout();
		if (mIsGoods) {
			initViews(contentView);
			initDrawerLayout();
		}
		if (mIsGoods
				&& ProviderFatory.getUserProvider().isLogined(
						contentView.getContext())) {
			insertGoodsHistory(contentView);
		}
		return contentView;
	}

	private void insertGoodsHistory(View contentView) {
		insertHistoryData(contentView.getContext(),
				new AsyncTaskListener<Void>() {

					@Override
					public void onSucceed(Context context, Void result) {
					}

					@Override
					public void onError(Context context, String msg) {
					}

					@Override
					public void onFinished(Context context, String msg) {
						if (!TextUtils.isEmpty(mBundleExtra)
								&& CollectionsFragment.FRAGMENT_TAG
										.equals(mBundleExtra)) {
							if (getView() != null) {
								PersonalCenterFragment userCenterFragment = (PersonalCenterFragment) getActivity()
										.getSupportFragmentManager()
										.findFragmentByTag(
												PersonalCenterFragment.FRAGEMNT_TAG);
								if (userCenterFragment != null) {
									HistoryFragment historyFragment = (HistoryFragment) userCenterFragment
											.getFragmentByPosition(1);
									if (historyFragment != null) {
										historyFragment.onUpdate();
									}
								}
							}
						}
					}

				});
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
			mBtnComment.setEnabled(!TextUtils.isEmpty(word.toString().trim()) ? true : false);
		}
	};

	void setCanGoBack(boolean enabled) {
		mBtnGoBack.setEnabled(enabled ? true : false);
		mBtnGoBack.setImageResource(enabled ? R.drawable.ic_preview
				: R.drawable.ic_preview_normal);

	}

	void setCanGoForward(boolean enabled) {
		mBtnGoForward.setEnabled(enabled ? true : false);
		mBtnGoForward.setImageResource(enabled ? R.drawable.ic_forward
				: R.drawable.ic_forward_normal);
	}

	private void initViews(View contentView) {
		mTvTitle=(TextView)contentView.findViewById(R.id.text_title);
		contentView.findViewById(R.id.iv_back).setOnClickListener(
				mOnClickListener);
		mBtnShowPopup=(ImageView)contentView.findViewById(R.id.overflow_tag);
		mBtnShowPopup.setOnClickListener(mOnClickListener);
		final View bottomView = contentView.findViewById(R.id.ll_bottom_btn);
		final View commentsView = contentView.findViewById(R.id.comment_tag);
		final View floatView = contentView
				.findViewById(R.id.float_comment_layout);
		mBtnGoBack = (ImageView) contentView.findViewById(R.id.btn_back);
		mBtnGoForward = (ImageView) contentView.findViewById(R.id.btn_forward);
		mBtnCollect = (ImageView) contentView.findViewById(R.id.btn_collect);
		mBtnCollect.setVisibility(mIsGoods ? View.VISIBLE : View.GONE);
		mEtContent = (EditText) contentView.findViewById(R.id.comment_edit);
		mEtContent.addTextChangedListener(mTextChangedListener);
		mBtnComment = (ImageView) contentView.findViewById(R.id.btn_comment);
		mBtnComment.setEnabled(false);
		mBtnComment.setOnClickListener(mOnClickListener);
		mDrawerLayout = (DrawerLayout) contentView
				.findViewById(R.id.comments_parent);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_right_shadow,
				GravityCompat.END);
		commentsView.setOnClickListener(mOnClickListener);
		mBtnGoBack.setOnClickListener(mOnClickListener);
		mBtnGoForward.setOnClickListener(mOnClickListener);
		mBtnCollect.setOnClickListener(mOnClickListener);
		mDrawerLayout.setDrawerListener(new DrawerListener() {

			@Override
			public void onDrawerStateChanged(int state) {

			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				mTvTitle.setText(getString(R.string.comments_title));
				bottomView.setVisibility(View.GONE);
				floatView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				mTvTitle.setText(getString(R.string.detail_title));
				SoftInput.hideSoftInput(drawerView);
				bottomView.setVisibility(View.VISIBLE);
				floatView.setVisibility(View.GONE);
			}
		});
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			DetailWebFragment webFragment = (DetailWebFragment) getActivity()
					.getSupportFragmentManager().findFragmentByTag(
							DetailWebFragment.FRAGMENT_TAG);
			switch (v.getId()) {
			case R.id.btn_comment:
				String content = mEtContent.getText().toString();
				mDialog.show();
				ProviderFatory.getFeedbackProvider().postFeedback(
						v.getContext(), CommentType.COMMENT_GOODS,
						mGoodsBean.getGoodsId(), content,
						new ObtainListener<Void>() {

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
								GoodsCommentsFragment commentsFragment = (GoodsCommentsFragment) getActivity()
										.getSupportFragmentManager()
										.findFragmentByTag(
												GoodsCommentsFragment.FRAGMENT_TAG);
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
			case R.id.comment_tag:
				mDrawerLayout.openDrawer(GravityCompat.END);
				break;
			case R.id.btn_back:
				if (webFragment != null) {
					webFragment.webViewGoBack();
				}
				break;
			case R.id.btn_forward:
				if (webFragment != null) {
					webFragment.webViewGoForward();
				}
				break;
			case R.id.btn_collect:
				boolean isLogin = ProviderFatory.getUserProvider().isLogined(
						v.getContext());
				if (isLogin) {
					ProviderFatory.getGoodsProvider().collect(v.getContext(),
							mGoodsBean.getGoodsId(),
							mGoodsBean.isCollected ? UNCOLLECT : COLLECT,
							new ObtainListener<Void>() {

								@Override
								public void onSucceed(Context context,
										Void result) {

								}

								@Override
								public void onError(Context context,
										ResultCode code) {
									if (code.code != ResultCode.SUCCESS.code) {
										Toaster.toast(R.string.collect_failed);
									}
								}

								@Override
								public void onFinished(Context context,
										ResultCode code) {
									if (code.code == ResultCode.SUCCESS.code) {
										updateCollectData(
												context,
												mGoodsBean.isCollected,
												new AsyncTaskListener<Boolean>() {
													
													@Override
													public void onSucceed(
															Context context,
															Boolean result) {
														mBtnCollect
																.setImageResource(result
																		.booleanValue() ? R.drawable.ic_collects_1_focus
																		: R.drawable.ic_collects_1);
														mGoodsBean.isCollected = result
																.booleanValue();
														mBtnCollect
																.setEnabled(true);

														if (!TextUtils
																.isEmpty(mBundleExtra)) {
															if (getView() != null) {
																PersonalCenterFragment userCenterFragment = (PersonalCenterFragment) getActivity()
																		.getSupportFragmentManager()
																		.findFragmentByTag(
																				PersonalCenterFragment.FRAGEMNT_TAG);
																if (userCenterFragment != null) {
																	CollectionsFragment collectionsFragment = (CollectionsFragment) userCenterFragment
																			.getFragmentByPosition(0);
																	if (collectionsFragment != null) {
																		collectionsFragment
																				.onUpdate();
																	}
																}
															}
														}
													}

													@Override
													public void onError(
															Context context,
															String msg) {

													}

													@Override
													public void onFinished(
															Context context,
															String msg) {

													}

												});
									}
								}

							});
				} else {
					startFragment(new UserLoginFragment(), null,
							UserLoginFragment.class.getName(),
							UserLoginFragment.FRAGMENT_TAG);
				}
				break;
			case R.id.iv_back:
				finishFragment();
				break;
			case R.id.overflow_tag:
				mGoodsActionPopuWindow.showAsDropDown(mBtnShowPopup);
				break;
			case R.id.btn_share_goods:
				mGoodsActionPopuWindow.dismiss();
				Intent shareIntent = new Intent(Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(Intent.EXTRA_TEXT, mDetailUrl);
				startActivity(Intent.createChooser(shareIntent, "分享到..."));
				break;
			default:
				break;
			}
		}
	};

	private <T> void insertHistoryData(final Context context,
			AsyncTaskListener<T> listener) {
		Task<T> task = new Task<T>(context, listener);
		new CustomThreadAsyncTask<Object, Void, Task<T>>() {
			@Override
			protected Task<T> doInBackground(Object... params) {
				@SuppressWarnings("unchecked")
				Task<T> task = (Task<T>) params[0];
				try {
					String sign = ProviderFatory.getUserProvider()
							.getLoginName(context);
					if (!TextUtils.isEmpty(sign)) {
						boolean isExist = CacheDAO.getInstance().isGoodsExist(
								mGoodsBean.getGoodsId(),
								Global.HISTORY_CACHE_TYPE, sign);
						if (!isExist) {
							boolean success = CacheDAO.getInstance()
									.insert(mGoodsBean,
											Global.HISTORY_CACHE_TYPE, sign);
							task.msg = success ? "success" : "failed";
						} else {
							task.msg = "failed";
						}
					}

				} finally {
					task.listener.onFinishInBackgroud(task.context, task.msg,
							task.result);
				}
				return task;
			}

			@Override
			protected void onPostExecute(Task<T> result) {
				String msg = result.msg;
				if ("success".equals(msg)) {
					result.listener.onSucceed(result.context, result.result);
				} else {
					result.listener.onError(result.context, msg);
				}
				result.listener.onFinished(result.context, msg);
			}
		}.execute(task);
	}

	public void selfCollectStatusUpdate() {
		boolean isLogin = ProviderFatory.getUserProvider().isLogined(
				mBtnCollect.getContext());
		if (isLogin && mGoodsBean != null) {
			updateCollectStatus(mBtnCollect.getContext(),
					new AsyncTaskListener<Boolean>() {

						@Override
						public void onSucceed(Context context, Boolean result) {
							mBtnCollect.setImageResource(result.booleanValue() ? R.drawable.ic_collects_1_focus
									: R.drawable.ic_collects_1);
							mGoodsBean.isCollected = result.booleanValue();
							mBtnCollect.setEnabled(true);
						}

						@Override
						public void onError(Context context, String msg) {

						}

						@Override
						public void onFinished(Context context, String msg) {

						}
					});
		}
	}

	private <T> void updateCollectData(final Context context,
			final boolean isCollected, AsyncTaskListener<T> listener) {
		Task<T> task = new Task<T>(context, listener);
		new CustomThreadAsyncTask<Object, Void, Task<T>>() {
			@Override
			protected Task<T> doInBackground(Object... params) {
				@SuppressWarnings("unchecked")
				Task<T> task = (Task<T>) params[0];
				try {
					String sign = ProviderFatory.getUserProvider()
							.getLoginName(context);
					if (isCollected) {
						boolean isDeleted = CacheDAO.getInstance().detete(
								mGoodsBean.getGoodsId(),
								Global.COLLECTS_CACHE_TYPE, sign);
						task.msg = isDeleted ? "success" : "failed";
						Boolean collected = isDeleted ? false : true;
						task.result = (T) collected;
					} else {
						boolean isAdded = CacheDAO.getInstance().insert(
								mGoodsBean, Global.COLLECTS_CACHE_TYPE, sign);
						task.msg = isAdded ? "success" : "failed";
						Boolean collected = isAdded ? true : false;
						task.result = (T) collected;
					}

				} finally {
					task.listener.onFinishInBackgroud(task.context, task.msg,
							task.result);
				}
				return task;
			}

			@Override
			protected void onPreExecute() {
				mBtnCollect.setEnabled(false);
			}

			@Override
			protected void onPostExecute(Task<T> result) {
				String msg = result.msg;
				if ("success".equals(msg)) {
					result.listener.onSucceed(result.context, result.result);
				} else {
					result.listener.onError(result.context, msg);
				}
				result.listener.onFinished(result.context, msg);
			}
		}.execute(task);
	}

	private <T> void updateCollectStatus(final Context context,
			AsyncTaskListener<T> listener) {
		Task<T> task = new Task<T>(context, listener);
		new CustomThreadAsyncTask<Object, Void, Task<T>>() {
			@Override
			protected Task<T> doInBackground(Object... params) {
				@SuppressWarnings("unchecked")
				Task<T> task = (Task<T>) params[0];
				try {
					String sign = ProviderFatory.getUserProvider()
							.getLoginName(context);
					if (!TextUtils.isEmpty(sign)) {
						Boolean exist = CacheDAO.getInstance().isGoodsExist(
								mGoodsBean.getGoodsId(),
								Global.COLLECTS_CACHE_TYPE, sign);
						task.msg = exist ? "success" : "failed";
						task.result = (T) exist;
					}
				} finally {
					task.listener.onFinishInBackgroud(task.context, task.msg,
							task.result);
				}
				return task;
			}

			@Override
			protected void onPostExecute(Task<T> result) {
				String msg = result.msg;
				if ("success".equals(msg)) {
					result.listener.onSucceed(result.context, result.result);
				} else {
					result.listener.onError(result.context, msg);
				}
				result.listener.onFinished(result.context, msg);
			}
		}.execute(task);
	}

	@Override
	protected String getTDPageName() {
		return getString(R.string.detail_title);
	}

	private DetailWebFragment createWebFragment() {
		DetailWebFragment frag = new DetailWebFragment();
		Bundle args = new Bundle();
		args.putString(DETAIL_URL, mDetailUrl);
		args.putBoolean(BOOLEAN_KEY, mIsGoods);
		frag.setArguments(args);
		return frag;
	}

	private GoodsCommentsFragment createDrawerFragment() {
		GoodsCommentsFragment frag = new GoodsCommentsFragment();
		Bundle args = new Bundle();
		args.putInt(ID, mGoodsBean.getGoodsId());
		frag.setArguments(args);
		return frag;
	}

	private void initContentLayout() {
		DetailWebFragment webMainFragment = createWebFragment();
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.webmain_container, webMainFragment,
						DetailWebFragment.FRAGMENT_TAG)
				.commitAllowingStateLoss();
	}

	private void initDrawerLayout() {
		GoodsCommentsFragment drawerFragment = createDrawerFragment();
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.goods_comments_container, drawerFragment,
						GoodsCommentsFragment.FRAGMENT_TAG)
				.commitAllowingStateLoss();
	}

}

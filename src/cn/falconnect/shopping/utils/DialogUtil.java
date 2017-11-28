package cn.falconnect.shopping.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.widget.TasksCompletedView;

public class DialogUtil {
	private static Dialog dialog = null;
	private static TasksCompletedView taskv_ProgressBar;
	private static TextView tv_dialog_progress;
	public static final int LEFT_TAG = 1;
	public static final int RIGHT_TAG = 2;
	/**
	 * 相机
	 */
	public static final int CAMARE_TAG = 12;

	/**
	 * 相册
	 */
	public static final int GALLERY_TAG = 13;
	private CallBack callBack;

	public CallBack getCallBack() {
		return callBack;
	}

	public void setCallBack(CallBack callBack) {
		this.callBack = callBack;
	}


	/**
	 * 弹出选择对话框
	 * 
	 * @param context
	 * @param titler
	 *            标题 默认为null
	 * @param content
	 *            提示内容 默认为null
	 * @param leftText
	 *            标题 默认为null
	 * @param rightText
	 *            标题 默认为null
	 * @param listener
	 *            事件监听器 tag=LEFT_TAG 为左边按钮标识 tag=RIGHT_TAG 为右边按钮标识
	 */
	public static Dialog showChoiceDialog(Context context, String titler,
			String content, String leftText, String rightText,
			OnClickListener listener) {
		String defaultTitler = "温馨提示";
		String defaultContent = "";
		String defaultCancel = "取消";
		String defaultSure = "继续";
		isDialogShowing();
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_logout, null);
		TextView tv_dialog_warn_titler = (TextView) view
				.findViewById(R.id.tv_title_logout);
		TextView tv_dialog_warn_content = (TextView) view
				.findViewById(R.id.tv_content_logout);
		Button btn_dialog_choice_left = (Button) view
				.findViewById(R.id.btn_cancel_logout);
		Button btn_dialog_choice_right = (Button) view
				.findViewById(R.id.btn_ensure_logout);

		tv_dialog_warn_titler.setText(titler != null ? titler : defaultTitler);
		tv_dialog_warn_content.setText(content != null ? content
				: defaultContent);
		btn_dialog_choice_left.setText(leftText != null ? leftText
				: defaultSure);
		btn_dialog_choice_right.setText(rightText != null ? rightText
				: defaultCancel);

		dialog = new Dialog(context, R.style.dialog);

		btn_dialog_choice_left.setTag(LEFT_TAG);
		btn_dialog_choice_right.setTag(RIGHT_TAG);

		btn_dialog_choice_left.setOnClickListener(listener);
		btn_dialog_choice_right.setOnClickListener(listener);

		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	/**
	 * 进度框
	 * 
	 * @param context
	 * @param content
	 */
	public static Dialog showProgressBar(Context context, String content) {
		isDialogShowing();
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_progressbar, null);
		String defaultContent ="正在加载";
		TextView tv_dialog_progress = (TextView) view
				.findViewById(R.id.tv_dialog_progress_content);
		RelativeLayout rl_progressbar = (RelativeLayout) view
				.findViewById(R.id.rl_progressbar);
		tv_dialog_progress.setText(!TextUtils.isEmpty(content) ? content
				+ "..." : defaultContent + "...");
		dialog = new Dialog(context, R.style.progress_dialog);
		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}
	
	public static Dialog showProgressBar(Context context, String content,int progress) {
		isDialogShowing();
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_image_upload, null);
		String defaultContent ="正在加载";
		taskv_ProgressBar=(TasksCompletedView) view.findViewById(R.id.progressBar);
		taskv_ProgressBar.setProgress(progress);
		tv_dialog_progress = (TextView) view
				.findViewById(R.id.tv_dialog_progress_content);
		RelativeLayout rl_progressbar = (RelativeLayout) view
				.findViewById(R.id.rl_progressbar);
		tv_dialog_progress.setText(!TextUtils.isEmpty(content) ? content
				+ "..." : defaultContent + "...");
		dialog = new Dialog(context, R.style.progress_dialog);
		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}
	
	public static void setProgress(int progress,int imagecounr){
		if(taskv_ProgressBar!=null){
			taskv_ProgressBar.setProgress(progress);
		}
		tv_dialog_progress.setText("正在加载第"+imagecounr+"张图片...");
	}
	

	public static Dialog editNicknameDialog(final Context context,
			final CallBack listener) {
		isDialogShowing();
		View view = LayoutInflater.from(context).inflate(
				R.layout.edit_nickname, null);
		final EditText et_input_nickname = (EditText) view
				.findViewById(R.id.et_input_nickname);
		Button btn_cancel_nickname = (Button) view
				.findViewById(R.id.btn_cancel_nickname);
		Button btn_ensure_nickname = (Button) view
				.findViewById(R.id.btn_ensure_nickname);
		dialog = new Dialog(context, R.style.dialog);
		dialog.setContentView(view);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		btn_cancel_nickname.setTag(LEFT_TAG);
		btn_ensure_nickname.setTag(RIGHT_TAG);
		btn_cancel_nickname.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CommonUtil.hideSoftInput(context,
						et_input_nickname.getWindowToken());
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		});
		btn_ensure_nickname.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.setResult(et_input_nickname.getText().toString());
				CommonUtil.hideSoftInput(context,
						et_input_nickname.getWindowToken());
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		});
		dialog.show();
		return dialog;
	}

	private static void isDialogShowing() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	public interface CallBack {
		public void setResult(String content);
	}

}

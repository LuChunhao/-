package cn.falconnect.shopping.ui;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.utils.CommonUtil;

public class GoodsActionPopupWindow extends PopupWindow {
	public GoodsActionPopupWindow(Context context, OnClickListener listener) {
		View contentView = View.inflate(context, R.layout.dialog_goods_action,
				null);
		setWidth(CommonUtil.convertDipToPx(context, 180));
		setHeight(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(context.getResources().getDrawable(
				R.drawable.bg_probar));
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(contentView);
		Button btnShare = (Button) contentView
				.findViewById(R.id.btn_share_goods);
		btnShare.setOnClickListener(listener);
	}

}

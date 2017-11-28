package cn.falconnect.shopping.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.entity.Feedback;
import cn.falconnect.shopping.provider.web.ProviderFatory;
import cn.falconnect.shopping.utils.ImageHelper;
import cn.falconnect.shopping.utils.StringUtil;

public class CommentsAdapter extends GenericListAdapter<Feedback> {

	public CommentsAdapter() {
		super();
	}

	public void setData(List<Feedback> list) {
		if (list != null && list.size() > 0) {
			this.mDataList = list;
			notifyDataSetChanged();
		}
	}

	public void addItems(List<Feedback> list) {
		if (list != null && list.size() > 0) {
			this.mDataList.addAll(list);
			notifyDataSetChanged();
		}
	}

	@Override
	protected void bindView(View paramView, int position) {
		ViewHolder holder = (ViewHolder) paramView.getTag();
		Feedback comment = getItem(position);
		ImageHelper.displayDefaultCircleIcon(holder.ivAvatar, comment.headUrl);
		int id = ProviderFatory.getUserProvider().getUserId(paramView.getContext());
		holder.tvNickName.setText(TextUtils.isEmpty(comment.nickName)? paramView.getContext().getResources().getString(R.string.comment_desc)+id : comment.nickName);
		holder.tvPostTime.setText(StringUtil.formatDate(comment.postTime));
		holder.tvContent.setText(comment.content);
	}

	@Override
	protected View newView(Context context, int position) {
		View convertView = LayoutInflater.from(context).inflate(
				R.layout.comment_item, null);
		ViewHolder holder = new ViewHolder();
		holder.ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
		holder.tvPostTime = (TextView) convertView
				.findViewById(R.id.tv_post_time);
		holder.tvNickName = (TextView) convertView
				.findViewById(R.id.tv_nick_name);
		holder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
		convertView.setTag(holder);
		return convertView;
	}

	private static final class ViewHolder {
		private ImageView ivAvatar;
		private TextView tvNickName;
		private TextView tvPostTime;
		private TextView tvContent;
	}
}

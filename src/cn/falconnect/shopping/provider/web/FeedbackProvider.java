package cn.falconnect.shopping.provider.web;

import android.content.Context;
import cn.falconnect.shopping.entity.Feedback;

public class FeedbackProvider extends BaseProvider {
	public void submitFeedback(Context context, String content, String contact, ObtainListener<Feedback> listener) {
		CatShopApi.submitFeedback(context, content, contact, listener);
	}
	
	public void postFeedback(Context context,int type,int id,String content,ObtainListener<Void> listener){
		CatShopApi.postFeedback(context, type, id, content, listener);
	}
	
}

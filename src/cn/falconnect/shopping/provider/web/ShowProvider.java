package cn.falconnect.shopping.provider.web;

import java.util.List;

import android.content.Context;
import cn.falconnect.shopping.entity.Feedback;
import cn.falconnect.shopping.entity.Show;

public class ShowProvider extends BaseProvider {
	public void getMyShow(Context context, int offset, int size,
			ObtainListener<List<Show>> listener) {
		CatShopApi.getMyShow(context, offset, size, listener);
	}
	public void getMyShow(Context context, int offset, int size,int id,
			ObtainListener<List<Show>> listener) {
		CatShopApi.getMyShow(context, offset, size,id, listener);
	}

	public void getShowFeedback(Context context, int id, int offset, int size,
			ObtainListener<List<Feedback>> listener) {
		CatShopApi.getShowFeedback(context, id, offset, size, listener);
	}

	public void deleteMyShow(Context context, int id,
			ObtainListener<Void> listener) {
		CatShopApi.deleteMyShow(context, id, listener);
	}

	public void getAllShow(Context context, int offset, int size,
			ObtainListener<List<Show>> listener) {
		CatShopApi.getAllShow(context, offset, size, listener);
	}

	public void showSomething(Context context, String description,
			String[] picUrls, ObtainListener<Void> listener) {
		CatShopApi.showSomething(context, description, picUrls, listener);
	}
}

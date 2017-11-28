package cn.falconnect.shopping.provider.web;

import java.util.List;

import android.content.Context;
import cn.falconnect.shopping.entity.LaunchMission;

public class LaunchPageProvider extends BaseProvider {

	public void obtainLaunchPage(Context context, ObtainListener<List<LaunchMission>> listener) {
		CatShopApi.getLaunchPage(context, listener);
	}
}

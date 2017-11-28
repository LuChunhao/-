package cn.falconnect.shopping.provider.web;

import android.content.Context;
import cn.falconnect.shopping.entity.VersionInfo;

public class VersionProvider extends BaseProvider {
	public void obtainVersionInfo(Context context, int versionCode, ObtainListener<VersionInfo> listener) {
		CatShopApi.getVersionInfo(context, versionCode, listener);
	}
}

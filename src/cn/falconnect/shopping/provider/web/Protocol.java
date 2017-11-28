package cn.falconnect.shopping.provider.web;

import android.os.Environment;

public final class Protocol {

	// public static final String ServerUrl =
	// "http://10.0.0.250/shopping/index.php?s=/Api/Entry/Index";
	public static final String ServerUrl = "http://shopping.falconnect.cn/index.php?s=/Api/Entry/Index";
	public static final String ResourceUrl = "http://shopping.falconnect.cn/index.php?s=/Api/Upload/Index";
	// public static final String ResourceUrl =
	// "http://10.0.0.250/shopping/index.php?s=/Api/Upload/Index";

	public static final String AppCenterUrl = "http://appcenter.falconnect.cn/?s=/Home/Index/Api/";

	public static final String FilePath = Environment.getExternalStorageDirectory() + "/falcon/";

	public static final int DEVICE = 1;

	public static String getServerUrl(int action) {
		return ServerUrl;
	}

	public static String getAppCenterUrl(int action) {
		return AppCenterUrl;
	}
}

package cn.falconnect.shopping.constants;

public class Global {
	// 收藏操作
	public static final int COLLECT = 0;
	public static final int UNCOLLECT = 1;

	public static final int HISTORY_CACHE_TYPE = 0;
	public static final int COLLECTS_CACHE_TYPE = 1;
	public static final String APP_LINK_URL = "http://appcenter.falconnect.cn/?s=/Home/Index/Api/action/1003/package/";
	public static final String LINK_END_URL = "/os/1";
	public static final String ACTION_NAME = "login_action";
	public static boolean AD_OPEN = true;
	public static final class Asortment {
		public static final int BY_DEFAULT = 0;
		public static final int BY_SALES = 1;
		public static final int BY_PRICE_ASC = 2;
		public static final int BY_PRICE_DESC = 3;
		public static final int BY_LATEST = 4;
	}

	public static final class CommentType {
		public static final int COMMENT_GOODS = 1;
		public static final int COMMENT_TOPIC = 2;
	}

	public static final class BundleKey {
		public static final String ID = "bundle_key_id";
		public static final String BOOLEAN_KEY = "bundle_key_boolean";
		public static final String BRAND_TYPE = "bundle_brand_type";
		public static final String BRAND_LOGO = "bundle_brand_logo";
		public static final String BRAND_NAME = "bundle_brand_name";
		public static final String GOODS_BEAN = "bundle_goods_bean";
		public static final String SHOW_BEAN = "bundle_show_bean";
		public static final String DETAIL_URL = "bundle_detail_url";
		public static final String GOODS_TYPE_NAME = "bundle_good_type_name";
		public static final String COLLECTS_ENTRY_KEY = "bundle_collects_entry_key";
	}
}

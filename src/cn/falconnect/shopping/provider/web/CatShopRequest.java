package cn.falconnect.shopping.provider.web;

import org.aurora.library.json.JsonColunm;
import org.aurora.library.util.NetworkUtil;

import cn.falconnect.shopping.utils.CommonUtil;
import android.content.Context;

class CatShopRequest {

	@JsonColunm(name = "action")
	Integer action;
	@JsonColunm(name = "device")
	Integer device;
	@JsonColunm(name = "version")
	Integer appVersion;
	@JsonColunm(name = "network")
	Integer network;
	@JsonColunm(name = "packageName")
	String packageName;
	@JsonColunm(name = "package")
	String app_package;
	@JsonColunm(name = "os")
	Integer os;
	//
	@JsonColunm(name = "uid")
	public Integer uid;
	@JsonColunm(name = "id")
	public Integer id;
	@JsonColunm(name = "offset")
	public Integer offset;
	@JsonColunm(name = "nickName")
	public String nickName;
	@JsonColunm(name = "headUrl")
	public String headUrl;
	@JsonColunm(name = "backgroundUrl")
	public String backgroundUrl;
	@JsonColunm(name = "count")
	public Integer count;
	@JsonColunm(name = "name")
	public String name;
	@JsonColunm(name = "sex")
	public Integer sex;
	@JsonColunm(name = "account")
	public String account;
	@JsonColunm(name = "passwd")
	public String passwd;
	@JsonColunm(name = "sign")
	public String sign;
	@JsonColunm(name = "title")
	public String title;
	@JsonColunm(name = "content")
	public String content;
	@JsonColunm(name = "place")
	public String place;
	@JsonColunm(name = "over_time")
	public Long overTime;
	@JsonColunm(name = "description")
	public String description;
	@JsonColunm(name = "contact")
	public String contact;
	@JsonColunm(name = "index")
	public Integer index;
	@JsonColunm(name = "size")
	public Integer size;
	@JsonColunm(name = "type")
	public Integer type;
	@JsonColunm(name = "sortType")
	public Integer sortType;
	@JsonColunm(name = "picUrls")
	public String[] picUrls;

	@JsonColunm(name = "versionCode")
	public Integer versionCode;
	@JsonColunm(name = "key")
	public String key;
	
	public CatShopRequest(int action) {
		this.action = action;
	}

	public void buildParams(Context context) {
		this.device = Protocol.DEVICE;
		try {
			this.appVersion = CommonUtil.getVersionCode(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.network = NetworkUtil.getNetworkType(context);
		this.packageName = context.getPackageName();
		this.sign = ProviderFatory.getUserProvider().getLoginSign(context);
	}
	
	public void buildAppCenterParams(Context context){
		this.os = Protocol.DEVICE;
		this.app_package = context.getPackageName();
	}
}

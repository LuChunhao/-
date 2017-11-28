package cn.falconnect.shopping.provider.web;

import org.aurora.library.json.JsonColunm;

public class CatShopResponse {
	@JsonColunm(name = "code")
	Integer code;
	@JsonColunm(name = "data")
	String data;

	CatShopResponse(int code, String data) {
		this.code = code;
		this.data = data;
	}
}

package cn.falconnect.shopping.entity;

import org.aurora.library.json.JsonColunm;

public class Brand extends BaseEntity {
	
	@JsonColunm(name = "id")
	public Integer id;
	@JsonColunm(name = "name")
	public String name;
	@JsonColunm(name = "currentPrice")
	public Float price;
	@JsonColunm(name = "discount")
	public String discount;
	@JsonColunm(name = "picUrl")
	public String goodsUrl;
	@JsonColunm(name = "logoUrl")
	public String logoUrl;

	public int getBrandId() {
		return id != null ? id : NO_ID;
	}
}

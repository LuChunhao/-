package cn.falconnect.shopping.entity;

import org.aurora.library.json.JsonColunm;

public class GoodsType extends BaseEntity {

	@JsonColunm(name = "id")
	public Integer id;
	@JsonColunm(name = "name")
	public String name;
	@JsonColunm(name = "picUrl")
	public String url;
	@JsonColunm(name = "color")
	public String color;
	public int getTypeId() {
		return id != null ? id : NO_ID;
	}
}

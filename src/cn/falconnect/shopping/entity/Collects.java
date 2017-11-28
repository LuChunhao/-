package cn.falconnect.shopping.entity;

import org.aurora.library.db.DbClassInfo;
import org.aurora.library.db.DbFieldInfo;
import org.aurora.library.json.JsonColunm;

@DbClassInfo(tableName = "collects_info")
public class Collects extends BaseEntity {

	@DbFieldInfo(columnName = "goods_id")
	@JsonColunm(name = "id")
	public Integer id;

	@DbFieldInfo(columnName = "goods_name")
	@JsonColunm(name = "name")
	public String name;

	@DbFieldInfo(columnName = "current_price")
	@JsonColunm(name = "currentPrice")
	public Float currentPrice;

	@DbFieldInfo(columnName = "original_price")
	@JsonColunm(name = "originalPrice")
	public Float originalPrice;

	@DbFieldInfo(columnName = "discount")
	@JsonColunm(name = "discount")
	public String discount;

	@DbFieldInfo(columnName = "sales")
	@JsonColunm(name = "salesVolume")
	public Integer salesVolume;

	@DbFieldInfo(columnName = "postage")
	@JsonColunm(name = "isShippingFree")
	public Integer isShippingFree;

	@DbFieldInfo(columnName = "pic_url")
	@JsonColunm(name = "picUrl")
	public String picUrl;

	@DbFieldInfo(columnName = "detail_url")
	@JsonColunm(name = "detailUrl")
	public String detailUrl;

	@DbFieldInfo(columnName = "description")
	@JsonColunm(name = "description")
	public String description;

	@JsonColunm(name = "width")
	public Integer width;

	@JsonColunm(name = "height")
	public Integer height;

	public int getGoodsId() {
		return id != null ? id : NO_ID;
	}

}

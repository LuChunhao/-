package cn.falconnect.shopping.entity;

import java.io.Serializable;
import java.util.Arrays;

import org.aurora.library.json.JsonColunm;

public class Show extends BaseEntity implements Serializable {

	@JsonColunm(name = "id")
	public Integer id;
	@JsonColunm(name = "uid")
	public Integer uid;
	@JsonColunm(name = "nickName")
	public String nickName;
	@JsonColunm(name = "headUrl")
	public String headUrl;
	@JsonColunm(name = "postTime")
	public Long postTime;
	@JsonColunm(name = "description")
	public String description;
	@JsonColunm(name = "picUrls")
	public String[] picUrls;
	@JsonColunm(name = "width")
	public Integer width;
	@JsonColunm(name = "height")
	public Integer height;
	@JsonColunm(name = "backgroundUrl")
	public String backgroundUrl;

	
}

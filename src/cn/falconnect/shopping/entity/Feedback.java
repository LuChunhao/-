package cn.falconnect.shopping.entity;

import org.aurora.library.json.JsonColunm;

public class Feedback extends BaseEntity {

	@JsonColunm(name = "content")
	public String content;
	@JsonColunm(name = "contact")
	public String contact;
	@JsonColunm(name = "headUrl")
	public String headUrl;
	@JsonColunm(name = "nickName")
	public String nickName;
	@JsonColunm(name = "postTime")
	public Long postTime;
}

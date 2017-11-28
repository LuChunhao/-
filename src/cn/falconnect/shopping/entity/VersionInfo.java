package cn.falconnect.shopping.entity;

import org.aurora.library.json.JsonColunm;

public class VersionInfo extends BaseEntity {

	@JsonColunm(name = "updateTime")
	public Long updateTime;
	@JsonColunm(name = "downloadUrl")
	public String downloadUrl;
	@JsonColunm(name = "packageName")
	public String packageName;
	@JsonColunm(name = "content")
	public String content;
	@JsonColunm(name = "versionCode")
	public Integer versionCode;
	@JsonColunm(name = "versionName")
	public String versionName;
	@JsonColunm(name = "isForce")
	public Integer isForce;
}

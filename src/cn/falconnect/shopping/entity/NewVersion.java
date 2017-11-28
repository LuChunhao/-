package cn.falconnect.shopping.entity;

import org.aurora.library.json.JsonColunm;

public class NewVersion extends BaseEntity {
	@JsonColunm(name = "id")
	public Integer id;
	@JsonColunm(name = "name")
	public String name;
	@JsonColunm(name = "os")
	public String os;
	@JsonColunm(name = "rsurl")
	public String downloadUrl;
	@JsonColunm(name = "iconurl")
	public String iconUrl;
	@JsonColunm(name = "package")
	public String packageName;
	@JsonColunm(name = "appdesc")
	public String appdesc;
	@JsonColunm(name = "versionCode")
	public Integer versionCode;
	@JsonColunm(name = "versionName")
	public String versionName;
	@JsonColunm(name = "forceUpdate")
	public Integer forceUpdate;
	@JsonColunm(name = "sort")
	public Integer sort;
}

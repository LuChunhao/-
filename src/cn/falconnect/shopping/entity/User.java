package cn.falconnect.shopping.entity;

import java.io.Serializable;

import org.aurora.library.json.JsonColunm;

public class User extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@JsonColunm(name = "uid")
	public Integer uid;
	@JsonColunm(name = "nickName")
	public String nickName;
	@JsonColunm(name = "sex")
	public Integer sex;
	@JsonColunm(name = "account")
	public String account;
	@JsonColunm(name = "password")
	public String passowrd;
	@JsonColunm(name = "sign")
	public String sign;
	@JsonColunm(name = "icon")
	public String icon;
	@JsonColunm(name = "age")
	public Integer age;
	@JsonColunm(name = "headUrl")
	public String headUrl;
	@JsonColunm(name = "backgroundUrl")
	public String backgroundUrl;
	@Override
	public String toString() {
		return "User [uid=" + uid + ", nickName=" + nickName + ", sex=" + sex
				+ ", account=" + account + ", passowrd=" + passowrd + ", sign="
				+ sign + ", icon=" + icon + ", age=" + age + ", headUrl="
				+ headUrl + ", backgroundUrl=" + backgroundUrl + "]";
	}
	
}

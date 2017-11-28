package cn.falconnect.shopping.entity;

import java.io.Serializable;

import org.aurora.library.db.DbClassInfo;
import org.aurora.library.db.DbFieldInfo;
import org.aurora.library.json.JsonColunm;

@DbClassInfo(tableName = "launch_info")
public class LaunchMission extends BaseEntity implements Serializable{
	@DbFieldInfo(columnName = "url")
	@JsonColunm(name = "picUrl")
	public String url;
	
	@DbFieldInfo(columnName = "begin_time")
	@JsonColunm(name = "beginTime")
	public Long beginTime;
	
	@DbFieldInfo(columnName = "end_time")
	@JsonColunm(name = "endTime")
	public Long endTime;
	
	@DbFieldInfo(columnName = "mission_id")
	@JsonColunm(name = "missionId")
	public Integer missionId;
}

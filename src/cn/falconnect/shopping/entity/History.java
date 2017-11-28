package cn.falconnect.shopping.entity;

import org.aurora.library.db.DbClassInfo;
import org.aurora.library.db.DbFieldInfo;

@DbClassInfo(tableName = "history_info")
public class History extends BaseEntity {
	@DbFieldInfo(columnName = "_id")
	public Integer pid;

	@DbFieldInfo(columnName = "name")
	public String name;

}

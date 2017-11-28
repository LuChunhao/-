package cn.falconnect.shopping.provider.db;

import java.util.ArrayList;
import java.util.List;

import org.aurora.library.db.orm.DBModel;
import org.aurora.library.db.orm.OrmSqliteHelper;

import cn.falconnect.shopping.entity.LaunchMission;

public class LaunchPageDAO {

	private static LaunchPageDAO sInstance = new LaunchPageDAO();

	private LaunchPageDAO() {
	}

	public static LaunchPageDAO getInstance() {
		return sInstance;
	}

	
	public List<LaunchMission> queryAll() {
		DBModel model = new DBModel(LaunchMission.class);
		return OrmSqliteHelper.getInstance().queryList(model, LaunchMission.class);
	}

	public List<LaunchMission> query(int offset, int count) {
		DBModel model = new DBModel(LaunchMission.class);
		StringBuilder s = new StringBuilder();
		s.append(offset).append(", ").append(count);
		model.limit = s.toString();
		return OrmSqliteHelper.getInstance().queryList(model, LaunchMission.class);
	}

	public boolean detete(int missionId) {
		DBModel model = new DBModel(LaunchMission.class);
		model.whereClause = "mission_id = ?";
		model.whereArgs = new String[] { String.valueOf(missionId) };
		int result = OrmSqliteHelper.getInstance().delete(model);
		return result > 0;
	}

	public boolean deleteAll() {
		DBModel model = new DBModel(LaunchMission.class);
		int result = OrmSqliteHelper.getInstance().delete(model);
		return result > 0;
	}

	public boolean insert(LaunchMission page) {
		DBModel model = new DBModel(page);
		long result = OrmSqliteHelper.getInstance().insert(model);
		return result != -1;
	}

	public boolean insertAll(List<LaunchMission> launchPages) {
		if (launchPages == null) {
			return false;
		}
		List<DBModel> dbModelList = new ArrayList<DBModel>();
		for (int i = 0; i < launchPages.size(); i++) {
			DBModel model = new DBModel(launchPages.get(i));
			dbModelList.add(model);
		}
		int result = OrmSqliteHelper.getInstance().bulkInsert(dbModelList);
		return result != -1;
	}

}

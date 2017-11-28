package cn.falconnect.shopping.provider.db;

import java.util.ArrayList;
import java.util.List;

import org.aurora.library.db.orm.DBModel;
import org.aurora.library.db.orm.OrmSqliteHelper;

import cn.falconnect.shopping.entity.History;

public class HistoryDAO {
	private static HistoryDAO sInstance = new HistoryDAO();

	private HistoryDAO() {
	}

	public static HistoryDAO getInstance() {
		return sInstance;
	}

	public boolean isGoodsExist(String name) {
		DBModel model = new DBModel(History.class);
		model.selection = "name = ?";
		model.selectionArgs = new String[] { name };
		History entity = OrmSqliteHelper.getInstance().query(model,
				History.class);
		return entity != null;
	}

	public List<String> queryAll() {
		DBModel model = new DBModel(History.class);
		model.distinct = true;
		model.orderBy = "_id desc";
		model.limit = "9";
		List<String> keyDatas = new ArrayList<String>();
		List<History> datas = OrmSqliteHelper.getInstance().queryList(model,
				History.class);
		for (History keyHistory : datas) {
			keyDatas.add(keyHistory.name);
		}
		return keyDatas;
	}

	public boolean detete(String name) {
		DBModel model = new DBModel(History.class);
		model.whereClause = "name = ?";
		model.whereArgs = new String[] { name };
		int result = OrmSqliteHelper.getInstance().delete(model);
		return result > 0;
	}

	public boolean deleteAll() {
		DBModel model = new DBModel(History.class);
		int result = OrmSqliteHelper.getInstance().delete(model);
		return result > 0;
	}

	public boolean insert(History history) {
		DBModel model = new DBModel(history);
		long result = OrmSqliteHelper.getInstance().insert(model);
		return result != -1;
	}

}

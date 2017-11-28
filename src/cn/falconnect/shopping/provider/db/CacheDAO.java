package cn.falconnect.shopping.provider.db;

import java.util.ArrayList;
import java.util.List;

import org.aurora.library.db.orm.DBModel;
import org.aurora.library.db.orm.OrmSqliteHelper;

import cn.falconnect.shopping.entity.Goods;

public class CacheDAO {
	private static CacheDAO sInstance = new CacheDAO();

	private CacheDAO() {
	}

	public static CacheDAO getInstance() {
		return sInstance;
	}

	public List<Goods> queryAll(int type, String sign) {
		DBModel model = new DBModel(Goods.class);
		model.distinct = true;
		model.selection = "cache_type=? and user_sign=?";
		model.selectionArgs = new String[] { String.valueOf(type), sign };
		model.orderBy = "_id desc";
		return OrmSqliteHelper.getInstance().queryList(model, Goods.class);
	}

	public List<Goods> query(int offset, int count, int type, String sign) {
		DBModel model = new DBModel(Goods.class);
		model.distinct = true;
		model.selection = "cache_type=? and user_sign=?";
		model.selectionArgs = new String[] { String.valueOf(type), sign };
		model.orderBy = "_id desc";
		StringBuilder s = new StringBuilder();
		s.append(offset).append(", ").append(count);
		model.limit = s.toString();
		return OrmSqliteHelper.getInstance().queryList(model, Goods.class);
	}

	public boolean isGoodsExist(int goodsId, int type, String sign) {
		DBModel model = new DBModel(Goods.class);
		model.selection = "goods_id = ? and cache_type=? and user_sign=?";
		model.selectionArgs = new String[] { String.valueOf(goodsId),
				String.valueOf(type), sign };
		Goods entity = OrmSqliteHelper.getInstance().query(model, Goods.class);
		return entity != null;
	}

	public boolean detete(int goodsId, int type, String sign) {
		DBModel model = new DBModel(Goods.class);
		model.whereClause = "goods_id = ? and cache_type=? and user_sign=?";
		model.whereArgs = new String[] { String.valueOf(goodsId),
				String.valueOf(type), sign };
		int result = OrmSqliteHelper.getInstance().delete(model);
		return result > 0;
	}

	public boolean deleteAll(int type, String sign) {
		DBModel model = new DBModel(Goods.class);
		model.whereClause = "cache_type=? and user_sign=?";
		model.whereArgs = new String[] { String.valueOf(type), sign };
		int result = OrmSqliteHelper.getInstance().delete(model);
		return result > 0;
	}

	public boolean insert(Goods goods, int type, String sign) {
		goods.type = type;
		goods.sign = sign;
		goods.pid = null;
		DBModel model = new DBModel(goods);
		long result = OrmSqliteHelper.getInstance().insert(model);
		return result != -1;
	}

	public boolean insertAll(List<Goods> goodsList, int type, String sign) {
		if (goodsList == null) {
			return false;
		}
		List<DBModel> dbModelList = new ArrayList<DBModel>();
		for (int i = 0; i < goodsList.size(); i++) {
			Goods goods = goodsList.get(i);
			goods.type = type;
			goods.sign = sign;
			goods.pid = null;
			DBModel model = new DBModel(goods);
			dbModelList.add(model);
		}
		int result = OrmSqliteHelper.getInstance().bulkInsert(dbModelList);
		return result != -1;
	}
}

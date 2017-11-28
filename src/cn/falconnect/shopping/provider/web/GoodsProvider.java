package cn.falconnect.shopping.provider.web;

import java.util.List;

import android.content.Context;
import cn.falconnect.shopping.entity.Brand;
import cn.falconnect.shopping.entity.Feedback;
import cn.falconnect.shopping.entity.Goods;
import cn.falconnect.shopping.entity.GoodsType;

public class GoodsProvider extends BaseProvider {

	public void obtainBanner(Context context,
			ObtainListener<List<Goods>> listener) {
		CatShopApi.getBanner(context, listener);
	}

	public void obtainFirstPageGoodsType(Context context,
			ObtainListener<List<GoodsType>> listener) {
		CatShopApi.getFirstPageGoodsType(context, listener);
	}

	public void obtainRecommendGoodsList(Context context, int offset, int size,
			ObtainListener<List<Goods>> listener) {
		CatShopApi.getRecommendGoodsList(context, offset, size, listener);
	}

	public void obtainGoodsByType(Context context, int offset, int size, int id,
			int sortType, ObtainListener<List<Goods>> listener) {
		CatShopApi.getGoodsByType(context, offset, size, id, sortType, listener);
	}

	public void obtainBrandList(Context context, int index, int size, int type,
			ObtainListener<List<Brand>> listener) {
		CatShopApi.getBrandList(context, index, size, type, listener);
	}

	public void obtainGoodsListByBrand(Context context, int index, int size,
			int id, int type, ObtainListener<List<Goods>> listener) {
		CatShopApi
				.getGoodsListByBrand(context, index, size, id, type, listener);
	}

	public void obtainRandomLabel(Context context,
			ObtainListener<List<GoodsType>> listener) {
		CatShopApi.getRandomLabel(context, listener);
	}

	public void obtainRandomSortList(Context context, int index, int size,
			int id, ObtainListener<List<Goods>> listener) {
		CatShopApi.getRandomSortList(context, index, size, id, listener);
	}

	public void obtainSearchTypes(Context context,
			ObtainListener<List<GoodsType>> listener) {
		CatShopApi.getSearchTypes(context, listener);
	}

	public void obtainSearchResultList(Context context, int offset, int size,
			String name, int sortType, ObtainListener<List<Goods>> listener) {
		CatShopApi.getSearchResultList(context, offset, size, name, sortType,
				listener);
	}

	public void getHotWords(Context context,
			ObtainListener<List<String>> listener) {
		CatShopApi.getHotWords(context, listener);
	}

	public void findSomething(Context context, ObtainListener<Goods> listener) {
		CatShopApi.findSomething(context, listener);
	}

	public void collect(Context context, int id, int type,
			ObtainListener<Void> listener) {
		CatShopApi.collect(context, id, type, listener);
	}

	public void obtainUserCollectionList(Context context,
			ObtainListener<List<Goods>> listener) {
		CatShopApi.getUserCollectionList(context, listener);
	}

	public void getGoodsFeedback(Context context, int id, int offset, int size,
			ObtainListener<List<Feedback>> listener) {
		CatShopApi.getGoodsFeedback(context, id, offset, size, listener);
	}
}

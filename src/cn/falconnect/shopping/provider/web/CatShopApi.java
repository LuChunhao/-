package cn.falconnect.shopping.provider.web;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.aurora.library.downloader.core.CustomThreadAsyncTask;
import org.aurora.library.json.JsonBuilder;
import org.aurora.library.util.HttpUtil;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import cn.falconnect.shopping.entity.Brand;
import cn.falconnect.shopping.entity.Feedback;
import cn.falconnect.shopping.entity.Goods;
import cn.falconnect.shopping.entity.GoodsType;
import cn.falconnect.shopping.entity.LaunchMission;
import cn.falconnect.shopping.entity.NewVersion;
import cn.falconnect.shopping.entity.Show;
import cn.falconnect.shopping.entity.User;
import cn.falconnect.shopping.entity.VersionInfo;

public class CatShopApi {

	public static void login(Context context, String account,
			String passwordMD5, ObtainListener<User> listener) {
		CatShopRequest request = new CatShopRequest(1001);
		request.account = account;
		request.passwd = passwordMD5;
		post(context, request, listener);
	}

	public static void register(Context context, String account,
			String passwordMD5, ObtainListener<User> listener) {
		CatShopRequest request = new CatShopRequest(1002);
		request.account = account;
		request.passwd = passwordMD5;
		post(context, request, listener);
	}

	public static void submitFeedback(Context context, String content,
			String contact, ObtainListener<Feedback> listener) {
		CatShopRequest request = new CatShopRequest(1003);
		request.contact = contact;
		request.content = content;
		post(context, request, listener);
	}

	public static void getVersionInfo(Context context, int versionCode,
			ObtainListener<VersionInfo> listener) {
		CatShopRequest request = new CatShopRequest(1004);
		request.versionCode = versionCode;
		post(context, request, listener);
	}

	public static void forgetPasswd(Context context, String account,
			ObtainListener<Void> listener) {
		CatShopRequest request = new CatShopRequest(1006);
		request.account = account;
		post(context, request, listener);
	}

	public static void editPersonInfo(Context context, String headUrl,
			String backgroundUrl, String nickName, ObtainListener<Void> listener) {
		CatShopRequest request = new CatShopRequest(1007);
		request.headUrl = headUrl;
		request.backgroundUrl = backgroundUrl;
		request.nickName = nickName;
		post(context, request, listener);
	}

	public static void getUserInfo(Context context,
			ObtainListener<User> listener) {
		CatShopRequest request = new CatShopRequest(1008);
		post(context, request, listener);
	}

	public static void getUserInfo(Context context,
			ObtainListener<User> listener, int uid) {
		CatShopRequest request = new CatShopRequest(1008);
		request.uid = uid;
		post(context, request, listener);
	}

	public static void getLaunchPage(Context context,
			ObtainListener<List<LaunchMission>> listener) {
		CatShopRequest request = new CatShopRequest(2001);
		post(context, request, listener);
	}

	public static void getBanner(Context context,
			ObtainListener<List<Goods>> listener) {
		CatShopRequest request = new CatShopRequest(2002);
		post(context, request, listener);
	}

	public static void getFirstPageGoodsType(Context context,
			ObtainListener<List<GoodsType>> listener) {
		CatShopRequest request = new CatShopRequest(2003);
		post(context, request, listener);
	}

	public static void getRecommendGoodsList(Context context, int offset,
			int size, ObtainListener<List<Goods>> listener) {
		CatShopRequest request = new CatShopRequest(2004);
		request.offset = offset;
		request.size = size;
		post(context, request, listener);
	}

	public static void getBrandList(Context context, int index, int size,
			int type, ObtainListener<List<Brand>> listener) {
		CatShopRequest request = new CatShopRequest(2005);
		request.index = index;
		request.size = size;
		request.type = type;
		post(context, request, listener);
	}

	public static void getGoodsListByBrand(Context context, int index,
			int size, int id, int type, ObtainListener<List<Goods>> listener) {
		CatShopRequest request = new CatShopRequest(2006);
		request.index = index;
		request.size = size;
		request.id = id;
		request.type = type;
		post(context, request, listener);
	}

	public static void getGoodsByType(Context context, int offset, int size,
			int id, int sortType, ObtainListener<List<Goods>> listener) {
		CatShopRequest request = new CatShopRequest(2007);
		request.offset = offset;
		request.size = size;
		request.id = id;
		request.sortType = sortType;
		post(context, request, listener);
	}

	public static void getRandomLabel(Context context,
			ObtainListener<List<GoodsType>> listener) {
		CatShopRequest request = new CatShopRequest(2008);
		post(context, request, listener);
	}

	public static void getRandomSortList(Context context, int index, int size,
			int id, ObtainListener<List<Goods>> listener) {
		CatShopRequest request = new CatShopRequest(2009);
		request.index = index;
		request.size = size;
		request.id = id;
		post(context, request, listener);
	}

	public static void getSearchTypes(Context context,
			ObtainListener<List<GoodsType>> listener) {
		CatShopRequest request = new CatShopRequest(3001);
		post(context, request, listener);
	}

	public static void getSearchResultList(Context context, int offset,
			int size, String name, int sortType,
			ObtainListener<List<Goods>> listener) {
		CatShopRequest request = new CatShopRequest(3002);
		request.offset = offset;
		request.size = size;
		request.name = name;
		request.sortType = sortType;
		post(context, request, listener);
	}

	public static void collect(Context context, int id, int type,
			ObtainListener<Void> listener) {
		CatShopRequest request = new CatShopRequest(3003);
		request.id = id;
		request.type = type;
		post(context, request, listener);
	}

	public static void getUserCollectionList(Context context,
			ObtainListener<List<Goods>> listener) {
		CatShopRequest request = new CatShopRequest(3004);
		post(context, request, listener);
	}

	public static void getMyShow(Context context, int offset, int size,
			ObtainListener<List<Show>> listener) {
		CatShopRequest request = new CatShopRequest(3005);
		request.offset = offset;
		request.size = size;
		post(context, request, listener);
	}

	public static void getMyShow(Context context, int offset, int size, int id,
			ObtainListener<List<Show>> listener) {
		CatShopRequest request = new CatShopRequest(3005);
		request.offset = offset;
		request.uid = id;
		request.size = size;
		post(context, request, listener);
	}

	public static void getShowFeedback(Context context, int id, int offset,
			int size, ObtainListener<List<Feedback>> listener) {
		CatShopRequest request = new CatShopRequest(3006);
		request.id = id;
		request.offset = offset;
		request.size = size;
		post(context, request, listener);

	}

	public static void deleteMyShow(Context context, int id,
			ObtainListener<Void> listener) {
		CatShopRequest request = new CatShopRequest(3007);
		request.id = id;
		post(context, request, listener);
	}

	public static void getAllShow(Context context, int offset, int size,
			ObtainListener<List<Show>> listener) {
		CatShopRequest request = new CatShopRequest(3008);
		request.offset = offset;
		request.size = size;
		post(context, request, listener);
	}

	public static void postFeedback(Context context, int type, int id,
			String content, ObtainListener<Void> listener) {
		CatShopRequest request = new CatShopRequest(3009);
		request.id = id;
		request.type = type;
		request.content = content;
		post(context, request, listener);
	}

	public static void showSomething(Context context, String description,
			String[] picUrls, ObtainListener<Void> listener) {
		CatShopRequest request = new CatShopRequest(3010);
		request.description = description;
		request.picUrls = picUrls;
		post(context, request, listener);
	}

	public static void findSomething(Context context,
			ObtainListener<Goods> listener) {
		CatShopRequest request = new CatShopRequest(3011);
		post(context, request, listener);
	}

	public static void getHotWords(Context context,
			ObtainListener<List<String>> listener) {
		CatShopRequest request = new CatShopRequest(3012);
		post(context, request, listener);
	}

	public static void getGoodsFeedback(Context context, int id, int offset,
			int size, ObtainListener<List<Feedback>> listener) {
		CatShopRequest request = new CatShopRequest(3013);
		request.id = id;
		request.offset = offset;
		request.size = size;
		post(context, request, listener);
	}

	public static void getAppCenterStatus(Context context, String key,
			ObtainListener<String> listener) {
		CatShopRequest request = new CatShopRequest(1004);
		request.key = key;
		postAppCenter(context, request, listener);
	}

	public static void getNewVersion(Context context,
			ObtainListener<NewVersion> listener) {
		CatShopRequest request = new CatShopRequest(1002);
		postAppCenter(context, request, listener);
	}

	private static <T> void post(Context context, CatShopRequest request,
			ObtainListener<T> listener) {
		Task<T> task = new Task<T>(context, request, listener);
		new CustomThreadAsyncTask<Object, Void, Task<T>>() {
			@Override
			protected Task<T> doInBackground(Object... params) {
				@SuppressWarnings("unchecked")
				Task<T> task = (Task<T>) params[0];
				try {
					JsonBuilder jsonBuilder = new JsonBuilder();
					task.request.buildParams(task.context);
					String requestStr = jsonBuilder.toJson(task.request);
					String responseStr = HttpUtil
							.doPost(new URL(Protocol
									.getServerUrl(task.request.action)),
									requestStr);
//					Log.i("--------", task.request.action+"");
					Log.e("request==>", requestStr);

					Log.e("response==>", responseStr);
					if (!TextUtils.isEmpty(responseStr)) {
						JSONObject jsonObject = new JSONObject(responseStr);
						int code = jsonObject.getInt("code");
						String data = jsonObject.getString("data");
						task.response = new CatShopResponse(code, data);
						if (task.response.code == ResultCode.SUCCESS.code
								&& !(TextUtils.isEmpty(task.response.data) || "\"\""
										.equals(task.response.data))) {
							task.result = jsonBuilder.fromJson(
									task.response.data, task.listener.type);

						} else {

						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					if (task.response == null) {
						task.response = new CatShopResponse(
								ResultCode.NET_ERROR.code, "");
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (task.response == null) {
						task.response = new CatShopResponse(
								ResultCode.SERVER_ERROR.code, "");
					}
					task.listener
							.onFinishInBackgroud(task.context,
									ResultCode.getEnum(task.response.code),
									task.result);
				}

				return task;
			}

			@Override
			protected void onPostExecute(Task<T> result) {
				ResultCode code = ResultCode.getEnum(result.response.code);
				if (code == ResultCode.SUCCESS) {
					result.listener.onSucceed(result.context, result.result);
				} else {
					result.listener.onError(result.context, code);
				}
				result.listener.onFinished(result.context, code);
			}
		}.execute(task);
	}

	private static <T> void postAppCenter(Context context,
			CatShopRequest request, ObtainListener<T> listener) {
		Task<T> task = new Task<T>(context, request, listener);
		new CustomThreadAsyncTask<Object, Void, Task<T>>() {
			@Override
			protected Task<T> doInBackground(Object... params) {
				@SuppressWarnings("unchecked")
				Task<T> task = (Task<T>) params[0];
				try {
					JsonBuilder jsonBuilder = new JsonBuilder();
					task.request.buildAppCenterParams(task.context);
					String requestStr = jsonBuilder.toJson(task.request);
					String responseStr = HttpUtil.doPost(
							new URL(Protocol
									.getAppCenterUrl(task.request.action)),
							requestStr);
					 Log.e("request==>", requestStr);
					 Log.e("response==>", responseStr);
					if (!TextUtils.isEmpty(responseStr)) {
						JSONObject jsonObject = new JSONObject(responseStr);
						int code = jsonObject.getInt("code");
						String data = jsonObject.getString("data");
						task.response = new CatShopResponse(code, data);
						if (task.response.code == ResultCode.SUCCESS.code
								&& !(TextUtils.isEmpty(task.response.data) || "\"\""
										.equals(task.response.data))) {
							task.result = jsonBuilder.fromJson(
									task.response.data, task.listener.type);

						} else {

						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					if (task.response == null) {
						task.response = new CatShopResponse(
								ResultCode.NET_ERROR.code, "");
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (task.response == null) {
						task.response = new CatShopResponse(
								ResultCode.SERVER_ERROR.code, "");
					}
					task.listener
							.onFinishInBackgroud(task.context,
									ResultCode.getEnum(task.response.code),
									task.result);
				}

				return task;
			}

			@Override
			protected void onPostExecute(Task<T> result) {
				ResultCode code = ResultCode.getEnum(result.response.code);
				if (code == ResultCode.SUCCESS) {
					result.listener.onSucceed(result.context, result.result);
				} else {
					result.listener.onError(result.context, code);
				}
				result.listener.onFinished(result.context, code);
			}
		}.execute(task);
	}

	private static class Task<T> {
		private Context context;
		private CatShopRequest request;
		private CatShopResponse response;
		private T result;
		private ObtainListener<T> listener;

		private Task(Context context, CatShopRequest request,
				ObtainListener<T> listener) {
			this.request = request;
			this.listener = listener;
			if (context != null) {
				this.context = context.getApplicationContext();
			}
		}
	}

}

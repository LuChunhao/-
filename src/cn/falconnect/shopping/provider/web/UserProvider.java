package cn.falconnect.shopping.provider.web;

import org.aurora.library.encrypt.MD5Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import cn.falconnect.shopping.entity.User;

public class UserProvider extends BaseProvider {
	private static final String FILE = "user.pro";
	private static final String SIGN = "key_sign";
	private static final String USER_ID = "key_user_id";
	private static final String AGE = "key_user_age";
	private static final String SEX = "key_user_sex";
	private static final String ICON = "key_user_icon";
	private static final String ACCOUNT = "key_user_account";
	private static final String NAME = "key_user_name";
	private static final String MIND_ACCOUNT = "key_min_account";

	public void login(Context context, String account, String password,
			ObtainListener<User> listener) {
		String passwordMD5 = passwordMd5(password);
		CatShopApi.login(context, account, passwordMD5, listener);
	}

	public void register(Context context, String account, String password,
			ObtainListener<User> listener) {
		String passwordMD5 = passwordMd5(password);
		CatShopApi.register(context, account, passwordMD5, listener);
	}

	public void getUserInfo(Context context, ObtainListener<User> listener) {
		CatShopApi.getUserInfo(context, listener);
	}
	public void getUserInfo(Context context, ObtainListener<User> listener,int uid) {
		CatShopApi.getUserInfo(context, listener,uid);
	}
	

	public void forgetPasswd(Context context, String account,
			ObtainListener<Void> listener) {
		CatShopApi.forgetPasswd(context, account, listener);
	}

	public void editPersonInfo(Context context, String headUrl,
			String backgroundUrl, String nickName, ObtainListener<Void> listener) {
		CatShopApi.editPersonInfo(context, headUrl, backgroundUrl, nickName,
				listener);
	}

	private String passwordMd5(String password) {
		return MD5Util.MD5(password);
	}

	public void saveLoginUser(Context context, User user) {
		SharedPreferences share = getShare(context);
		Editor edit = share.edit();
		if (user.sign != null)
			edit.putString(SIGN, user.sign);
		if (user.uid != null)
			edit.putInt(USER_ID, user.uid);
		if (user.age != null)
			edit.putInt(AGE, user.age);
		if (user.sex != null)
			edit.putInt(SEX, user.sex);
		if (user.icon != null)
			edit.putString(ICON, user.icon);
		if (user.nickName != null)
			edit.putString(NAME, user.nickName);
		if (user.account != null) {
			edit.putString(ACCOUNT, user.account);
		}
		edit.commit();
	}

	public void saveUserAccount(Context context, String account) {
		SharedPreferences share = getShare(context);
		Editor edit = share.edit();
		if (!TextUtils.isEmpty(account)) {
			edit.putString(MIND_ACCOUNT, account).commit();
		}
	}

	public String restoreUserAccount(Context context) {
		return getShare(context).getString(MIND_ACCOUNT, null);
	}

	public void logout(Context context) {
		SharedPreferences share = getShare(context);
		Editor edit = share.edit();
		edit.putString(SIGN, null);
		edit.putInt(USER_ID, 0);
		edit.putInt(AGE, 0);
		edit.putInt(SEX, 0);
		edit.putString(ICON, null);
		edit.putString(NAME, null);
		edit.putString(ACCOUNT, null);
		edit.commit();
	}

	public User restoreUser(Context context) {
		SharedPreferences share = getShare(context);
		User user = new User();
		user.uid = share.getInt(USER_ID, 0);
		user.sign = share.getString(SIGN, null);
		user.age = share.getInt(AGE, 0);
		user.sex = share.getInt(SEX, 0);
		user.nickName = share.getString(NAME, null);
		user.icon = share.getString(ICON, null);
		user.account = share.getString(ACCOUNT, null);
		return user;
	}

	public boolean isLogined(Context context) {
		return getLoginSign(context) != null;
	}

	public String getLoginSign(Context context) {
		String sign = restoreUser(context).sign;
		return TextUtils.isEmpty(sign) ? null : sign;
	}

	public String getLoginName(Context context) {
		String account = restoreUser(context).account;
		return TextUtils.isEmpty(account) ? null : account;
	}

	public int getUserId(Context context) {
		return restoreUser(context).uid;
	}

	private SharedPreferences getShare(Context context) {
		return context.getSharedPreferences(FILE, Context.MODE_PRIVATE);
	}
}

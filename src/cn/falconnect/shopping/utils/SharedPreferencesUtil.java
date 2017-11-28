package cn.falconnect.shopping.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
	public static void saveImageURL(Context context, String key, String value) {
		SharedPreferences sdf = context.getSharedPreferences("saveAppInfo",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sdf.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getImageURL(Context context, String key) {
		SharedPreferences sdf = context.getSharedPreferences("saveAppInfo",
				Context.MODE_PRIVATE);
		String value = sdf.getString(key, key);
		return value;
	}

	public static void saveBoolean(Context context, String key, boolean value) {
		SharedPreferences sdf = context.getSharedPreferences("saveAppInfo",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sdf.edit();
		editor.putBoolean(key, value);
		editor.commit();

	}

	public static boolean getBoolean(Context context, String key) {
		SharedPreferences sdf = context.getSharedPreferences("saveAppInfo",
				Context.MODE_PRIVATE);
		boolean value = sdf.getBoolean(key, false);

		return value;
	}

	public static void saveOffset(Context context, String key, int value) {
		SharedPreferences sdf = context.getSharedPreferences("saveAppInfo",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sdf.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int getOffset(Context context, String key) {
		SharedPreferences sdf = context.getSharedPreferences("saveAppInfo",
				Context.MODE_PRIVATE);
		int value = sdf.getInt(key, 0);

		return value;
	}

}

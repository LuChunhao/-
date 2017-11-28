package cn.falconnect.shopping;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import cn.falconnect.shopping.cat.R;

import com.tendcloud.tenddata.TCAgent;

public class TalkingDataHelper {
	public static void init(Context context) {
		if (context.getResources().getBoolean(R.bool.talking_data)) {
			try {
				TCAgent.init(context);
				TCAgent.setReportUncaughtExceptions(true);
			} catch (Throwable e) {
			}
		}
	}

	public static void onResume(Activity activity) {
		if (activity.getResources().getBoolean(R.bool.talking_data)) {

			try {
				TCAgent.onResume(activity);
			} catch (Throwable e) {
			}
		}
	}

	public static void onPause(Activity activity) {
		if (activity.getResources().getBoolean(R.bool.talking_data)) {
			try {
				TCAgent.onPause(activity);
			} catch (Throwable e) {
			}
		}
	}

	public static void reportPageStart(Fragment fragment, int resId) {
		if (fragment.getResources().getBoolean(R.bool.talking_data)) {
			reportPageStart(fragment, fragment.getActivity().getString(resId));
		}
	}

	public static void reportPageEnd(Fragment fragment, int resId) {
		if (fragment.getResources().getBoolean(R.bool.talking_data)) {
			reportPageEnd(fragment, fragment.getActivity().getString(resId));
		}
	}

	public static void reportPageStart(Fragment fragment, String name) {
		if (fragment.getResources().getBoolean(R.bool.talking_data)) {
			try {
				TCAgent.onPageStart(fragment.getActivity(), name);
			} catch (Throwable e) {
			}
		}
	}

	public static void reportPageEnd(Fragment fragment, String name) {
		if (fragment.getResources().getBoolean(R.bool.talking_data)) {
			try {
				TCAgent.onPageEnd(fragment.getActivity(), name);
			} catch (Throwable e) {
			}
		}
	}

	public static void reportEvent(Context context, String name, String tag, Map<?, ?> params) {
		if (context.getResources().getBoolean(R.bool.talking_data)) {
			try {
				TCAgent.onEvent(context, name, tag, params);
			} catch (Throwable e) {
			}
		}
	}

	public static void reportEvent(Context context, String tag) {
		if (context.getResources().getBoolean(R.bool.talking_data)) {
			try {
				TCAgent.onEvent(context, tag);
			} catch (Throwable e) {
			}
		}
	}

}

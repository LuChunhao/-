package cn.falconnect.shopping.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.IBinder;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;

public class CommonUtil {

	/**
	 * 将dip转换为px
	 */
	public static int convertDipToPx(Context context, float dip) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dip, context.getResources().getDisplayMetrics());
	}

	public static void hideSoftInput(Context context, IBinder windowToken) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager.isActive()) {
			inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
		}
	}

	public static Bitmap rotate(Bitmap b, int degrees) {
		if (degrees != 0 && b != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) b.getWidth() / 2,
					(float) b.getHeight() / 2);
			try {
				Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(),
						b.getHeight(), m, true);
				if (b != b2) {
					// b.recycle(); //Android开发网再次提示Bitmap操作完应该显示的释放
					b = b2;
				}
			} catch (OutOfMemoryError ex) {
				// Android123建议大家如何出现了内存不足异常，最好return 原始的bitmap对象。.
			}
		}
		return b;
	}

	public static int getVersionCode(Context context) throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(
				context.getPackageName(), 0);
		return packInfo.versionCode;
	}
}

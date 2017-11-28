package cn.falconnect.shopping;

import org.aurora.library.db.SqliteHelper;
import org.aurora.library.views.Toaster;

import android.app.Application;
import android.util.DisplayMetrics;
import cn.falconnect.shopping.cat.R;
import cn.falconnect.shopping.utils.ImageHelper;

public class CatShopApplication extends Application {
	private static final String DBNAME = "shopping.db3";
	private static final int DB_VERSION = 1;
	public static int sScreenWidth;
	public static int sScreenHeight;

	@Override
	public void onCreate() {
		super.onCreate();
		ImageHelper.init(getApplicationContext());
		Toaster.init(getApplicationContext(), R.layout.toast,
				android.R.id.message);
		SqliteHelper.setup(getApplicationContext(), DBNAME, false,
				R.raw.dbscript, DB_VERSION);
		DisplayMetrics display = getApplicationContext().getResources()
				.getDisplayMetrics();
		sScreenWidth = display.widthPixels;
		sScreenHeight = display.heightPixels;
	}

}

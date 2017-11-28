package cn.falconnect.shopping.provider.web;

import java.util.HashMap;


public class ProviderFatory {
	private static ProviderFatory sInstance;

	private static ProviderFatory getInstance() {
		if (sInstance == null) {
			synchronized (ProviderFatory.class) {
				if (sInstance == null) {
					sInstance = new ProviderFatory();
				}
			}
		}
		return sInstance;
	}

	public static UserProvider getUserProvider() {
		return (UserProvider) getInstance().getProvider(UserProvider.class);
	}
	
	public static GoodsProvider getGoodsProvider(){
		return (GoodsProvider) getInstance().getProvider(GoodsProvider.class);
	}
	
	public static FeedbackProvider getFeedbackProvider(){
		return (FeedbackProvider) getInstance().getProvider(FeedbackProvider.class);
	}
	
	public static VersionProvider getVersionProvider(){
		return (VersionProvider) getInstance().getProvider(VersionProvider.class);
	}
	
	public static LaunchPageProvider getLaunchPageProvider(){
		return (LaunchPageProvider) getInstance().getProvider(LaunchPageProvider.class);
	}
	
	public static ShowProvider getShowProvider(){
		return (ShowProvider)getInstance().getProvider(ShowProvider.class);
	}
	private HashMap<Class<? extends BaseProvider>, BaseProvider> mProviderPool = new HashMap<Class<? extends BaseProvider>, BaseProvider>();

	private synchronized BaseProvider getProvider(Class<? extends BaseProvider> providerClass) {
		BaseProvider provider = mProviderPool.get(providerClass);
		if (provider == null) {
			try {
				provider = providerClass.newInstance();
				mProviderPool.put(providerClass, provider);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return provider;
	}
}

package cn.falconnect.shopping;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import android.content.Context;

public class Task<T> {
	public Context context;
	public T result;
	public String msg;
	public AsyncTaskListener<T> listener;

	public Task(Context context, AsyncTaskListener<T> listener) {
		this.listener = listener;
		if (context != null) {
			this.context = context.getApplicationContext();
		}
	}

	public static abstract class AsyncTaskListener<T> {
		public final Type type;

		public AsyncTaskListener() {
			type = getSuperclassTypeParameter(getClass());
		}

		public abstract void onSucceed(Context context, T result);

		public abstract void onError(Context context, String msg);

		public void onFinishInBackgroud(Context context, String msg, T t) {
		}

		public abstract void onFinished(Context context, String msg);

		public Type getSuperclassTypeParameter(Class<?> subclass) {
			Type superclass = subclass.getGenericSuperclass();
			if (superclass instanceof Class) {
				throw new RuntimeException("Missing type parameter.");
			}
			ParameterizedType parameterized = (ParameterizedType) superclass;
			return parameterized.getActualTypeArguments()[0];
		}
	}
}

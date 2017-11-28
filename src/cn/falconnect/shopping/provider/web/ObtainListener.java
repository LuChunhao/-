package cn.falconnect.shopping.provider.web;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import android.content.Context;

public abstract class ObtainListener<T> {
	public final Type type;

	public ObtainListener() {
		type = getSuperclassTypeParameter(getClass());
	}

	public abstract void onSucceed(Context context, T result);

	public abstract void onError(Context context, ResultCode code);

	public void onFinishInBackgroud(Context context, ResultCode code, T t) {
	}

	public abstract void onFinished(Context context, ResultCode code);

	public Type getSuperclassTypeParameter(Class<?> subclass) {
		Type superclass = subclass.getGenericSuperclass();
		if (superclass instanceof Class) {
			throw new RuntimeException("Missing type parameter.");
		}
		ParameterizedType parameterized = (ParameterizedType) superclass;
		return parameterized.getActualTypeArguments()[0];
	}
}

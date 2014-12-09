package me.yanaga.winter.data.jpa.proxy;

import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;

import java.lang.reflect.Method;

public class ProxyFactory {

	private ProxyFactory() {
	}

	public static <T> T newProxy(Object target, Class<T> targetInterface) {
		return Reflection.newProxy(targetInterface, new AbstractInvocationHandler() {
			@Override
			protected Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
				return method.invoke(target, args);
			}
		});
	}

}

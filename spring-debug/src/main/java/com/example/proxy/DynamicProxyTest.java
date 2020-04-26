package com.example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wangjie
 * Created on 2020/4/25 16:35
 */
public class DynamicProxyTest implements InvocationHandler {
	private Test target;

	private DynamicProxyTest(Test target) {
		this.target = target;
	}

	public static Test newProxyInstance(Test target) {
		return (Test) Proxy
				.newProxyInstance(DynamicProxyTest.class.getClassLoader(),
						new Class<?>[] { Test.class },
						new DynamicProxyTest(target));

	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		return method.invoke(target, args);
	}
}
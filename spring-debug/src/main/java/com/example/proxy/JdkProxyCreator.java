package com.example.proxy;

import com.example.proxy.ProxyCreator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wangjie
 * Created on 2020/4/25 16:00
 */
public class JdkProxyCreator implements ProxyCreator, InvocationHandler {

	private Object target;

	public JdkProxyCreator(Object target) {
		assert target != null;
		Class<?>[] interfaces = target.getClass().getInterfaces();
		if (interfaces.length == 0) {
			throw new IllegalArgumentException("target class don`t implement any interface");
		}
		this.target = target;
	}

	@Override
	public Object getProxy() {
		Class<?> clazz = target.getClass();
		// 生成代理对象
		return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println(System.currentTimeMillis() + " - " + method.getName() + " method start");
		// 调用目标方法
		Object retVal = method.invoke(target, args);
		System.out.println(System.currentTimeMillis() + " - " + method.getName() + " method over");

		return retVal;
	}
}

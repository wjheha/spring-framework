package com.example.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

/**
 * @author wangjie
 * Created on 2020/4/25 15:57
 */
public class CglibProxyCreator implements ProxyCreator {

	private Object target;

	private MethodInterceptor methodInterceptor;

	public CglibProxyCreator(Object target, MethodInterceptor methodInterceptor) {
		assert (target != null && methodInterceptor != null);
		this.target = target;
		this.methodInterceptor = methodInterceptor;
	}

	@Override
	public Object getProxy() {
		Enhancer enhancer = new Enhancer();
		// 设置代理类的父类
		enhancer.setSuperclass(target.getClass());
		// 设置代理逻辑
		enhancer.setCallback(methodInterceptor);
		// 创建代理对象
		return enhancer.create();
	}
}

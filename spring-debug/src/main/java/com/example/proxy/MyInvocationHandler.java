package com.example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author wangjie
 * Created on 2020/4/23 22:28
 */
public class MyInvocationHandler implements InvocationHandler {

	// 目标对象
	private Object target;

	/**
	 * 构造方法
	 * @param target 目标对象
	 */
	public MyInvocationHandler(Object target) {
		super();
		this.target = target;
	}

	/**
	 * 执行目标对象的方法
	 * @param proxy
	 * @param method
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// 在目标对象的方法执行之前简单打印一下
		System.out.println("----------------------before------------------");
        // 执行目标对象的方法
		Object result = method.invoke(target , args) ;
        // 在目标对象的方法执行之后简单打印一下
		System.out.println("----------------------after------------------");
		return null;
	}

	/**
	 * 获取目标对象的代理对象
	 */
	public Object getProxy() {
		return Proxy.newProxyInstance(
				Thread.currentThread().getContextClassLoader(),		// loader - 类加载器
				target.getClass().getInterfaces(), 		// interfaces - 目标类所实现的接口列表
				this		//用于封装代理逻辑
		);
	}
}

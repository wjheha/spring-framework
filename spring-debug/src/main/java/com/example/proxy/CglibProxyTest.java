package com.example.proxy;


import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
/**
 * @author wangjie
 * Created on 2020/4/25 16:36
 */
public class CglibProxyTest implements MethodInterceptor {

	private CglibProxyTest() {
	}

	public static <T extends Test> Test newProxyInstance(Class<T> targetInstanceClazz){
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(targetInstanceClazz);
		enhancer.setCallback(new CglibProxyTest());
		return (Test) enhancer.create();
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args,
							MethodProxy proxy) throws Throwable {
		return proxy.invokeSuper(obj, args);
	}

}
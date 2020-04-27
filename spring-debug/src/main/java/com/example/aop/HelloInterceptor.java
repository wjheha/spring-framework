package com.example.aop;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2020/4/27 19:15
 */
public class HelloInterceptor implements MethodInterceptor {

	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		System.out.println("begin time -----> "+ System.currentTimeMillis());
		Object o1 = methodProxy.invokeSuper(o, objects);
		System.out.println("end time -----> "+ System.currentTimeMillis());
		return o1;
	}
}

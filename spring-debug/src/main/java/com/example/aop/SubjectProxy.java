package com.example.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2020/4/27 19:05
 */
public class SubjectProxy implements InvocationHandler {

	private Subject subject;

	public SubjectProxy(Subject subject) {
		this.subject = subject;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("--------------begin-------------");
		Object invoke = method.invoke(subject, args);
		System.out.println("--------------end-------------");
		return invoke;
	}
}

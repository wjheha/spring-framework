package com.example.aop;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2020/4/26 10:56
 */
public class LogResultAdvice implements AfterReturningAdvice {

	@Override
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target)
			throws Throwable {
		System.out.println(method.getName() + "方法返回：" + returnValue);
	}
}

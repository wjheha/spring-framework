package com.example;

import com.example.aop.CGsubject;
import com.example.aop.HelloInterceptor;
import org.springframework.cglib.proxy.Enhancer;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2020/4/27 19:16
 */
public class CglibProxyMain {

	public static void main(String[] args) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(CGsubject.class);
		enhancer.setCallback(new HelloInterceptor());
		CGsubject cGsubject = (CGsubject) enhancer.create();
		cGsubject.sayHello();
	}
}

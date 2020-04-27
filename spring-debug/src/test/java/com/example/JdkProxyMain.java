package com.example;

import com.example.aop.Subject;
import com.example.aop.SubjectImpl;
import com.example.aop.SubjectProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2020/4/27 19:06
 */
public class JdkProxyMain {

	public static void main(String[] args) {
		Subject subject = new SubjectImpl();
		InvocationHandler subjectProxy = new SubjectProxy(subject);
		Subject proxyInstance = (Subject) Proxy.newProxyInstance(
				subjectProxy.getClass().getClassLoader(),    //代理类的类加载器
				subject.getClass().getInterfaces(),   //被代理类的接口，如果有多个就以数组形式传入
				subjectProxy
		);
		proxyInstance.hello("world");
	}
}

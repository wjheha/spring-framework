package com.example;

import com.example.proxy.MyInvocationHandler;
import com.example.proxy.UserServiceImpl;
import com.example.proxy.UserService;
import org.junit.Test;

/**
 * @author wangjie
 * Created on 2020/4/23 22:35
 */
public class ProxyTest {

	@Test
	public void testProxy() throws Throwable {
		// 实例化目标对象
		UserService userService = new UserServiceImpl();

		// 实例化InvocationHandler
		MyInvocationHandler invocationHandler = new MyInvocationHandler(userService);

		// 根据目标对象获取代理对象
		UserService proxy = (UserService) invocationHandler.getProxy();

		//调用目标对象的方法
		proxy.add();
	}
}

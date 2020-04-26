package com.example;

import com.example.proxy.JdkProxyCreator;
import com.example.proxy.ProxyCreator;
import com.example.proxy.UserService;
import com.example.proxy.UserServiceImpl;
import org.junit.Test;
import org.springframework.context.annotation.Spr12636Tests;

/**
 * @author wangjie
 * Created on 2020/4/25 16:11
 */
public class JdkProxyCreatorTest {

	@Test
	public void getProxy() throws Exception {
		ProxyCreator proxyCreator = new JdkProxyCreator(new UserServiceImpl());
		UserService userService = (UserService) proxyCreator.getProxy();

		System.out.println("proxy type = " + userService.getClass());
		System.out.println();
		userService.save(null);
		System.out.println();
		userService.update(null);
	}
}

package com.example;

import com.example.aop.OrderService;
import com.example.aop.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2020/4/26 10:57
 */
public class SpringAopSourceApplication {

	public static void main(String[] args) {

		// 启动 Spring 的 IOC 容器
		ApplicationContext context = new ClassPathXmlApplicationContext("DefaultAdvisorAutoProxy.xml");

		UserService userService = context.getBean(UserService.class);
		OrderService orderService = context.getBean(OrderService.class);

		userService.createUser("Tom", "Cruise", 55);
		userService.queryUser();

		orderService.createOrder("Leo", "随便买点什么");
		orderService.queryOrder("Leo");
	}
}

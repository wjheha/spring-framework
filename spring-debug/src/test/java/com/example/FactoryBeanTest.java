package com.example;


import com.example.demo.FactoryBeanService;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author wangjie
 * Created on 2020/9/8 21:19
 */
public class FactoryBeanTest {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext cac = new ClassPathXmlApplicationContext("applicationsContext3.xml");
		FactoryBeanService beanService = cac.getBean(FactoryBeanService.class);
		beanService.testFactoryBean();
	}

	//单测
	@Test
	public void test() {
		System.out.println("wj");
	}
}

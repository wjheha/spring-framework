package com.example;

import com.example.aop.TestBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2021/2/19 15:26
 */
public class JavaXmlTest {

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext2.xml");
		TestBean bean = (TestBean) ac.getBean("test");
		bean.test();
	}
}

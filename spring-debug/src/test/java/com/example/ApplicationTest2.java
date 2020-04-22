package com.example;

import com.example.aop.Dao;
import com.example.aop.TestBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author wangjie
 * Created on 2020/4/21 21:38
 */
public class ApplicationTest2 {

	public static void main(String[] args) {
		ApplicationContext bf = new ClassPathXmlApplicationContext("applicationContext2.xml");
		TestBean bean = (TestBean) bf.getBean("test");
		bean.test();

		System.out.println();
		System.out.println();
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext2.xml");
		Dao dao = (Dao)ac.getBean("daoImpl");
		dao.select();
	}
}

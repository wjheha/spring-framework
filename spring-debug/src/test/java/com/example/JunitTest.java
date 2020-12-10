package com.example;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author wangjie
 * Created on 2020/3/24 00:07
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:applicationContext.xml"})
public class JunitTest {

	@org.junit.Test
	public void test4() {
		System.out.println("Hello World");
	}

	//报错，版本不兼容
//	@org.junit.jupiter.api.Test
//	public void test5() {
//		System.out.println("Hello World");
//	}

}

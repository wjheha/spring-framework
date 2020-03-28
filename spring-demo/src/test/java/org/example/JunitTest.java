package org.example;

import org.example.appcontext.ILogin;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author wangjie
 * Created on 2020/3/20 23:43
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:config/spring-config.xml"})
public class JunitTest {

	@Autowired
	private ILogin loginImpl;

	@Test
	public void myTest() {
		loginImpl.loginCheck("wjheha", "huiyan");
		System.out.println("hello world");
	}
}

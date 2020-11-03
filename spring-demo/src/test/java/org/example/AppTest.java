package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.example.beanfactory.TestBean;
import org.example.appcontext.ILogin;
import org.example.test.customtag.User;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue( true );
    }

	public static void main(String[] args) {
		//ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-config.xml");
		String XMLPath = "//Users/wangjie/IdeaProjects/spring-framework/spring-demo/src/main/resources/config/spring-config.xml";
		ApplicationContext applicationContext = new FileSystemXmlApplicationContext(XMLPath);
		ILogin login = (ILogin) applicationContext.getBean("loginService");
		login.loginCheck("boy", "123");
		ApplicationContext applicationContext2 = new FileSystemXmlApplicationContext(XMLPath);
		System.out.println(applicationContext.toString());
		System.out.println(applicationContext2.toString());

//		ApplicationContext applicationContext3 = new ClassPathXmlApplicationContext("config/user.xml");
//		User user = (User) applicationContext3.getBean("myUser");
//		System.out.println(user.getUserName() + "," + user.getEmail());
//		BeanFactory bf = new XmlBeanFactory( new ClassPathResource("config/user.xml"));
//		User user2 = (User) bf.getBean("user");
//		System.out.println(user2);

		BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("config/spring-config.xml"));
		TestBean bean = (TestBean) beanFactory.getBean("testBean");
		System.out.println(bean.getTestStr());
		assertEquals("testStr", bean.getTestStr());
	}
}

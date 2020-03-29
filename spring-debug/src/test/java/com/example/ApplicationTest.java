package com.example;

import com.example.beans.Hello;
import com.example.beans.TestBean;
import com.example.beans.User;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.assertEquals;

/**
 * @author wangjie
 * Created on 2020/3/23 23:58
 */
public class ApplicationTest {

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		Hello hello = (Hello) ac.getBean("hello");
		hello.sayHello();

		// 根据 Xml 配置文件创建 Resource 资源对象。ClassPathResource 是 Resource 接口的子类，
		// bean.xml 文件中的内容是我们定义的 Bean 信息。
		ClassPathResource resource = new ClassPathResource("applicationContext.xml");
		// 创建一个 BeanFactory 。DefaultListableBeanFactory 是 BeanFactory 的一个子类，
		// BeanFactory 作为一个接口，其实它本身是不具有独立使用的功能的，而 DefaultListableBeanFactory 则是真正可以独立使用的 IoC 容器，
		// 它是整个 Spring IoC 的始祖，在后续会有专门的文章来分析它。
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		// 创建 XmlBeanDefinitionReader 读取器，用于载入 BeanDefinition 。
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		// 开始 BeanDefinition 的载入和注册进程，完成后的 BeanDefinition 放置在 IoC 容器中。
		reader.loadBeanDefinitions(resource);

		BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("applicationContext.xml"));
		TestBean bean = (TestBean) beanFactory.getBean("testBean");
		System.out.println(bean.getTestStr());
		assertEquals("testStr", bean.getTestStr());

		//自定义标签使用
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		User user = (User) context.getBean("user");
		System.out.println(user.getUserName() + "----" + user.getEmail());
	}
}

package com.example;

import com.example.beans.Hello;
import com.example.beans.TestBean;
import com.example.beans.User;
import com.example.demo.*;
import com.example.service.StudentService;
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

		System.out.println("**************************************** 0 ***********************************************");

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
		// 测试Aware, MyApplicationAware的setApplicationContext()方法未执行，display()报空指针错误
		MyApplicationAware applicationAware = (MyApplicationAware) factory.getBean("myApplicationAware");
		// applicationAware.display();
		// 测试Aware, MyApplicationAware的setApplicationContext()方法执行了，display()正常执行
		// ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		// MyApplicationAware applicationAware = (MyApplicationAware) applicationContext.getBean("myApplicationAware");
		// applicationAware.display();
		// 测试BeanPostProcessor，一般普通的 BeanFactory 是不支持自动注册 BeanPostProcessor 的，
		// 需要我们手动调用 #addBeanPostProcessor(BeanPostProcessor beanPostProcessor) 方法进行注册。
		// 注册后的 BeanPostProcessor 适用于所有该 BeanFactory 创建的 bean，但是 ApplicationContext
		// 可以在其 bean 定义中自动检测所有的 BeanPostProcessor 并自动完成注册，同时将他们应用到随后创建的任何 Bean 中
		BeanPostProcessorTest beanPostProcessorTest = new BeanPostProcessorTest();
		factory.addBeanPostProcessor(beanPostProcessorTest);
		BeanPostProcessorTest test = (BeanPostProcessorTest) factory.getBean("beanPostProcessorTest");
		test.display();
		// 测试InitializingBean和init-method
		InitializingBeanTest initTest = (InitializingBeanTest) factory.getBean("initializingBeanTest");
		System.out.println("name ：" + initTest.getName());
		InitializingBeanTest2 initTest2 = (InitializingBeanTest2) factory.getBean("initializingBeanTest2");
		System.out.println("name ：" + initTest2.getName());
		// BeanFactory 容器一定要调用该方法进行 BeanPostProcessor 注册
		factory.addBeanPostProcessor(new LifeCycleBean());
		LifeCycleBean lifeCycleBean = (LifeCycleBean) factory.getBean("lifeCycle");
		lifeCycleBean.display();
		// 对于 BeanFactory 容器来说，BeanFactoryPostProcessor 一样需要容器主动去进行注册调用，方法如下：
		BeanFactoryPostProcessor_1 beanFactoryPostProcessor1 = new BeanFactoryPostProcessor_1();
		BeanFactoryPostProcessor_2 beanFactoryPostProcessor2 = new BeanFactoryPostProcessor_2();
		beanFactoryPostProcessor1.postProcessBeanFactory(factory);
		beanFactoryPostProcessor2.postProcessBeanFactory(factory);
		System.out.println("方法调用完成，容器开始关闭....");
		// 关闭容器
		factory.destroySingletons();
		// 显示分隔符 //
		System.out.println("**************************************** 1 ***********************************************");


		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		Hello hello = (Hello) context.getBean("hello");
		hello.sayHello();
		StudentService studentService = (StudentService) context.getBean("studentService");
		System.out.println("student name:" + studentService.getName() + "-- age:" + studentService.getAge());
		StudentService studentService2 = (StudentService) context.getBean("studentService2");
		System.out.println("student name:" + studentService2.getName() + "-- age:" + studentService2.getAge());
		// 测试xml配置文件中两个同类型bean
		System.out.println(studentService.toString() + "---" + studentService2.toString());
		StudentService studentService3 = (StudentService) context.getBean("student");
		System.out.println("student name:" + studentService3.getName());
		StudentService studentService4 = (StudentService) context.getBean("student2");
		System.out.println("student name:" + studentService4.getName());
		// 显示分隔符 //
		System.out.println("**************************************** 2 ***********************************************");


		// XmlBeanFactory继承自DefaultListableBeanFactory，而DefaultListableBeanFactory是整个bean加载的核心部分，
		// 是Spring注册及加载bean的默认实现，而对于XmlBeanFactory和DefaultListableBeanFactory不同的地方其实是在XmlBeanFactory中
		// 使用了自定义的XML读取器XmlBeanDefinitionReader，实现了个性化的BeanDefinitionReader读取。
		BeanFactory beanFactory = new XmlBeanFactory(new ClassPathResource("applicationContext.xml"));
		TestBean bean = (TestBean) beanFactory.getBean("testBean");
		System.out.println(bean.getTestStr());
		assertEquals("testStr", bean.getTestStr());
		// 显示分隔符 //
		System.out.println("**************************************** 3 ***********************************************");


		// 自定义标签使用
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		User user = (User) applicationContext.getBean("user");
		System.out.println(user.getUserName() + "----" + user.getEmail());
		// 显示分隔符 //
		System.out.println("**************************************** 4 ***********************************************");
	}
}

package com.example.demo;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.Ordered;

/**
 * @author wangjie
 * Created on 2020/4/11 22:48
 */
public class BeanFactoryPostProcessor_2 implements BeanFactoryPostProcessor, Ordered {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("调用 BeanFactoryPostProcessor_2 ...");

		// 获取指定的 BeanDefinition
		BeanDefinition bd = beanFactory.getBeanDefinition("studentService");

		MutablePropertyValues pvs = bd.getPropertyValues();

		pvs.addPropertyValue("age",18);
	}

	@Override
	public int getOrder() {
		return 2;
	}
}

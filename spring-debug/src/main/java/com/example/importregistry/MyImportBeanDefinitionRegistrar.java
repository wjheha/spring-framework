package com.example.importregistry;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2021/2/19 20:59
 */
public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

	/**
	 * AnnotationMetadata：当前类的注解信息
	 * BeanDefinitionRegistry：BeanDefinition注册类
	 * <p>
	 * 我们可以通过调用BeanDefinitionRegistry接口中的registerBeanDefinition方法，手动注册所有需要添加到容器中的bean
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		boolean definition = registry.containsBeanDefinition("com.example.importregistry.Red");
		boolean definition2 = registry.containsBeanDefinition("com.example.importregistry.Bule");
		if (definition && definition2) {
			// 指定bean的定义信息，包括bean的类型、作用域等等
			// RootBeanDefinition是BeanDefinition接口的一个实现类
			RootBeanDefinition beanDefinition = new RootBeanDefinition(RainBow.class); // bean的定义信息
			// 注册一个bean，并且指定bean的名称
			registry.registerBeanDefinition("rainBow", beanDefinition);
		}
	}
}


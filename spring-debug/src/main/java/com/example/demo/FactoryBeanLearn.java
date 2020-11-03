package com.example.demo;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author wangjie
 * Created on 2020/9/8 21:17
 */
public class FactoryBeanLearn implements FactoryBean<FactoryBeanServiceImpl> {

	@Override
	public FactoryBeanServiceImpl getObject() throws Exception {
		//这个Bean是我们自己new的，这里我们就可以控制Bean的创建过程了
		return new FactoryBeanServiceImpl();
	}

	@Override
	public Class<?> getObjectType() {
		return FactoryBeanService.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}

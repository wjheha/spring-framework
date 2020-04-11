package com.example.demo;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author wangjie
 * Created on 2020/4/11 18:59
 */
public class InitializingBeanTest implements InitializingBean {

	private String name;

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("InitializingBeanTest initializing...");
		this.name = "chenssy 2 号";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

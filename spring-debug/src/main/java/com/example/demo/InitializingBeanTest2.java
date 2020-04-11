package com.example.demo;

/**
 * @author wangjie
 * Created on 2020/4/11 19:15
 */
public class InitializingBeanTest2 {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOtherName(){
		System.out.println("InitializingBeanTest setOtherName...");
		this.name = "chenssy 3 号";
	}
}

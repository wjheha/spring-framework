package org.example.beanfactory;

import org.springframework.stereotype.Component;

/**
 * @author wangjie
 * Created on 2020/3/17 21:55
 */
@Component
public class TestBean {
	private String testStr = "testStr";

	public String getTestStr() {
		return testStr;
	}

	public void setTestStr(String testStr) {
		this.testStr = testStr;
	}
}

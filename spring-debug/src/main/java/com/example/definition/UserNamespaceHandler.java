package com.example.definition;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author wangjie
 * Created on 2020/3/29 12:41
 */
public class UserNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("user", new UserDefinitionParser());
	}
}

package org.example.test.customtag;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 自定义标签解码入口
 * @author wangjie
 * Created on 2020/3/22 18:51
 */
public class MyNamespaceHandler extends NamespaceHandlerSupport {
	@Override
	public void init() {
		registerBeanDefinitionParser("user",new UserBeanDefinitionParser());
	}
}

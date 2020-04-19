package com.example.impl;

import com.example.interfaces.MessageService;

/**
 * @author wangjie
 * Created on 2020/4/18 14:48
 */
public class MessageServiceImpl implements MessageService {

	@Override
	public String getMessage() {
		return "hello world";
	}
}

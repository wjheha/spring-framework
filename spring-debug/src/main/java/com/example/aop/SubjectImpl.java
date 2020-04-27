package com.example.aop;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2020/4/27 19:04
 */
public class SubjectImpl implements Subject {

	@Override
	public void hello(String param) {
		System.out.println("hello  " + param);
	}
}

package com.example.proxy;

/**
 * @author wangjie
 * Created on 2020/4/25 16:35
 */
public class DecoratorTest implements Test{
	private Test target;

	public DecoratorTest(Test target) {
		this.target = target;
	}

	@Override
	public int test(int i) {
		return target.test(i);
	}
}

package com.example.proxy;

/**
 * @author wangjie
 * Created on 2020/4/25 16:34
 */
public class TestImpl implements Test{

	@Override
	public int test(int i) {
		return i+1;
	}
}

package com.example.proxy;

/**
 * @author wangjie
 * Created on 2020/4/23 22:26
 */
public class UserServiceImpl implements UserService {

	@Override
	public void add() {
		System.out.println("-------------------add-------------------");
	}

	@Override
	public void save(User user) {
		System.out.println("save user info");
	}

	@Override
	public void update(User user) {
		System.out.println("update user info");
	}
}

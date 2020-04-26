package com.example.proxy;

/**
 * @author wangjie
 * Created on 2020/4/23 22:25
 */
public interface UserService {

	/**
	 * 目标方法
	 */
	public abstract void add();

	void save(User user);

	void update(User user);
}

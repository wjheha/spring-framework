package com.example.aop;

/**
 * @author wangjie
 * Created on 2020/4/21 22:31
 */
public class DaoImpl implements Dao{

	@Override
	public void select() {
		System.out.println("Enter DaoImpl.select()");
	}

	@Override
	public void insert() {
		System.out.println("Enter DaoImpl.insert()");
	}
}

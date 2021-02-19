package com.example.importregistry;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2021/2/19 21:01
 */
public class Person {

	private String name;

	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}
}

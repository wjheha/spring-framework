package com.example.aop;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2020/4/26 10:49
 */
public interface UserService {

	User createUser(String firstName, String lastName, int age);

	User queryUser();
}

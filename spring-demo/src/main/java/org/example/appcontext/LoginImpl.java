package org.example.appcontext;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wangjie
 * Created on 2020/3/17 00:59
 */
@Component
public class LoginImpl implements ILogin {

	String id = "";

	@Autowired(required = false)
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String loginCheck(String userName, String password) {
		System.out.println("boy登录...");
		return "success";
	}
}

package com.example.aop;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2020/4/26 10:55
 */
public class LogArgsAdvice implements MethodBeforeAdvice {

	@Override
	public void before(Method method, Object[] args, Object target) throws Throwable {
		System.out.println("准备执行方法: " + method.getName() + ", 参数列表：" + Arrays.toString(args));
	}

}

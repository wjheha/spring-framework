package com.example.aop;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2020/4/26 10:48
 */
public interface OrderService {

	Order createOrder(String username, String product);

	Order queryOrder(String username);
}

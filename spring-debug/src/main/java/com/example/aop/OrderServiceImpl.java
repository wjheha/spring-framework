package com.example.aop;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2020/4/26 10:50
 */
public class OrderServiceImpl implements OrderService {

	@Override
	public Order createOrder(String username, String product) {
		Order order = new Order();
		order.setUsername(username);
		order.setProduct(product);
		return order;
	}

	@Override
	public Order queryOrder(String username) {
		Order order = new Order();
		order.setUsername("test");
		order.setProduct("test");
		return order;
	}
}

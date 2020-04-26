package com.example;

import com.example.proxy.CglibProxyCreator;
import com.example.proxy.ProxyCreator;
import com.example.proxy.Tank59;
import com.example.proxy.TankRemanufacture;
import org.junit.Test;

/**
 * @author wangjie
 * Created on 2020/4/25 16:06
 */
public class CglibProxyCreatorTest {

	@Test
	public void getProxy() throws Exception {
		ProxyCreator proxyCreator = new CglibProxyCreator(new Tank59(), new TankRemanufacture());
		Tank59 tank59 = (Tank59) proxyCreator.getProxy();

		System.out.println("proxy class = " + tank59.getClass() + "\n");
		tank59.run();
		System.out.println();
		System.out.print("射击测试：");
		tank59.shoot();
	}
}

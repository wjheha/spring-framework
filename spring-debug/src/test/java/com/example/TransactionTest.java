package com.example;


import org.springframework.transaction.annotation.Transactional;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2020/4/29 13:38
 */
public class TransactionTest {

	public static void main(String[] args) {
		UserInfoExtendService userInfoExtendService = new UserInfoExtendService();
		new TransactionTest().serviceHI(userInfoExtendService);

	}

	@Transactional
	public void serviceHI(UserInfoExtendService userInfoExtendService) {
		userInfoExtendService.serviceI();
		try {
			new Thread(() -> userInfoExtendService.serviceH()).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		userInfoExtendService.serviceJ();
	}

	static class UserInfoExtendService {

		void serviceI() {

		}

		void serviceH() {

		}

		void serviceJ() {

		}
	}
}

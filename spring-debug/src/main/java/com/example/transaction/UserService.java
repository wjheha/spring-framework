package com.example.transaction;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2020/4/28 16:14
 */
@Transactional(propagation = Propagation.REQUIRED)
public interface UserService {

	public void save(User user) throws Exception;
}

package com.example.transaction;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Types;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2020/4/28 16:15
 */
public class UserServiceImpl implements UserService {

	private JdbcTemplate jdbcTemplate;

	//设置数据源
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void save(User user) throws Exception {
		jdbcTemplate.update("insert into user(name, age, sex) value (?,?,?)",
				new Object[]{
						user.getName(), user.getAge(), user.getSex()
				},
				new int[]{
						Types.VARCHAR, Types.INTEGER, Types.VARCHAR
				}
		);
		// 事务测试，加上这句代码则数据不会保存到数据库中
		throw new RuntimeException("aa");
	}
}

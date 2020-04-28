package com.example.transaction;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * desc:
 *
 * @author jwangz
 * Created on 2020/4/28 15:48
 */
public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet set, int rowNum) throws SQLException {
		User person = new User(
				set.getInt("id"),
				set.getString("name"),
				set.getInt("age"),
				set.getString("sex")
				);
		return person;
	}
}

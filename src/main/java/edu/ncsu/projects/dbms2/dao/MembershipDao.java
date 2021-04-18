package edu.ncsu.projects.dbms2.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class MembershipDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public Double getMembershipDurationInMonths(String membershipLevel) {
		String sql = " SELECT DURATION_MONTHS FROM MEMBERSHIPS WHERE MEMBERSHIP_LEVEL = ? ";
		
		return jdbcTemplate.queryForObject(sql, Double.class, membershipLevel);
	}
}

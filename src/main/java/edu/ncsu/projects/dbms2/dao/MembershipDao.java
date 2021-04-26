package edu.ncsu.projects.dbms2.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class MembershipDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * Gives the duration in months for mentioned membership level
	 * @param membershipLevel
	 * @return duration for the selected membership level e.g. 12 months
	 */
	public Double getMembershipDurationInMonths(String membershipLevel) {
		String sql = " SELECT DURATION_MONTHS FROM MEMBERSHIPS WHERE MEMBERSHIP_LEVEL = ? ";
		
		return jdbcTemplate.queryForObject(sql, Double.class, membershipLevel);
	}
}

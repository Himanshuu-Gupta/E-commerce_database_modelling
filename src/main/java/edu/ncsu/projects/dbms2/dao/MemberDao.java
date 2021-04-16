package edu.ncsu.projects.dbms2.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.entity.Member;

@Component
public class MemberDao {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<Member> findAll() {
		return jdbcTemplate.query("SELECT * FROM MEMBERS", new BeanPropertyRowMapper<Member>(Member.class));
	}
}

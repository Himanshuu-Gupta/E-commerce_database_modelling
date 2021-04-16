package edu.ncsu.projects.dbms2.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.entity.Books;

@Component
public class BooksDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<Books> findAll() {
		return jdbcTemplate.query("SELECT * FROM BOOKS", new BeanPropertyRowMapper<Books>(Books.class));
	}
}

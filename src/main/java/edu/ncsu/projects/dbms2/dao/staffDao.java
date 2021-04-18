package edu.ncsu.projects.dbms2.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.entity.Staff;
import java.sql.Date;  

@Component
public class staffDao {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<Staff> findAll() {
		String sql = " SELECT * FROM STAFF ";
		
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Staff>(Staff.class));
	}
	
	public Staff findByAttribute(String attributeName, Object attributeValue) {
		String sql = " SELECT * FROM STAFF WHERE "+ attributeName +" = ? ";
		
		List<Staff> staff = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Staff>(Staff.class), attributeValue);
		
		return staff != null && !staff.isEmpty() ? staff.get(0) : null;
	}
	
	public int addStaff(Staff staff) {
		String sql = "INSERT INTO STAFF VALUES (?,?,?,?,?,?,?,?,?,?)";
		
		return jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, null);
				ps.setString(2, staff.getName());
				ps.setString(3, staff.getPhone());
				ps.setString(4, staff.getAddress());
				ps.setString(5, staff.getJobTitle());
				ps.setString(6, staff.getEmail());
				ps.setInt(7, staff.getAge());
				ps.setDate(8, staff.getStartDate());
				ps.setDate(9, null);
				ps.setInt(10, staff.getStoreId());
			}
		});
	}
	
	public int updateByAttribute(String attributeName, Object attributeValue, Integer staffId) {
		String sql = " UPDATE STAFF SET "+ attributeName +" = ? WHERE STAFF_ID = ? ";
		
		return jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, attributeValue);
				ps.setInt(2, staffId);
			}
		});
	}
	
	public int deleteStaff(Integer staffId) {
		
		long millis=System.currentTimeMillis();  
        java.sql.Date date = new java.sql.Date(millis);
        
		String sql = " UPDATE STAFF SET END_DATE = ? WHERE STAFF_ID = ? ";
		
		return jdbcTemplate.update(sql, date, staffId);
	}
	
	public Staff removeFromDb(Integer staffId) {
		//TODO
		return null;
	}
	
	
	
	
}

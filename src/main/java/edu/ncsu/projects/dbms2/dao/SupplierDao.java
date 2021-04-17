package edu.ncsu.projects.dbms2.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import edu.ncsu.projects.dbms2.entity.Supplier;

@Component
public class SupplierDao {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public List<Supplier> findAll() {
		String sql = " SELECT * FROM SUPPLIERS ";
		
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Supplier>(Supplier.class));
	}
	
	public Supplier findByAttribute(String attributeName, Object attributeValue) {
		String sql = " SELECT * FROM SUPPLIERS WHERE "+ attributeName +" = ? ";
		
		List<Supplier> suppliers = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Supplier>(Supplier.class), attributeValue);
		
		return suppliers != null && !suppliers.isEmpty() ? suppliers.get(0) : null;
	}
	
	public int addSupplier(Supplier supplier) {
		String sql = " INSERT INTO SUPPLIERS VALUES (null, ?, ?, ?, ?, ?) ";
		
		return jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, supplier.getSupplierName());
				ps.setString(2, supplier.getAddress());
				ps.setString(3, supplier.getPhone());
				ps.setString(4, supplier.getEmail());
				ps.setBoolean(5, supplier.getActiveStatus());
			}
		});
	}
	
	public int updateByAttribute(String attributeName, Object attributeValue, Integer supplierId) {
		String sql = " UPDATE SUPPLIERS SET "+ attributeName +" = ? WHERE SUPPLIER_ID = ? ";
		
		return jdbcTemplate.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setObject(1, attributeValue);
				ps.setInt(2, supplierId);
			}
		});
	}
	
	public int deleteSupplier(Integer supplierId) {
		String sql = " UPDATE SUPPLIERS SET ACTIVE_STATUS = false WHERE SUPPLIER_ID = ? ";
		
		return jdbcTemplate.update(sql, supplierId);
	}
	
	public int removeFromDb(Integer supplierId) {
		String sql = " DELETE FROM SUPPLIERS WHERE SUPPLIER_ID = ? ";
		
		return jdbcTemplate.update(sql, supplierId);
	}
}
